package com.example.bian.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.lib.OkHttpClientEx;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by adimn on 2021/6/24. 查询老师的交易记录
 */
public class ChaXun {

    static String id349 = "24";//老的是id 126000  0.0004=50
//    static String id349 = "35";//手术刀 11400 0.005=57

    public static void main(String[] args) throws IOException, InterruptedException {
        OkHttpClient client = OkHttpClientEx.getOk();
        Response response = login(client);
        int i = 32;
        Map<String, Integer> map = new HashMap<>();//每个币种老师交易的次数
        String classPath = new File("").getAbsolutePath();

        File file = new File(classPath + "\\laoshi.txt");
        /*if (fileLog.exists()) {
            fileLog.delete();
        }
        fileLog.createNewFile();*/
        FileWriter fileWriter = new FileWriter(file, true);
        try {
            while (true) {

                Request lastRequest = getLastRequest(response, i);
                Call callLast = client.newCall(lastRequest);
                Response responseLast = callLast.execute();
                String html = responseLast.body().string();
                Document doc = Jsoup.parse(html);
                Element body = doc.body();

                for (int j = 0; j < 10; j++) {
                    String time = body.getElementById("tbddd").child(j).getAllElements().get(1).childNodes().get(0).toString();
                    String symbol = body.getElementById("tbddd").child(j).getAllElements().get(2).childNodes().get(0).toString();
                    String caoZuo = body.getElementById("tbddd").child(j).getAllElements().get(3).childNodes().get(0).toString();
                    String shuLiang = body.getElementById("tbddd").child(j).getAllElements().get(4).childNodes().get(0).toString();
                    String jiaGe = body.getElementById("tbddd").child(j).getAllElements().get(5).childNodes().get(0).toString();

                    if (map.get(symbol) == null) {
                        map.put(symbol, 1);
                    } else {
                        map.put(symbol, map.get(symbol) + 1);
                    }

                    fileWriter.write(time + " " + symbol + " " + caoZuo + " " + jiaGe);
                    fileWriter.write("\n");

                }

                System.out.println(i);
                i++;
                Thread.sleep(10000);
                if (i == 104) {
                    break;
                }
            }
        } catch (Exception e) {

        }
        fileWriter.flush();
        fileWriter.close();

        System.out.println(map);

    }



    public static Response login(OkHttpClient client) throws IOException {
        String urlLogin = "http://coolco.vip/doLogin";
        FormBody formBody = new FormBody.Builder()
                .add("userName", "dale121")
                .add("password", "hulong1226")
                .build();

        Request request = new Request.Builder()
                .url(urlLogin)
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        Response response = call.execute();
        JSONObject json = JSON.parseObject(response.body().string());
        System.out.println(json);
        return response;
    }

    public static Request getLastRequest(Response response, int i){
        Request requestLast = new Request.Builder()
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9,zh-TW;q=0.8")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Cookie", response.header("Set-Cookie"))
                .addHeader("Host", "coolco.vip")
                .addHeader("Pragma", "no-cache")
                .addHeader("Proxy-Connection", "keep-alive")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4503.5 Safari/537.36")
                .url("http://coolco.vip/track?pn=" + i + "&id="+id349)
                .get()
                .build();
        return requestLast;
    }
}
