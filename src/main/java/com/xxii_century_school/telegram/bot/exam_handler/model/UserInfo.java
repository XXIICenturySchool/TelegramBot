package com.xxii_century_school.telegram.bot.exam_handler.model;

import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
    private Integer currentExamId;
    private Integer currentQuestionId;

    private List<Boolean> answerResults;
}
