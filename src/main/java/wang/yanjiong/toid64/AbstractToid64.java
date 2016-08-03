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
 * Created by WangYanJiong on 8/3/16.
 */
public abstract class AbstractToid64 {

    static final String DELIMITER = "-";

    private static final int FIELD_R = 0;

    String stringValue;

    long id;

    short[] array;

    abstract void parse();

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
}
