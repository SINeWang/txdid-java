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

/**
 * Created by WangYanJiong on 8/2/16.
 */
public abstract class Abstract64Generator {

    static final int LEN_TOTAL = 64;

    static final int LEN_TO = 1;

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
