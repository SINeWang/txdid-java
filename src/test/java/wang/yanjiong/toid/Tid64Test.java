package wang.yanjiong.toid;

import org.junit.Test;

/**
 * Created by WangYanJiong on 7/26/16.
 */

public class Tid64Test {

    @Test
    public void TestGenerator() {
        Tid64Generator generator = new Tid64Generator(Tid64Type.T8, 12, 12);
        Tid64 tid64 = generator.next();

        String[] s = tid64.toString().split("-");


        long now = System.currentTimeMillis();
        for (int i = 0; i < 200; i++) {
            Tid64 id = generator.next();
            System.out.println(id.value() + ", " + id + ", "  + id.decoded() + ", " + Tid64Generator.fromString(id.toString()));
        }
        System.out.println(System.currentTimeMillis() - now);

    }

}
