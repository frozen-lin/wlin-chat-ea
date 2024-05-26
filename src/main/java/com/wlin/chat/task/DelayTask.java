package com.wlin.chat.task;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
 
public class DelayTask implements Delayed {
    final private TaskBase data;
    final private long expire;
 
    /**
     * 构造延时任务
     * @param data      业务数据
     * @param expire    任务延时时间（ms）
     */
    public DelayTask(TaskBase data, long expire) {
        super();
        this.data = data;
        this.expire = expire + System.currentTimeMillis();
    }
 
    public TaskBase getData() {
        return data;
    }
 
    public long getExpire() {
        return expire;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DelayTask) {
            return this.data.equals(((DelayTask) obj).getData());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public String toString() {
        return "{" + "data:" + data.toString() + "," + "expire:" + new Date(expire) + "}";
    }
 
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expire - System.currentTimeMillis(), unit);
    }
 
    @Override
    public int compareTo(Delayed o) {
        long delta = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return (int) delta;
    }
}