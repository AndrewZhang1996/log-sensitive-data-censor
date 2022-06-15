package org.zys.java.tools.censor;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zys
 * @version 1.0
 * @date 2022/06/10 11:52:27
 */
public class LogSensitiveDataCensor {

//    private static final String CONTAIN_MOBILE_PHONE_NUMBER_REGEX = "^.*\\D+1[3456789]\\d{9}\\D+.*$";
    private static final String CONTAIN_MOBILE_PHONE_NUMBER_REGEX = "\\D+1[3456789]\\d{9}\\D+";

    private static final String CONTAIN_POSSIBLE_CHINESE_NAME_REGEX = "^.*\\[.*[\\u4e00-\\u9fa5]{2,3}.*].*$";

//    private static final String CONTAIN_ID_NO_REGEX = "^.*\\D+(([1][1-5])|([2][1-3])|([3][1-7])|([4][1-6])|([5][0-4])|([6][1-5])|([7][1])|([8][1-2]))\\d{4}(([1][9]\\d{2})|([2]\\d{3}))(([0][1-9])|([1][0-2]))(([0][1-9])|([1-2][0-9])|([3][0-1]))\\d{3}[0-9xX]\\D+.*$";

//    private static final String CONTAIN_ID_NO_REGEX = "^.*\\D+[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]\\D+.*$";
    private static final String CONTAIN_ID_NO_REGEX = "\\D+[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]\\D+";

//    private static final String CONTAIN_BANK_ACCOUNT_REGEX = "^.*\\D+(1|[3-5])(\\d{11}|\\d{15}|\\d{16}|\\d{17}|\\d{18})\\D+.*$";
    private static final String CONTAIN_BANK_ACCOUNT_REGEX = "\\D+(1|[3-5])(\\d{11}|\\d{15}|\\d{16}|\\d{17}|\\d{18})\\D+";

    private static final String CONTAIN_EMAIL_REGEX = "[A-Za-z0-9\\\\u4e00-\\\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+";

    private static final Pattern ffCustomer = Pattern.compile("4008908970");
    private static final Pattern mobilePhoneNumber = Pattern.compile(CONTAIN_MOBILE_PHONE_NUMBER_REGEX);
    private static final Pattern idNo = Pattern.compile(CONTAIN_ID_NO_REGEX);
    private static final Pattern email = Pattern.compile(CONTAIN_EMAIL_REGEX);
    private static final Pattern bankAccount = Pattern.compile(CONTAIN_BANK_ACCOUNT_REGEX);

    private static Map<String, String> regexExtendMap = new HashMap<>();

