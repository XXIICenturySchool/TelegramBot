package com.xxii_century_school.telegram.bot.message_handlers;

import com.xxii_century_school.telegram.bot.ExamBot;
import com.xxii_century_school.telegram.bot.MessageHandler;
import com.xxii_century_school.telegram.bot.exam_handler.ExamInteractionUtil;
import com.xxii_century_school.telegram.bot.exam_handler.UserManager;
import com.xxii_century_school.telegram.bot.localization.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Set;

public class AnswerMessageHandler implements MessageHandler {

    @Autowired
    ExamInteractionUtil examInteractionUtil;

    @Autowired
    UserManager userManager;

    @Autowired
    Localization localization;

    @Override
    public boolean handleMessage(Message message, ExamBot bot) {
        User from = message.getFrom();
        if (userManager.isInExam(from) && message.hasText()) {
            String text = message.getText();
            Set<String> currentAnswers = examInteractionUtil.getCurrentAnswers(from);
            try {
                if (examInteractionUtil.checkAnswer(text, userManager.getCurrentQuestion(from))) {
                    currentAnswers.add(text);
                    SendMessage sendMessage = new SendMessage()
                            .setChatId(message.getChatId())
                            .setReplyToMessageId(message.getMessageId())
                            .setText(localization.get(message.getFrom().getLanguageCode()).getMessage("youAreCorrect"));
                    bot.callApiMethod(sendMessage);

                } else {
                    SendMessage sendMessage = new SendMessage()
                            .setChatId(message.getChatId())
                            .setReplyToMessageId(message.getMessageId())
                            .setText(localization.get(message.getFrom().getLanguageCode()).getMessage("youAreIncorrect"));
                    bot.callApiMethod(sendMessage);
                    userManager.nextQuestion(message.getFrom(), false);
                }
                if (examInteractionUtil.endedAnswers(message.getFrom())) {
                    userManager.nextQuestion(message.getFrom(), true);
                    examInteractionUtil.sendCurrentQuestion(message, bot);
                }
            } catch (TelegramApiException | IOException e) {
                bot.sendError(message);
                e.printStackTrace();
                return true;
            }

        }
        return false;
    }
}
