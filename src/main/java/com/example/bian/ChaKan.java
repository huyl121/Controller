package com.example.bian;

import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;

import java.util.List;

public class ChaKan {
    public static void main(String[] args) {
        System.setProperty("https.proxySet", "true");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyPort", "10809");
        args = new String[1];
        System.out.println("开始啦");
        args[0] = "E://code//biance";
        PrivateConfig.before(args[0], "0");
        ChaKan postOrder = new ChaKan();
        postOrder.method(PrivateConfig.genDan_personInfoList);
    }

    public void method( List<JSONObject> listPersonInfo ) {

        for (JSONObject personInfo : listPersonInfo) {
            try {
                SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));
                System.out.println(personInfo.getString(PrivateConfig.alias) + "：" + syncRequestClient.getAccountInformation().getTotalMarginBalance());
//                System.out.println(personInfo.getString(PrivateConfig.alias) + "：" + syncRequestClient.getAccountInformation());
            } catch (Exception e) {
                System.out.println(personInfo.getString(PrivateConfig.alias));
                e.printStackTrace();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}