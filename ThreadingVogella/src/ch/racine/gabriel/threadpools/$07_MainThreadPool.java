package ch.racine.gabriel.threadpools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class MyRunnable implements Runnable {
  private long countUntil;

  public MyRunnable(long countUntil) {
    this.countUntil = countUntil;
  }

  @Override
  public void run() {
    long count = 0;
    for (long i = 1; i <= countUntil; i++) {
      count++;
    }
    System.out.println(count);
  }
}

public class $07_MainThreadPool {
  private static final int NTHREADS = 2;
  static ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);

  public static void main(String[] args) throws InterruptedException {
    for (int i = 1; i <= 25; i++) {
      MyRunnable worker = new MyRunnable(1000L + i);
      executor.execute(worker);
    }
    executor.shutdown();
    executor.awaitTermination(365, TimeUnit.DAYS);
    System.out.println("Finished all threads!");
  }
}
