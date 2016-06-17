package com.candao.www.printer.listener;

import org.springframework.jms.listener.DefaultMessageListenerContainer;

/**
 * Created by Administrator on 2016-6-12.
 */
public class DefaultMessageListenerContainerAdapter extends DefaultMessageListenerContainer {

    @Override
    public void start() {
    }

    public void trulyStart() {
        super.start();
    }

    @Override
    public void stop(Runnable callback) {
        // TODO: 2016/6/17 有任务阻塞的监听器不能关闭 导致外层阻塞
        this.shutdown();
        callback.run();
    }
}
