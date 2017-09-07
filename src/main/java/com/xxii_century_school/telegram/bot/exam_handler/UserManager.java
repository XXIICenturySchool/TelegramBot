package com.xxii_century_school.telegram.bot.exam_handler;

import com.xxii_century_school.telegram.bot.exam_handler.model.Exam;
import com.xxii_century_school.telegram.bot.exam_handler.model.Question;
import org.telegram.telegrambots.api.objects.User;

import java.util.List;
import java.util.Set;

public interface UserManager {
    boolean isInExam(User user);

    boolean hasTeacher(User user);

    Integer getTeacherId(User user);

    void setTeacherId(User user, Integer teacherId);

    Exam getCurrentExam(User user);

    Question getCurrentQuestion(User user);

    Integer getCurrentQuestionId(User user);

    void nextQuestion(User user, boolean wasCorrect);

    boolean startExam(User user, int examId);

    List<Boolean> getAnswerResults(User user);

    void addAnswerResult(User user, boolean result);

    void endCurrentExam(User user);

    Set<String> getCurrentAnswers(User user);

    void addCurrendtAnswer(User user, String currentAnswer);

    void endCurrentQuestion(User user);
}
