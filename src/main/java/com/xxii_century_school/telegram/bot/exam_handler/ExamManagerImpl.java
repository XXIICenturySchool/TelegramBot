package com.xxii_century_school.telegram.bot.exam_handler;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.xxii_century_school.telegram.bot.exam_handler.model.Exam;
import javafx.util.Pair;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ExamManagerImpl implements ExamManager {
    @Autowired
    ExamQuestionsLoader loader;

    Cache<Pair<Integer, Integer>, Exam> examCache = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .maximumSize(128)
            .build(new CacheLoader<Pair<Integer, Integer>, Exam>() {
                @Override
                public Exam load(Pair<Integer, Integer> key) throws Exception {
                    return loader.loadExam(key.getKey(), key.getValue());
                }
            });

    @Override
    @SneakyThrows
    public Exam getExam(int examId, int teacherId) {
        Exam e = examCache.get(new Pair<Integer, Integer>(examId, teacherId), () -> loader.loadExam(examId, teacherId));
        return e;
    }
}
