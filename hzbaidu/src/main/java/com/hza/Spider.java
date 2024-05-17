package com.hza;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hza.api.BaiduApi;
import com.hza.entity.ZybangTaskResponse;
import com.hza.utils.OkHttpUtil;
import okhttp3.*;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.hza.api.BaiduApi.*;


public class Spider {
    private static final int LIMIT = 5;

    private final static SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");
    private static final Gson gson = new Gson();

    /* 代理相关*/
    private final static String ip = "tps267.kdlapi.com";
    private final static String port = "15818";
    private final static String mode = "1";
    private final static String username = "t15096838352379";
    private final static String password = "0nm781nk";

    static int count = 0;

    public static void main(String[] args) {
        //创建线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(LIMIT,
                LIMIT * 2,
                5L,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(10),
                Executors.defaultThreadFactory());
        //死循环拿任务
        while (true) {
            // 时间控制
            try {
                String format = sdf2.format(new Date());
                int date = Integer.parseInt(format);
                if (date > 234955 || date < 65422) {
                    Thread.sleep(1000);
                    continue;
                }
            } catch (Exception err) {
                err("data exception: " + err.getMessage());
            }

            try {

                // 投递任务
                for (int i = 0; i < LIMIT; i++) {

                    // 如果队列满了，就等等！
                    while (threadPoolExecutor.getActiveCount() >= threadPoolExecutor.getCorePoolSize()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignore) {
                        }
                    }


                    threadPoolExecutor.execute(new Query());

//                    // 抓取速度
//                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class Query implements Runnable {
        String imgId;
        String srcImgUrl;
        String solarId;


        public Query() {
            this.imgId = imgId;
            this.srcImgUrl = srcImgUrl;
            this.solarId = solarId;
            info("已创建任务!");
        }


        @Override
        public void run() {
            int retry = 0;
            while (retry < 1) {
                retry++;

                boolean infoState = imgInfo();
                if (!infoState) {
                    continue;
                }

                Buid = getRanInArr(BuidArrays);
                try {
                    bug();
                    HzaRun();
                } catch (Exception e) {
                    e.printStackTrace();
                }


/*

                // 第一步 获取Sign  Data
                String threadSignData = null;
                try {
                    Thread.sleep(6000);
                    threadSignData = postToSign();
                } catch (Exception e) {
                }

//                获取Sign
                String threadSign = null;
                try {
                    threadSign = analysisJson(threadSignData);
                    Thread.sleep(1500);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                // 第二步 获取答案 html
                String threadHtml = null;
                try {
                    threadHtml = set_Sign(threadSign);
                } catch (Exception e) {
                    e.printStackTrace();
                }
*/

                // 第三步 写日志
//                try {
//                    putLog(threadHtml);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }


            }
        }


    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static void err(String msg) {
        System.err.println("[ " + sdf.format(new Date()) + " ] err: " + msg);
    }

    private static void info(String msg) {
        System.out.println("[ " + sdf.format(new Date()) + " ] info: " + msg);
    }

}
