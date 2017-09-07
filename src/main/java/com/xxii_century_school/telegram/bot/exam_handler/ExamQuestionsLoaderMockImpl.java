package com.xxii_century_school.telegram.bot.exam_handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xxii_century_school.telegram.bot.exam_handler.model.Exam;
import com.xxii_century_school.telegram.bot.exam_handler.model.Question;

public class ExamQuestionsLoaderMockImpl implements ExamQuestionsLoader {
    @Override
    public Exam loadExam(int id, int teacherId) {
        String jsonStr = "{\"id\":\"59b155aed90320065429cd6d\",\"tasks\":[{\"type\":\"math.arithmetic\",\"question\":\"15+x = 18\",\"answer\":[\"3\"],\"options\":null},{\"type\":\"math.arithmetic\",\"question\":\"12+x = 18\",\"answer\":[\"6\"],\"options\":null},{\"type\":\"math.arithmetic\",\"question\":\"19+x = 22\",\"answer\":[\"3\"],\"options\":null},{\"type\":\"math.arithmetic\",\"question\":\"17+x = 24\",\"answer\":[\"7\"],\"options\":null},{\"type\":\"math.arithmetic\",\"question\":\"17+x = 21\",\"answer\":[\"4\"],\"options\":null},{\"type\":\"math.arithmetic\",\"question\":\"13+x = 21\",\"answer\":[\"8\"],\"options\":null},{\"type\":\"math.arithmetic\",\"question\":\"12+x = 15\",\"answer\":[\"3\"],\"options\":null},{\"type\":\"math.arithmetic\",\"question\":\"10+x = 15\",\"answer\":[\"5\"],\"options\":null},{\"type\":\"math.arithmetic\",\"question\":\"14+x = 22\",\"answer\":[\"8\"],\"options\":null},{\"type\":\"math.arithmetic\",\"question\":\"19+x = 26\",\"answer\":[\"7\"],\"options\":null},{\"type\":\"math.arithmetic\",\"question\":\"18+x = 23\",\"answer\":[\"5\"],\"options\":null},{\"type\":\"math.arithmetic\",\"question\":\"18+x = 24\",\"answer\":[\"6\"],\"options\":null}],\"teacherId\":\"1\"}";
        Gson gson = new GsonBuilder().serializeNulls().create();
        Exam e = gson.fromJson(jsonStr, Exam.class);
        return e;
    }

    @Override
    public Question loadQuestion(int examId, int questionId) {
        return null;
    }
}
