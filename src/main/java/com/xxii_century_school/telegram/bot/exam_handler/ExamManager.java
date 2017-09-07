package com.xxii_century_school.telegram.bot.exam_handler;

import com.xxii_century_school.telegram.bot.exam_handler.model.Exam;

public interface ExamManager {
    Exam getExam(int examId, int teacherId);
}
