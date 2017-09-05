package com.xxii_century_school.telegram.bot;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.generics.LongPollingBot;

public interface ExamBot extends LongPollingBot {
    @Override
    void onUpdateReceived(Update update);

    @Override
    String getBotUsername();

    @Override
    String getBotToken();
}
