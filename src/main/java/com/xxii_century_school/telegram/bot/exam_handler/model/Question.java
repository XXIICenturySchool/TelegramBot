package com.xxii_century_school.telegram.bot.exam_handler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Question {
    private String question;
    private String pictureUrl;
    private List<String> options;
    private List<String> answer;


    public boolean isMultiChoise() {
        return answer.size() > 1;
    }
}