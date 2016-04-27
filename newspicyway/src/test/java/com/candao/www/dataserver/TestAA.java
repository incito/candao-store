package com.candao.www.dataserver;

/**
 * Created by ytq on 2016/4/13.
 */
public class TestAA {
    static Object object = new Object();

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        System.out.println((int) System.currentTimeMillis());
        Integer a = Math.round(2.5f);
        System.out.println();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (TestAA.class) {
                    try {
                        TestAA.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("###########");
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (TestAA.class) {
                    try {
                        TestAA.class.notify();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("###########1");
                }
            }
        }).start();
    }
}
