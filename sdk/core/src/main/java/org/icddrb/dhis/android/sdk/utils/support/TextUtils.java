package org.icddrb.dhis.android.sdk.utils.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class TextUtils {
    private static final String DELIMITER = ", ";
    public static final String EMPTY = "";
    public static final TextUtils INSTANCE = new TextUtils();
    private static final Pattern LINK_PATTERN = Pattern.compile("((http://|https://|www\\.).+?)($|\\n|\\r|\\r\\n| )");
    public static final String LN = System.getProperty("line.separator");
    public static final String SEP = "-";
    public static final String SPACE = " ";

    public static String htmlify(String text) {
        return htmlNewline(htmlLinks(text));
    }

    public static String htmlLinks(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        Matcher matcher = LINK_PATTERN.matcher(text);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String ref;
            String url = matcher.group(1);
            String suffix = matcher.group(3);
            if (url.startsWith("www.")) {
                ref = "http://" + url;
            } else {
                ref = url;
            }
            matcher.appendReplacement(buffer, "<a href=\"" + ref + "\">" + url + "</a>" + suffix);
        }
        return matcher.appendTail(buffer).toString();
    }

    public static String htmlNewline(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        return text.replaceAll("(\n|\r|\r\n)", "<br>");
    }

    public static List<String> getTokens(String string) {
        if (string == null) {
            return null;
        }
        return new ArrayList(Arrays.asList(string.split("\\s")));
    }

    public static String subString(String string, int beginIndex, int length) {
        int endIndex = beginIndex + length;
        if (beginIndex >= string.length()) {
            return "";
        }
        if (endIndex > string.length()) {
            return string.substring(beginIndex, string.length());
        }
        return string.substring(beginIndex, endIndex);
    }

    public static String removeLast(String string, int characters) {
        if (string == null) {
            return null;
        }
        if (characters < 0 || characters > string.length()) {
            return "";
        }
        return string.substring(0, string.length() - characters);
    }

    public static String removeLastOr(String string) {
        return StringUtils.removeEndIgnoreCase(StringUtils.stripEnd(string, " "), "or");
    }

    public static String removeLastAnd(String string) {
        return StringUtils.removeEndIgnoreCase(StringUtils.stripEnd(string, " "), "and");
    }

    public static String removeLastComma(String string) {
        return StringUtils.removeEndIgnoreCase(StringUtils.stripEnd(string, " "), ",");
    }

    public static String trimEnd(String value, int length) {
        if (value == null || length > value.length()) {
            return "";
        }
        return value.substring(0, value.length() - length);
    }

    public static String getString(String string, boolean emptyString) {
        return emptyString ? "" : string;
    }

    public static String getCommaDelimitedString(Collection<?> elements) {
        StringBuilder builder = new StringBuilder();
        if (elements == null || elements.isEmpty()) {
            return builder.toString();
        }
        for (Object element : elements) {
            builder.append(element.toString()).append(DELIMITER);
        }
        return builder.substring(0, builder.length() - DELIMITER.length());
    }

    public static <T> String join(List<T> list, String separator, T nullReplacement) {
        if (list == null) {
            return null;
        }
        Iterable objects = new ArrayList(list);
        if (nullReplacement != null) {
            Collections.replaceAll(objects, null, nullReplacement);
        }
        return StringUtils.join(objects, separator);
    }

    public static String getCommaDelimitedString(Collection<?> elements, boolean delimitPrefix, boolean delimitSuffix) {
        StringBuilder builder = new StringBuilder();
        if (!(elements == null || elements.isEmpty())) {
            if (delimitPrefix) {
                builder.append(DELIMITER);
            }
            builder.append(getCommaDelimitedString(elements));
            if (delimitSuffix) {
                builder.append(DELIMITER);
            }
        }
        return builder.toString();
    }

    public static String getQuotedCommaDelimitedString(Collection<String> elements) {
        if (elements == null || elements.size() <= 0) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        for (String element : elements) {
            buffer.append("'").append(element.toString()).append("', ");
        }
        return buffer.substring(0, buffer.length() - DELIMITER.length());
    }

    public static String nullIfEmpty(String string) {
        return (string == null || string.trim().length() != 0) ? string : null;
    }

    public static boolean equalsNullSafe(String s1, String s2) {
        if (s1 == null) {
            return s2 == null;
        } else {
            return s1.equals(s2);
        }
    }

    public static String valueOf(Boolean value) {
        return value != null ? String.valueOf(value) : null;
    }

    public static Boolean valueOf(String value) {
        return value != null ? Boolean.valueOf(value) : null;
    }

    public static String lower(String string) {
        return string != null ? string.toLowerCase() : null;
    }

    public static String toString(String[] array, String separator) {
        StringBuilder builder = new StringBuilder();
        if (array != null && array.length > 0) {
            for (String string : array) {
                builder.append(string).append(separator);
            }
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public static String toString(Object object) {
        return object != null ? object.toString() : null;
    }

    public static String appendTail(Matcher matcher, StringBuffer sb) {
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String quote(String value) {
        return MathUtils.isNumeric(value) ? value : "\"" + value.replace("\"", "\\\"") + "\"";
    }

    public static String fromDouble(Double value) {
        if (value == null || Double.isNaN(value.doubleValue())) {
            return "";
        }
        return String.valueOf(Double.valueOf(MathUtils.getRounded(value.doubleValue(), 2))).replaceAll("\\.0+$", "");
    }
}
