package com.example.bian.genDan;

import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.client.model.enums.PositionSide;
import com.example.bian.client.model.trade.Order;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

import static com.example.bian.client.bushu.PrivateConfig.getXSM;

public class MulPostOrders implements Callable {

    private JSONObject personInfo;
    private Order order;

    public MulPostOrders(JSONObject personInfo, Order order){
        this.personInfo = personInfo;
        this.order = order;
    }

    @Override
    public Object call() throws Exception {

        try {
            BigDecimal beiShu = new BigDecimal(personInfo.getString(PrivateConfig.beiShu));
            SyncRequestClient syncRequestClient = (SyncRequestClient) personInfo.get(PrivateConfig.syncRequestClient);
            //跟单数量
            BigDecimal geShu = order.getOrigQty().multiply(beiShu);
            String getOrigQty = geShu.setScale(getXSM(order.getSymbol()), BigDecimal.ROUND_DOWN).toString();

            if (order.getPositionSide().equals(PositionSide.BOTH.toString())) {
                PrivateConfig.postOrder(syncRequestClient,
                        order.getSymbol(),
                        order.getSide(),
                        order.getPositionSide(),
                        order.getReduceOnly().toString(),
                        getOrigQty);
            } else {
                PrivateConfig.postOrder(syncRequestClient,
                        order.getSymbol(),
                        order.getSide(),
                        order.getPositionSide(),
                        null,
                        getOrigQty);
            }


            String msg = Thread.currentThread().getName() + "---下单成功，没有问题！个数为：" + getOrigQty + "---" + PrivateConfig.getCurrentTime();
            PrivateConfig.printLog(msg);

        } catch (Exception e) {
            e.printStackTrace();
//            PrivateConfig.printLog(PrivateConfig.fileWriter, e);
            T5.searchAll("连续3次，有问题，关闭软件，重新启动7。" + personInfo.getString(PrivateConfig.name) + e.getMessage());
        }
        return null;
    }


}
