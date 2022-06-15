package org.zys.java.tools.censor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 类<code>util.TestUtil</code>用于在测试中的控制台上打印标准化的信息。
 *
 * @author zys
 * @version 1.0
 * @date 2021-07-23 10:59:12
 */
public class ConsolePrinter {

    /**
     * 方法<code>println</code>用于打印换行符
     *
     * @title 打印换行符
     * @description 打印换行符
     * @author zys
     * @date 2021-07-23 10:59:25
     */
    public static void println() {
        System.out.println();
    }

    /**
     * 方法<code>println</code>用于打印字符串+换行符
     *
     * @param s 字符串
     * @title 打印字符串+换行符
     * @description 打印字符串+换行符
     * @author zys
     * @date 2021-07-23 11:00:26
     */
    public static void println(String s) {
        System.out.println(s);
    }

    /**
     * 方法<code>printlnMainTitle</code>用于打印标题
     *
     * @param s 字符串
     * @title 打印标题
     * @description 可以更改底色和字色
     * @author zys
     * @date 2021-07-23 11:00:26
     */
    public static void printlnMainTitle(String s) {
        System.out.println(ConsoleColors.YELLOW_BACKGROUND_RED_BOLD + s + ConsoleColors.RESET);
    }

    /**
     * 方法<code>printOnlyShortLines</code>用于打印100个'-'短直线
     *
     * @title 打印100个'-'短直线
     * @description 只打印100个'-'短直线+换行
     * @author zys
     * @date 2021-07-23 11:00:52
     */
    public static void printOnlyShortLines() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("-");
        }
        println(sb.toString());
        sb = null;
    }

    /**
     * 方法<code>stringOnlyShortLines</code>用于返回100个'-'短直线字符串
     *
     * @return java.lang.String 100个'-'短直线的字符串
     * @title 返回100个'-'短直线字符串
     * @description 返回100个'-'短直线字符串
     * @author zys
     * @date 2021-07-23 11:01:54
     */
    public static String stringOnlyShortLines() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("-");
        }
        return sb.toString();
    }

    /**
     * 方法<code>printWordsBetweenShortLines</code>用于打印'-'短直线+信息，信息在短直线中间
     *
     * @param words 信息
     * @title 打印'-'短直线+信息
     * @description 信息在短直线中间
     * @author zys
     * @date 2021-07-23 11:02:47
     */
    public static void printWordsBetweenShortLines(String words) {
        int length = words.length();
        int mid = (100 - length) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mid + 1; i++) {
            sb.append("-");
        }
        sb.append(words);
        for (int i = 0; i < mid + 1; i++) {
            sb.append("-");
        }
        println(sb.toString());
        sb = null;
    }

    /**
     * 方法<code>stringWordsBetweenShortLines</code>用于返回'-'短直线+信息的字符串，信息在短直线中间
     *
     * @param words 信息
     * @return java.lang.String 信息在短直线中间的字符串
     * @title 返回'-'短直线+信息的字符串
     * @description 信息在短直线中间
     * @author zys
     * @date 2021-07-23 11:03:40
     */
    public static String stringWordsBetweenShortLines(String words) {
        int length = words.length();
        int mid = (100 - length) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mid + 1; i++) {
            sb.append("-");
        }
        sb.append(words);
        for (int i = 0; i < mid + 1; i++) {
            sb.append("-");
        }
        return sb.toString();
    }

    /**
     * 方法<code>printListMapStringObject</code>用于打印<code></>List<Map<String, Object>></code>类型值在控制台上
     *
     * @param list 列表
     * @param num  缩进格数
     * @param name 列表名称
     * @title 打印<code></>List<Map<String, Object>></code>类型值
     * @description 打印<code></>List<Map<String, Object>></code>类型值在控制台上
     * @author zys
     * @date 2021-07-23 11:04:41
     */
    public static void printListMapStringObject(List list, int num, String name) {
        StringBuilder sb = new StringBuilder();
        if (num != 0) {
            sb.append("|");
        }
        for (int i = 0; i < num; i++) {
            sb.append("\t\t");
            sb.append("|");
        }
        sb.append("---");
        sb.append(stringWordsBetweenShortLines("列表" + name));
        println(sb.toString());
        sb.setLength(0);
        for (int m = 0; m < list.size(); m++) {
            if (num != 0) {
                sb.append("|");
            }
            for (int i = 0; i < num; i++) {
                sb.append("\t\t");
                sb.append("|");
            }
            sb.append("---");
            sb.append(stringWordsBetweenShortLines("列表" + name + "的第" + m + "个元素"));
            println(sb.toString());
            sb.setLength(0);
            Map<String, Object> tempMap = new HashMap<>();
            if (!(list.get(m) instanceof Map)) {
                tempMap.put("元素", list.get(m));
            } else {
                tempMap = (Map<String, Object>) list.get(m);
            }
            printMapStringObject(tempMap, num);
        }
        if (num != 0) {
            sb.append("|");
        }
        for (int i = 0; i < num; i++) {
            sb.append("\t\t");
            sb.append("|");
        }
        sb.append("---");
        sb.append(stringWordsBetweenShortLines("列表" + name + "结束"));
        println(sb.toString());
    }

    /**
     * 方法<code>printMapStringObject</code>用于打印<code>Map<String,Object></code>类型值在控制台上
     *
     * @param map map值
     * @param num 缩进格数
     * @return void
     * @title 打印<code>Map<String,Object></code>类型值
     * @description 打印<code>Map<String,Object></code>类型值在控制台上
     * @author zys
     * @date 2021-07-23 11:06:47
     */
    public static void printMapStringObject(Map<String, Object> map, int num) {
        AtomicInteger n = new AtomicInteger();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append("|");
            for (int i = 0; i < num; i++) {
                sb.append("\t\t");
                sb.append("|");
            }
            sb.append("---");
            sb.append(ConsoleColors.GREEN_BRIGHT).append(entry.getKey()).append(ConsoleColors.RESET).append(":");

            if (entry.getValue() instanceof Map) {
                println(sb.toString());
                n.getAndIncrement();
                printMapStringObject((Map<String, Object>) entry.getValue(), n.get() + num);
                n.getAndDecrement();
                continue;
            }
            if (entry.getValue() instanceof List) {
                println(sb.toString());
                n.getAndIncrement();
                printListMapStringObject((List) entry.getValue(), n.get() + num, (String) entry.getKey());
                n.getAndDecrement();
                continue;
            }
            sb.append(" { 值：").append(ConsoleColors.CYAN_BRIGHT).append(entry.getValue()).append(ConsoleColors.RESET);
            if (entry.getValue() != null) {
                sb.append("\t类型：[").append(ConsoleColors.YELLOW_BRIGHT).append(entry.getValue().getClass()).append(ConsoleColors.RESET).append("]");
            }
            sb.append(" }");
            println(sb.toString());
        }
    }

    /**
     * 方法<code>printMapStringObject</code>用于打印结果集
     *
     * @param name 定义输出名称
     * @param map  结果集
     * @return void
     * @title
     * @description
     * @flow
     * @author zys
     * @date 2021-08-11 17:02:56
     */
    public static void printMapStringObject(String name, Map<String, Object> map) {
        printWordsBetweenShortLines("输出" + name + "开始");
        printMapStringObject(map, 0);
        printWordsBetweenShortLines("输出" + name + "结束");
    }

    /**
     * 方法<code>getAllParamCombinations</code>获取所需要测试参数的组合，预留所有情况的接口，
     * 但是问题在于所有组合的情况数量太过庞大，所以无法测试所有情况，
     * 而且对于现有接口来说，不需要进行业务类型的测试，
     * 所以目前仅测试 1.空 2.全参数 这两种情况。
     *
     * @param input 参数集合
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>> 所有参数组合结果
     * @title
     * @description
     * @flow
     * @author zys
     * @date 2021-08-11 17:04:07
     */
    public static List<Map<String, Object>> getAllParamCombinations(List<Map.Entry<String, Object>> input) {
        List<Map<String, Object>> result = new ArrayList<>();
        result.add(new HashMap<>());
        if (input.size() == 0) {
            return result;
        }
        int count = input.size();
        Map<Integer, Object> indexes = new HashMap<>();
        indexes.put(input.size(), new Object());
        for (Integer k : indexes.keySet()) {
            result.addAll(getCombinations(input, k));
        }
        return result;
    }

    /**
     * 方法<code>getCombinations</code>获取所有参数的有k个参数的所有组合情况
     *
     * @param input 输入的参数集
     * @param k     需要计算的k个参数的组合情况
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>> 参数组合结果
     * @title
     * @description
     * @flow
     * @author zys
     * @date 2021-08-11 17:12:23
     */
    public static List<Map<String, Object>> getCombinations(List<Map.Entry<String, Object>> input, int k) {
        List<Map<String, Object>> subsets = new ArrayList<>();

        int[] s = new int[k];

        if (k <= input.size()) {
            for (int i = 0; (s[i] = i) < k - 1; i++) ;
            subsets.add(getSubset(input, s));
            for (; ; ) {
                int i;
                for (i = k - 1; i >= 0 && s[i] == input.size() - k + i; i--) ;
                if (i < 0) {
                    break;
                }
                s[i]++;
                for (++i; i < k; i++) {
                    s[i] = s[i - 1] + 1;
                }
                subsets.add(getSubset(input, s));
            }
        }

        return subsets;
    }

    /**
     * 方法<code>getSubset</code>得到所有情况子集
     *
     * @param input  输入的参数集合
     * @param subset 序列子集
     * @return java.util.Map<java.lang.String, java.lang.Object> 情况Map
     * @title
     * @description
     * @flow
     * @author zys
     * @date 2021-08-11 17:13:52
     */
    public static Map<String, Object> getSubset(List<Map.Entry<String, Object>> input, int[] subset) {
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < subset.length; i++) {
            result.put(input.get(subset[i]).getKey(), input.get(subset[i]).getValue());
        }
        return result;
    }

}
