package hexlet.code.utils;

import java.sql.Timestamp;

public class BeautyTime {
    public static String getBeautyTime(Timestamp time) {
        var stringTime = String.valueOf(time);
        var arrTime = stringTime.split(":");
        var beautyTime = arrTime[0] + ":";
        var minutes = time.toLocalDateTime().getMinute() > 9 ? String.valueOf(time.toLocalDateTime().getMinute())
                : "0" + time.toLocalDateTime().getMinute();
        beautyTime = beautyTime + minutes;
        arrTime = beautyTime.split(" ");
        var hoursAndMinutes = arrTime[1];
        arrTime = arrTime[0].split("-");
        beautyTime = arrTime[2] + "/" + arrTime[1] + "/" + arrTime[0] + " " + hoursAndMinutes;
        return beautyTime;
    }
}
