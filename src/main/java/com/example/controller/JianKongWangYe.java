package com.example.controller;

import com.example.bian.client.bushu.PrivateConfig;
import com.example.bian.client.bushu.T5;
import com.example.bian.xin.QingCang3;

import static com.example.bian.client.bushu.PrivateConfig.fangWenTiem;
import static com.example.bian.client.bushu.PrivateConfig.stop;

public class JianKongWangYe {
    public void method() throws InterruptedException {

        PrivateConfig.printLog("启动网页调用监控线程" );
        int errorCount = 0;
        while (true) {
            if(PrivateConfig.oldOrders != null){
                if (System.currentTimeMillis() - fangWenTiem > 20 * 1000) {
                    errorCount++;
                    T5.searchAll("前端出问题了，抓紧联系胡亚龙");
                } else {
                    errorCount = 0;
                }
                if (errorCount > PrivateConfig.getPon) {
                    stop = true;
                    QingCang3 qingCang3 = new QingCang3();
                    qingCang3.qingCang(PrivateConfig.genDan_personInfoList, null, null);
                    T5.searchAll("前端出问题了，停止带单，抓紧联系胡亚龙");
                    break;
                }
            }
            PrivateConfig.printLog("errorCount：" + errorCount);
            Thread.sleep(60 * 1000);
        }

    }
}
