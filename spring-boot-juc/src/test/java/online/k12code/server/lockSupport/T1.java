package online.k12code.server.lockSupport;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Carl
 * @date 2023/9/15
 **/
public class T1 {

    /**
     * 使用Object堵塞线程
     */

    @Test
    void o1 () {
        Object object = new Object();
        new Thread(() -> {
            // 打印线程
            System.out.println(Thread.currentThread().getName()+"=====进入");
            synchronized (object) {
                try {
                    System.out.println("睡着");
                    object.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName()+"被唤醒");
            }
        },"t1").start();
        // 等待一秒
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            synchronized (object) {
                System.out.println(Thread.currentThread().getName()+"进行唤醒");
                object.notify();
            }
        },"t2").start();

    }

    @Test
    void o2() {
        // 创建锁对象
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();
        new Thread(() -> {
            reentrantLock.lock();
            try {
                System.out.println(Thread.currentThread().getName()+"睡着");
                condition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                // 注意锁的关闭
                reentrantLock.unlock();
            }
            System.out.println(Thread.currentThread().getName()+"被唤醒");
        },"t1").start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(() -> {
            reentrantLock.lock();
            condition.signal();
            reentrantLock.unlock();
            System.out.println(Thread.currentThread().getName()+"唤醒");
        },"t2").start();
    }

    @Test
    void o3() {
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "睡着");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName()+"被唤醒");
        }, "t1");
        t1.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Thread t2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "唤醒");
            LockSupport.unpark(t1);
        }, "t2");
        t2.start();

    }
}
