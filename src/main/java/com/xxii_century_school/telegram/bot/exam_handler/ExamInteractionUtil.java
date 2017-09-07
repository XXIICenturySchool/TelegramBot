package com.xxii_century_school.telegram.bot.exam_handler;

import com.xxii_century_school.telegram.bot.ExamBot;
import com.xxii_century_school.telegram.bot.Utils;
import com.xxii_century_school.telegram.bot.exam_handler.model.Question;
import com.xxii_century_school.telegram.bot.localization.Localization;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@Service
@Log
public class ExamInteractionUtil {

    @Autowired
    UserManager userManager;
    Map<Integer, Set<String>> usersAnswers = new HashMap<>();

    @Autowired
    Localization localization;


    public void sendCurrentQuestion(Message message, ExamBot bot) throws TelegramApiException, IOException {
        Question currentQuestion = userManager.getCurrentQuestion(message.getFrom());
        if (currentQuestion == null) {
            sendExamEndedMessage(message, bot);
            sendExamResultsToServer(message.getFrom(), userManager.getAnswerResults(message.getFrom()));
            return;
        }
        SendMessage sendMessage = new SendMessage()
                .setChatId(message.getChatId())
                .setText(currentQuestion.getQuestion());
        Message questionMessage = bot.callApiMethod(sendMessage);
        if (currentQuestion.getUrl() != null) {
            SendPhoto sendPhoto = new SendPhoto()
                    .setReplyToMessageId(questionMessage.getMessageId())
                    .setChatId(message.getChatId())
                    .setNewPhoto("", new URL(currentQuestion.getUrl()).openStream());
            bot.sendPhoto(sendPhoto);
        }
        sendAnswersMessage(message, bot, currentQuestion, questionMessage);
    }

    private void sendExamResultsToServer(User from, List<Boolean> answerResults) {
        log.severe("TODO: implement ExamInteractionUtil.sendExamResultsToServer(User from, List<Boolean> answerResults)");//TODO: implement
    }

    private void sendExamEndedMessage(Message message, ExamBot bot) throws TelegramApiException {
        String text = localization.get(message.getFrom().getLanguageCode()).getMessage("examEnd");
        SendMessage sendMessage = new SendMessage()
                .setChatId(message.getChatId())
                .setText(text);
        bot.callApiMethod(sendMessage);
    }

    public void sendAnswersMessage(Message message, ExamBot bot, Question currentQuestion) throws TelegramApiException {
        sendAnswersMessage(message, bot, currentQuestion, null);
    }

    public void sendAnswersMessage(Message message, ExamBot bot, Question currentQuestion, Message messageToReply) throws TelegramApiException {
        SendMessage sendMessage;
        sendMessage = new SendMessage()
                .setChatId(message.getChatId())
                .setText(Utils.formatAnswerOptions(currentQuestion.getOptions()));
        if (messageToReply != null) {
            sendMessage.setReplyToMessageId(messageToReply.getMessageId());
        }
        Set<String> currentAnswers = getCurrentAnswers(message.getFrom());
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        for (int i = 0; i < currentQuestion.getOptions().size(); i++) {
            KeyboardRow keyboardButtons = new KeyboardRow();
            String text = "" + i;
            if (!currentAnswers.contains(text)) {
                keyboardButtons.add(new KeyboardButton(text));
                keyboard.add(keyboardButtons);
            }
        }
        ReplyKeyboard replyKeyboard = new ReplyKeyboardMarkup().setKeyboard(keyboard);
        bot.callApiMethod(sendMessage);
    }

    public boolean checkAnswer(String answer, Question question) {
        return question.getAnswer().contains(answer);
    }

    public Set<String> getCurrentAnswers(User user) {
        Set<String> answers = usersAnswers.get(user.getId());
        if (answers == null) {
            answers = new HashSet<>();
            usersAnswers.put(user.getId(), answers);
        }
        return answers;
    }

    public void sendQuestionNumber(Message message, ExamBot bot) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage()
                .setChatId(message.getChatId())
                .setReplyToMessageId(message.getMessageId())
                .setParseMode("Markdown")
                .setText(localization.get(message.getFrom().getLanguageCode()).getMessage("questionStart") + 1);
        bot.callApiMethod(sendMessage);
    }

    public boolean endedAnswers(User user) {
        return getCurrentAnswers(user).size() == userManager.getCurrentQuestion(user).getAnswer().size();
    }
}
