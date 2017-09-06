package com.xxii_century_school.telegram.bot.exam_handler;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.xxii_century_school.telegram.bot.exam_handler.model.Exam;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public class ExamManagerImpl implements ExamManager {
    @Autowired
    ExamQuestionsLoader loader;

    Cache<Integer, Exam> examCache = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .maximumSize(128)
            .build(new CacheLoader<Integer, Exam>() {
                @Override
                public Exam load(Integer key) throws Exception {
                    return loader.loadExam(key);
                }
            });

    @Override
    @SneakyThrows
    public Exam getExam(int examId) {
        Exam e = examCache.get(examId, () -> loader.loadExam(examId));
        return e;
    }
}
