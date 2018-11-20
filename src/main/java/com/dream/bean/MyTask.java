package com.dream.bean;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;

public class MyTask {
    private HistoricTaskInstance htask;
    private Apply apply;
    private User user;
    private Task task;

    public MyTask(Task task, Apply apply, User user) {
        this.task = task;
        this.apply = apply;
        this.user=user;
    }


    public MyTask(HistoricTaskInstance htask, Apply apply, User user) {
        this.htask = htask;
        this.apply = apply;
        this.user=user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Apply getApply() {
        return apply;
    }

    public void setApply(Apply apply) {
        this.apply = apply;
    }

    public HistoricTaskInstance getHtask() {
        return htask;
    }

    public void setHtask(HistoricTaskInstance htask) {
        this.htask = htask;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
}
