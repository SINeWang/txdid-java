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

    private static final int LEN_TO = 1;

    private static final int LEN_TYPE = 3;

    private static final int LEN_DATE_YY = 6;

    private static final int LEN_DATE_MM = 4;

    private static final int LEN_DATE_DD = 5;

    private static final int LEN_TIME_HH = 5;

    private static final int LEN_TIME_MM = 6;

    private static final int LEN_TIME_SS = 6;

    private static final int LEN_DATE = LEN_DATE_YY + LEN_DATE_MM + LEN_DATE_DD;

    private static final int LEN_TIME = LEN_TIME_HH + LEN_TIME_MM + LEN_TIME_SS;

    private static final int LEN_SIS = LEN_TOTAL - LEN_DATE - LEN_TIME - LEN_TO - LEN_TYPE;

    private static final int[] LEN_TYPE_SYS = {5, 6, 7, 8, 9, 10, 11, 12};

    private static final int[] LEN_TYPE_INS = {12, 11, 10, 9, 9, 8, 7, 7};

    private static final int[] LEN_TYPE_SER = {11, 11, 11, 11, 10, 10, 10, 9};

    private static final int LEN_TTSIS = LEN_TO + LEN_TYPE + LEN_SIS;

    private static final long MASK_SECOND = (1 << LEN_TIME_SS) - 1;
    private static final long MASK_MINUTE = (1 << LEN_TIME_MM) - 1;
    private static final long MASK_HOUR = (1 << LEN_TIME_HH) - 1;
    private static final long MASK_DATE = (1 << LEN_DATE_DD) - 1;
    private static final long MASK_MONTH = (1 << LEN_DATE_MM) - 1;
    private static final long MASK_YEAR = (1 << LEN_DATE_YY) - 1;


    private long type;

    private long si;

    private long timestamp;

    private long dttt;

    private AtomicInteger serial;


    public Tid64Generator(Tid64Type type, int system, int instance) {
        this.timestamp = 0;
        this.type = type.ordinal();
        long lengthOfT = LEN_TYPE_SER[type.ordinal()];
        long lengthOfIT = LEN_TYPE_INS[type.ordinal()] + lengthOfT;
        this.si |= system << lengthOfIT;
        this.si |= instance << lengthOfT;

    }

    public Tid64 next() {
        long now = System.currentTimeMillis();
        if (now - timestamp > 1000) {
            refresh(now);
        }
        long id = dttt | si | serial.incrementAndGet();
        return new Tid64(id);
    }

    private synchronized void refresh(long now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(now));
        long year = calendar.get(Calendar.YEAR);
        long month = calendar.get(Calendar.MONTH) + 1; // base 0
        long date = calendar.get(Calendar.DATE); // base 1
        long hour = calendar.get(Calendar.HOUR_OF_DAY);
        long minute = calendar.get(Calendar.MINUTE);
        long second = calendar.get(Calendar.SECOND);

        long tidYear = year - 2000;


        dttt |= (tidYear << (LEN_DATE_MM + LEN_DATE_DD));

        dttt |= (month << LEN_DATE_DD);

        dttt |= date;

        dttt <<= LEN_TIME;

        long hms = hour << LEN_TIME_MM + LEN_TIME_SS;

        hms |= minute << LEN_TIME_SS;

        hms |= second;

        dttt |= hms;

        dttt <<= LEN_TO;

        dttt <<= LEN_TYPE;

        dttt |= type;

        dttt <<= LEN_SIS;
        timestamp = now;
        serial = new AtomicInteger();
    }


    static short[] id2Array(final long id) {
        final short type = (short) ((0x7) & (id >> LEN_SIS));

        final int l_ser = LEN_TYPE_SER[type];
        final int l_sisser = LEN_TYPE_INS[type] + l_ser;

        final long MASK_SYSTEM = (1 << LEN_TYPE_SYS[type]) - 1;
        final long MASK_INSTANCE = (1 << LEN_TYPE_INS[type]) - 1;
        final long MASK_SER = (1 << LEN_TYPE_SER[type]) - 1;


        final short serial = (short) (id & MASK_SER);
        final short instance = (short) ((id >> l_ser) & MASK_INSTANCE);
        final short system = (short) ((id >> l_sisser) & MASK_SYSTEM);

        final short second = (short) ((id >> LEN_TTSIS) & MASK_SECOND);
        final short minute = (short) ((id >> (LEN_TIME_SS + LEN_TTSIS)) & MASK_MINUTE);
        final short hour = (short) ((id >> (LEN_TIME_MM + LEN_TIME_SS + LEN_TTSIS)) & MASK_HOUR);
        final short date = (short) ((id >> (LEN_TIME + LEN_TTSIS)) & MASK_DATE);
        final short month = (short) ((id >> (LEN_DATE_DD + LEN_TIME + LEN_TTSIS)) & MASK_MONTH);
        final short year = (short) ((id >> (LEN_DATE_MM + LEN_DATE_DD + LEN_TIME + LEN_TTSIS)) & MASK_YEAR);
        final short to = (short) ((0x1) & id >> (LEN_SIS + LEN_TYPE));

        return new short[]{to, type, year, month, date, hour, minute, second, system, instance, serial};

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

    static String padding(short number) {
        return PADS[number];
    }

    public static long fromString(String id) {
        return Long.valueOf(id, 16);
    }
}
