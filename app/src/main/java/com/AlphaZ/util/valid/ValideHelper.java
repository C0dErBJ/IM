package com.AlphaZ.util.valid;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * ProjectName: XSY
 * PackageName: com.AlphaZ.utils
 * User: C0dEr
 * Date: 2016-10-11
 * Time: 11:33
 * Description:
 */
public class ValideHelper {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNullOrEmpty(Object str) {
        return str == null;
    }

    public static boolean isNullOrEmpty(Map map) {
        return map == null || map.size() == 0;
    }

    public static boolean isNullOrEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean isNullOrEmpty(Float flt) {
        return flt == null || flt == 0.0;
    }

    public static boolean isNullOrEmpty(Integer itg) {
        return itg == null || itg == 0;
    }

    public static boolean isNullOrEmpty(Double itg) {
        return itg == null || itg == 0.0;
    }

    public static boolean isNumericSpace(String str) {
        if (str == null) {
            return false;
        } else {
            int sz = str.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != 32) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isIP(String ip) {
        String reg = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    public static boolean isPhone(String phone) {
        if (isNullOrEmpty(phone)) {
            return false;
        }
        String reg = "^1[34578]\\d{9}$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static boolean isAGoodPassowrd(String pwd) {
        if (isNullOrEmpty(pwd)) {
            return false;
        }
        String reg = "((?=.*\\d)(?=.*\\D)|(?=.*[a-zA-Z])(?=.*[^a-zA-Z]))^.{8,16}$";//长度8~16位,数字、字母、字符至少包含两种
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(pwd);
        return matcher.matches();
    }

    public static boolean isAGoodPayPassowrd(String pwd) {
        if (isNullOrEmpty(pwd)) {
            return false;
        }
        String reg = "\\d{6}";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(pwd);
        return matcher.matches();
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null
                && request.getHeader("X-Requested-With").toLowerCase().equals("xmlhttprequest"));
    }

    public static boolean isAppRequest(HttpServletRequest request) {
        return (request.getHeader("user-agent") != null
                && (request.getHeader("user-agent").toLowerCase().contains("android")
                || request.getHeader("user-agent").toLowerCase().contains("iphone")
                || request.getHeader("user-agent").toLowerCase().contains("ipad")));
    }

    @SuppressWarnings("unchecked")
    public static boolean IDCardValidate(String IDStr) {
        String[] ValCodeArr = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        // ================ 号码的长度18位 ================
        if (IDStr.length() != 18) {
            return false;
        }
        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        }
        if (isNumeric(Ai) == false) {
            //errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return false;
        }
        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 日
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
//          errorInfo = "身份证生日无效。";
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                //errorInfo = "身份证生日不在有效范围。";
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            //errorInfo = "身份证月份无效";
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            //errorInfo = "身份证日期无效";
            return false;
        }
        // ================ 地区码时候有效 ================
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
//      hashtable.put("71", "台湾");
//      hashtable.put("81", "香港");
//      hashtable.put("82", "澳门");
//      hashtable.put("91", "国外");
        if (hashtable.get(Ai.substring(0, 2)) == null) {
            //errorInfo = "身份证地区编码错误。";
            return false;
        }
        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                //errorInfo = "身份证无效，不是合法的身份证号码";
                return false;
            }
        } else {
            return true;
        }
        return true;
    }

    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAlphabet(String alphabet) {
        if (isNullOrEmpty(alphabet)) {
            return false;
        }
        String reg = "^[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(alphabet);
        return matcher.matches();
    }

    public static boolean isAGoodPetName(String name) {
        if (isNullOrEmpty(name)) {
            return false;
        }
        String reg = "^[A-Za-z][A-Za-z0-9]{5,11}+$";
        //String reg = "^[A-Za-z](?=.*[a-zA-Z])[a-zA-Z\\d]{5,11}$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static boolean isAGoodGuest(String guest) {
        if (isNullOrEmpty(guest)) {
            return false;
        }
        String reg = "\\d{3,9}";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(guest);
        return matcher.matches();
    }


}
