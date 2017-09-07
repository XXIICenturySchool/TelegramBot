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
public class TeacherIdMessageHandler implements MessageHandler {
    @Autowired
    UserManager userManager;

    @Autowired
    ExamInteractionUtil examInteractionUtil;

    @Autowired
    Localization localization;

    @Override
    public boolean handleMessage(Message message, ExamBot bot) {
        if (message.hasText() && !userManager.hasTeacher(message.getFrom())) {
            try {
                int teacherId = Integer.parseInt(message.getText());
                userManager.setTeacherId(message.getFrom(), teacherId);
                SendMessage sendMessage = new SendMessage()
                        .setReplyMarkup(examInteractionUtil.defaultReplyMarkup(message.getFrom()))
                        .setText(localization.get(message.getFrom().getLanguageCode()).getMessage("askForExamKey"))
                        .setChatId(message.getChatId());
                bot.callApiMethod(sendMessage);
                return true;
            } catch (NumberFormatException e) {
                bot.sendError(message);
            } catch (TelegramApiException e) {
                bot.sendError(message);
                e.printStackTrace();
            }
        }
        return false;
    }
}
