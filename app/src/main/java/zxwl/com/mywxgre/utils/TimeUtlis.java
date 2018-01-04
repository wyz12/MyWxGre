package zxwl.com.mywxgre.utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/1/3.
 */

public class TimeUtlis {

    private   static boolean pd;

    public static boolean isPd() {
        return pd;
    }

    public static void setPd(boolean pd) {
        TimeUtlis.pd = pd;
    }

    private static DateFormat datetime;

    public static String formatTime(Long ms) {
        Integer mi = 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = ms - day * dd - hour * hh - minute * mi ;


        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"天");
        }
        if(hour > 0) {
            sb.append(hour+"小时");
        }
        if(minute > 0) {
            sb.append(minute+"分");
        }
        if(second > 0) {
            sb.append(second+"秒");
        }

        return sb.toString();
    }


    public static long getDateFromStr(String dateStr) {
        long temp = 0L;
        Date date = null;
        try {
            date = datetime.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return temp;
        }
        temp = date.getTime();
        return temp;
    }


    public static String parseTime(String s){

        //将字符串转化为Long类型
        Long timeLong = Long.parseLong(s);
        //将Long类型转化为Date
        Date date = new Date(timeLong);

        //将Date类型格式化
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = sdf.format(date);
        return dateString;

    }
    public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[0-9\\D])|(18[0-9]))\\d{8}$";

    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$";
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }
}
