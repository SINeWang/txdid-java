package wang.yanjiong.toid.tid64;

/**
 * Created by WangYanJiong on 7/29/16.
 */
public class Tid64 {

    private String stringValue;

    public static final int FIELD_TO = 0;

    public static final int FIELD_T = 1;

    public static final int FIELD_YY = 2;

    public static final int FIELD_MM = 3;

    public static final int FIELD_DD = 4;

    public static final int FIELD_HH = 5;

    public static final int FIELD_mm = 6;

    public static final int FIELD_SS = 7;

    public static final int FIELD_SYS = 8;

    public static final int FIELD_INS = 9;

    public static final int FIELD_SER = 10;

    public static final String DELIMITER = "-";

    private short[] array;

    private long id;

    public long value() {
        return id;
    }

    Tid64(long id) {
        this.id = id;
    }

    public String toString() {
        return Long.toHexString(id);
    }

    public String decoded() {
        if (id != 0) {
            if (array == null) {
                parse();
            }
            if (array[FIELD_TO] != 0) {
                throw new IllegalArgumentException("Invalid TID start with 0x1, {" + Long.toHexString(id) + "}");
            }
        }
        return stringValue;
    }

    private synchronized void parse() {
        if (array != null) {
            return;
        }
        array = Tid64Generator.id2Array(id);
        stringValue = "T" + getType() + DELIMITER + getYear() + Tid64Generator.padding(getMonth()) + Tid64Generator.padding(getDate())
                + "." + Tid64Generator.padding(getHour()) + Tid64Generator.padding(getMinute()) + Tid64Generator.padding(getSecond()) +
                DELIMITER + getSystem() + "." + getInstance() + "." + getSerial();
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

    public short getType() {
        if (array == null) {
            parse();
        }
        return array[FIELD_T];
    }

    public short getSystem() {
        if (array == null) {
            parse();
        }
        return array[FIELD_SYS];
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
