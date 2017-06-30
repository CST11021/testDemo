import sun.rmi.runtime.Log;

import java.util.Iterator;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Created by wb-whz291815 on 2017/6/27.
 */
public class Demo {

    // 方案一：
    public static long sequentialSum1(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .reduce(0L, Long::sum);
    }

    // 方案二：使用数值流，杜绝拆/装箱的额外开销
    public static long sequentialSum2(long n) {
        return LongStream.iterate(1L, i -> i+1)
                .limit(n)
                .reduce(0L, Long::sum);
    }

    // 方案三：用传统 for 循环的迭代版本执行起来比方案二会快一点点，因为它更为底层，同时也不需要对原始类型做任何装箱或拆箱操作
    public static long sequentialSum3(long n) {
        long result = 0;
        for (long i = 1L; i <= n; i++) {
            result += i;
        }
        return result;
    }

    // 方案四：并行化处理
    public static long parallelSum1(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .parallel()
                .reduce(0L, Long::sum);
    }

    // 方案五：并行化，并杜绝拆装箱开销
    public static long parallelSum2(long n) {
        return LongStream.iterate(1L, i -> i+1)
                .limit(n)
                .parallel()
                .reduce(0L, Long::sum);
    }

    // 方案六：迭代问题的优化
    public static long RangedSum(long n) {
        return LongStream.rangeClosed(1, n)
                .reduce(0L, Long::sum);
    }

    // 方案七：迭代问题的优化并做并行处理
    public static long parallelRangedSum(long n) {
        return LongStream.rangeClosed(1, n)
                .parallel()
                .reduce(0L, Long::sum);
    }

    /**
     * measureSumPerf方法接受一个函数和一个 long 作为参数,它会对传给方法的 long 应用函数10次，
     * 记录每次执行的时间（以毫秒为单位），并返回最短的一次执行时间，用这个框架来测试顺序加法器函数对前一千万个自然数求和要用多久。
     * @param adder
     * @param n
     * @return
     */
    public static long measureSumPerf(Function<Long, Long> adder, long n) {
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            long sum = adder.apply(n);
            long duration = (System.nanoTime() - start) / 1000000;
//            System.out.println("Result: " + sum);
            if (duration < fastest) fastest = duration;
        }
        return fastest;
    }

    public static void main(String[] args) {

        //System.out.println("本机器的处理器数量：" + Runtime.getRuntime().availableProcessors());
        System.out.println("sequentialSum1:" + measureSumPerf(Demo::sequentialSum1, 10000000) + " msecs");//sequentialSum:96 msecs
        System.out.println("sequentialSum2:" + measureSumPerf(Demo::sequentialSum2, 10000000) + " msecs");//sequentialSum:96 msecs
        System.out.println("sequentialSum3:" + measureSumPerf(Demo::sequentialSum3, 10000000) + " msecs");//sequentialSum:96 msecs
        System.out.println("parallelSum1:" + measureSumPerf(Demo::parallelSum1, 10000000) + " msecs");//Iterative:6 msecs
        System.out.println("parallelSum2:" + measureSumPerf(Demo::parallelSum2, 10000000) + " msecs");//parallelSum:134 msec
        System.out.println("RangedSum:" + measureSumPerf(Demo::RangedSum, 10000000) + " msecs");//parallelRangedSum:1 msecs
        System.out.println("parallelRangedSum:" + measureSumPerf(Demo::parallelRangedSum, 10000000) + " msecs");//parallelRangedSum:1 msec



        ForkJoinPool forkJoinPool;















    }
}

