package com.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.bian.ChangeInitialLeverage;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.client.model.trade.AccountInformation;
import com.example.bian.client.model.trade.Position;
import com.example.bian.genDan.*;
import com.example.bian.xin.QingCang3;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import static com.example.bian.client.bushu.PrivateConfig.*;

/**
 * 根据老师现有的持仓，自己分析他是加仓还是减仓
 */
public class JianKong4 {

    static Boolean isBaoJing = false;

    public static void main(String[] args) throws IOException, InterruptedException {

        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10819");

        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(1,
                        1,
                        10,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.DiscardPolicy());


        System.out.println("bnc-uuid=5207c285-8ded-465c-b32c-b8a179a931f9; source=organic; campaign=www.google.com.hk; BNC_FV_KEY=32cff046e95aad60abddf679ef4b55c5d7d3d0ee; se_gd=wIFFQW1ANGQVgBRAQU1ZgZZAxAQwUBZV1Qc9YUUZlNSWgBlNWUwX1; se_gsd=czM2CjBlIzM3UCMtJDU0FSojWgkLAwYTUl9CWlxSU1NUVFNT1; theme=dark; BNC-Location=BINANCE; changeBasisTimeZone=; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22416570327%22%2C%22first_id%22%3A%2217f356327f2170-06dc6dfab34654-3f3a5e08-2073600-17f356327f3728%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%2217f356327f2170-06dc6dfab34654-3f3a5e08-2073600-17f356327f3728%22%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMThiYWYyY2QwMjQzMzAtMGQ5OWIwMTMxYTdhNTg4LTNmM2E1ZTA4LTIwNzM2MDAtMThiYWYyY2QwMjVkOGMiLCIkaWRlbnRpdHlfbG9naW5faWQiOiI0MTY1NzAzMjcifQ%3D%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%22416570327%22%7D%7D; userPreferredCurrency=USD_USD; futures-layout=pro; OptanonAlertBoxClosed=2024-02-01T13:12:40.082Z; logined=y; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22416570327%22%2C%22first_id%22%3A%2217f356327f2170-06dc6dfab34654-3f3a5e08-2073600-17f356327f3728%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%2217f356327f2170-06dc6dfab34654-3f3a5e08-2073600-17f356327f3728%22%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMThiYWYyY2QwMjQzMzAtMGQ5OWIwMTMxYTdhNTg4LTNmM2E1ZTA4LTIwNzM2MDAtMThiYWYyY2QwMjVkOGMiLCIkaWRlbnRpdHlfbG9naW5faWQiOiI0MTY1NzAzMjcifQ%3D%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%22416570327%22%7D%7D; g_state={\"i_l\":4,\"i_p\":1711721974032}; BNC_FV_KEY_T=101-Q2swi1HnFxNqyf%2Fsv3%2FA8wuyksWx86uRVctpaCkJuRDMSB%2F0sc7p27b%2Fyi2XLZwO7Z8dE7%2FHueaV4Ra1jhH%2FRQ%3D%3D-p%2BpAKXRkYB1nnvWC1nllNw%3D%3D-1f; BNC_FV_KEY_EXPIRE=1709483362706; se_sd=wVSFRBwlSRXBBMMMXGhggZZCRBghQEUV1URZfVUJlhQVQB1NWU5Q1; cr00=5181467319C7B8339A604A12D116CA73; d1og=web.416570327.3817691A007925562E2FD223E811C0A2; r2o1=web.416570327.91FDE3C7199FF99EA68159CFD89207FA; f30l=web.416570327.C1175A820BD7475923667210579ACA4D; p20t=web.416570327.4C8ECBBEAF5304E118DCFC03233A1296; lang=zh-cn; OptanonConsent=isGpcEnabled=0&datestamp=Sun+Mar+03+2024+22%3A53%3A15+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=202310.2.0&browserGpcFlag=0&isIABGlobal=false&hosts=&consentId=785d4232-2955-4abb-b9b2-ff78207a3cb6&interactionCount=3&landingPath=NotLandingPage&groups=C0001%3A1%2CC0003%3A1%2CC0004%3A1%2CC0002%3A1&AwaitingReconsent=false&geolocation=JP%3B13");
        args = new String[2];
        System.out.println("开始啦");
        args[0] = "D://model//Controller//biance";
        args[1] = "jianKong";
        PrivateConfig.before(args[0], args[1]);
        PrivateConfig.getJGXsw();
        PrivateConfig.xsw(true);

        PrivateConfig.fileLog.setLastModified(System.currentTimeMillis());



        JianKong4 jianKong4 = new JianKong4();
        jianKong4.method(threadPoolExecutor, PrivateConfig.genDan_personInfoList);

    }


