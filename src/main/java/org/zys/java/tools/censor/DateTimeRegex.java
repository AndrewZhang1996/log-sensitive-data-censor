package org.zys.java.tools.censor;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zys
 * @version 1.0
 * @date 2022/06/13 15:57:25
 */
public class DateTimeRegex {
    public static void main(String[] args) throws ParseException {
        //近45分钟的时间
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-05-26 00:00:00");
//        Date start = DateUtils.addDays(date, -30);
        Date start = date;
        System.out.println("start:" + DateFormatUtils.format(start, "yyyy-MM-dd HH:mm:ss"));
        System.out.println("end:" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        List<String> list = DateTimeRegex.dateUntilNowFormat(start);
        list.forEach(System.out::println);
    }

    /**
     * 大于start的时间正则
     *
     * @param start
     * @return
     */
    public static List<String> dateUntilNow(Date start) {
        String s = DateFormatUtils.format(start, "yyyyMMddHHmmss");
        String e = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
        List<String> regexList = new ArrayList<>();
        Map<String, String> map = bigThanMin(s, e);
        regexList.add(map.get("regex"));
        int index = Integer.parseInt(map.get("index"));
        String end = map.get("end");
        for (int i = index + 1; i < s.length(); i++) {
            if (end.equals(s)) {
                regexList.add(end);
                break;
            }
            map = bigThanMin(s, end);
            regexList.add(map.get("regex"));
            end = map.get("end");
        }
        return regexList;
    }

    /**
     * 大于start的时间正则
     *
     * @param start
     * @return
     */
    public static List<String> dateUntilNowFormat(Date start) {
        List<String> regexList = dateUntilNow(start);
        return regexList.stream().map(DateTimeRegex::formatDateRegex).collect(Collectors.toList());
    }

    /**
     * 格式化生成正则
     * @param ss
     * @return
     */
    private static String formatDateRegex(String ss) {
        String[] d = new String[14];
        int idx = 0;
        for (int i = 0; i < ss.length(); i++) {
            char c = ss.charAt(i);
            if (c != '[') {
                d[idx++] = String.valueOf(c);
            } else {
                d[idx++] = new String(Arrays.copyOfRange(ss.toCharArray(), i, i + 5));
                i = i + 4;
            }
        }
        StringBuilder ds = new StringBuilder();
        for (int i = 0; i < d.length; i++) {
            String dd = d[i];
            if (i == 4 || i == 6) {
                ds.append("-");
            }
            if (i == 8) {
                ds.append(" ");
            }
            if (i == 10 || i == 12) {
                ds.append(":");
            }
            ds.append(dd);
        }
        return ds.toString();
    }

    private static Map<String, String> bigThanMin(String min, String max) {
        Map<String, String> rst = new HashMap<>(8);
        StringBuilder regex = new StringBuilder();
        char[] sCharArray = min.toCharArray();
        char[] eCharArray = max.toCharArray();
        //第一个不同数字的坐标
        int index = 0;
        for (int i = 0, charArrayLength = sCharArray.length; i < charArrayLength; i++) {
            char sc = sCharArray[i];
            char ec = eCharArray[i];
            if (sc == ec) {
                continue;
            }
            index = i;
            break;
        }
        //第一个不同数字
        int start = Integer.parseInt(String.valueOf(sCharArray[index]));
        if (start == 9) {
            start = Integer.parseInt(String.valueOf(sCharArray[index - 1]));
            index = index - 1;
        }
        rst.put("index", String.valueOf(index));
        char[] prefix = Arrays.copyOfRange(sCharArray, 0, index);
        regex.append(prefix);
        regex.append("[").append(start + 1).append("-9]");
        StringBuilder end = new StringBuilder(new String(prefix));
        end.append(sCharArray[index]);
        for (int i = index + 1; i < sCharArray.length; i++) {
            regex.append("[0-9]");
            end.append("9");
        }
        rst.put("regex", regex.toString());
        rst.put("end", end.toString());
        return rst;
    }
}
