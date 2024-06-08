package hexlet.code.utils;

import java.sql.Timestamp;

public class BeautyTime {
    public static String getBeautyTime(Timestamp time) {
        var stringTime = String.valueOf(time);
        var arrTime = stringTime.split(":");
        var beautyTime = arrTime[0] + ":";
        beautyTime = beautyTime + time.toLocalDateTime().getMinute();
        beautyTime = beautyTime.replace('-', '/');
        return beautyTime;
    }
}
