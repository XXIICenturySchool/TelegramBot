package com.xxii_century_school.telegram.bot;

import com.marasm.jtdispatch.DispatchQueue;

public interface QueueManager {
    DispatchQueue getQueue(int userId);
}
