package com.cgx.Job;

public class Thread1 implements Runnable {
    public void run() {

            for (int i = 0; i < 5000; i++) {
                System.out.println(Thread.currentThread().getName() + " synchronized loop " + i);
            }

    }
    public static void main(String[] args) {
        Thread1 t1 = new Thread1();
        Thread ta = new Thread(t1, "A");
        Thread tb = new Thread(t1, "B");
        ta.start();
        tb.start();
    }
}