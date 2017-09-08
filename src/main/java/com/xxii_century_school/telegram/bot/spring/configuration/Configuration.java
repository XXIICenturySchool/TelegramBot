package com.xxii_century_school.telegram.bot.spring.configuration;

import com.xxii_century_school.telegram.FixedSizePoolQueueManager;
import com.xxii_century_school.telegram.bot.ExamBot;
import com.xxii_century_school.telegram.bot.ExamBotImpl;
import com.xxii_century_school.telegram.bot.MessageHandler;
import com.xxii_century_school.telegram.bot.QueueManager;
import com.xxii_century_school.telegram.bot.exam_handler.ExamQuestionsLoader;
import com.xxii_century_school.telegram.bot.exam_handler.ExamQuestionsLoaderImpl;
import com.xxii_century_school.telegram.bot.message_handlers.*;
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

    @Autowired
    private AnswerMessageHandler answerMessageHandler;

    @Autowired
    EndExamMessageHandler endExamMessageHandler;

    @Autowired
    TeacherIdMessageHandler teacherIdMessageHandler;
    @Autowired
    TeacherMessageHandler teacherMessageHandler;
    @Autowired
    SkipMessageHandler skipMessageHandler;


    @Bean
    public List<MessageHandler> messageHandlers() {
        List<MessageHandler> aList = new ArrayList<>();
        aList.add(endExamMessageHandler);
        aList.add(skipMessageHandler);
        aList.add(startMessageHandler);
        aList.add(numberMessageHandler);
        aList.add(answerMessageHandler);
        aList.add(teacherIdMessageHandler);
        aList.add(teacherMessageHandler);
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

    @Bean
    ExamQuestionsLoader xamQuestionLoader() {
        return new ExamQuestionsLoaderImpl();
    }
}
