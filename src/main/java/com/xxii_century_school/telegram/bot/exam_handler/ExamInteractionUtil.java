package com.xxii_century_school.telegram.bot.exam_handler;

import com.xxii_century_school.telegram.bot.ExamBot;
import com.xxii_century_school.telegram.bot.Services;
import com.xxii_century_school.telegram.bot.Utils;
import com.xxii_century_school.telegram.bot.exam_handler.model.ExamResult;
import com.xxii_century_school.telegram.bot.exam_handler.model.Question;
import com.xxii_century_school.telegram.bot.exam_handler.model.UserInfo;
import com.xxii_century_school.telegram.bot.localization.Localization;
import com.xxii_century_school.telegram.bot.localization.Localizer;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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
import java.util.ArrayList;
import java.util.Set;

@Service
@Log
public class ExamInteractionUtil {

    @Autowired
    UserManager userManager;

    @Autowired
    Localization localization;

    @Autowired
    DiscoveryClient discoveryClient;


    public void sendCurrentQuestion(Message message, ExamBot bot) throws TelegramApiException, IOException {
        Question currentQuestion = userManager.getCurrentQuestion(message.getFrom());
        if (currentQuestion == null) {
            if (userManager.isInExam(message.getFrom())) {
                sendExamEndedMessage(message, bot);
                sendExamResults(message, bot);
            }
            userManager.endCurrentExam(message.getFrom());
            return;
        }
        SendMessage sendMessage = new SendMessage()
                .setChatId(message.getChatId())
                .setText(currentQuestion.getQuestion());
        Message questionMessage = bot.callApiMethod(sendMessage);
        if (currentQuestion.getPictureUrl() != null) {
            String pictureURL = Utils.preparePictureURL(currentQuestion.getPictureUrl());
            SendPhoto sendPhoto = new SendPhoto()
                    .setReplyToMessageId(questionMessage.getMessageId())
                    .setChatId(message.getChatId())
                    .setNewPhoto("image", new URL(pictureURL).openStream());
            bot.sendPhoto(sendPhoto);
        }
        sendAnswersMessage(message, bot, currentQuestion, questionMessage);
    }

    private void sendExamResults(Message message, ExamBot bot) {
        UserInfo userInfo = userManager.getUserInfo(message.getFrom());
        ExamResult examResult = new ExamResult(userInfo);
        Localizer localizer = localization.get(message.getFrom().getLanguageCode());
        SendMessage sendMessage = new SendMessage()
                .setChatId(message.getChatId())
                .setText(localizer.getMessage("yourResults") + " " +
                        userInfo.getWrongAnswers() + " " +
                        localizer.getMessage("wrong") + " " +
                        userInfo.getSkippedAnswers() + " " +
                        localizer.getMessage("skipped"));
        try {
            bot.callApiMethod(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        String url = Services.GATE.pickRandomInstance(discoveryClient).getUri() + "/sentResult";
        log.info("sending results to " + url);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.postForObject(url, examResult, Void.class);
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
        String text = Utils.formatAnswerOptions(currentQuestion.getOptions());
        if (text == null || text.length() == 0) {
            return;
        }
        sendMessage = new SendMessage()
                .setChatId(message.getChatId())
                .setText(text);
        if (messageToReply != null) {
            sendMessage.setReplyToMessageId(messageToReply.getMessageId());
        }
        ReplyKeyboard replyKeyboard = getReplyKeyboard(message, currentQuestion);
        sendMessage.setReplyMarkup(replyKeyboard);
        bot.callApiMethod(sendMessage);
    }

    public ReplyKeyboard getReplyKeyboard(Message message, Question currentQuestion) {
        if (currentQuestion.getOptions() == null) {
            return defaultReplyMarkup(message.getFrom());
        }
        Set<String> currentAnswers = userManager.getCurrentAnswers(message.getFrom());
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        for (int i = 0; i < currentQuestion.getOptions().size(); i++) {
            KeyboardRow keyboardButtons = new KeyboardRow();
            String text = currentQuestion.getOptions().get(i);
            if (!currentAnswers.contains(text)) {
                keyboardButtons.add(new KeyboardButton(text));
                keyboard.add(keyboardButtons);
            }
        }
        keyboard.add(examKeyboardRow());
        return new ReplyKeyboardMarkup().setKeyboard(keyboard).setOneTimeKeyboard(true);
    }

    public boolean checkAnswer(String answer, Question question) {
        return question.getAnswer().contains(answer);
    }

    public void sendQuestionNumber(Message message, ExamBot bot) throws TelegramApiException {
        if (userManager.isInExam(message.getFrom())) {
            int currentQuestionId = userManager.getCurrentQuestionId(message.getFrom()) + 1;
            int totalQuestions = userManager.getCurrentExam(message.getFrom()).getTasks().size();
            if (currentQuestionId <= totalQuestions) {
                SendMessage sendMessage = new SendMessage()
                        .setChatId(message.getChatId())
                        .setReplyToMessageId(message.getMessageId())
                        .setParseMode("Markdown")
                        .setText(localization.get(message.getFrom().getLanguageCode()).getMessage("questionStart") +
                                currentQuestionId + " / " +
                                totalQuestions);
                bot.callApiMethod(sendMessage);
            }
        }
    }

    public boolean endedAnswers(User user) {
        if (userManager.getCurrentAnswers(user) == null) {
            return true;
        }
        if (userManager.getCurrentQuestion(user) == null) {
            return true;
        }
        return userManager.getCurrentAnswers(user).size() >= userManager.getCurrentQuestion(user).getAnswer().size();
    }

    public ReplyKeyboardMarkup defaultReplyMarkup(User user) {
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardButtons;
        if (userManager.isInExam(user)) {
            keyboard.add(examKeyboardRow());
        } else {
            keyboard = new ArrayList<>();
            keyboardButtons = new KeyboardRow();
            keyboardButtons.add(new KeyboardButton("/start"));
            keyboard.add(keyboardButtons);
            keyboardButtons = new KeyboardRow();
            keyboardButtons.add(new KeyboardButton("/teacher"));
            keyboard.add(keyboardButtons);
        }
        return new ReplyKeyboardMarkup().setKeyboard(keyboard).setOneTimeKeyboard(true);
    }

    private KeyboardRow examKeyboardRow() {
        KeyboardRow keyboardButtons = new KeyboardRow();
        //keyboardButtons.add(new KeyboardButton("/endExam"));
        keyboardButtons.add("/skip");
        return keyboardButtons;
    }
}
