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
package wang.yanjiong.toid64;

import wang.yanjiong.toid64.Tid64.Tid64Type;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by WangYanJiong on 7/22/16.
 */


public class Tid64Generator extends Abstract64Generator {

    private static final int LEN_SIS = LEN_TOTAL - LEN_DATE - LEN_TIME - LEN_R - LEN_TID_TYPE;

//    private static final int[] LEN_TYPE_SYS = {5, 6, 7, 8, 9, 10, 11, 12};

    private static final int[] LEN_TYPE_INS = {12, 11, 10, 9, 9, 8, 7, 7};

    private static final int[] LEN_TYPE_SER = {11, 11, 11, 11, 10, 10, 10, 9};


    private long type;

    private long si;

    private long ttdt;

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
        long seq = serial.incrementAndGet();
        if (seq >= (2 << LEN_TYPE_SER[(int) type])) {
            waiting();
            refresh(System.currentTimeMillis());
        }
        long id = ttdt | si | seq;
        return new Tid64(id);
    }

    private synchronized void refresh(long now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(now));
        long year = calendar.get(Calendar.YEAR) - BASE_YEAR;
        long month = calendar.get(Calendar.MONTH) + 1; // base 0
        long date = calendar.get(Calendar.DATE); // base 1
        long hour = calendar.get(Calendar.HOUR_OF_DAY);
        long minute = calendar.get(Calendar.MINUTE);
        long second = calendar.get(Calendar.SECOND);

        timestamp = now;
        serial = new AtomicInteger();

        ttdt <<= LEN_TID_TYPE;
        ttdt |= type;

        ttdt <<= LEN_DATE;
        ttdt |= (year << (LEN_DATE_MM + LEN_DATE_DD));
        ttdt |= (month << LEN_DATE_DD);
        ttdt |= date;

        ttdt <<= LEN_TIME;
        long hms = hour << LEN_TIME_MM + LEN_TIME_SS;
        hms |= minute << LEN_TIME_SS;
        hms |= second;
        ttdt |= hms;

        ttdt <<= LEN_SIS;

    }


}
