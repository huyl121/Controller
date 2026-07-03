package com.example.OK.conn.trade.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.OK.conn.ClosePositions;
import com.example.OK.conn.PlaceOrder;
import com.example.OK.conn.client.APIClient;
import com.example.OK.conn.trade.TradeAPIService;


public class TradeAPIServiceImpl implements TradeAPIService {

    //下单 Place Order
    @Override
    public JSONObject placeOrder(APIClient client, TradeAPI tradeAPI, PlaceOrder placeOrder) {
        return client.executeSync(tradeAPI.placeOrder(JSONObject.parseObject(JSON.toJSONString(placeOrder))));
    }


    //市价仓位全平 Close Positions
    @Override
    public JSONObject closePositions(APIClient client, TradeAPI tradeAPI, ClosePositions closePositions) {
        return client.executeSync(tradeAPI.closePositions(closePositions));
    }

    @Override
    public JSONObject getAccountAndPosition(APIClient client, TradeAPI tradeAPI,String instType) {
        return client.executeSync(tradeAPI.getAccountAndPosition(instType));
    }

}
