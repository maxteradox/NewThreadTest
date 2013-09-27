package ch.racine.gabriel.threadpools;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class $10ForkJoin {

  // A recursive resultless ForkJoinTask
  static class Solver extends RecursiveAction {
    private static final long serialVersionUID = 1L;

    private int[] list;
    private long result;

    public Solver(int[] array) {
      this.list = array;
    }

    @Override
    protected void compute() {
      if (list.length == 1) {
        result = list[0];
      } else {
        int midPoint = list.length / 2;
        int[] l1 = Arrays.copyOfRange(list, 0, midPoint);
        int[] l2 = Arrays.copyOfRange(list, midPoint, list.length);
        Solver s1 = new Solver(l1);
        Solver s2 = new Solver(l2);
        invokeAll(s1, s2);
        // forkJoin(s1, s2);
        result = s1.result + s2.result;
      }
    }

    public long getResult() {
      return result;
    }
  }

  static class Problem {
    private final int[] list = new int[20000];

    public Problem() {
      Random generator = new Random(123545);
      for (int i = 0; i < list.length; i++) {
        list[i] = generator.nextInt(500);
      }

    }

    public int[] getList() {
      return list;
    }
  }

  public static void main(String[] args) {
    Problem test = new Problem();

    int nThreads = Runtime.getRuntime().availableProcessors();
    System.out.printf("Anzahl Prozessoren: %d%n", nThreads);
    int[] list = test.getList();
    Solver mfj = new Solver(list);

    ForkJoinPool pool = new ForkJoinPool(nThreads);
    pool.invoke(mfj);

    long result = mfj.getResult();
    System.out.println("Done. Result = " + result);

    int sum = 0;
    for (int i = 0; i < test.getList().length; i++) {
      sum += test.getList()[i];
    }
    System.out.println("Done. Result = " + sum);
  }
}
