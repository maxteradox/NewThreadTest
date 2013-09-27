package ch.racine.gabriel.threadpools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class $08CallableFutures {

  private static class MyCallable implements Callable<Long> {
    private long countUntil;

    public MyCallable(long countUntil) {
      this.countUntil = countUntil;
    }

    @Override
    public Long call() throws Exception {
      long count = 0;
      for (int i = 0; i < countUntil; i++) {
        count++;
      }
      return count;
    }
  }

  private static final int NTHREADS = 5;

  public static void main(String[] args) throws InterruptedException,
      ExecutionException {
    ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
    List<Future<Long>> list = new ArrayList<Future<Long>>();
    for (int i = 1; i <= 100; i++) {
      Callable<Long> worker = new MyCallable(1000L + i);
      Future<Long> submit = executor.submit(worker);
      // Resultat in Liste einfügen
      list.add(submit);
    }

    // Retrieve the results
    int i = 0;
    for (Future<Long> future : list) {
      System.out.printf("%4d: %d%n", ++i, future.get());
    }
    executor.shutdown();
  }
}
