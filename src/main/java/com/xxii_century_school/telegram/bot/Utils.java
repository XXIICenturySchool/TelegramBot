package com.xxii_century_school.telegram.bot;

import java.util.List;

public class Utils {
    public static String formatAnswerOptions(List<String> answerOptions) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < answerOptions.size(); i++) {
            result.append(i + ". " + answerOptions.get(i));
        }
        return result.toString().trim();
    }
}
