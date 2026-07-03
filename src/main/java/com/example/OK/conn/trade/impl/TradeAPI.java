package com.example.OK.conn.trade.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.OK.conn.ClosePositions;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TradeAPI {

    //下单 Place Order
    @POST("/api/v5/trade/order")
    Call<JSONObject> placeOrder(@Body JSONObject jsonObject);

    //市价仓位全平 Close Positions
    @POST("/api/v5/trade/close-position")
    Call<JSONObject> closePositions(@Body ClosePositions closePositions);

    @GET("/api/v5/account/account-position-risk")
    Call<JSONObject> getAccountAndPosition(@Query("instType") String instType);

}
