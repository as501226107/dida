package com.dream.service;

import com.dream.bean.Apply;
import com.dream.bean.Approve;
import com.dream.bean.User;
import org.activiti.engine.task.Task;

import java.util.Map;

public interface ProcessService {
    public int apply(Apply apply, User user);
    public int aprrove(Approve approve,String taskId);
    Map<String, Object> findCoordingByTask(Task task);
}
