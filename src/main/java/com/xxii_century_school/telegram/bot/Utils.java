package com.xxii_century_school.telegram.bot;

import java.util.List;

public class Utils {
    public static String formatAnswerOptions(List<String> answerOptions) {
        if (answerOptions == null) {
            return "Send me your answer";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < answerOptions.size(); i++) {
            result.append(i + ". " + answerOptions.get(i) + "\n");
        }
        return result.toString().trim();
    }

    public static String preparePictureURL(String pictureUrl) {
        if (pictureUrl.startsWith("http")) {
            return pictureUrl;
        }
        return "http://" + pictureUrl;
    }
}
