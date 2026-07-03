package com.example.bian.client.bushu;


import com.example.bian.email.SendEmail;
import okhttp3.*;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class T5 {

    /*
    * 问题1：
    *   打开微信窗口的快捷键，在微信已经处于输入状态时，会关闭微信的窗口
    * */


    static Long ShangCiFaSongShiJian = 0L;
    public static void main(String[] args) throws InterruptedException, IOException {


        jiLu("theme=dark; bnc-uuid=df6668ed-e5a3-40e4-9b15-22640e93cc9e; BNC_FV_KEY=33e8f2f16254aaa2b374cd8ea090c99cb3238c6e; se_gd=g5bDxQBoTHUGQ8L5aUhUgZZHQVRIUBVUlYOFRUU5lVVWwDFNWUQT1; se_gsd=BwM2PEp7LDcmIw03JhwyBVYHElMOAwYRUlRKW1JbU1VTDVNT1; BNC-Location=BINANCE; source=referral; campaign=accounts.binancezh.info; userPreferredCurrency=CNY_USD; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22416570327%22%2C%22first_id%22%3A%2218cc974b3561eb-0e38ad9dd061c38-26001951-2073600-18cc974b357f11%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMThjYzk3NGIzNTYxZWItMGUzOGFkOWRkMDYxYzM4LTI2MDAxOTUxLTIwNzM2MDAtMThjYzk3NGIzNTdmMTEiLCIkaWRlbnRpdHlfbG9naW5faWQiOiI0MTY1NzAzMjcifQ%3D%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%22416570327%22%7D%2C%22%24device_id%22%3A%2218cc974b3561eb-0e38ad9dd061c38-26001951-2073600-18cc974b357f11%22%7D; aliyungf_tc=fafbb8629a1cfdeec399aa36fcf9968be694b1a49c0387a4c5dc6dec65b8f2b5; se_sd=w9TUAXwcWRXDwkXMECxJgZZV1DA8QEZVlsNNfU05lZWVAEVNWU0S1; lang=zh-cn; BNC_FV_KEY_T=101-G7ZSyVmnnNV2dye0tP2zIbohxOtN%2FEvCgtjFavwlUsupCglMbEv4Xb%2BpJvRopdqjNvwBN18wVvAKEZ%2B5zNVBng%3D%3D-%2BLiAr9iy%2F09zBgMFyIFwEQ%3D%3D-0c; BNC_FV_KEY_EXPIRE=1707647741876; cr00=AFA59D849A93FF449BC4112240D64F0A; d1og=web.416570327.B4ABBDE4CB9BC76FDE5F6D94A5ADC912; r2o1=web.416570327.4994668EFDD36FA416A7A126C8A3CB96; f30l=web.416570327.BB838F34D876024DA47FBCFFB02FBA76; logined=y; p20t=web.416570327.E7504222E3FFDF34BFC86F6940B407B8");

        System.out.println("结束");
    }

    public static Request getLsRequest0(String text) {

        String getLsUrl = "https://tsn.baidu.com/text2audio?lan=zh&ctp=1&cuid=bDu1QoncRMNImUZ3ocBjyyYw&tok=24.1fee8b8a866c9aa057e3edbe46fe02d2.2592000.1632713333.282335-24502748&vol=9&per=0&spd=5&pit=5&aue=3&tex=%E6%B5%8B%E8%AF%95" + text;

        Request requestLs = new Request.Builder()
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cookie", "BIDUPSID=003A79927E3A3005E1F25A504CC7E545; PSTM=1629968089; BAIDUID=003A79927E3A3005B0268C172000510D:FG=1; __yjs_duid=1_dd5f60f175d6ea463cd21c07e191ab841629968285262; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; BA_HECTOR=a02k2520012ha484sq1gijb6b0q; ab_sr=1.0.1_ZjRiMTFmMjFjZjk3OWEyM2RhMTNjNDIyZWNjMDE3NDM1ZTE5MmRmZWU0Y2ZkOWM3ZDA0ODRkYzBiMDI5M2ZhNmVmMjcyNjc3M2YwNTQ4YTMwMWQxZGNkY2E4N2Q1MGQ3NTZjNGE3M2JmMmJhYWM0ZWYyYjM0YjA4NGNhNDJjNDYyNmUyZGJkYzJkODYxNWUwZTgyNTU5ZGY3ZTM1ZTg1NA==; BAIDUID_BFESS=056CC998B69A8C086CC7AC78233D9C3F:FG=1")
                .addHeader("Host", "tsn.baidu.com")
                .addHeader("Pragma", "no-cache")
                .addHeader("sec-ch-ua", "\"Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"92\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("Sec-Fetch-Dest", "document")
                .addHeader("Sec-Fetch-Mode", "navigate")
                .addHeader("Sec-Fetch-Site", "none")
                .addHeader("Sec-Fetch-User", "?1")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36")
                .url(getLsUrl)
                .get()
                .build();
        return requestLs;
    }

    public static Request getLsRequest(String text) {

        String getLsUrl = "https://tsn.baidu.com/text2audio?lan=zh&ctp=1&cuid=bDu1QoncRMNImUZ3ocBjyyYw&tok=24.1fee8b8a866c9aa057e3edbe46fe02d2.2592000.1632713333.282335-24502748&vol=9&per=0&spd=5&pit=5&aue=3&tex=%E6%B5%8B%E8%AF%95" + text;

        Request requestLs = new Request.Builder()
                .addHeader("Accept", "*/*")
                .addHeader("Accept-Encoding", "identity;q=1, *;q=0")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cookie", "BIDUPSID=003A79927E3A3005E1F25A504CC7E545; PSTM=1629968089; BAIDUID=003A79927E3A3005B0268C172000510D:FG=1; __yjs_duid=1_dd5f60f175d6ea463cd21c07e191ab841629968285262; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; BA_HECTOR=a02k2520012ha484sq1gijb6b0q; ab_sr=1.0.1_ZjRiMTFmMjFjZjk3OWEyM2RhMTNjNDIyZWNjMDE3NDM1ZTE5MmRmZWU0Y2ZkOWM3ZDA0ODRkYzBiMDI5M2ZhNmVmMjcyNjc3M2YwNTQ4YTMwMWQxZGNkY2E4N2Q1MGQ3NTZjNGE3M2JmMmJhYWM0ZWYyYjM0YjA4NGNhNDJjNDYyNmUyZGJkYzJkODYxNWUwZTgyNTU5ZGY3ZTM1ZTg1NA==; BAIDUID_BFESS=056CC998B69A8C086CC7AC78233D9C3F:FG=1")
                .addHeader("Host", "tsn.baidu.com")
                .addHeader("Pragma", "no-cache")
                .addHeader("Range", "bytes=0-")
                .addHeader("Referer", "https://tsn.baidu.com/text2audio?lan=zh&ctp=1&cuid=bDu1QoncRMNImUZ3ocBjyyYw&tok=24.1fee8b8a866c9aa057e3edbe46fe02d2.2592000.1632713333.282335-24502748&vol=9&per=0&spd=5&pit=5&aue=3&tex=%E6%B5%8B%E8%AF%95" + text)
                .addHeader("sec-ch-ua", "\"Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"92\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("Sec-Fetch-Dest", "video")
                .addHeader("Sec-Fetch-Mode", "no-cors")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36")
                .url(getLsUrl)
                .get()
                .build();
        return requestLs;
    }

    //我也可以收到
    public static void sendMe(String txt) throws InterruptedException {
//        return;
        Long currentTime = System.currentTimeMillis();
        if ((currentTime - ShangCiFaSongShiJian) > 1000 * 20) {
            ShangCiFaSongShiJian = currentTime;
        } else {
            return;
        }
        T6 t6 = new T6(txt, true);
        FutureTask ft = new FutureTask<>(t6);
        new Thread(ft).start();
    }

    public static void searchAll(String txt){
//        return;
        Long currentTime = System.currentTimeMillis();
        if ((currentTime - ShangCiFaSongShiJian) > 1000 * 20) {
            ShangCiFaSongShiJian = currentTime;
        } else {
            return;
        }
        T6 t6 = new T6(txt);
        FutureTask ft = new FutureTask<>(t6);
        new Thread(ft).start();
    }

    //去除了时间限制，一定会发送，发送给我自己
    public static void searchAgain(String txt) throws InterruptedException {

        T6 t6 = new T6(txt, false);
        FutureTask ft = new FutureTask<>(t6);
        new Thread(ft).start();
    }

    public static void jiLu(String txt) throws InterruptedException {
        T7 t7 = new T7(txt);
        FutureTask ft = new FutureTask<>(t7);
        new Thread(ft).start();
    }

}

class T6 implements Callable {

    // 好友昵称，可以是微信群名称
//    static String friendNickName = "文件传输助手";
    static String friendNickName = "恭喜发财2021";
    static Map<String,String> map = System.getenv();

    String txt;
    Boolean isMe = false;
    public T6(String txt){
        this.txt = txt;
    }

    public T6(String txt, Boolean isMe){
        this.txt = txt;
        this.isMe = isMe;
    }

    @Override
    public Object call() throws Exception {

        //发邮件
//        SendEmail.method(map.get("COMPUTERNAME") + "的电脑发送的信息", txt, isMe);
        SendEmail.method(PrivateConfig.computer, txt, isMe);


        //发微信
//        sendMe(map.get("COMPUTERNAME") + "的电脑发送的信息，" + txt);

        //只能使外接的音箱发声音。电脑主板上的喇叭不响
        /*for(int i=0; i<3; i++) {
            try {
                Toolkit.getDefaultToolkit().beep();
                Thread.sleep(2000);
            } catch (Exception e) {
            }
        }*/
        return null;
    }




    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis())); // 时间戳转换日期
    }

    private static void sendMsg(String txt) throws InterruptedException {

        String[] mottoes = {
                txt,
                txt,
                txt
        };
        for (String motto : mottoes) {
            sendOneMsg(motto);
        }
        Thread.sleep(2000);

//        sendOneMsg("[得意]就问你，腻不腻害！");
    }

    private static void sendOneMsg(String msg) {
        // 创建Robot对象
        Robot robot = getRobot();
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 将字符串复制到剪切板
        Transferable tText = new StringSelection(msg);
        clip.setContents(tText, null);
        // 以下两行按下了ctrl+v，完成粘贴功能
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        // 回车发送
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.delay(2000);
    }

    private static Robot getRobot(){
        // 创建Robot对象
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return robot;
    }
}

class T7 implements Callable {

    static Map<String,String> map = System.getenv();

    String txt;
    public T7(String txt){
        this.txt = txt;
    }


    @Override
    public Object call() throws Exception {

        //发邮件
//        SendEmail.methodMe(txt, map.get("COMPUTERNAME") + "的电脑发送的信息");
        SendEmail.methodMe(txt, PrivateConfig.computer);

        return null;
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis())); // 时间戳转换日期
    }



}