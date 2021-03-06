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
package one.kii.txdid.txd64;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by WangYanJiong on 8/2/16.
 */
public class T1Did64Generator extends Abstract64Generator {

    static final int LEN_INS = 12;

    static final int LEN_SEQ = 19;

    private long tdt;

    private AtomicInteger serial;

    private long instance;

    @Deprecated
    public T1Did64Generator() {
        this.timestamp = 0;
        this.instance = 0;
    }


    public T1Did64Generator(int instance) {
        if ((instance >> 12) > 0) {
            throw new IllegalArgumentException("illegal instance {" + instance + "}");
        }
        this.timestamp = 0;
        this.instance = instance;
        this.instance <<= LEN_SEQ;
    }

    public long born() {
        long now = System.currentTimeMillis();
        if (now - timestamp > 1000) {
            refresh(now);
        }
        long seq = serial.incrementAndGet();
        if (seq > 0x7FFFF) {
            waiting();
            refresh(System.currentTimeMillis());
        }
        return tdt | instance | seq;
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

        calendar.set(Calendar.MILLISECOND, 0);

        timestamp = calendar.getTimeInMillis();
        serial = new AtomicInteger();

        tdt |= (year << (LEN_DATE_MM + LEN_DATE_DD));
        tdt |= (month << LEN_DATE_DD);
        tdt |= date;

        tdt <<= LEN_TIME;
        long hms = hour << LEN_TIME_MM + LEN_TIME_SS;
        hms |= minute << LEN_TIME_SS;
        hms |= second;
        tdt |= hms;

        tdt <<= (LEN_INS + LEN_SEQ);
    }

}
