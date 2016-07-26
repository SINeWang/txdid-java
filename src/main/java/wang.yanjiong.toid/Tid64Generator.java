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

    public static final int[] LEN_TYPE_SYS = {5, 6, 7, 7, 10, 11, 12, 13};

    public static final int[] LEN_TYPE_INS = {11, 10, 9, 10, 7, 6, 6, 5};

    public static final int[] LEN_TYPE_TPS = {11, 11, 11, 10, 10, 10, 9, 9};

    private Integer type;

    private Integer system;

    private Integer instance;

    private Integer tps;

    private long time;

    private Tid64Generator(int type, int system, int instance) {
        this.type = type;
        this.system = system;
        this.instance = instance;
    }

    public static Tid64Generator newInstance(int type, int system, int instance) {
        Tid64Generator tid64 = new Tid64Generator(type, system, instance);
        return tid64;
    }

    public long nextId() {
        return time = System.currentTimeMillis();
    }
}
