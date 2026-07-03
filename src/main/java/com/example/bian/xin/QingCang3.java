package com.example.bian.xin;

import com.alibaba.fastjson.JSONObject;
import com.example.bian.client.SyncRequestClient;
import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.model.trade.AccountInformation;
import com.example.bian.client.model.trade.Position;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

public class QingCang3 {
    static String All = "all";

    public static void main(String[] args)
            throws IOException, InterruptedException {
        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10809");


        args = new String[5];

        System.out.println("开始啦");
        args[0] = "E://code//biance";
        args[1] = "qingCang";
        args[2] = "IDUSDT";
        args[3] = "SHORT";
        args[4] = "--server.port=10187";
        PrivateConfig.before(args[0], "0");
        QingCang3 qingCang = new QingCang3();
        qingCang.method(args, PrivateConfig.genDan_personInfoList);
    }

    public void method(String[] args, List<JSONObject> listPersonInfo) {
        if (args.length > 3) {
            if(args.length>4){
                qingCang(listPersonInfo, args[2], args[3]);
            }else {
                qingCang(listPersonInfo, args[2], null);
            }
        } else {
            qingCang(listPersonInfo, null, null);
        }

    }

    /**
     *
     * @param listPersonInfo
     * @param symbol 为null时，清仓所有，此时忽略positionSide的值
     * @param positionSide 为null时，清仓所有方向的。否则清仓指定方向
     */
    public void qingCang(List<JSONObject> listPersonInfo, String symbol, String positionSide) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(listPersonInfo.size(), listPersonInfo.size(), 10L, TimeUnit.SECONDS, new LinkedBlockingQueue(), Executors.defaultThreadFactory(), new AbortPolicy());
        for (JSONObject jsonObject : listPersonInfo) {
            MulTradeOrder1 mulTradeOrder1 = new MulTradeOrder1(jsonObject, symbol, positionSide);
            threadPoolExecutor.submit(mulTradeOrder1);
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (threadPoolExecutor.getActiveCount() > 0) {
            Thread.yield();
        }
        System.out.println("清仓结束。");
        threadPoolExecutor.shutdown();
    }
}

class MulTradeOrder1  implements Callable {

    JSONObject personInfo;
    String symbol;
    String positionSide;

    /**
     * 当symbol为null时，清仓所有，此时positionSide的值可以忽略
     * @param jsonObject
     * @param symbol
     * @param positionSide
     */
    MulTradeOrder1(JSONObject jsonObject, String symbol, String positionSide) {
        this.personInfo = jsonObject;
        this.symbol = symbol;
        this.positionSide = positionSide;
    }

    @Override
    public Object call()
            throws Exception {
        try {
            SyncRequestClient syncRequestClient = (SyncRequestClient) this.personInfo.get("syncRequestClient");
            AccountInformation accountInformation = syncRequestClient.getAccountInformation();
            List<Position> positionList = accountInformation.getPositions();
            for (Position position : positionList) {
                BigDecimal geShu = position.getPositionAmt().abs();
                if (geShu.compareTo(PrivateConfig.ling) > 0) {
                    boolean qing = false;
                    if (StringUtils.isBlank(this.symbol)) {
                        qing = true;
                    } else if (position.getSymbol().equals(this.symbol.trim())) {
                        if (StringUtils.isBlank(this.positionSide)) {
                            qing = true;
                        } else if (position.getPositionSide().equals(this.positionSide.trim())) {
                            qing = true;
                        }
                    }
                    if (qing) {
                        PrivateConfig.jianCang(syncRequestClient, position.getSymbol(), position.getPositionSide(), geShu.toString(), position.getPositionAmt());
                        Thread.sleep(50);
                    }
                }
            }
//            System.out.println(this.personInfo.getString("alias") + "清仓完毕。");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

