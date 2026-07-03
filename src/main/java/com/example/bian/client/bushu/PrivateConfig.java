package com.example.bian.client.bushu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.binance.connector.client.impl.SpotClientImpl;
import com.example.bian.client.RequestOptions;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.model.enums.NewOrderRespType;
import com.example.bian.client.model.enums.OrderSide;
import com.example.bian.client.model.enums.OrderType;
import com.example.bian.client.model.enums.PositionSide;
import com.example.bian.client.model.market.ExchangeInfoEntry;
import com.example.bian.client.model.market.ExchangeInformation;
import com.example.bian.client.model.trade.AccountInformation;
import com.example.bian.client.model.trade.Order;
import com.example.bian.client.model.trade.Position;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class PrivateConfig {

    // #放开

    /*
    *  业务
    *

        cumQuote：下单金额，单位人民币。 下单的数量和价格的乘积，和USDT什么关系呢？
        像保证金不足的委托，代码里直接出错了，可能在系统里的历史委托中就找不到

        下单后撤销
            如果都完全成功的话
                它们是对同一个订单的操作，历史记录里只会有一条数据
                唯一的区别是，下单状态为new，撤销状态为CANCELED
            如果部分成功呢？疑问？
                下了的单部分成交了，此时状态为PARTIALLY_FILLED，此时还能撤销吗？撤销后原来的订单状态是什么，撤销是否会创建一个新的订单呢？

        撤单失败
            1、田下单没有成交，我们成交了，然后田撤单，我们会提示撤单失败
                买入：需要田那里保证他一定能买入（之前沟通的说是软件上有这个功能）
                卖出：这种情况小号已经卖了，那就不管啦

        下单失败
            1、此时应该是平仓的错误，平仓也叫下单，当第一次平仓时，田的没有成交，我们的成交了，此时田第二次平仓，我们就会下单失败，因为我们已经平过了。

        * 问题？？？
    *   下单时，不同的symbol，个数和价格的小数位有要求吗？对的
    *   市价单是不是不用设置价格？是的
    *
    * */

    /*
    buy：越大越容易成交
    sell：越小越容易成交c

        下面是双向操作的，也就是可以同时开多开空，此时ReduceOnly不能填写，注意是不能，传null才行。单向持仓时，不能同时持有
        * 做多
            买：side=BUY,positionSide=LONG
            卖：side=SELL,positionSide=LONG

        做空
            买：side=SELL,positionSide=SHORT
            卖：side=BUY,positionSide=SHORT

        单向持仓时，ReduceOnly必填， positionSide=both
        做多
            买：side=BUY,ReduceOnly=false
            卖：side=SELL,ReduceOnly=true

        做空
            买：side=SELL,ReduceOnly=false
            卖：side=BUY,ReduceOnly=true


        在AccountInformation中，positionAmt：做多为正数，做空位负数

        接口参数说明
        NEW	订单被交易引擎接受
        PARTIALLY_FILLED	部分订单被成交
        FILLED	订单完全成交
        CANCELED	用户撤销了订单
        PENDING_CANCEL	撤销中(目前并未使用)
        REJECTED	订单没有被交易引擎接受，也没被处理
        EXPIRED	订单被交易引擎取消, 比如  LIMIT FOK 订单没有成交        市价单没有完全成交        强平期间被取消的订单        交易所维护期间被取消的订单





        zhenShiBeiShu：自动计算的值再乘以此值，就是最终的倍数
        money：最大跟单比例
        beiShu：是变化的，根据这个值下单
    * */


    public static String symbol = "symbol";
    public static String side = "side";
    public static String positionSide = "positionSide";
    public static String positionAmount = "positionAmount";
    public static BigDecimal ling = new BigDecimal(0);

    public static final String name = "name";
    public static final String alias = "alias";

    public static final String apiKey = "API_KEY";
    public static final String secretKey = "secretKey";
    public static final String beiShu = "beiShu";
    public static final String zhenShiBeiShu = "zhenShiBeiShu";
    public static final String syncRequestClient = "syncRequestClient";
    public static final String spotClient = "spotClient";

    public static BigDecimal ling035;

    public static  String computer = "东京";
    public static  String linux = "1";

    public static String Leverage = "1";
    public static String daiLi = "1";
    public static String noLeverage = "1";
    public static Integer LeverageCount = 8;
    public static  Integer isLinux = 0;
    public static  String ceShi = "0";
    public static  String shiJian = "800";
    public static  String buCang = "0"; //是否自动补仓
    public static  int getPon = 12000; //获取仓位次数，1分钟一次
    public static String Leverage4164 = "ETHUSDT,BTCUSDT";
    public static  int LeverageCount4164 = 40;

    public static  JSONArray genDan_xianYou;
    public static JSONObject getXsw;
    public static Map<String, Integer> jgXsw = new HashMap<>();//价格小数位
    public static Map<String, Integer> gsXsw = new HashMap<>();//个数小数位
    public static Map<String, BigDecimal> zuiDiMoney = new HashMap<>();//币种最低购买金额
    public static List<JSONObject> personInfoList;
    public static List<JSONObject> genDan_personInfoList;
    public static  String xiaoCangBeiShu; //当购买个数太小时，取最小购买个数
    public static ThreadPoolExecutor threadPoolExecutor;
    public static Writer fileWriter;
    public static Writer fileWriterJianKong;
    public static String classPath;
    public static File fileLog;
    public static File fileLogJianKong;
    public static String password = "sduroelejsyzbbac";

    public static long fangWenTiem;
    public static Map<String, Order> oldOrders = null;
    public static boolean stop = false;

    public static volatile Map<String, Integer> personJiaCangMap = new HashMap<>();//每个人自动加仓的次数，不能太多，防止错误
    public static volatile Map<String, Position> mapTian = new HashMap<>();//老师的最新持仓

    public static  String changeInitialLeverage = "0"; //是否自动设置杠杆

    public static void before(String classPath1, String logName) {
        classPath = classPath1;

        getListNew(classPath + "//info.json");

    }

    public static void jiaCang(SyncRequestClient syncRequestClient, String symbol, String positionSide, String getOrigQty, BigDecimal positionAmt) {
        if(positionAmt.compareTo(PrivateConfig.ling) > 0){
            System.out.println("做的多");
            PrivateConfig.postOrder(syncRequestClient, symbol, OrderSide.BUY.toString(), PositionSide.LONG.toString(), null, getOrigQty);
        }else {
            System.out.println("做的空");
            PrivateConfig.postOrder(syncRequestClient, symbol, OrderSide.SELL.toString(), PositionSide.SHORT.toString(), null, getOrigQty);
        }
    }

    public static AccountInformation getAccountInformation(SyncRequestClient syncRequestClient, ThreadPoolExecutor threadPoolExecutor) throws InterruptedException {
        Callable callable = new Callable() {
            @Override
            public AccountInformation call() throws Exception {
                return syncRequestClient.getAccountInformation();
            }
        };
        int h = 0;
        while (true) {
            /*if(PrivateConfig.daYinLog){
                System.out.println("getAccountInformation:" + getCurrentTime());
            }*/
            Future future = threadPoolExecutor.submit(callable);
            try {
                AccountInformation accountInformation = (AccountInformation) (future.get(3, TimeUnit.SECONDS));
                return accountInformation;
            } catch (TimeoutException e) {
                e.printStackTrace();
                Thread.sleep(10000);//前面有超时，歇2秒再跟
            }catch (Exception e){
                e.printStackTrace();
                Thread.sleep(10000);//前面有超时，歇2秒再跟
            }catch(Throwable t){
                t.printStackTrace();
                Thread.sleep(10000);//前面有超时，歇2秒再跟
            }finally {
                future.cancel(true);
                h++;
                if(h>5){
                    h=0;
                    T5.searchAll("连续5次，有问题，getAccountInformation超时！");
                }
            }
        }
    }

    public static String getPositionSide(String positionSide, BigDecimal amount){
        if(positionSide.equals(PositionSide.BOTH.toString())){
            if(amount.compareTo(ling)>0){
                positionSide = PositionSide.LONG.toString();
            }else {
                positionSide = PositionSide.SHORT.toString();
            }
        }

        return positionSide;
    }


    public static void jianCang(SyncRequestClient syncRequestClient, String symbol, String positionSide, String getOrigQty, BigDecimal positionAmt) {

        if(positionSide.equals(PositionSide.BOTH.toString())){
//            System.out.println("单向");
            if(positionAmt.compareTo(PrivateConfig.ling) > 0){
//                System.out.println("做的多");
                PrivateConfig.postOrder(syncRequestClient, symbol, OrderSide.SELL.toString(), PositionSide.BOTH.toString(), "TRUE", getOrigQty);
            }else {
//                System.out.println("做的空");
                PrivateConfig.postOrder(syncRequestClient, symbol, OrderSide.BUY.toString(), PositionSide.BOTH.toString(), "TRUE", getOrigQty);
            }
        }else {
//            System.out.println("双向");
            if(positionAmt.compareTo(PrivateConfig.ling) > 0){
//                System.out.println("做的多");
                PrivateConfig.postOrder(syncRequestClient, symbol, OrderSide.SELL.toString(), PositionSide.LONG.toString(), null, getOrigQty);
            }else {
//                System.out.println("做的空");
                PrivateConfig.postOrder(syncRequestClient, symbol, OrderSide.BUY.toString(), PositionSide.SHORT.toString(), null, getOrigQty);
            }
        }
    }


    /**
     * 市场单时，如果手里只有2个，卖5个，最后也会成功卖2个
     * 限价单时，如果手里只有2个，挂单卖5个，最后也会成功挂单2个
     * @param syncRequestClient
     * @param symbol
     * @param side
     * @param positionSide
     * @param reduceOnly
     * @param getOrigQty
     */
    public static void postOrder(SyncRequestClient syncRequestClient, String symbol, String side, String positionSide, String reduceOnly, String getOrigQty) {
        for (int i = 0; i < 1; i++) {
            try {
                syncRequestClient.postOrder(
                        symbol,
                        OrderSide.valueOf(side),//买还是卖，做多时，buy sell；做空时，sell buy
                        PositionSide.valueOf(positionSide),//双向：long short 单向：both
                        OrderType.valueOf("MARKET"),// 订单类型，limit：限价单；MARKET：市价单（想要成功买卖，使用这个）
                        null,//TimeInForce.valueOf("GTC"),//成交为止，一直有效，不用管
                        getOrigQty,//跟单数量（下单时，跟单个数一定是大于0的，通过AccountInformation查询到的，做空的个数小于0，做多的大于0）
                        null,//跟单单价，总价需要大于5（市价时，可以不填）
                        reduceOnly,//order.getReduceOnly().toString(), //双向持仓时，只能传null
                        null,//order.getClientOrderId(),
                        null,//order.getStopPrice().toString(),
                        null,//WorkingType.valueOf(order.getWorkingType()),
                        NewOrderRespType.RESULT);
                System.out.println("下单了：" + PrivateConfig.getCurrentTime()+"-" + symbol+"-"+side+"-"+positionSide+"-"+getOrigQty+"-"+reduceOnly);
                return;
            } catch (Exception e) {
                try {
                    e.printStackTrace();
//                    PrivateConfig.printLog(fileWriter, e); -2019
                    BigDecimal count = new BigDecimal(getOrigQty);
                    boolean putAgain = false;
                    if(e.getMessage().contains("-2019")){
                        //保证金不够，调整杠杆买进去
//                        count = count.multiply(new BigDecimal("0.5")).setScale(PrivateConfig.getXSM(symbol), BigDecimal.ROUND_HALF_UP);
                        if(PrivateConfig.Leverage4164.contains(symbol)){
                            syncRequestClient.changeInitialLeverage(symbol, PrivateConfig.LeverageCount4164);
                        }else {
                            syncRequestClient.changeInitialLeverage(symbol, 20);
                        }
//                        T5.searchAll("保证金不足，看看是否购买成功");
                        Thread.sleep(1000);
                        putAgain = true;
                    } else if(e.getMessage().contains("-4164")) {
                        if ("1".equals(PrivateConfig.xiaoCangBeiShu)) {
//                            会存在一个问题，买入的太多，检测时，又自动减仓，没意义，不过这个逻辑已验证是对的，打开后，一定要关闭个数的判断
                            BigDecimal markPrice = syncRequestClient.getMarkPrice(symbol).get(0).getMarkPrice();
                            BigDecimal zuiDiCount = zuiDiMoney.get(symbol).divide(markPrice, getXSM(symbol), BigDecimal.ROUND_UP);
                            count = zuiDiCount;
//                            count = count.multiply(new BigDecimal(PrivateConfig.xiaoCangBeiShu)).min(zuiDiCount);
                        }
                    }

                    if(putAgain){
                        //只下单已知错误，未知错误不处理，是币安的锅
                        syncRequestClient.postOrder(
                                symbol,
                                OrderSide.valueOf(side),//买还是卖，做多时，buy sell；做空时，sell buy
                                PositionSide.valueOf(positionSide),//双向：long short 单向：both
                                OrderType.valueOf("MARKET"),// 订单类型，limit：限价单；MARKET：市价单（想要成功买卖，使用这个）
                                null,//TimeInForce.valueOf("GTC"),//成交为止，一直有效，不用管
                                count.toString(),//跟单数量（下单时，跟单个数一定是大于0的，通过AccountInformation查询到的，做空的个数小于0，做多的大于0）
                                null,//跟单单价，总价需要大于5（市价时，可以不填）
                                reduceOnly,//order.getReduceOnly().toString(), //双向持仓时，只能传null
                                null,//order.getClientOrderId(),
                                null,//order.getStopPrice().toString(),
                                null,//WorkingType.valueOf(order.getWorkingType()),
                                NewOrderRespType.RESULT);
                        System.out.println("下单了：" + PrivateConfig.getCurrentTime()+"-" + symbol+"-"+side+"-"+positionSide+"-"+count.toString()+"-"+reduceOnly);
                    }
                    return;
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    /*public static void postOrder(SyncRequestClient syncRequestClient, String symbol, String side, String positionSide, String reduceOnly, String getOrigQty) {
        if(PrivateConfig.ceShi.equals("1")){
            return;
        }
        for (int i = 0; i < 2; i++) {
            try {
                syncRequestClient.postOrder(
                        symbol,
                        OrderSide.valueOf(side),//买还是卖，做多时，buy sell；做空时，sell buy
                        PositionSide.valueOf(positionSide),//双向：long short 单向：both
                        OrderType.valueOf("MARKET"),// 订单类型，limit：限价单；MARKET：市价单（想要成功买卖，使用这个）
                        null,//TimeInForce.valueOf("GTC"),//成交为止，一直有效，不用管
                        getOrigQty,//跟单数量（下单时，跟单个数一定是大于0的，通过AccountInformation查询到的，做空的个数小于0，做多的大于0）
                        null,//跟单单价，总价需要大于5（市价时，可以不填）
                        reduceOnly,//order.getReduceOnly().toString(), //双向持仓时，只能传null
                        null,//order.getClientOrderId(),
                        null,//order.getStopPrice().toString(),
                        null,//WorkingType.valueOf(order.getWorkingType()),
                        NewOrderRespType.RESULT);
                System.out.println("下单了：" + PrivateConfig.getCurrentTime()+"-" + symbol+"-"+side+"-"+positionSide+"-"+getOrigQty+"-"+reduceOnly);
                return;
            } catch (Exception e) {
                try {
                    e.printStackTrace();
//                    PrivateConfig.printLog(fileWriter, e); -2019
                    BigDecimal count = new BigDecimal(getOrigQty);

                    if(e.getMessage().contains("-2019")){
                        //保证金不够，调整杠杆买进去
//                        count = count.multiply(new BigDecimal("0.5")).setScale(PrivateConfig.getXSM(symbol), BigDecimal.ROUND_HALF_UP);
                        if(PrivateConfig.Leverage4164.contains(symbol)){
                            syncRequestClient.changeInitialLeverage(symbol, PrivateConfig.LeverageCount4164);
                        }else {
                            syncRequestClient.changeInitialLeverage(symbol, 20);
                        }
                        T5.searchAll("保证金不足，看看是否购买成功");
                        Thread.sleep(1000);
                    } else if(e.getMessage().contains("-4164")) {
                        if ("1".equals(PrivateConfig.xiaoCangBeiShu)) {
//                            会存在一个问题，买入的太多，检测时，又自动减仓，没意义，不过这个逻辑已验证是对的，打开后，一定要关闭个数的判断
                            BigDecimal markPrice = syncRequestClient.getMarkPrice(symbol).get(0).getMarkPrice();
                            BigDecimal zuiDiCount = zuiDiMoney.get(symbol).divide(markPrice, getXSM(symbol), BigDecimal.ROUND_UP);
                            count = zuiDiCount;
//                            count = count.multiply(new BigDecimal(PrivateConfig.xiaoCangBeiShu)).min(zuiDiCount);
                        }
                    }

                    syncRequestClient.postOrder(
                            symbol,
                            OrderSide.valueOf(side),//买还是卖，做多时，buy sell；做空时，sell buy
                            PositionSide.valueOf(positionSide),//双向：long short 单向：both
                            OrderType.valueOf("MARKET"),// 订单类型，limit：限价单；MARKET：市价单（想要成功买卖，使用这个）
                            null,//TimeInForce.valueOf("GTC"),//成交为止，一直有效，不用管
                            count.toString(),//跟单数量（下单时，跟单个数一定是大于0的，通过AccountInformation查询到的，做空的个数小于0，做多的大于0）
                            null,//跟单单价，总价需要大于5（市价时，可以不填）
                            reduceOnly,//order.getReduceOnly().toString(), //双向持仓时，只能传null
                            null,//order.getClientOrderId(),
                            null,//order.getStopPrice().toString(),
                            null,//WorkingType.valueOf(order.getWorkingType()),
                            NewOrderRespType.RESULT);
                    System.out.println("下单了：" + PrivateConfig.getCurrentTime()+"-" + symbol+"-"+side+"-"+positionSide+"-"+count.toString()+"-"+reduceOnly);
                    return;
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }

    }*/

    private static Boolean jinTianDiaoYongGuo = false;
    public static void xsw(Boolean start){
        Long currentTime = System.currentTimeMillis();
        // 每天7到8点之间，调用一次
        if (start || (!jinTianDiaoYongGuo && (currentTime > getTodayStartTime(7, 0, 0) && currentTime < getTodayStartTime(8, 0, 0)))) {
            jinTianDiaoYongGuo = true;

            for (int i = 0; i < 3; i++) {
                try {
                    jgXsw.clear();
                    gsXsw.clear();
                    zuiDiMoney.clear();


                    RequestOptions options = new RequestOptions();
                    SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY,
                            options);
                    ExchangeInformation exchangeInformation = syncRequestClient.getExchangeInformation();
                    List<ExchangeInfoEntry> exchangeInfoEntryList = exchangeInformation.getSymbols();
                    System.out.println();
                    for(ExchangeInfoEntry entry : exchangeInfoEntryList) {

                        List<List<Map<String, String>>> filters = entry.getFilters();
                        String tick = "1";
                        for (List<Map<String, String>> filter : filters) {
                            for (Map<String, String> map : filter) {
                                if (map.keySet().contains("tickSize")) {
//                        System.out.println(entry.getSymbol());
                                    if ("1".equals(map.get("tickSize"))) {
                                        tick = "1";
                                    } else {
                                        tick = map.get("tickSize").split("1")[0] + "1";
                                    }
                                    break;
                                }
                            }

                            //查找每个币种最低购买金额
                            for (Map<String, String> map : filter) {
                                if (map.values().contains("MIN_NOTIONAL")) {
                                    for (Map<String, String> map1 : filter) {
                                        if(map1.get("notional") != null){
                                            zuiDiMoney.put(entry.getSymbol(), new BigDecimal(map1.get("notional")));
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        jgXsw.put(entry.getSymbol(), get(tick));
                        gsXsw.put(entry.getSymbol(), Integer.parseInt(entry.getQuantityPrecision().toString()));
                    }
                    System.out.println("调用一次" + jgXsw.size() + ", " + getCurrentTime());
                    break;
                } catch (Exception e) {
                    try {
                        System.out.println(e.getMessage());
                        Thread.sleep(1000 * 3);
                    } catch (Exception e1) {

                    }
                }
            }
        }

        if(jgXsw.size()<=0){
            try {
                Thread.sleep(1000 * 3);
                if(jgXsw.size()<=0) {
                    T5.searchAll("获取小数位出错了，有问题");
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if(jinTianDiaoYongGuo && (currentTime > getTodayStartTime(9, 0, 0))){
            jinTianDiaoYongGuo = false;
        }

    }

    //个数小数位
    public static Integer getXSM(String symbol){
        if(gsXsw.get(symbol) != null){
            return gsXsw.get(symbol);
        }
        Integer xiaoShu = getXsw().get(symbol);
        if(xiaoShu == null){
            return 0;
        }else {
            return xiaoShu;
        }
    }

    //价格小数位
    public static Map<String, Integer> getJGXsw() {

        Map<String, Integer> map = new HashMap<>();
        if (getXsw != null) {
            Iterator it = getXsw.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                map.put(entry.getKey(), Integer.parseInt(entry.getValue().toString().split(",")[0]));
            }
        }

        return map;
    }

    //个数小数位
    public static Map<String, Integer> getXsw() {
//        https://www.binance.com/zh-CN/futures/XMR_USDT

        Map<String, Integer> map = new HashMap<>();

        if(getXsw != null){
            Iterator it =getXsw.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                map.put(entry.getKey(), Integer.parseInt(entry.getValue().toString().split(",")[1]));
            }
        }

        return map;

    }

    /**
     * 获取当天的8点时间戳
     *
     * @return
     */
    public static long getTodayStartTime(int hour, int minute, int secont) {
        //设置时区
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, secont);
        return calendar.getTimeInMillis();
    }

    public static int get(String tick){
        int dcimalDigits = 0;
        int indexOf = tick.indexOf(".");
        if (indexOf > 0) {
            dcimalDigits = tick.length() - 1 - indexOf;
        }
        return dcimalDigits;
    }

    public static void logInit(String logName){
        try {
            logName = "log";
            String logPath = classPath + "//" + logName + ".txt";
            fileLog = new File(logPath);
            if (!fileLog.exists()) {
                fileLog.createNewFile();
            }
            fileWriter = new OutputStreamWriter(new FileOutputStream(fileLog, true),"gbk"); //gbk UTF-8

            String logPathJianKong = classPath + "//" + "JianKong.txt";
            fileLogJianKong = new File(logPathJianKong);
            if (!fileLogJianKong.exists()) {
                fileLogJianKong.createNewFile();
            }
            fileWriterJianKong = new OutputStreamWriter(new FileOutputStream(fileLogJianKong, true),"gbk"); //gbk UTF-8
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void init(String configPath) {

        JSONObject config = readJsonFile(configPath  + "//info.json");

        if (config.getString("daiLi") != null) {
            daiLi = config.getString("daiLi");
        }
        if(config.get("linux") != null){
            linux = config.getString("linux");
        }
        classPath = configPath;
        logInit("");
    }

    public static void getListNew(String configPath) {

        RequestOptions options = new RequestOptions();
        JSONObject config = readJsonFile(configPath);

        if(config.get("Leverage") != null){
            Leverage = config.getString("Leverage");
        }
        if(config.get("LeverageCount") != null){
            LeverageCount = config.getInteger("LeverageCount");
        }
        if(config.get("noLeverage") != null){
            noLeverage = config.getString("noLeverage");
        }
        if(config.get("isLinux") != null){
            isLinux = config.getInteger("isLinux");
        }
        if(config.get("ceShi") != null){
            ceShi = config.getString("ceShi");
        }
        if(config.get("computer") != null){
            computer = config.getString("computer");
        }
        if(config.get("shiJian") != null){
            shiJian = config.getString("shiJian");
        }
        if(config.get("buCang") != null){
            buCang = config.getString("buCang");
        }if(config.get("Leverage4164") != null){
            Leverage4164 = config.getString("Leverage4164");
        }if(config.get("LeverageCount4164") != null){
            LeverageCount4164 = config.getInteger("LeverageCount4164");
        }
        if(config.get("xiaoCangBeiShu") != null){
            xiaoCangBeiShu = config.getString("xiaoCangBeiShu");
        }
        if(config.get("getPon") != null){
            getPon = config.getInteger("getPon");
        }

        if(config.get("changeInitialLeverage") != null){
            changeInitialLeverage = config.getString("changeInitialLeverage");
        }

        getXsw = config.getJSONObject("getXsw");

        ling035 = new BigDecimal(config.getString("ling035"));

        if(config.getJSONObject("genDan") != null){

            JSONArray genDan_personInfo = config.getJSONObject("genDan").getJSONArray("personInfo");
            List<JSONObject> genDan_list = new ArrayList<>();
            if(!CollectionUtils.isEmpty(genDan_personInfo)){
                for(Object object : genDan_personInfo){
                    JSONObject jsonObject = (JSONObject)object;
                    jsonObject.put(PrivateConfig.syncRequestClient , SyncRequestClient.create(jsonObject.get(PrivateConfig.apiKey).toString(), jsonObject.get(PrivateConfig.secretKey).toString(), options));
                    jsonObject.put(PrivateConfig.spotClient, new SpotClientImpl(jsonObject.get(PrivateConfig.apiKey).toString(), jsonObject.get(PrivateConfig.secretKey).toString()));
                    genDan_list.add(jsonObject);
                }
            }
            genDan_xianYou = config.getJSONObject("genDan").getJSONArray("xianYou");

            genDan_personInfoList = genDan_list;
        }


    }

    public static void printLog(String msg) {
        System.out.println(msg);

        if(linux.equals("0")){
            PrivateConfig.fileLog.setLastModified(System.currentTimeMillis());
            try {
                fileWriter.write(getCurrentTime() + "\n" + msg + "\n");
                fileWriter.flush();
                PrivateConfig.fileLog.setLastModified(System.currentTimeMillis());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
    public static void printLogJianKong() {

        PrivateConfig.fileLogJianKong.setLastModified(System.currentTimeMillis());
        try {
            fileWriterJianKong.write(getCurrentTime() + "\n" + "DiaoYong" + "\n");
            fileWriterJianKong.flush();
            PrivateConfig.fileLogJianKong.setLastModified(System.currentTimeMillis());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    public static boolean compareTimeDay(Long time, int day){
        if(System.currentTimeMillis() - time < day * 86400 * 1000){
            return true;
        }else {
            return false;
        }
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis())); // 时间戳转换日期
    }


    // 测试用 胡亚龙
    /*public static final String API_KEY = "fz5Wl4Jg6VYrxoyyHcu4imA0RYT77TEncjf1CYLqVZBCyXYswjRg5YP9CQALtrTV";
    public static final String SECRET_KEY = "uDGTrk1C30hXEiQr3xXjg9PxW0Yv11s4eXoFH9KOesRKfjhhNJmJRbFvhWH7Totg";*/

    // 杰哥
    public static final String API_KEY = "oVRZJW4Q7DBaMNWiCizyyEhyiM8KPeqZztb4Xaq3jfsnC72MlGX2THGOMcsFwC5Z";
    public static final String SECRET_KEY = "MVZuD9h6w3Q2aqa16x6XP2Qwd5NMxKWJrVFFGFF9TX9wJGfMJQhkIuCdnxfsPjuu";


    /**
     * 读取json文件，返回json串
     * @param fileName
     * @return
     */
    public static JSONObject readJsonFile(String fileName) {
        String jsonStr = "";
        try {
//            fileWriter.write("du qu json" + "\n");
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }

            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            JSONObject jobj = JSON.parseObject(jsonStr);
            return jobj;
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }


    public static JSONObject get1(String... args){
        JSONObject j1 = new JSONObject();
        j1.put(PrivateConfig.name, args[0]);
        j1.put(PrivateConfig.alias, args[1]);
        j1.put(PrivateConfig.apiKey, args[2]);
        j1.put(PrivateConfig.secretKey, args[3]);
        j1.put(PrivateConfig.beiShu, args[4]);
        return j1;
    }



    public static void main(String[] args) throws IOException, InterruptedException {

        args = new String[2];
        PrivateConfig.printLog("开始啦");
        args[0] = "E://code//biance";
        args[1] = "0-genDan";
        PrivateConfig.init((args[0]));
        PrivateConfig.before(args[0], args[1]);

        // 对https也开启代理

        System.out.println("开代理");
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10809");


        PrivateConfig.getJGXsw();
        PrivateConfig.xsw(true);

        /*ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(5, 5, 10,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.DiscardPolicy());

        setBeiShu(threadPoolExecutor, PrivateConfig.genDan_personInfoList);*/


        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, options);

        postOrder(syncRequestClient, "USDCUSDT", OrderSide.BUY.toString(), PositionSide.LONG.toString(), null, "6");

        System.out.println();
    }

};

