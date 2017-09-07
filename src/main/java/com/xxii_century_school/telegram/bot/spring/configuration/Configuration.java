package com.xxii_century_school.telegram.bot.spring.configuration;

import com.xxii_century_school.telegram.FixedSizePoolQueueManager;
import com.xxii_century_school.telegram.bot.ExamBot;
import com.xxii_century_school.telegram.bot.ExamBotImpl;
import com.xxii_century_school.telegram.bot.MessageHandler;
import com.xxii_century_school.telegram.bot.QueueManager;
import com.xxii_century_school.telegram.bot.message_handlers.ExamIdMessageHandler;
import com.xxii_century_school.telegram.bot.message_handlers.StartMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Configuration {

    @Bean
    StartMessageHandler startMessageHandler() {
        return new StartMessageHandler();
    }

    @Bean
    ExamIdMessageHandler numberMessageHandler() {
        return new ExamIdMessageHandler();
    }

    @Autowired
    private StartMessageHandler startMessageHandler;

    @Autowired
    private ExamIdMessageHandler numberMessageHandler;

    @Bean
    public List<MessageHandler> messageHandlers() {
        List<MessageHandler> aList = new ArrayList<>();
        aList.add(startMessageHandler);
        aList.add(numberMessageHandler);
        return aList;
    }

    @Bean
    ExamBot examBot() {
        return new ExamBotImpl();
    }


    @Bean
    QueueManager queueManager() {
        return new FixedSizePoolQueueManager();
    }
}
