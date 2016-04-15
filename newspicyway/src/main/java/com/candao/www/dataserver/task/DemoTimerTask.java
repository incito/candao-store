package com.candao.www.dataserver.task;

import com.candao.www.dataserver.service.msghandler.MsgForwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ytq on 2016/4/15.
 */
@Service
public class DemoTimerTask {
    @Autowired
    private MsgForwardService msgForwardService;
    private Timer timer;
    private String group;
    private String userId;
    private String msgType;
    private String msg;
    private Boolean isSingle;

    public synchronized void run(String group, String userId, String msgType, String msg, Boolean isSingle, int seconds) {
        if (timer != null) {
            cancel();
        }
        this.group = group;
        this.userId = userId;
        this.msgType = msgType;
        this.msg = msg;
        this.isSingle = isSingle;
        this.timer = new Timer();
        timer.schedule(new RemindTask(), 0, seconds * 1000);
    }

    class RemindTask extends TimerTask {
        public void run() {
            if (null == group && null == isSingle) {
                msgForwardService.broadCastMsg(userId, msgType, msg);
            } else {
                if (null == group) {
                    msgForwardService.broadCastMsg(userId, msgType, msg, isSingle);
                } else {
                    msgForwardService.broadCastMsg(group, userId, msgType, msg, isSingle);
                }
            }
        }
    }

    public synchronized void cancel() {
        timer.cancel();
    }
}
