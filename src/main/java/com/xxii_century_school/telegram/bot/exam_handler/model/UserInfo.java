package com.xxii_century_school.telegram.bot.exam_handler.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class UserInfo implements Serializable {
    private int userId;

    private Integer currentExamId;
    private Integer currentQuestionId;

    private List<Boolean> answerResults = new ArrayList<>();
    private Integer teacherId;
    private Set<String> currentAnswers = new HashSet<>();
}
