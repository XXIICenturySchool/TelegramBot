package com.xxii_century_school.telegram.bot.exam_handler;

import com.xxii_century_school.telegram.bot.exam_handler.model.Exam;
import com.xxii_century_school.telegram.bot.exam_handler.model.Question;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExamQuestionsLoaderImpl implements ExamQuestionsLoader {
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    DiscoveryClient discoveryClient;

    @Override
    public Exam loadExam(int id) {
        //return restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Exam.class);
        return null;//todo
    }

    @Override
    public Question loadQuestion(int examId, int questionId) {
        return null;
    }

    @SneakyThrows
    private String getServiceURL() {
        ServiceInstance serviceInstance = discoveryClient.getInstances("").get(0);
        return serviceInstance.getUri().toURL().toString();
    }
}
