package com.xxii_century_school.telegram.bot.exam_handler.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserInfo implements Serializable {

    private String firstName;
    private String lastName;
    private String userName;

    private int userId;

    private Integer currentExamId;
    private Integer currentQuestionId;

    private int wrongAnswers;
    private int skippedAnswers;

    private Integer teacherId;
    private Set<String> currentAnswers = new HashSet<>();

    private Date examStart;
}
