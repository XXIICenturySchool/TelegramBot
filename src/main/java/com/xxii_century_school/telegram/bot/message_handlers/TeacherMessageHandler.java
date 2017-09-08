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
public class TeacherMessageHandler implements MessageHandler {
    @Autowired
    private UserManager userManager;

    @Autowired
    private ExamInteractionUtil examInteractionUtil;

    @Autowired
    private Localization localization;

    @Override
    public boolean handleMessage(Message message, ExamBot bot) {
        if (message.hasText() && message.getText().equals("/teacher")) {
            userManager.setTeacherId(message.getFrom(), null);
            SendMessage sendMessage = new SendMessage().setReplyMarkup(examInteractionUtil.defaultReplyMarkup(message.getFrom()))
                    .setChatId(message.getChatId())
                    .setText(localization.get(message.getFrom().getLanguageCode()).getMessage("askForTeacherId"));
            try {
                bot.callApiMethod(sendMessage);
                return true;
            } catch (TelegramApiException e) {
                e.printStackTrace();
                bot.sendError(message);
            }
        }
        return false;
    }
}
