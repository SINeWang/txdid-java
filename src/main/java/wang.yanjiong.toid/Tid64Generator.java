/*
The MIT License (MIT)

Copyright (c) 2016 Yanjiong Wang

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
package wang.yanjiong.toid;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by WangYanJiong on 7/22/16.
 */


public class Tid64Generator {

    public static final int LEN_TOTAL = 64;

    public static final int LEN_TO = 1;

    public static final int LEN_TYPE = 3;

    public static final int LEN_DATE_YY = 7;

    public static final int LEN_DATE_MM = 4;

    public static final int LEN_DATE_DD = 5;

    public static final int LEN_TIME_HH = 5;

    public static final int LEN_TIME_MM = 6;

    public static final int LEN_TIME_SS = 6;

    private static final int LEN_DATE = LEN_DATE_YY + LEN_DATE_MM + LEN_DATE_DD;

    private static final int LEN_TIME = LEN_TIME_HH + LEN_TIME_MM + LEN_TIME_SS;

    private static final int LEN_TSIT = LEN_TOTAL - LEN_DATE - LEN_TIME - LEN_TO;

    public static final int[] LEN_TYPE_SYS = {5, 6, 7, 7, 10, 11, 12, 13};

    public static final int[] LEN_TYPE_INS = {11, 10, 9, 10, 7, 6, 6, 5};

    public static final int[] LEN_TYPE_TPS = {11, 11, 11, 10, 10, 10, 9, 9};

    private long tsi;

    private long timestamp;

    private long rdt;

    private AtomicInteger tps;

    private Tid64Generator(int type, int system, int instance) {
        this.timestamp = 0;
        long lengthOfT = LEN_TYPE_TPS[type];
        long lengthOfIT = LEN_TYPE_INS[type] + lengthOfT;
        long lengthOfSIT = LEN_TYPE_SYS[type] + lengthOfIT;
        this.tsi |= type << lengthOfSIT;
        this.tsi |= system << lengthOfIT;
        this.tsi |= instance << lengthOfT;

    }

    public static Tid64Generator newInstance(int type, int system, int instance) {
        Tid64Generator tid64 = new Tid64Generator(type, system, instance);
        return tid64;


    }

    public long nextId() {
        long now = System.currentTimeMillis();
        long nowRDT = buildRDT(now);
        return nowRDT | tsi | tps.incrementAndGet();
    }

    private synchronized long buildRDT(long now) {

        if (now - timestamp > 1000) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(now));
            long year = calendar.get(Calendar.YEAR);
            long month = calendar.get(Calendar.MONTH) + 1; // base 0
            long date = calendar.get(Calendar.DATE); // base 1
            long hour = calendar.get(Calendar.HOUR_OF_DAY);
            long minute = calendar.get(Calendar.MINUTE);
            long second = calendar.get(Calendar.SECOND);

            long tidYear = year - 2000;

            rdt = (tidYear << (LEN_DATE_MM + LEN_DATE_DD));

            rdt |= (month << LEN_DATE_DD);

            rdt |= date;

            rdt <<= LEN_TIME;

            long hms = hour << LEN_TIME_MM + LEN_TIME_SS;

            hms |= minute << LEN_TIME_SS;

            hms |= second;

            rdt |= hms;

            rdt <<= LEN_TSIT;
            timestamp = now;
            tps = new AtomicInteger();
        }

        return rdt;
    }


    public static String toLogString(final long id) {
        final int type = (short) ((id >> (LEN_TSIT - LEN_TYPE)) & (0x7));

        final int l_t = LEN_TYPE_TPS[type];
        final int l_it = LEN_TYPE_INS[type] + l_t;

        final long MASK_TPS = (1 << LEN_TYPE_TPS[type]) - 1;
        final long MASK_INSTANCE = (1 << LEN_TYPE_INS[type]) - 1;
        final long MASK_SYSTEM = (1 << LEN_TYPE_SYS[type]) - 1;
        final long MASK_SECOND = (1 << LEN_TIME_SS) - 1;
        final long MASK_MINUTE = (1 << LEN_TIME_MM) - 1;
        final long MASK_HOUR = (1 << LEN_TIME_HH) - 1;
        final long MASK_DATE = (1 << LEN_DATE_DD) - 1;
        final long MASK_MONTH = (1 << LEN_DATE_MM) - 1;
        final long MASK_YEAR = (1 << LEN_DATE_YY) - 1;

        final short tps = (short) (id & MASK_TPS);
        final short instance = (short) ((id >> l_t) & MASK_INSTANCE);
        final short system = (short) ((id >> l_it) & MASK_SYSTEM);

        final short second = (short) ((id >> LEN_TSIT) & MASK_SECOND);
        final short minute = (short) ((id >> (LEN_TSIT + LEN_TIME_SS)) & MASK_MINUTE);
        final short hour = (short) ((id >> (LEN_TSIT + LEN_TIME_MM + LEN_TIME_SS)) & MASK_HOUR);
        final short date = (short) ((id >> (LEN_TSIT + LEN_TIME)) & MASK_DATE);
        final short month = (short) ((id >> (LEN_TSIT + LEN_TIME + LEN_DATE_DD)) & MASK_MONTH);
        final short tidYear = (short) ((id >> (LEN_TSIT + LEN_TIME + LEN_DATE_DD + LEN_DATE_MM)) & MASK_YEAR);

        if ((id >> (LEN_TOTAL - 1)) == 0) {
            return "T." + tidYear + padding(month) + padding(date) + padding(hour) + padding(minute) + padding(second) + "." + type + "." + system + "." + instance + "." + tps;
        } else {
            new IllegalArgumentException("Invalid TID start with 0x1, {" + Long.toHexString(id) + "}");
        }
        return "";
    }

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

    private static String padding(short number) {
        return PADS[number];
    }

    public static String parseToHexString(String id) {
        // TODO
        return null;
    }
}
