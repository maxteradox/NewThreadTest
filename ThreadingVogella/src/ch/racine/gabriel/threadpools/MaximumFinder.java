package ch.racine.gabriel.threadpools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MaximumFinder extends RecursiveTask<Integer> {

  private static final int SEQUENTIAL_THRESHOLD = 2;
  private final int[] data;
  private final int start;
  private final int end;

  public MaximumFinder(int[] data) {
    this(data, 0, data.length);
  }

  public MaximumFinder(int[] data, int start, int end) {
    this.data = data;
    this.start = start;
    this.end = end;
  }

  @Override
  protected Integer compute() {
    final int length = end - start;
    if (length < SEQUENTIAL_THRESHOLD) {
      return computeDirectly();
    }
    final int split = length / 2;
    final MaximumFinder left = new MaximumFinder(data, start, start + split);
    left.fork();
    final MaximumFinder right = new MaximumFinder(data, start + split, end);
    Integer r = right.compute();
    Integer l = left.join();
    System.out.printf("r=%d l=%d%n", r, l);
    return Math.max(r, l);
  }

  private Integer computeDirectly() {
    System.out.println(Thread.currentThread() + " computing: " + start + " to "
        + end);
    int max = data[start];
    for (int i = start + 1; i < end; i++) {
      if (data[i] > max) {
        max = data[i];
      }
    }
    return max;
  }

  public static void main(String[] args) {
    // create a random data set
    final int[] data = new int[10];
    final Random random = new Random();
    for (int i = 0; i < data.length; i++) {
      data[i] = random.nextInt(100);
    }
    System.out.println(Arrays.toString(data));
    
    
    // submit the task to the pool
    final ForkJoinPool pool = new ForkJoinPool(4);
    final MaximumFinder finder = new MaximumFinder(data);
    Integer result = pool.invoke(finder);
    System.out.println(result);
  }
}
