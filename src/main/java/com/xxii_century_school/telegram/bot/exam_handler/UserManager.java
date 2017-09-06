package com.xxii_century_school.telegram.bot.exam_handler;

import com.xxii_century_school.telegram.bot.exam_handler.model.Exam;
import com.xxii_century_school.telegram.bot.exam_handler.model.Question;
import org.telegram.telegrambots.api.objects.User;

public interface UserManager {
    boolean isInExam(User user);

    Exam getCurrentExam(User user);

    Question getCurrentQuestion(User user);

    void nextQuestion(User user);

    void startExam(User user, int examId);
}
