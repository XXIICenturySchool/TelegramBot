package com.xxii_century_school.telegram;

import com.marasm.jtdispatch.DispatchQueue;
import com.marasm.jtdispatch.SerialQueue;
import com.xxii_century_school.telegram.bot.QueueManager;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class FixedSizePoolQueueManager implements QueueManager {
    List<DispatchQueue> pool = new ArrayList<>();

    @PostConstruct
    void init() {
        for (int i = 0; i < getCpuCoresPoolSize(); i++) {
            pool.add(SerialQueue.get(getClass().getName() + ".queue_" + i));
        }
    }


    @Override
    public DispatchQueue getQueue(int userId) {
        return pool.get(userId % pool.size());
    }


    private int getCpuCoresPoolSize() {
        int cores = Runtime.getRuntime().availableProcessors();
        return cores > 1 ? cores : 2;
    }
}
