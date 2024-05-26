package com.wlin.chat.task;


import lombok.Data;

import java.util.Objects;
import java.util.function.Consumer;

@Data
public class TaskBase implements Runnable{
    private String objectType;
    private String objectNo;
    private Consumer<TaskBase> consumer;

    public TaskBase(String objectType, String objectNo, Consumer<TaskBase> consumer) {
        this.objectType = objectType;
        this.objectNo = objectNo;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        this.consumer.accept(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskBase taskBase = (TaskBase) o;

        if (!Objects.equals(objectType, taskBase.objectType)) return false;
        return Objects.equals(objectNo, taskBase.objectNo);
    }

    @Override
    public int hashCode() {
        int result = objectType != null ? objectType.hashCode() : 0;
        result = 31 * result + (objectNo != null ? objectNo.hashCode() : 0);
        return result;
    }
}