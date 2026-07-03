package com.example;

import com.alibaba.fastjson.JSONObject;
import com.example.bian.ChaKan;
import com.example.bian.ChangeInitialLeverage;
import com.example.bian.client.bushu.PrivateConfig;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

import com.example.bian.xin.QingCang3;
import com.example.controller.JianKong4;
import com.example.controller.JianKongWangYe;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BianApplication {
    public static void main(String[] args)
            throws InterruptedException, IOException {
        SpringApplication.run(BianApplication.class,args);


        if(args.length==0){
            args = new String[2];
            args[0] = "D://model//Controller//biance";
            args[1] = "wangYe";

        }

        PrivateConfig.init((args[0]));
        if(PrivateConfig.daiLi.equals("1")) {
            PrivateConfig.printLog("开代理");
            System.setProperty("https.proxySet", "true");
            System.setProperty("https.proxyHost", "127.0.0.1");
            System.setProperty("https.proxyPort", "10819");
        }
        PrivateConfig.before(args[0], "0-"+args[1]);
        PrivateConfig.xsw(true);


        for(String s : args){
            System.out.println(s);
        }


        /*SpringApplicationBuilder builder = new SpringApplicationBuilder(new Class[]{BianApplication.class});
        builder.headless(false).run(args);*/
        List<JSONObject> personInfoList = PrivateConfig.genDan_personInfoList;

        if ("chaKan".equals(args[1])) {
            System.out.println("查看账号余额");

            ChaKan chaKan = new ChaKan();
            chaKan.method(personInfoList);

            System.out.println("查看完成");
        }


        if ("qingCang".equals(args[1])) {
            System.out.println("清仓");
            QingCang3 qingCang3 = new QingCang3();
            qingCang3.method(args, personInfoList);
            System.out.println("清仓完成");
        }



        if ("sheZhi".equals(args[1])) {
            System.out.println("设置杠杆");
            ChangeInitialLeverage changeInitialLeverage = new ChangeInitialLeverage();
            changeInitialLeverage.method(personInfoList);
            System.out.println("设置完成");
        }


        if ("wangYe".equals(args[1])) {
            ThreadPoolExecutor threadPoolExecutor =
                    new ThreadPoolExecutor(5, 5, 10,
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(),
                            Executors.defaultThreadFactory(),
                            new ThreadPoolExecutor.DiscardPolicy());
            PrivateConfig.threadPoolExecutor = threadPoolExecutor;

            //启动监控线程
            Callable callable1 = new Callable() {
                @Override
                public String call() throws Exception {
                    JianKongWangYe jianKongWangYe = new JianKongWangYe();
                    jianKongWangYe.method();
                    return null;
                }
            };
            threadPoolExecutor.submit(callable1);

            Callable callable2 = new Callable() {
                @Override
                public String call() throws Exception {
                    JianKong4 jianKong4 = new JianKong4();
                    jianKong4.method(threadPoolExecutor, PrivateConfig.genDan_personInfoList);
                    return "";
                }
            };
            threadPoolExecutor.submit(callable2);
        }


        System.out.println("启动成功");
    }

    public static void main1(String[] args)
            throws InterruptedException {
        SpringApplication.run(BianApplication.class, args);
    }
}
