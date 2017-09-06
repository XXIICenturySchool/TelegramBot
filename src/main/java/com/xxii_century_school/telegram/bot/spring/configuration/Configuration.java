package com.xxii_century_school.telegram.bot.spring.configuration;

import com.xxii_century_school.telegram.bot.ExamBot;
import com.xxii_century_school.telegram.bot.ExamBotImpl;
import com.xxii_century_school.telegram.bot.MessageHandler;
import com.xxii_century_school.telegram.bot.localization.Localization;
import com.xxii_century_school.telegram.bot.localization.LocalizationImpl;
import com.xxii_century_school.telegram.bot.message_handlers.StartMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Configuration {

    @Bean
    MessageHandler startMessageHandler() {
        return new StartMessageHandler();
    }

    @Autowired
    private MessageHandler startMessageHandler;

    @Bean
    public List<MessageHandler> messageHandlers() {
        List<MessageHandler> aList = new ArrayList<>();
        aList.add(startMessageHandler);
        return aList;
    }

    @Bean
    Localization localization() {
        return new LocalizationImpl();
    }

    @Bean
    ExamBot examBot() {
        return new ExamBotImpl();
    }
}
