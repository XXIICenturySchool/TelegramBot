package com.xxii_century_school.telegram.bot.exam_handler;

import com.google.gson.Gson;
import com.xxii_century_school.telegram.bot.Services;
import com.xxii_century_school.telegram.bot.exam_handler.model.Exam;
import com.xxii_century_school.telegram.bot.exam_handler.model.Question;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Log
public class ExamQuestionsLoaderImpl implements ExamQuestionsLoader {
    private Gson gson = new Gson();

    @Autowired
    DiscoveryClient discoveryClient;

    @Override
    @SneakyThrows
    public Exam loadExam(int id, int teacherId) {
        ServiceInstance serviceInstance = Services.GATE.pickRandomInstance(discoveryClient);
        String loadExamURL = serviceInstance.getUri().toString() + "/exam/getExam?examId=" + id + "&teacherId=" + teacherId;
        RestTemplate restTemplate = new RestTemplate();
        log.info("loading exam from " + loadExamURL);
        ResponseEntity<Exam> examResponseEntity = restTemplate.getForEntity(loadExamURL, Exam.class);
        Exam e = examResponseEntity.getBody();
        return e;
    }

    @Override
    public Question loadQuestion(int examId, int questionId) {
        return null;
    }

}
