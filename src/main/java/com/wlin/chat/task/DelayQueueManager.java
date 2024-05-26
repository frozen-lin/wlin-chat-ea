package com.wlin.chat.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;
 
@Component
@Slf4j
public class DelayQueueManager implements CommandLineRunner {
    private DelayQueue<DelayTask> delayQueue = new DelayQueue<>();
    
    /**
     * 加入到延时队列中
     * @param task
     */
    public void put(DelayTask task) {
        log.info("加入延时任务：{}", task);
        delayQueue.put(task);
    }

    public void clear() {
        delayQueue.clear();
    }
        /**
         * 取消延时任务
         * @param task
         * @return
         */
    public boolean remove(DelayTask task) {
        log.info("取消延时任务：{}", task);
        return delayQueue.remove(task);
    }
 
    /**
     * 取消延时任务
     * @param taskBase
     * @return
     */
    public boolean remove(TaskBase taskBase) {
        return remove(new DelayTask(taskBase, 0));
    }
 
    @Override
    public void run(String... args) throws Exception {
        log.info("初始化延时队列");
        Executors.newSingleThreadExecutor().execute(this::excuteThread);
    }
 
    /**
     * 延时任务执行线程
     */
    private void excuteThread() {
        while (true) {
            try {
                DelayTask task = delayQueue.take();
                log.info("开始执行：{}", task);
                task.getData().run();
            } catch (InterruptedException e) {
                break;
            }
        }
    }

}