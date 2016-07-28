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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by WangYanJiong on 7/22/16.
 */


public class Tid64Generator {

    public static final int LEN_R = 1;

    public static final int LEN_DATE_YY = 7;

    public static final int LEN_DATE_MM = 4;

    public static final int LEN_DATE_DD = 5;

    public static final int LEN_TIME_HH = 5;

    public static final int LEN_TIME_MM = 6;

    public static final int LEN_TIME_SS = 6;

    private static final int LEN_DATE = LEN_DATE_YY + LEN_DATE_MM + LEN_DATE_DD;

    private static final int LEN_TIME = LEN_TIME_HH + LEN_TIME_MM + LEN_TIME_SS;

    private static final int LEN_TSIT = 64 - LEN_DATE - LEN_TIME - LEN_R;

    public static final int[] LEN_TYPE_SYS = {5, 6, 7, 7, 10, 11, 12, 13};

    public static final int[] LEN_TYPE_INS = {11, 10, 9, 10, 7, 6, 6, 5};

    public static final int[] LEN_TYPE_TPS = {11, 11, 11, 10, 10, 10, 9, 9};

    private Integer type;

    private Integer system;

    private Integer instance;

    private Integer lengthOfT;

    private Integer lengthOfIT;

    private Integer lengthOfSIT;

    private Long rdt;

    private Long hms;

    private long timestamp;

    private AtomicInteger tps;

    private Tid64Generator(int type, int system, int instance) {
        this.type = type;
        this.system = system;
        this.instance = instance;
        this.timestamp = 0;
        this.lengthOfT = LEN_TYPE_TPS[type];
        this.lengthOfIT = LEN_TYPE_INS[type] + lengthOfT;
        this.lengthOfSIT = LEN_TYPE_SYS[type] + lengthOfIT;

    }

    public static Tid64Generator newInstance(int type, int system, int instance) {
        Tid64Generator tid64 = new Tid64Generator(type, system, instance);
        return tid64;
    }

    public long nextId() {
        long now = System.currentTimeMillis();
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

            rdt = rdt << LEN_TSIT;

            hms = hour << LEN_TIME_MM + LEN_TIME_SS;

            hms |= minute << LEN_TIME_SS;

            hms |= second;

            timestamp = now;
            tps = new AtomicInteger();
        }

        long id = rdt | (type << lengthOfSIT);
        id |= (system << lengthOfIT);

        id |= (instance << lengthOfT);

        id |= hms << LEN_TSIT;

        long mask = -1 << lengthOfT;

        id = (id & mask) | tps.incrementAndGet();

        id = id | rdt;

        return id;
    }


    public String toLogString(long id) {
        return null;
    }
}
