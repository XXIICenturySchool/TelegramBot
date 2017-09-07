package com.xxii_century_school.telegram.bot.message_handlers;

import com.marasm.jtdispatch.ConcurrentQueue;
import com.xxii_century_school.telegram.bot.ExamBot;
import com.xxii_century_school.telegram.bot.MessageHandler;
import com.xxii_century_school.telegram.bot.exam_handler.ExamInteractionUtil;
import com.xxii_century_school.telegram.bot.exam_handler.UserManager;
import com.xxii_century_school.telegram.bot.localization.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;


public class ExamIdMessageHandler implements MessageHandler {
    @Autowired
    UserManager userManager;

    @Autowired
    ExamInteractionUtil examInteractionUtil;

    @Autowired
    Localization localization;

    @Override
    public boolean handleMessage(Message message, ExamBot bot) {
        if (message.hasText()) {
            try {
                int number = Integer.parseInt(message.getText());
                if (!userManager.isInExam(message.getFrom())) {
                    startNewExam(message, number, bot);
                    return true;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    private void startNewExam(Message message, int examId, ExamBot bot) {
        ConcurrentQueue.get(getClass().getName() + ".queue", 32).async(() -> {
            try {
                String locale = message.getFrom().getLanguageCode();
                SendMessage sendMessage = new SendMessage()
                        .setChatId(message.getChatId())
                        .setReplyToMessageId(message.getMessageId())
                        .setParseMode("Markdown")
                        .setText(localization.get(locale).getMessage("downloadingExam"));
                try {
                    bot.callApiMethod(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                if (!userManager.startExam(message.getFrom(), examId)) {
                    sendMessage = new SendMessage()
                            .setChatId(message.getChatId())
                            .setReplyToMessageId(message.getMessageId())
                            .setParseMode("Markdown")
                            .setText(localization.get(locale).getMessage("invalidExamID") + examId);
                    try {
                        bot.callApiMethod(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessage = new SendMessage()
                            .setChatId(message.getChatId())
                            .setReplyToMessageId(message.getMessageId())
                            .setParseMode("Markdown")
                            .setText(localization.get(locale).getMessage("examStart"));
                    bot.callApiMethod(sendMessage);
                    examInteractionUtil.sendQuestionNumber(message, bot);
                    examInteractionUtil.sendCurrentQuestion(message, bot);
                }
            } catch (Exception e) {
                bot.sendError(message);
                e.printStackTrace();
            }
        });
    }
}