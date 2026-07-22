package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.client.model.enums.OrderSide;
import com.example.bian.client.model.enums.PositionSide;
import com.example.bian.client.model.trade.Order;
import com.example.bian.client.model.trade.Position;
import com.example.bian.genDan.MulPostOrders;
import com.example.bian.xin.QingCang3;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.bian.client.bushu.PrivateConfig.*;

@Service
public class BizService {
    // 指定单线程池
    int diaoYongCount1 = 0;
    int diaoYongCount2 = 0;

    int youXiaoCount = 0;
    @Async("singleBizExecutor")
    public void doBiz(String data) throws InterruptedException {

        diaoYongCount1++;
        diaoYongCount2++;
        if (!jiaoYan(data)) {
            youXiaoCount++;
            if (youXiaoCount > 20) {
                //数据错误超过3次，清空再说
                data = null;
            } else {
                PrivateConfig.printLog("前端传递数据错误：" + data);
                T5.searchAll("前端传递数据错误：" + data);
                return;
            }
        } else {
            youXiaoCount = 0;
        }

        if (diaoYongCount1 > 11) {
            diaoYongCount1 = 0;
            PrivateConfig.printLog("网页跟单:" + this.getCurrentTime());
            PrivateConfig.printLog(data);
        }
        mapTian = stringToPosition(data);
        if (oldOrders == null) {
            //后端服务重启后，old是null，需要给傅初始值
            PrivateConfig.printLog("给old赋值");
            oldOrders = stringToMap(mapTian);
        }

        Map<String, Order> newOrders = stringToMap(mapTian);
        xiaDan(oldOrders, newOrders, data);

    }



    public void xiaDan(Map<String, Order> oldOrders, Map<String, Order> newOrders, String data)  {

        if(diaoYongCount2 > 100){
            diaoYongCount2 = 0;
            PrivateConfig.printLogJianKong();
            PrivateConfig.printLog("newOrders");
            for(Map.Entry<String, Order> entryNew : newOrders.entrySet()){
                PrivateConfig.printLog(entryNew.getKey());
                PrivateConfig.printLog(entryNew.getValue().toString());
            }
            PrivateConfig.printLog("oldOrders");
            for(Map.Entry<String, Order> entryNew : oldOrders.entrySet()){
                PrivateConfig.printLog(entryNew.getKey());
                PrivateConfig.printLog(entryNew.getValue().toString());
            }
        }

        List<Order> orderListNew = new ArrayList<>();

        for(Map.Entry<String, Order> entryNew : newOrders.entrySet()){
            String key = entryNew.getKey();
            if(!oldOrders.containsKey(key)){
                //有新单子了
                String symbolPoSide = key.split("__")[0];
                for(Map.Entry<String, Order> entryOld : oldOrders.entrySet()){
                    if(entryOld.getKey().contains(symbolPoSide)){

                        Order orderNew = new Order();
                        orderNew.setSymbol(entryNew.getValue().getSymbol());
                        orderNew.setPositionSide(getPositionSide(entryNew.getValue().getPositionSide(), entryNew.getValue().getOrigQty()));
                        BigDecimal oldCount = entryOld.getValue().getOrigQty();
                        BigDecimal newCount = entryNew.getValue().getOrigQty();
                        BigDecimal result = newCount.subtract(oldCount);
                        orderNew.setOrigQty(result.abs());

                        orderNew.setSide(OrderSide.BUY.toString());
                        if(result.compareTo(ling)<0){
                            orderNew.setSide(OrderSide.SELL.toString());
                        }
                        orderListNew.add(orderNew);

                        //更新旧单子（当数量从1变成2时，数量1的订单移除，所以再从2变成1时，又是一个新单子了）
                        oldOrders.remove(entryOld.getKey());
                        oldOrders.put(key, entryNew.getValue());
                        break;
                    }
                }

                if(!oldOrders.containsKey(key)){
                    //有新币种的单子了
                    Order orderNew = new Order();
                    orderNew.setSymbol(entryNew.getValue().getSymbol());
                    orderNew.setOrigQty(entryNew.getValue().getOrigQty().abs());
                    orderNew.setPositionSide(getPositionSide(entryNew.getValue().getPositionSide(), entryNew.getValue().getOrigQty()));
                    orderNew.setSide(OrderSide.BUY.toString());
                    if(entryNew.getValue().getOrigQty().compareTo(ling)<0){
                        orderNew.setSide(OrderSide.SELL.toString());
                    }
                    orderListNew.add(orderNew);
                    //更新旧单子
                    oldOrders.put(key, entryNew.getValue());
                }
            }
        }

        //处理清仓的币种
        Iterator<Map.Entry<String, Order>> iterator = oldOrders.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Order> entryOld = iterator.next();
            String symbolPoSide = entryOld.getKey().split("__")[0];
            boolean qingCang = true;
            for(Map.Entry<String, Order> entryNew : newOrders.entrySet()){
                if(entryNew.getKey().contains(symbolPoSide)){
                    //现在还有
                    qingCang = false;
                    break;
                }
            }
            //现在没有了
            if(qingCang){
                QingCang3 qingCang3 = new QingCang3();
                qingCang3.qingCang(PrivateConfig.genDan_personInfoList, entryOld.getValue().getSymbol(), getPositionSide(entryOld.getValue().getPositionSide(), entryOld.getValue().getOrigQty()));
                //删除旧订单
                iterator.remove();
            }
        }


