package com.xxii_century_school.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class WebUI {
    @Autowired
    DiscoveryClient discoveryClient;

    @RequestMapping("/services")
    public List<String> discovery() {
        return discoveryClient.getServices();
    }
    @RequestMapping("instances/{name}")
    public List<ServiceInstance> instances(@PathVariable(value = "name") String name){
        return discoveryClient.getInstances(name);
    }

}
