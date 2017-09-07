package com.xxii_century_school.telegram.bot;

import com.marasm.jtdispatch.DispatchQueue;
import com.marasm.jtdispatch.SerialQueue;
import com.xxii_century_school.telegram.bot.localization.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class ExamBotImpl extends TelegramLongPollingBot implements ExamBot {
    @Autowired
    Localization localization;

    @Autowired
    QueueManager queueManager;

    @Resource(name = "messageHandlers")
    private List<MessageHandler> handlers;

    public void setHandlers(List<MessageHandler> handlers) {
        this.handlers = handlers;
    }


    @PostConstruct
    void init() {
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage() != null) {
            ExamBot bot = this;
            queueManager.getQueue(update.getMessage().getFrom().getId()).async(() -> {
                Chat chat = update.getMessage().getChat();
                for (MessageHandler handler : handlers) {
                    if (handler.handleMessage(update.getMessage(), bot)) {
                        return;
                    }
                }
                Message message = update.getMessage();
                String text = localization.get(message.getFrom().getLanguageCode()).localize("illegalCommand");
                try {
                    callApiMethod(new SendMessage().setChatId(chat.getId()).setReplyToMessageId(message.getMessageId()).setText(text).setParseMode("Markdown"));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                    sendError(message);
                }
            });
        }
    }

    @Override
    public String getBotUsername() {
        return "XXII_century_examinator_bot";
    }

    @Override
    public String getBotToken() {
        return "427525056:AAFeD75_heirrNwVm6gISSzJPiRnQen4PeY";
    }

    @Override
    public void onClosing() {

    }

    @Override
    public Message callApiMethod(BotApiMethod method) throws TelegramApiException {
        return (Message) sendApiMethod(method);
    }

    Map<Message, Void> messagesWithErrors = new WeakHashMap<>();
    DispatchQueue errorQueue = SerialQueue.get("com.xxii_century_school.telegram.bot.ExamBotImpl.errorQueue");

    @Override
    public void sendError(Message message) {
        errorQueue.sync(() -> {
            if (messagesWithErrors.containsKey(message)) {
                return;
            }
            messagesWithErrors.put(message, null);
            String text = localization.get(message.getFrom().getLanguageCode()).getMessage("error");
            try {
                callApiMethod(new SendMessage().setChatId(message.getChat().getId()).setReplyToMessageId(message.getMessageId()).setText(text).setParseMode("Markdown"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        });
    }
}
