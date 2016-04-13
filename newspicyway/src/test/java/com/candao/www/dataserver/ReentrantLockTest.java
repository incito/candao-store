package com.candao.www.dataserver;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ytq on 2016/4/13.
 */
public class ReentrantLockTest {
    public static void main(String[] args) {
        final Lock lock = new ReentrantLock();
        final Condition condition = lock.newCondition();
        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("###########");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                condition.signal();
                System.out.println("###########1");
            }
        }).start();
    }
}