        if (CollectionUtils.isNotEmpty(orderListNew)) {
            PrivateConfig.printLog(data);
            //下单
            for (JSONObject person : PrivateConfig.genDan_personInfoList) {

                try {
                    for (Order order : orderListNew) {
                        MulPostOrders mulGetAllOrders = new MulPostOrders(person, order);
                        threadPoolExecutor.submit(mulGetAllOrders);//启动一般的线程
                        //两个账号之间执行的间隔
                        Thread.sleep(50);
                    }

                } catch (Exception e) {
                    PrivateConfig.printLog(e.getMessage());
                    T5.searchAll("连续三次，有问题，关闭软件，重新启动33。" + e.getMessage());
                }
            }
            JianKong4.needCheck = true;
            JianKong4.checkCount = 0;
//            T5.sendMe("下单了：" + orderListNew.get(0).getSymbol());
        }
    }

    public boolean jiaoYan(String data){
        boolean youXiao = true;
        if (StringUtils.isBlank(data)) {
            return youXiao;
        }
        JSONArray jsonArrayLS = JSON.parseArray(data);
        for (Object o : jsonArrayLS) {
            try{
                JSONObject trade = (JSONObject) o;
                String symbol = trade.getString(PrivateConfig.symbol).toUpperCase();
                String positionSide = trade.getString(PrivateConfig.positionSide).toUpperCase();
                BigDecimal qty = trade.getBigDecimal(PrivateConfig.positionAmount);
                if(StringUtils.isBlank(symbol) || !symbol.contains("USDT")){
                    youXiao = false;
                    break;
                }
                if(StringUtils.isBlank(positionSide)){
                    youXiao = false;
                    break;
                }
                if(!PositionSide.SHORT.toString().equals(positionSide.toUpperCase())
                        && !PositionSide.LONG.toString().equals(positionSide.toUpperCase())){
                    youXiao = false;
                    break;
                }
                if(qty == null){
                    youXiao = false;
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
                youXiao = false;
                break;
            }
        }

        return youXiao;
    }

    public Map<String, Order> stringToMap(Map<String, Position> positionMap) {
        Map<String, Order> map = new HashMap<>();
        for (Map.Entry<String, Position> entry : positionMap.entrySet()) {
            Order order = new Order();
            order.setSymbol(entry.getValue().getSymbol());
            order.setPositionSide(entry.getValue().getPositionSide());
            if(order.getPositionSide().equals(PositionSide.SHORT.toString())){
                //做空时取相反数
                order.setOrigQty(entry.getValue().getPositionAmt().negate());
            }else {
                order.setOrigQty(entry.getValue().getPositionAmt());
            }
            map.put(flg1(entry.getValue()), order);
        }
        return map;
    }

    public Map<String, Position> stringToPosition(String data) {
        Map<String, Position> positionMap = new HashMap<>();
        if (StringUtils.isBlank(data)) {
            return positionMap;
        }

        JSONArray jsonArrayLS = JSON.parseArray(data);
        for (Object o : jsonArrayLS) {
            JSONObject trade = (JSONObject) o;
            String symbol = trade.getString(PrivateConfig.symbol).toUpperCase();
            String positionSide = trade.getString(PrivateConfig.positionSide).toUpperCase();
            BigDecimal qty = trade.getBigDecimal(PrivateConfig.positionAmount);

            String symBolPosition = symbol + "_" + positionSide;
            if(positionMap.containsKey(symBolPosition)){
                Position position = positionMap.get(symBolPosition);
                position.setPositionAmt(qty.add(position.getPositionAmt()));
            }else {
                Position positionNew = new Position();
                positionNew.setSymbol(symbol);
                positionNew.setPositionSide(positionSide);
                positionNew.setPositionAmt(qty);
                positionMap.put(symBolPosition, positionNew);
            }
        }
        return positionMap;
    }

    public String flg1(Position trade){
        String symbol = trade.getSymbol();
        String positionSide = trade.getPositionSide();
        BigDecimal qty = trade.getPositionAmt();
        return new StringBuilder().append(symbol).append("_").append(positionSide).append("__").append(qty).toString();
    }


    public String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

}