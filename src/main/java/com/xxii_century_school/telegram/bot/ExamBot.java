package com.xxii_century_school.telegram.bot;

import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.generics.LongPollingBot;

public interface ExamBot extends LongPollingBot {
    @Override
    void onUpdateReceived(Update update);

    @Override
    String getBotUsername();

    @Override
    String getBotToken();

    void callApiMethod(BotApiMethod method) throws TelegramApiException;

    void sendError(Message message);
}
