package com.xxii_century_school.telegram.bot.exam_handler.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ExamResult {
    private int examId;
    private int teacherId;
    private int numberOfSkipedAnswers;
    private int numberOfWrongAnswers;
    private int seconds;
    private String name;

    public ExamResult(UserInfo userInfo) {
        this(userInfo.getCurrentExamId(),
                userInfo.getTeacherId(),
                userInfo.getSkippedAnswers(),
                userInfo.getWrongAnswers(),
                (int) ((new Date().getTime() - userInfo.getExamStart().getTime()) / 1000L),
                userInfo.getFirstName() + " " + userInfo.getLastName());
    }
}