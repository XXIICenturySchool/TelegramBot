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

@Service
public class EndExamMessageHandler implements MessageHandler {
    @Autowired
    private UserManager userManager;

    @Autowired
    private ExamInteractionUtil examInteractionUtil;

    @Autowired
    private Localization localization;

    @Override
    public boolean handleMessage(Message message, ExamBot bot) {
        if (userManager.isInExam(message.getFrom()) && message.hasText() && message.getText().equals("/endExam")) {
            userManager.endCurrentExam(message.getFrom());
            SendMessage sendMessage = new SendMessage()
                    .setReplyMarkup(examInteractionUtil.defaultReplyMarkup(message.getFrom()))
                    .setChatId(message.getChatId())
                    .setReplyToMessageId(message.getMessageId())
                    .setText(localization.get(message.getFrom().getLanguageCode()).getMessage("examEnd"));
            try {
                bot.callApiMethod(sendMessage);
                return true;
            } catch (TelegramApiException e) {
                bot.sendError(message);
                e.printStackTrace();
            }
        }
        return false;
    }
}
