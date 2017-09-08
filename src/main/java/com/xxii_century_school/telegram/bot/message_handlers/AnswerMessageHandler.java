package com.xxii_century_school.telegram.bot.message_handlers;

import com.xxii_century_school.telegram.bot.ExamBot;
import com.xxii_century_school.telegram.bot.MessageHandler;
import com.xxii_century_school.telegram.bot.exam_handler.ExamInteractionUtil;
import com.xxii_century_school.telegram.bot.exam_handler.UserManager;
import com.xxii_century_school.telegram.bot.localization.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;

@Service
public class AnswerMessageHandler implements MessageHandler {

    @Autowired
    private ExamInteractionUtil examInteractionUtil;

    @Autowired
    private UserManager userManager;

    @Autowired
    private Localization localization;

    @Override
    public boolean handleMessage(Message message, ExamBot bot) {
        User user = message.getFrom();
        if (userManager.isInExam(user) && message.hasText()) {
            String text = message.getText();
            try {
                if (examInteractionUtil.checkAnswer(text, userManager.getCurrentQuestion(user))) {
                    userManager.addCurrentAnswer(user, text);
                    SendMessage sendMessage = new SendMessage()
                            .setChatId(message.getChatId())
                            .setReplyToMessageId(message.getMessageId())
                            .setReplyMarkup(examInteractionUtil.getReplyKeyboard(message, userManager.getCurrentQuestion(user)))
                            .setText(localization.get(user.getLanguageCode()).getMessage("youAreCorrect"));
                    bot.callApiMethod(sendMessage);
                    userManager.addCurrentAnswer(user, text);
                } else {
                    SendMessage sendMessage = new SendMessage().setReplyMarkup(examInteractionUtil.defaultReplyMarkup(user))
                            .setChatId(message.getChatId())
                            .setReplyToMessageId(message.getMessageId())
                            .setText(localization.get(user.getLanguageCode()).getMessage("youAreIncorrect"));
                    bot.callApiMethod(sendMessage);
                    userManager.nextQuestion(user, false, false);
                }
                if (examInteractionUtil.endedAnswers(user)) {
                    userManager.nextQuestion(user, true, false);
                }
                examInteractionUtil.sendQuestionNumber(message, bot, false);
                examInteractionUtil.sendCurrentQuestion(message, bot);
                return true;
            } catch (TelegramApiException | IOException e) {
                bot.sendError(message);
                e.printStackTrace();
                return true;
            }

        }
        return false;
    }
}
