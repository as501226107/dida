package com.dream.bean;

import org.activiti.engine.task.Task;

public class MyTask {
    private Task task;
    private Apply apply;
    private User user;
    public MyTask(Task task, Apply apply,User user) {
        this.task = task;
        this.apply = apply;
        this.user=user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Apply getApply() {
        return apply;
    }

    public void setApply(Apply apply) {
        this.apply = apply;
    }
}
