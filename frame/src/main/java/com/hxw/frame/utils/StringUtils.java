package com.hxw.frame.utils;

import android.text.InputFilter;
import android.text.Spanned;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * 字符串相关工具类
 * Created by hxw on 2017/2/8.
 */

public class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    //emoji过滤器
    public static final InputFilter EMOJI_FILTER = new InputFilter() {
        //关键的正则表达式
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\ud83e\udd00-\ud83e\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                                   int dend) {

            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                return "";
            }

            return null;
        }
    };

    /**
     * 字符串转换成十六进制字符串
     *
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String str2HexStr(String str) {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString().trim();
    }

    /**
     * json 格式化
     *
     * @param json
     * @return
     */
    public static String jsonFormat(String json) {
        if (isSpace(json)) {
            return "json 数据为空!";
        }
        String message = "";
        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                message = jsonObject.toString(4);
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                message = jsonArray.toString(4);
            } else {
                message = json;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            message = e.getCause().getMessage() + LINE_SEPARATOR + json;
        }
        return message;
    }

    /**
     * xml 格式化
     *
     * @param xml
     * @return
     */
    public static String xmlFormat(String xml) {
        if (isSpace(xml)) {
            return "xml 数据为空!";
        }
        String message = "";
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(xmlInput, xmlOutput);
            message = xmlOutput.getWriter().toString().replaceFirst(">", ">" + LINE_SEPARATOR);

        } catch (TransformerException e) {
            e.printStackTrace();
            message = e.getCause().getMessage() + LINE_SEPARATOR + xml;
        }
        return message;
    }

    /**
     * 格式化Map集合
     *
     * @param map
     * @return
     */
    public static String mapFormat(Map map) {
        if (map == null) {
            return "map 为空!";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Object entry : map.entrySet()) {
            stringBuilder.append("[key] → ");
            stringBuilder.append(((Map.Entry) entry).getKey());
            stringBuilder.append(",[value] → ");
            stringBuilder.append(((Map.Entry) entry).getValue());
            stringBuilder.append(LINE_SEPARATOR);
        }

        return stringBuilder.toString();
    }

    /**
     * 格式化List集合
     *
     * @param list
     * @return
     */
    public static String listFormat(List list) {
        if (list == null) {
            return "list 为空!";
        }
        StringBuilder stringBuilder = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            stringBuilder.append("[" + i + "] → ");
            stringBuilder.append(list.get(i));
            stringBuilder.append(LINE_SEPARATOR);
        }
        return stringBuilder.toString();
    }

    /**
     * 判断字符串是否为null或全为空格,TextUtils.isEmpty()是判断为空或者空字符,
     * 若是带有空格是判断不出来的,用这个
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }
}
