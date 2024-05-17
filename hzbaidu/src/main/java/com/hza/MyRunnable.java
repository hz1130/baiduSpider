package com.hza;

import com.hza.api.BaiduApi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


/**
 * ExecuTors获取ExecutorService，然后调用方法，提交任务
 */
public class MyRunnable {

    static final int threadPoolSize = 5;

    private static void test2() {
        //1.使用工厂类获取线程池对象
        ExecutorService es = Executors.newFixedThreadPool(threadPoolSize, new ThreadFactory() {
            int n = 1;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "自定义的线程名称" + n++);
            }
        });
        //2.提交任务
        for (int i = 1; i <= threadPoolSize; i++) {
            es.submit(new MyRunnable2(i));
        }
    }

    public static void main(String[] args) {
        test2();
    }


}

/**
 * 任务类，包含一个任务编号，在任务中打印出是那一个线程正在执行任务
 */
class MyRunnable2 implements Runnable {
    private int id;

    public MyRunnable2(int id) {
        this.id = id;
    }

    BaiduApi createTokenApi = new BaiduApi();

    @Override
    public void run() {
        try {
            createTokenApi.HzaRun();
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取线程的名称
//        String name = Thread.currentThread().getName();
//        System.out.println(name + "执行了任务" + id);
    }

}


