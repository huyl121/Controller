package com.example.bian;

import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.RequestOptions;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.client.model.trade.Leverage;

import java.util.*;



/**
 * Created by adimn on 2021/8/2.
 */
public class ChangeInitialLeverage {

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("https.proxySet", "true");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyPort", "10809");
        ChangeInitialLeverage postOrder = new ChangeInitialLeverage();

//        postOrder.method1();

        args = new String[1];
        System.out.println("开始啦");
        args[0] = "E://code//biance";
        PrivateConfig.before(args[0], "0");
        PrivateConfig.xsw(true);
        PrivateConfig.getListNew(args[0] + "//info.json");
        postOrder.method(PrivateConfig.genDan_personInfoList);


    }

    public void method(List<JSONObject> listPersonInfo) throws InterruptedException {

        Map<String, Integer> map = PrivateConfig.gsXsw;
        for (JSONObject personInfo : listPersonInfo) {

            try {
                SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));
//                System.out.println(personInfo.getString(PrivateConfig.alias) + "：" + syncRequestClient.getAccountInformation().getTotalMarginBalance());
                System.out.println(personInfo.getString(PrivateConfig.alias));

                Iterator iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) iterator.next();

                    try {
                        if ("BTCUSDT,ETHUSDT".contains(entry.getKey())) {
                            syncRequestClient.changeInitialLeverage(entry.getKey(), 20);
                        }
                        if (PrivateConfig.Leverage.contains(entry.getKey())) {
                            syncRequestClient.changeInitialLeverage(entry.getKey(), PrivateConfig.LeverageCount);
                        } else if (PrivateConfig.noLeverage.contains(entry.getKey())) {
                            //这里的是不用设置的
                            continue;
                        } else {
                            syncRequestClient.changeInitialLeverage(entry.getKey(), 20);
                        }
                        Thread.sleep(100);
                    } catch (Exception e) {
                        try {
                            //报错后的，设置成5，先让成功再说
                            syncRequestClient.changeInitialLeverage(entry.getKey(), 10);
                        } catch (Exception e1) {
                            try {
                                //报错后的，设置成5，先让成功再说
                                syncRequestClient.changeInitialLeverage(entry.getKey(), 5);
                            } catch (Exception e2) {
                                System.out.println(entry.getKey());
                                e2.printStackTrace();
                            }
                            System.out.println(entry.getKey());
                            e1.printStackTrace();
                        }
//                            T5.searchAll(entry.getKey() + ", 设置出错了5，收到抓紧联系胡亚龙");
                        System.out.println(entry.getKey());
                        e.printStackTrace();
                        Thread.sleep(100);
                    }
                }
                Thread.sleep(100);

            } catch (Exception e) {
                System.out.println(personInfo.getString(PrivateConfig.alias));
                e.printStackTrace();
            }
            Thread.sleep(3000);
        }
        System.out.println("设置完成");

    }

    public void method1() {

        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY,
                options);
        List<String> symbolList = new ArrayList<String>(){{
            add("NEARBUSD");
            add("ETCBUSD");
            add("AVAXBUSD");
            add("FILBUSD");
            add("PHBBUSD");
            add("GMTBUSD");
            add("SANDBUSD");
            add("UNIBUSD");
            add("TLMBUSD");
        }};
        for(String symbol : symbolList){
            syncRequestClient.changeInitialLeverage(symbol, 8);
        }

        System.out.println("设置完成");

    }
}
