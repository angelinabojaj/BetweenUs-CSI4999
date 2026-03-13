package com.example.betweenus.model;

public class Task {
    private int taskId;
    private int taskCreator;
    private String taskGoal;
    private String dueDate;
    private boolean isComplete;

    public Task(int taskId, int taskCreator, String taskGoal, String dueDate, boolean isComplete) {
        this.taskId = taskId;
        this.taskCreator = taskCreator;
        this.taskGoal = taskGoal;
        this.dueDate = dueDate;
        this.isComplete = isComplete;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getTaskCreator(){return taskCreator;}

    public String getTaskGoal() {
        return taskGoal;
    }

    public String getDueDate() {
        return dueDate;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
