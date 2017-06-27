import sun.rmi.runtime.Log;

import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Created by wb-whz291815 on 2017/6/27.
 */
public class Demo {



    public interface T {
        public Test a = new Test();

        public void test();
    }

}
