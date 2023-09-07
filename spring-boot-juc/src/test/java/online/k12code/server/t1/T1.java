package online.k12code.server.t1;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Carl
 * @date 2023/9/7
 **/
public class T1 {

    @Test
    public void t1(){
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }).thenApply(result -> {
            System.err.println(result);
            return result+1;
        }).whenComplete((result, throwable) -> {
            if(throwable==null){
                System.out.println(Thread.currentThread().getName()+"\t"+"result = " + result);
            }
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
        System.out.println(Thread.currentThread().getName()+"\t"+"over...");
        //主线程不要立即结束,否则CompletableFuture默认使用的线程池会立即关闭,暂停几秒
        try { TimeUnit.SECONDS.sleep(3);  } catch (InterruptedException e) {e.printStackTrace();}
    }

    @Test
    public void t2() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture
                .supplyAsync(() -> 1)
                .thenApply(result -> {
                    System.err.println(2);
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(result);
                    return result + 1;
                });

//        System.out.println(integerCompletableFuture.get());
    }
}
