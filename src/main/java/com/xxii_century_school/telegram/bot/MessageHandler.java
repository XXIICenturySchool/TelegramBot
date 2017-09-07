package com.xxii_century_school.telegram.bot;

import org.telegram.telegrambots.api.objects.Message;


public interface MessageHandler {
    boolean handleMessage(Message message, ExamBot bot);
}
