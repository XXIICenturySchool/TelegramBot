package com.xxii_century_school.telegram.bot;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

public enum Services {
    DISCOVERY("discovery"),
    MAPLOGIN("maplogin"),
    MAP("maplogin"),
    LOGIN("maplogin"),
    MATH("mathservice"),
    TELEGRAM("telegram"),
    AMERICAN_QUESTIONS("americanQuestions"),
    GEOMETRY("geometry"),
    GATE("gate");

    public final String id;

    Services(String id) {
        this.id = id;
    }

    private static final Random random = new Random(new Date().getTime());

    private static ServiceInstance pickRandomInstanceFromList(List<ServiceInstance> instances) {
        if (instances.size() == 0) {
            return null;
        }
        return instances.get(abs(random.nextInt(instances.size())) % instances.size());
    }

    public ServiceInstance pickRandomInstance(DiscoveryClient discoveryClient) {
        return pickRandomInstanceFromList(discoveryClient.getInstances(id));
    }
}
