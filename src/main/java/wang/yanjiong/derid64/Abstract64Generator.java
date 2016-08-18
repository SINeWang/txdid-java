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

/**
 * Created by WangYanJiong on 8/2/16.
 */
public abstract class Abstract64Generator {

    static final int LEN_TOTAL = 64;

    static final int LEN_R = 1;

    static final int LEN_TID_TYPE = 3;

    static final int LEN_DATE_YY = 6;

    static final int LEN_DATE_MM = 4;

    static final int LEN_DATE_DD = 5;

    static final int LEN_TIME_HH = 5;

    static final int LEN_TIME_MM = 6;

    static final int LEN_TIME_SS = 6;

    static final int LEN_DATE = LEN_DATE_YY + LEN_DATE_MM + LEN_DATE_DD;

    static final int LEN_TIME = LEN_TIME_HH + LEN_TIME_MM + LEN_TIME_SS;

    static final short BASE_YEAR = 2000;

    static final long MASK_SECOND = (1 << LEN_TIME_SS) - 1;

    static final long MASK_MINUTE = (1 << LEN_TIME_MM) - 1;

    static final long MASK_HOUR = (1 << LEN_TIME_HH) - 1;

    static final long MASK_DATE = (1 << LEN_DATE_DD) - 1;

    static final long MASK_MONTH = (1 << LEN_DATE_MM) - 1;

    static final long MASK_YEAR = (1 << LEN_DATE_YY) - 1;


    long timestamp;

    synchronized void waiting() {
        while (System.currentTimeMillis() - timestamp < 1000) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }
    }

}
