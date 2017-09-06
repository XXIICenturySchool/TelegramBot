package com.xxii_century_school.telegram.bot;

import com.marasm.jtdispatch.ConcurrentQueue;
import com.marasm.jtdispatch.DispatchQueue;
import org.telegram.telegrambots.api.objects.Message;


public interface MessageHandler {

    DispatchQueue queue = ConcurrentQueue.get("com.xxii_century_school.telegram.bot.MessageHandler.queue");

    boolean handleMessage(Message message, ExamBot bot);
}
