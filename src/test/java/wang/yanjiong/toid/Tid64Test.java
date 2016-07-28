package wang.yanjiong.toid;

import org.junit.Test;

/**
 * Created by WangYanJiong on 7/26/16.
 */

public class Tid64Test {

    @Test
    public void TestGenerator() {
        Tid64Generator tid = Tid64Generator.newInstance(Tid64Type.T8, 12, 12);
        for (int i = 0; i < 200; i++) {
            long id = tid.nextId();
            System.out.println(Long.toHexString(id) + "," + tid.toLogString(id));
        }
    }

}
