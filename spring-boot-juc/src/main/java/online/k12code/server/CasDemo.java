package online.k12code.server;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 自旋锁
 * @author Carl
 * @date 2023/9/22
 **/
public class CasDemo {

    AtomicReference<Thread> atomicReference = new AtomicReference<Thread>();

    public static void main(String[] args) {
        CasDemo casDemo = new CasDemo();
        new Thread(() -> {
            casDemo.lock();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            casDemo.unlock();
        },"t1").start();
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        new Thread(() -> {
            casDemo.lock();
            casDemo.unlock();
        },"t2").start();
    }

    // 加锁
    public void lock(){
        Thread thread = Thread.currentThread();
        System.out.println(Thread.currentThread().getName() + "\t -----------come in");
        while (!atomicReference.compareAndSet(null,thread)){

        }
    }

    // 去锁
    public void unlock(){
        Thread thread = Thread.currentThread();
        atomicReference.compareAndSet(thread,null);
        System.out.println(Thread.currentThread().getName() + "\t -----------come out");
    }

}
