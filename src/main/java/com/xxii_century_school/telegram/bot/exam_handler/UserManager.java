package com.xxii_century_school.telegram.bot.exam_handler;

import com.xxii_century_school.telegram.bot.exam_handler.model.Exam;
import com.xxii_century_school.telegram.bot.exam_handler.model.Question;
import com.xxii_century_school.telegram.bot.exam_handler.model.UserInfo;
import org.telegram.telegrambots.api.objects.User;

import java.util.Date;
import java.util.Set;

public interface UserManager {
    boolean isInExam(User user);

    boolean hasTeacher(User user);

    Integer getTeacherId(User user);

    void setTeacherId(User user, Integer teacherId);

    Exam getCurrentExam(User user);

    Question getCurrentQuestion(User user);

    Integer getCurrentQuestionId(User user);

    void nextQuestion(User user, boolean wasCorrect, boolean skipped);

    boolean startExam(User user, int examId);

    void skipCurrentQuestion(User user);

    void failCurrentQuestion(User user);

    void endCurrentExam(User user);

    Set<String> getCurrentAnswers(User user);

    void addCurrentAnswer(User user, String currentAnswer);

    void endCurrentQuestion(User user);

    Date getExamStartDate(User user);

    UserInfo getUserInfo(User from);
}
