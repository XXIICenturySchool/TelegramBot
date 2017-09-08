package com.xxii_century_school.telegram.bot.message_handlers;

import com.xxii_century_school.telegram.bot.ExamBot;
import com.xxii_century_school.telegram.bot.MessageHandler;
import com.xxii_century_school.telegram.bot.exam_handler.ExamInteractionUtil;
import com.xxii_century_school.telegram.bot.localization.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class StartMessageHandler implements MessageHandler {

    @Autowired
    private Localization locale;

    @Autowired
    private ExamInteractionUtil examInteractionUtil;


    @Override
    public boolean handleMessage(Message message, ExamBot bot) {
        if (message.hasText()) {
            if (message.getText().equalsIgnoreCase("/start")) {
                String localeCode = message.getFrom().getLanguageCode();
                String text = locale.get(localeCode).getMessage("welcomeMessage");
                SendMessage newMessage = new SendMessage().setReplyMarkup(examInteractionUtil.defaultReplyMarkup(message.getFrom())) // Create a SendMessage object with mandatory fields
                        .setChatId(message.getChatId())
                        .setParseMode("Markdown")
                        .setText(text)
                        .setReplyToMessageId(message.getMessageId());
                try {
                    bot.callApiMethod(newMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                    bot.sendError(message);
                }
                return true;
            }
        }
        return false;
    }
}
