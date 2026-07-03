package com.example.OK.conn.trade;

import com.alibaba.fastjson.JSONObject;
import com.example.OK.conn.ClosePositions;
import com.example.OK.conn.PlaceOrder;
import com.example.OK.conn.client.APIClient;
import com.example.OK.conn.trade.impl.TradeAPI;

public interface TradeAPIService {

    //下单 Place Order
    JSONObject placeOrder(APIClient client, TradeAPI tradeAPI, PlaceOrder placeOrder);
    //市价仓位全平 Close Positions
    JSONObject closePositions(APIClient client, TradeAPI tradeAPI,ClosePositions closePositions);

    JSONObject getAccountAndPosition(APIClient client, TradeAPI tradeAPI, String instType);


}
