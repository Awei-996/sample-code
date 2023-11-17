package online.k12code.server;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Carl
 * @date 2023/9/22
 **/
public class Atomic {

    public static final int SIZE  = 50;

        public static void main(String[] args) throws InterruptedException {
            T1 t = new T1();
            CountDownLatch countDownLatch = new CountDownLatch(SIZE);
               for (int i = 0; i < SIZE; i++) {
                    new Thread(() -> {
                        try {
                            for (int j = 0; j < 10; j++) {
                                t.t1();
                            }
                        } finally {
                            countDownLatch.countDown();
                        }
                    },String.valueOf(i)).start();
                   System.out.println(Thread.currentThread().getName());
                }

            countDownLatch.await();
            System.out.println(Thread.currentThread().getName() + "\t" + "result: " + t.atomicInteger.get());//main	result: 500

        }
}


class T1 {

    AtomicInteger atomicInteger = new AtomicInteger();

    public void t1(){
        atomicInteger.getAndIncrement();
    }
}
