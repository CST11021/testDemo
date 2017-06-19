import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ExecutorTest {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorProcessPool pool = ExecutorProcessPool.getInstance();

        for (int i = 0; i < 3; i++) {
            Future<?> future = pool.submit(new ExcuteTask1(i+""));
            // 如果接收线程返回值，future.get() 会阻塞，如果这样写主线就会停止，等待结果返回后才去提交下一个任务，
            // 那相当于一个线程一个线程的执行了。所以非特殊情况不建议使用接收返回值的。
            System.out.println(future.get());

        }


//        for (int i = 0; i < 20; i++) {
//            pool.execute(new ExcuteTask2(i+""));
//        }

        //关闭线程池，如果是需要长期运行的线程池，不用调用该方法。监听程序退出的时候最好执行一下。
        pool.shutdown();
    }

    //以Callable方式执行任务
    static class ExcuteTask1 implements Callable<String> {
        private String taskName;

        public ExcuteTask1(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public String call() throws Exception {
            long startTime = System.currentTimeMillis();
            try {
                //Java 6/7最佳的休眠方法为TimeUnit.MILLISECONDS.sleep(100);最好不要用 Thread.sleep(100);
                TimeUnit.MILLISECONDS.sleep((int)(Math.random() * 1000));// 1000毫秒以内的随机数，模拟业务逻辑处理
            } catch (Exception e) {
                e.printStackTrace();
            }
            long castTime = System.currentTimeMillis() - startTime;
            System.out.println("-------------这里执行业务逻辑，Callable TaskName = " + taskName + "耗时：" + castTime + "-------------");
            return ">>>>>>>>>>>>>线程返回值，Callable TaskName = " + taskName + "耗时：" + castTime + "<<<<<<<<<<<<<<";
        }
    }

    //以Runable方式执行任务
    static class ExcuteTask2 implements Runnable {
        private String taskName;

        public ExcuteTask2(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public void run() {
            try {
                TimeUnit.MILLISECONDS.sleep((int)(Math.random() * 1000));// 1000毫秒以内的随机数，模拟业务逻辑处理
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("-------------这里执行业务逻辑，Runnable TaskName = " + taskName + "-------------");
        }

    }
}