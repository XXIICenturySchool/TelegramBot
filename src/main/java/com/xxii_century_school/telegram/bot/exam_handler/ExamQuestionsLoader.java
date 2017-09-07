package com.xxii_century_school.telegram.bot.exam_handler;

import com.xxii_century_school.telegram.bot.exam_handler.model.Exam;
import com.xxii_century_school.telegram.bot.exam_handler.model.Question;

public interface ExamQuestionsLoader {
    Exam loadExam(int id, int teacherId);

    Question loadQuestion(int examId, int questionId);
}
