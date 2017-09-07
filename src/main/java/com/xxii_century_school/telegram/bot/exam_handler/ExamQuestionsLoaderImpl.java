package com.xxii_century_school.telegram.bot.exam_handler;

import com.google.gson.Gson;
import com.xxii_century_school.telegram.bot.Services;
import com.xxii_century_school.telegram.bot.exam_handler.model.Exam;
import com.xxii_century_school.telegram.bot.exam_handler.model.Question;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

@Service
@Log
public class ExamQuestionsLoaderImpl implements ExamQuestionsLoader {
    private Gson gson = new Gson();

    @Autowired
    DiscoveryClient discoveryClient;

    @Override
    @SneakyThrows
    public Exam loadExam(int id) {
        ServiceInstance serviceInstance = Services.MAPLOGIN.pickRandomInstance(discoveryClient);
        String loadExamUrl = serviceInstance.getUri().toString() + "/exam/" + id + "/all";
        log.info("loading exam from " + loadExamUrl);
        InputStream stream = new URL(loadExamUrl).openStream();
        Exam e = gson.fromJson(IOUtils.toString(stream, Charset.defaultCharset()), Exam.class);
        return e;
    }

    @Override
    public Question loadQuestion(int examId, int questionId) {
        return null;
    }

}
