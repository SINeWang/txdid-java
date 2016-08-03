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

import static wang.yanjiong.toid64.Tid64Parser.oid2Array;
import static wang.yanjiong.toid64.Toid64Parser.padding;

/**
 * Created by WangYanJiong on 8/1/16.
 */
public class Oid64 extends AbstractToid64 {

    private static final int FIELD_R = 0;

    private static final int FIELD_YY = 1;

    private static final int FIELD_MM = 2;

    private static final int FIELD_DD = 3;

    private static final int FIELD_HH = 4;

    private static final int FIELD_mm = 5;

    private static final int FIELD_SS = 6;

    private static final int FIELD_INS = 7;

    private static final int FIELD_SER = 8;

    public Oid64(long id) {
        this.id = id;
    }

    public long value() {
        return id;
    }

    public String toString() {
        return Long.toHexString(id);
    }

    public String decoded() {
        if (id != 0) {
            if (array == null) {
                parse();
            }
            if (array[FIELD_R] != 0) {
                throw new IllegalArgumentException("Invalid TOID start with 0x1, {" + Long.toHexString(id) + "}");
            }
        }
        return stringValue;
    }

    private synchronized void parse() {
        if (array != null) {
            return;
        }
        array = oid2Array(id);
        stringValue = "O" + DELIMITER + getYear() + padding(getMonth()) + padding(getDate())
                + "." + padding(getHour()) + padding(getMinute()) + padding(getSecond()) +
                DELIMITER + getInstance() + "." + getSerial();
    }


    public short getYear() {
        if (array == null) {
            parse();
        }
        return array[FIELD_YY];
    }

    public short getMonth() {
        if (array == null) {
            parse();
        }
        return array[FIELD_MM];
    }

    public short getDate() {
        if (array == null) {
            parse();
        }
        return array[FIELD_DD];
    }

    public short getHour() {
        if (array == null) {
            parse();
        }
        return array[FIELD_HH];
    }

    public short getMinute() {
        if (array == null) {
            parse();
        }
        return array[FIELD_mm];
    }

    public short getSecond() {
        if (array == null) {
            parse();
        }
        return array[FIELD_SS];
    }

    public short getInstance() {
        if (array == null) {
            parse();
        }
        return array[FIELD_INS];
    }

    public short getSerial() {
        if (array == null) {
            parse();
        }
        return array[FIELD_SER];
    }


}
