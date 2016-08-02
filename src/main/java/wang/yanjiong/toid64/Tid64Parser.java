package wang.yanjiong.toid64;

import static wang.yanjiong.toid64.Abstract64Generator.LEN_TID_TYPE;
import static wang.yanjiong.toid64.Abstract64Generator.LEN_TO;
import static wang.yanjiong.toid64.Abstract64Generator.LEN_TOTAL;
import static wang.yanjiong.toid64.Tid64Generator.*;

/**
 * Created by WangYanJiong on 8/2/16.
 */
public class Tid64Parser {


    static short[] id2Array(final long id) {
        final short type = (short) ((0x7) & (id >> (LEN_TOTAL - LEN_TO - LEN_TID_TYPE)));

        final int l_ser = LEN_TYPE_SER[type];
        final int l_sisser = LEN_TYPE_INS[type] + l_ser;

        final long MASK_SYSTEM = (1 << LEN_TYPE_SYS[type]) - 1;
        final long MASK_INSTANCE = (1 << LEN_TYPE_INS[type]) - 1;
        final long MASK_SER = (1 << LEN_TYPE_SER[type]) - 1;

        final short serial = (short) (id & MASK_SER);
        final short instance = (short) ((id >> l_ser) & MASK_INSTANCE);
        final short system = (short) ((id >> l_sisser) & MASK_SYSTEM);

        final short second = (short) ((id >> LEN_SIS) & MASK_SECOND);
        final short minute = (short) ((id >> (LEN_TIME_SS + LEN_SIS)) & MASK_MINUTE);
        final short hour = (short) ((id >> (LEN_TIME_MM + LEN_TIME_SS + LEN_SIS)) & MASK_HOUR);
        final short date = (short) ((id >> (LEN_TIME + LEN_SIS)) & MASK_DATE);
        final short month = (short) ((id >> (LEN_DATE_DD + LEN_TIME + LEN_SIS)) & MASK_MONTH);
        final short year = (short) ((id >> (LEN_DATE_MM + LEN_DATE_DD + LEN_TIME + LEN_SIS)) & MASK_YEAR);
        final short to = (short) ((0x1) & id >> (LEN_TOTAL - LEN_TO));

        return new short[]{to, type, year, month, date, hour, minute, second, system, instance, serial};

    }
}
