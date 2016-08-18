/*
The MIT License (MIT)

Copyright (c) 2016 Yanjiong Wang(https://yanjiong.wang)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package wang.yanjiong.derid64;

import static wang.yanjiong.derid64.Abstract64Generator.LEN_DATE_DD;
import static wang.yanjiong.derid64.Abstract64Generator.LEN_DATE_MM;
import static wang.yanjiong.derid64.Abstract64Generator.LEN_R;
import static wang.yanjiong.derid64.Abstract64Generator.LEN_TID_TYPE;
import static wang.yanjiong.derid64.Abstract64Generator.LEN_TIME;
import static wang.yanjiong.derid64.Abstract64Generator.LEN_TIME_MM;
import static wang.yanjiong.derid64.Abstract64Generator.LEN_TIME_SS;
import static wang.yanjiong.derid64.Abstract64Generator.LEN_TOTAL;
import static wang.yanjiong.derid64.Abstract64Generator.MASK_DATE;
import static wang.yanjiong.derid64.Abstract64Generator.MASK_HOUR;
import static wang.yanjiong.derid64.Abstract64Generator.MASK_MINUTE;
import static wang.yanjiong.derid64.Abstract64Generator.MASK_MONTH;
import static wang.yanjiong.derid64.Abstract64Generator.MASK_SECOND;
import static wang.yanjiong.derid64.Abstract64Generator.MASK_YEAR;
import static wang.yanjiong.derid64.Rid64Generator.*;

/**
 * Created by WangYanJiong on 8/2/16.
 */
public class Derid64Parser {

    private static String[] PADS = {
            "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
            "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
            "50", "51", "52", "53", "54", "55", "56", "57", "58", "59",
            "60", "61", "62", "63", "64", "65", "66", "67", "68", "69",
            "70", "71", "72", "73", "74", "75", "76", "77", "78", "79",
            "80", "81", "82", "83", "84", "85", "86", "87", "88", "89",
            "90", "91", "92", "93", "94", "95", "96", "97", "98", "99"
    };

    static String padding(short number) {
        return PADS[number];
    }

    public static long fromHexString(String id) {
        return Long.valueOf(id, 16);
    }

    static short[] tid2Array(final long id) {
        final short type = (short) ((0x7) & (id >> (LEN_TOTAL - LEN_R - LEN_TID_TYPE)));

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
        final short r = (short) ((0x1) & id >> (LEN_TOTAL - LEN_R));

        return new short[]{r, type, year, month, date, hour, minute, second, system, instance, serial};
    }

    static short[] oid2Array(final long id) {
        final int l_ser = 19;
        final int l_ins = 12;
        final int l_is = 31;
        final long MASK_INSTANCE = (1 << l_ins) - 1;
        final long MASK_SER = (1 << l_ser) - 1;

        final short serial = (short) (id & MASK_SER);
        final short instance = (short) ((id >> l_ser) & MASK_INSTANCE);

        final short second = (short) ((id >> l_is) & MASK_SECOND);
        final short minute = (short) ((id >> (LEN_TIME_SS + l_is)) & MASK_MINUTE);
        final short hour = (short) ((id >> (LEN_TIME_MM + LEN_TIME_SS + l_is)) & MASK_HOUR);
        final short date = (short) ((id >> (LEN_TIME + l_is)) & MASK_DATE);
        final short month = (short) ((id >> (LEN_DATE_DD + LEN_TIME + l_is)) & MASK_MONTH);
        final short year = (short) ((id >> (LEN_DATE_MM + LEN_DATE_DD + LEN_TIME + l_is)) & MASK_YEAR);
        final short r = (short) ((0x1) & id >> (LEN_TOTAL - LEN_R));

        return new short[]{r, year, month, date, hour, minute, second, instance, serial};

    }

}
