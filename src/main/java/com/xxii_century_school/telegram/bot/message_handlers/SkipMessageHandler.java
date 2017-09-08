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
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;

@Service
public class SkipMessageHandler implements MessageHandler {
    @Autowired
    private UserManager userManager;

    @Autowired
    private ExamInteractionUtil examInteractionUtil;

    @Autowired
    private Localization localization;

    @Override
    public boolean handleMessage(Message message, ExamBot bot) {
        if (userManager.isInExam(message.getFrom()) && message.hasText() && message.getText().equals("/skip")) {
            userManager.nextQuestion(message.getFrom(), false, true);
            SendMessage sendMessage = new SendMessage()
                    .setReplyMarkup(examInteractionUtil.defaultReplyMarkup(message.getFrom()))
                    .setChatId(message.getChatId())
                    .setReplyToMessageId(message.getMessageId())
                    .setText(localization.get(message.getFrom().getLanguageCode()).getMessage("questionSkipped"));
            try {
                bot.callApiMethod(sendMessage);
                examInteractionUtil.sendQuestionNumber(message, bot, false);
                examInteractionUtil.sendCurrentQuestion(message, bot);
                return true;
            } catch (TelegramApiException | IOException e) {
                bot.sendError(message);
                e.printStackTrace();
            }
        }
        return false;
    }
}