    public static void main(String[] args) {
//        String path = "D:\\工作\\学习资料\\my-java-tools\\log-sensitive-data-censor\\src\\main\\resource\\hpstempquery.log";
//        String path = "C:\\Users\\Administrator\\Documents\\敏感数据排查\\hpsquery-228-1.log";
//        String outputpath = "C:\\Users\\Administrator\\Documents\\敏感数据排查\\hpsquery-228-1.log.output.log";

        if (args.length == 0) {
            System.out.println(ConsoleColors.RED_BRIGHT + "请输入参数!" + ConsoleColors.RESET + "\n" +
                    "参数可以通过 " + ConsoleColors.BLUE_BOLD_BRIGHT + "--help 或 -h" + ConsoleColors.RESET + " 命令查看");
            return;
        }
        List<String> argsList = Arrays.asList(args);
        if (Commands.HELP.equalsIgnoreCase(argsList.get(0)) || Commands.HELP_2.equalsIgnoreCase(argsList.get(0))) {
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "--help / -h" + ConsoleColors.RESET + ": 打印此消息");
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "--in / -i" + ConsoleColors.RESET + ": 输入文件");
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "--out / -o" + ConsoleColors.RESET + ": 输出文件");
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "--date / -d" + ConsoleColors.RESET + ": 起始日期(包含), 格式必须为yyyy-MM-dd");
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "--regex / -r" + ConsoleColors.RESET + ": 自定义正则表达式, 格式应为“自定义名称 表达式”. 定义多个表达式，格式应为“自定义名称1 表达式1 自定义名称2 表达式2 ...”. 自定义名称和表达式中不要含有空格!");
            return;
        }
        String inputFilePath = "";
        String outputFilePath = "";
        String startDate = "";
        Optional<String> param;

        boolean checkParam = (argsList.contains(Commands.IN) ^ argsList.contains(Commands.IN_2)) && (argsList.contains(Commands.OUT) ^ argsList.contains(Commands.OUT_2));
        if (checkParam) {
            param = getParam(argsList, Commands.IN);
            if (param.isPresent()) {
                if (!(param.get().startsWith("-") || param.get().startsWith("--"))) {
                    inputFilePath = param.get();
                } else {
                    checkParam = false;
                }
            } else {
                param = getParam(argsList, Commands.IN_2);
                if (param.isPresent()) {
                    if (!(param.get().startsWith("-") || param.get().startsWith("--"))) {
                        inputFilePath = param.get();
                    } else {
                        checkParam = false;
                    }
                } else {
                    checkParam = false;
                }
            }
        }
        if (checkParam) {
            param = getParam(argsList, Commands.OUT);
            if (param.isPresent()) {
                if (!(param.get().startsWith("-") || param.get().startsWith("--"))) {
                    outputFilePath = param.get();
                } else {
                    checkParam = false;
                }
            } else {
                param = getParam(argsList, Commands.OUT_2);
                if (param.isPresent()) {
                    if (!(param.get().startsWith("-") || param.get().startsWith("--"))) {
                        outputFilePath = param.get();
                    } else {
                        checkParam = false;
                    }
                } else {
                    checkParam = false;
                }
            }
        }
        if (checkParam) {
            param = getParam(argsList, Commands.DATE);
            if (param.isPresent()) {
                if (!(param.get().startsWith("-") || param.get().startsWith("--"))) {
                    startDate = param.get();
                    try {
                        new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
                    } catch (ParseException e) {
                        System.out.println(ConsoleColors.RED_BRIGHT + "输入起始日期格式错误!" + ConsoleColors.RESET);
                        checkParam = false;
                    }
                } else {
                    checkParam = false;
                }
            } else {
                param = getParam(argsList, Commands.DATE_2);
                if (param.isPresent()) {
                    if (!(param.get().startsWith("-") || param.get().startsWith("--"))) {
                        startDate = param.get();
                        try {
                            new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
                        } catch (ParseException e) {
                            System.out.println(ConsoleColors.RED_BRIGHT + "输入起始日期格式错误!" + ConsoleColors.RESET);
                            checkParam = false;
                        }
                    } else {
                        checkParam = false;
                    }
                } else {
                    checkParam = false;
                }
            }
        }
        if (checkParam) {
            Optional<List<String>> regexParam = getParamRegex(argsList, Commands.REGEX);
            if (regexParam.isPresent()) {
                String key = null;
                String value = null;
                for(int i = 0; i < regexParam.get().size(); i++) {
                    if (i % 2 == 0) {
                        key = regexParam.get().get(i);
                    } else {
                        value = regexParam.get().get(i);
                        assert key != null;
                        regexExtendMap.put(key, value);
                        key = null;
                        value = null;
                    }
                }
            } else {
                regexParam = getParamRegex(argsList, Commands.REGEX_2);
                if (regexParam.isPresent()) {
                    String key = null;
                    String value = null;
                    for(int i = 0; i < regexParam.get().size(); i++) {
                        if (i % 2 == 0) {
                            key = regexParam.get().get(i);
                        } else {
                            value = regexParam.get().get(i);
                            assert key != null;
                            regexExtendMap.put(key, value);
                            key = null;
                            value = null;
                        }
                    }
                }
            }
        }
        if (checkParam && inputFilePath.length() > 0 && outputFilePath.length() > 0) {
            bufferedReaderReadFile(inputFilePath, outputFilePath, startDate);
            System.out.println(ConsoleColors.GREEN_BRIGHT + "完成" + ConsoleColors.RESET);
            return;
        }
        System.out.println(ConsoleColors.RED_BRIGHT + "输入参数错误!" + ConsoleColors.RESET + "\n" +
                "参数可以通过 " + ConsoleColors.BLUE_BOLD_BRIGHT + "--help 或 -h" + ConsoleColors.RESET + " 命令查看");
    }

    private static Optional<String> getParam(List<String> argsList, String command) {
        if (argsList.contains(command) && argsList.indexOf(command) < argsList.size() - 1) {
            return Optional.ofNullable(argsList.get(argsList.indexOf(command) + 1));
        }
        return Optional.empty();
    }

    private static Optional<List<String>> getParamRegex(List<String> argsList, String command) {
        if (argsList.contains(command) && argsList.indexOf(command) < argsList.size() - 1) {
            List<String> result = new ArrayList<>();
            for (int i = argsList.indexOf(command) + 1; i < argsList.size(); i++) {
                if (argsList.get(i).startsWith("-")) {
                    break;
                }
                result.add(argsList.get(i));
            }
            if (result.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(result);
        }
        return Optional.empty();
    }

    private static void bufferedReaderReadFile(String path, String outputPath, String startDate) {
        File inputFile = new File(path);
        Long fileSize = 0L;
        if (!inputFile.exists()) {
            System.out.println("不存在文件[" + ConsoleColors.RED_BRIGHT + path + ConsoleColors.RESET + "]");
            return;
        } else {
            fileSize = inputFile.length();
            System.out.println("文件[" + ConsoleColors.GREEN_BRIGHT + inputFile.getName() + ConsoleColors.RESET + "]大小为" + ConsoleColors.GREEN_BRIGHT + fileSize + ConsoleColors.RESET + "字节" + "(" + ConsoleColors.GREEN_BRIGHT + new BigDecimal(fileSize/(1024*1024d)).setScale(2, RoundingMode.HALF_UP) + ConsoleColors.RESET + "MB)");
        }
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        OutputStreamWriter writer = null;
        BufferedWriter bufferedWriter = null;
        OutputStreamWriter writerShort = null;
        BufferedWriter bufferedWriterShort = null;
        try {
            reader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(reader);
            writer = new OutputStreamWriter(new FileOutputStream(outputPath), StandardCharsets.UTF_8);
            bufferedWriter = new BufferedWriter(writer);
            String line = "";
            StringJoiner sj = new StringJoiner(", ");
            StringBuilder sbShort = new StringBuilder();
            boolean match = false;
            Long hasRead = 0L;
            Long currentLine = 1L;
            NumberFormat num = NumberFormat.getPercentInstance();
            String dateRegex = ".*";
            if (startDate.length() > 0) {
//                Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
//                if (start.after(new Date())) {
//                    System.out.println("输入时间大于当前时间!");
//                    return;
//                }
//                List<String> dateList = DateTimeRegex.dateUntilNowFormat(start);
//                dateRegex = "^" + dateList.get(dateList.size() - 1);
                dateRegex = "^" + startDate;
            }
            Pattern datePattern = Pattern.compile(dateRegex);

            Map<String, Pattern> regexExtendPattern = new HashMap<>();
            for (Map.Entry<String, String> entry: regexExtendMap.entrySet()) {
                regexExtendPattern.put(entry.getKey(), Pattern.compile(entry.getValue()));
            }

            boolean startLine = false;
            while ((line = bufferedReader.readLine()) != null) {
                Matcher dateMatcher = datePattern.matcher(line);
                if (!startLine && dateMatcher.find()) {
                    startLine = true;
                }
                if (startLine) {
                    Matcher ffCustomerMatcher = ffCustomer.matcher(line);
                    Matcher mobilePhoneNumberMatcher = mobilePhoneNumber.matcher(line);
                    Matcher idNoMatcher = idNo.matcher(line);
                    Matcher emailMatcher = email.matcher(line);
                    Matcher bankAccountMatcher = bankAccount.matcher(line);
                    if (ffCustomerMatcher.find()) {
                        sj.add("丰付客服4008908970");
                        sbShort.append("丰付客服4008908970: \n");
                        ffCustomerMatcher.reset();
                        while(ffCustomerMatcher.find()) {
                            sbShort.append(ffCustomerMatcher.group()).append("       ").append("\n");
                        }
                        match = true;
                    }
                    if (mobilePhoneNumberMatcher.find()) {
                        sj.add("手机号");
                        sbShort.append("手机号: \n");
                        mobilePhoneNumberMatcher.reset();
                        while(mobilePhoneNumberMatcher.find()) {
                            sbShort.append(mobilePhoneNumberMatcher.group()).append("       ").append("\n");
                        }
                        match = true;
                    }
                    if (idNoMatcher.find()) {
                        sj.add("身份证号");
                        sbShort.append("身份证号: \n");
                        idNoMatcher.reset();
                        while(idNoMatcher.find()) {
                            sbShort.append(idNoMatcher.group()).append("       ").append("\n");
                        }
                        match = true;
                    }
                    if (emailMatcher.find()) {
                        sj.add("邮箱");
                        sbShort.append("邮箱: \n");
                        emailMatcher.reset();
                        while(emailMatcher.find()) {
                            sbShort.append(emailMatcher.group()).append("       ").append("\n");
                        }
                        match = true;
                    }
                    if (bankAccountMatcher.find()) {
                        sj.add("银行账号");
                        sbShort.append("银行账号: \n");
                        bankAccountMatcher.reset();
                        while(bankAccountMatcher.find()) {
                            sbShort.append(bankAccountMatcher.group()).append("       ").append("\n");
                        }
                        match = true;
                    }
                    if (!regexExtendPattern.isEmpty()) {
                        for (Map.Entry<String, Pattern> patternEntry : regexExtendPattern.entrySet()) {
                            Matcher patternMatcher = patternEntry.getValue().matcher(line);
                            if (patternMatcher.find()) {
                                sj.add(patternEntry.getKey());
                                sbShort.append(patternEntry.getKey()).append("：\n");
                                patternMatcher.reset();
                                while (patternMatcher.find()) {
                                    if (patternMatcher.groupCount() != 0) {
                                        for (int j = 0; j < patternMatcher.groupCount(); j++) {
                                            sbShort.append(patternMatcher.group(j)).append("       ").append("\n");
                                        }
                                    } else {
                                        sbShort.append(patternMatcher.group()).append("       ").append("\n");
                                    }
                                }
                                match = true;
                            }
                        }
                    }
                    if (match) {
                        bufferedWriter.write("行号：" + currentLine + ", 可能存在" + sj + ": ");
                        bufferedWriter.newLine();
//                    bufferedWriter.write(line);
                        bufferedWriter.write(sbShort.toString());
                        bufferedWriter.newLine();
                        bufferedWriter.write("\n\n");
                        match = false;
                        sj = new StringJoiner(", ");
                        sbShort.setLength(0);
                    }
                }
                currentLine++;
                hasRead += line.getBytes().length;
                System.out.print("当前进度：" + ConsoleColors.GREEN_BRIGHT + num.format(hasRead/fileSize.doubleValue()) + ConsoleColors.RESET + "\r");
            }
            bufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ignored) {}
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception ignored) {}
            }
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (Exception ignored) {}
            }
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } catch (Exception ignored) {}
            }
            if (writerShort != null) {
                try {
                    writerShort.flush();
                    writerShort.close();
                } catch (Exception ignored) {}
            }
            if (bufferedWriterShort != null) {
                try {
                    bufferedWriterShort.flush();
                    bufferedWriterShort.close();
                } catch (Exception ignored) {}
            }
        }
    }

    private static void bufferedInputStreamReadFile(String path) {
        InputStream inputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            inputStream = new FileInputStream(path);
            bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[10240];
            while (bufferedInputStream.read(buffer) != -1) {
                System.out.println(new String(buffer));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception ignored){};
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (Exception ignored){};
            }
        }
    }
}