    // 币种的清仓次数
    Map<String, Integer> symbolMapQingCang = new HashMap<>();
    //币种的错误次数
    Map<String, Integer> symbolMapYouCuo = new HashMap<>();
    static Map<String, LogObject> logMap = new HashMap<>();//最多发10次邮件，2天过期
    BigDecimal ling02 = new BigDecimal("0.2");
    public static boolean needCheck = true;
    public static int checkCount = 0;

    public void method(ThreadPoolExecutor threadPoolExecutor, List<JSONObject> personInfoList) throws InterruptedException {
        try {
            PrivateConfig.printLog("跟单开启监控：" + PrivateConfig.getCurrentTime());
            //休息一下，等待主线程完成后，再监控
            if(PrivateConfig.ceShi.equals("0")){
                Thread.sleep(1000 * 30);
            }

            int j = 0;
            int g = 0;
            while (true) {
                //主线程没开启，监控线程就等着
                if(PrivateConfig.oldOrders == null){
                    Thread.sleep(1000 * 10);
                    continue;
                }
                try {

                    PrivateConfig.xsw(false);
                    if (needCheck) {
                        //下单后，隔10秒再检查
                        Thread.sleep(1000 * 30);
                        //1分钟一次
                        //为什么把监控间隔放入方法里面？如果有错的话，应该立马再次确认，而不是1分钟之后再检查，如果放在外边可能是要等了
                        isError(threadPoolExecutor, personInfoList);
                    }

                    j++;
                    if (j > 20) {
                        j = 0;
//                        PrivateConfig.printLog(PrivateConfig.fileWriter,  "jian kong");
                        System.out.println("jian kong");
                        needCheck = true;
                        checkCount = 0;
                    }

                    //设置杠杆
                    g++;
                    if (g > 2880) {
//                        PrivateConfig.printLog(PrivateConfig.fileWriter,  "gang gan");
                        System.out.println("gang gan");
                        g = 0;
                        if("1".equals(changeInitialLeverage)) {
                            ChangeInitialLeverage changeInitialLeverage = new ChangeInitialLeverage();
                            changeInitialLeverage.method(personInfoList);
                        }

                        Iterator<Map.Entry<String, LogObject>> iterator = logMap.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<String, LogObject> entry = iterator.next();
                            if (!PrivateConfig.compareTimeDay(entry.getValue().getTime(), 2)) {
                                iterator.remove();
                            }
                        }

                    }

                } catch (Exception e) {
                    Thread.sleep(1000 * 6);
                    e.printStackTrace();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                //多久监控一次
                if (!needCheck) {
                    if(PrivateConfig.ceShi.equals("1")){
                        Thread.sleep(1000);
                    }else {
                        Thread.sleep(1000 * 60);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("监控程序启动出错了");
            e.printStackTrace();
        }

    }





    public void isError(ThreadPoolExecutor threadPoolExecutor, List<JSONObject> personInfoList) throws InterruptedException {
        try {

            System.out.println(getCurrentTime() + "正在监控");

            for (JSONObject personInfo : personInfoList) {
                SyncRequestClient syncRequestClient = ((SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient));

                AccountInformation accountInformation = PrivateConfig.getAccountInformation(syncRequestClient, threadPoolExecutor);

                List<Position> positionList = accountInformation.getPositions();

                if (mapTian.isEmpty()) {
                    //老师没有持仓，我们有持仓时报错
                    for (Position position : positionList) {
                        String symbolSide = position.getSymbol() + "_" + position.getPositionSide();
                        if (position.getPositionAmt().abs().compareTo(ling) > 0) {
                            if (!hasErrorQingCang(symbolSide)) {
                                Thread.sleep(1000);
                                return;
                            }

                            // 先自动平仓解决
                            QingCang3 qingCang = new QingCang3();
                            qingCang.qingCang(personInfoList, position.getSymbol(), null);

                            if (position.getMaintMargin() != null) {
                                if (position.getMaintMargin().compareTo(new BigDecimal("0.5")) < 0) {
                                    // 如果保证金太少，证明是计算误差，清了就行啦，不用报警了
                                    return;
                                }
                            }

                            String msg = "连续三次，有问题，联系他手动平仓1，" + personInfo.getString(PrivateConfig.alias) + "，老师没有" + symbolSide;
                            PrivateConfig.printLog(msg);
                            PrivateConfig.printLog(position.toString());
                            T5.searchAll(msg);
                            //有错的话，报警后就return
                            Thread.sleep(1000 * 20);
                            return;
                        }
                    }
                    String msg = personInfo.getString(PrivateConfig.alias) + "和老师都没有持仓。没有问题！";
                    System.out.println(msg);
                } else {

                    // 如果和老师都有，并且symbol不同时，平仓
                    for (Position position : positionList) {
                        BigDecimal you = position.getPositionAmt().abs();
                        String symbolSide = position.getSymbol() + "_" + position.getPositionSide();
                        if (you.abs().compareTo(ling) > 0) {
                            if (!mapTian.containsKey(symbolSide)) {
                                if (!hasErrorQingCang(symbolSide)) {
                                    Thread.sleep(1000);
                                    return;
                                }
                                // 先自动平仓解决
                                QingCang3 qingCang = new QingCang3();
                                qingCang.qingCang(personInfoList, position.getSymbol(), position.getPositionSide());

                                if (position.getMaintMargin() != null) {
                                    if (position.getMaintMargin().compareTo(new BigDecimal("0.5")) < 0) {
                                        // 如果保证金太少，证明是计算误差，清了就行啦，不用报警了
                                        return;
                                    }
                                }

//                                PrivateConfig.printLog(position.toString());
                                String msg = "连续三次，有问题，联系他手动平仓2" + personInfo.getString(PrivateConfig.alias) + "，老师没有" + symbolSide;
//                                PrivateConfig.printLog(msg);
                                T5.searchAll(msg);
                                //有错的话，报警后就return
                                Thread.sleep(1000 * 20);
                                return;
                            }
                        }
                    }

                    BigDecimal beiShu = new BigDecimal(personInfo.getString(PrivateConfig.beiShu));
                    //只有倍数大时才校验个数是否正确
                    if (beiShu.compareTo(PrivateConfig.ling035) < 0) {
                        String msg = personInfo.getString(PrivateConfig.alias) + "的和老师相同，没有问题！";
                        PrivateConfig.printLog(msg);
                        continue;
                    }

                    for (Map.Entry<String, Position> entryLs : mapTian.entrySet()) {
                        String symbolSideLs = entryLs.getKey();
                        String symbolLs = entryLs.getKey().split("_")[0];
                        if (PrivateConfig.getXSM(symbolLs) <= 0.1) {
                            //小数位为0
                            ling02 = new BigDecimal("0.4");

                        }
                        //基准的数量除以倍数就是老师的数量
//                        BigDecimal youLs = entryLs.getValue().getPositionAmt().divide(beiShuMy, 5, BigDecimal.ROUND_HALF_UP).abs();
                        BigDecimal youLs = entryLs.getValue().getPositionAmt().abs();
                        Boolean hasProblem = true;
                        for (Position position : positionList) {
                            String symbolSide = position.getSymbol() + "_" + position.getPositionSide();
                            String symbol = position.getSymbol();
                            BigDecimal you = position.getPositionAmt().abs();
                            if (symbolSideLs.equals(symbolSide) && you.abs().compareTo(ling) > 0) {
                                BigDecimal youXueSheng = you.divide(beiShu, 5, BigDecimal.ROUND_HALF_UP);


                                if (youLs.subtract(youXueSheng).divide(youLs, 5, BigDecimal.ROUND_HALF_UP).abs().compareTo(ling02) > 0 &&
                                        youXueSheng.subtract(youLs).divide(youXueSheng, 5, BigDecimal.ROUND_HALF_UP).abs().compareTo(ling02) > 0) {
                                    if (PrivateConfig.getXSM(symbol) <= 0.1 && youLs.multiply(beiShu).compareTo(new BigDecimal("5")) < 0) {
                                        //小数位为0，且应该有的个数小于5时，不校验
                                        hasProblem = false;
                                        String msg = personInfo.getString(PrivateConfig.alias) + "的" + symbolSideLs + "和老师相同，没有问题！";
                                        PrivateConfig.printLog(msg);
                                        continue;
                                    }

                                    if (!hasError(symbolSide)) {
                                        Thread.sleep(1000);
                                        return;
                                    }

                                    String msg = "连续2次，有问题！" + personInfo.getString(PrivateConfig.alias) + "，" + position.getSymbol() + "和老师个数不同，应该有" + youLs.multiply(beiShu).setScale(3, RoundingMode.HALF_DOWN) + "个，现在有" + you + "个";

                                    //先自动减仓
                                    if (you.compareTo(youLs.multiply(beiShu)) > 0) {

                                        BigDecimal jianCangCount = you.subtract(youLs.multiply(beiShu)).setScale(PrivateConfig.getXSM(position.getSymbol()), BigDecimal.ROUND_HALF_UP);

                                        PrivateConfig.jianCang(syncRequestClient, position.getSymbol(), position.getPositionSide(), jianCangCount.toString(), position.getPositionAmt());
//                                                postOrder(syncRequestClient, position.getSymbol(), buy, "TRUE", mai.toString());

                                        //自动减仓是一个人一个人的减
                                        PrivateConfig.printLog(msg);
                                        T5.searchAll(msg);
                                        continue;
                                    }else {
                                        if ("1".equals(buCang)) {
                                            Integer jiaCangCount = personJiaCangMap.get(personInfo.getString(name));
                                            if (jiaCangCount == null) {
                                                jiaCangCount = 0;
                                            }
                                            if (jiaCangCount < 3) {//等稳定后，这个次数可以放大
                                                jiaCangCount++;
                                                personJiaCangMap.put(personInfo.getString(name), jiaCangCount);
                                                //自动加仓，向下取整，肯定不能加多了
                                                BigDecimal jianCangCount = youLs.multiply(beiShu).subtract(you).setScale(PrivateConfig.getXSM(position.getSymbol()), BigDecimal.ROUND_DOWN);
                                                PrivateConfig.jiaCang(syncRequestClient, position.getSymbol(), position.getPositionSide(), jianCangCount.toString(), position.getPositionAmt());
                                            }

                                            //自动减仓是一个人一个人的减
                                            PrivateConfig.printLog(msg);
                                            sendEmail1(logMap, msg);
                                            continue;
                                        }
                                    }

                                    PrivateConfig.printLog(msg);
                                    sendEmail1(logMap, msg);
//                                        PrivateConfig.printLog(PrivateConfig.fileWriter, msg);

                                    hasProblem = false;
                                    //有错的话，报警后就return
                                    Thread.sleep(1000 * 20);
                                    continue;
                                } else {
                                    //和老师相同就退出
                                    hasProblem = false;
                                    PrivateConfig.printLog(personInfo.getString(PrivateConfig.alias) + "的" + symbolSideLs + "和老师相同，没有问题!");
                                }
//                                }
                            } else if (symbolSideLs.equals(symbolSide) && you.abs().compareTo(ling) == 0) {
                                if (!hasError(symbolSideLs)) {
                                    Thread.sleep(1000);
                                    return;
                                }
                                hasProblem = false;
                                String msg = personInfo.getString(PrivateConfig.alias) + "的" + symbolSideLs + "没有，而老师有，可能是我们的挂单卖了，没有问题!";
                                System.out.println(msg);
                            }
                        }
                        if (hasProblem) {
                            if (PrivateConfig.getXSM(symbolLs) <= 0.1 && youLs.multiply(beiShu).compareTo(new BigDecimal("5")) < 0) {
                                //小数位为0，且应该有的个数小于5时，不校验
                                hasProblem = false;
                                String msg = personInfo.getString(PrivateConfig.alias) + "的" + symbolSideLs + "和老师相同，没有问题！";
                                PrivateConfig.printLog(msg);
                                continue;
                            }

                            if (!hasError(symbolSideLs)) {
                                Thread.sleep(1000);
                                return;
                            }

                            String msg = personInfo.getString(PrivateConfig.alias) + "应该有" + youLs.multiply(beiShu).setScale(3, RoundingMode.HALF_DOWN) + "个" + symbolSideLs + "，现在没有。有问题！";
                            PrivateConfig.printLog(msg);
                            //有错的话，报警后就return
                            /*Thread.sleep(1000 * 20);
                            continue;*/
                        }
                    }
                }
                //学员之间的间隔
                Thread.sleep(3000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        // 能够走到这里，证明没有错
        symbolMapQingCang.clear();
        symbolMapYouCuo.clear();

        checkCount++;
        PrivateConfig.printLog("经过1次：" + checkCount);

        if (checkCount > 1) {
            needCheck = false;
            checkCount = 0;
        }
        //2次监控之间的间隔
        if(PrivateConfig.ceShi.equals("1")){
            Thread.sleep(1000 * 6);
        }else {
            Thread.sleep(1000 * 60);
        }
    }

    public static void sendEmail1(Map<String, LogObject> logMap, String msg) {
        if (logMap.containsKey(msg)) {
            if (logMap.get(msg).getCount() < 4) {
                logMap.get(msg).setCount(logMap.get(msg).getCount() + 1);
            }
        } else {
            LogObject logObject = new LogObject();
            logObject.setTime(System.currentTimeMillis());
            logObject.setCount(1);
            logMap.put(msg, logObject);
        }

        if (logMap.get(msg).getCount() < 3) {
            T5.searchAll(msg);
        }
    }


    /**
     * 第三次检查还有错，证明是真的有错了
     *
     * @param symbolLs
     * @return
     */
    public Boolean hasError(String symbolLs) {
        if (symbolMapYouCuo.get(symbolLs) == null) {
            symbolMapYouCuo.put(symbolLs, 0);
        }
        symbolMapYouCuo.put(symbolLs, symbolMapYouCuo.get(symbolLs) + 1);
        if (symbolMapYouCuo.get(symbolLs) < 3) {
            return false;
        }
        symbolMapYouCuo.remove(symbolLs);
        return true;
    }

    /**
     * 第三次检查还有错，证明是真的有错了
     *
     * @param symbolLs
     * @return
     */
    public Boolean hasErrorQingCang(String symbolLs) {
        if (symbolMapQingCang.get(symbolLs) == null) {
            symbolMapQingCang.put(symbolLs, 0);
        }
        symbolMapQingCang.put(symbolLs, symbolMapQingCang.get(symbolLs) + 1);
        if (symbolMapQingCang.get(symbolLs) < 3) {
            return false;
        }
        symbolMapQingCang.remove(symbolLs);
        return true;
    }

    public String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis())); // 时间戳转换日期
    }

}

