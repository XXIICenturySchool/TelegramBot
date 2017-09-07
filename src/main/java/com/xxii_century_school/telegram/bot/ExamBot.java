package com.xxii_century_school.telegram.bot;

import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
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

    Message callApiMethod(BotApiMethod method) throws TelegramApiException;

    Message sendPhoto(SendPhoto sendPhoto) throws TelegramApiException;

    void sendError(Message message);
}
