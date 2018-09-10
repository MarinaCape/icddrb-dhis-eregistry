package org.icddrb.dhis.android.sdk.utils.support;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class DateUtils {
    public static final SimpleDateFormat ACCESS_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final double DAYS_IN_YEAR = 365.0d;
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String DEFAULT_DATE_REGEX = "\\b\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-2])\\b";
    public static final SimpleDateFormat HTTP_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
    public static final SimpleDateFormat LONG_DATE_FORMAT = new SimpleDateFormat(TIMESTAMP_PATTERN);
    private static final long MS_PER_DAY = 86400000;
    private static final long MS_PER_S = 1000;
    private static final String SEP = ", ";
    public static final SimpleDateFormat[] SUPPORTED_DATE_FORMATS = new SimpleDateFormat[]{new SimpleDateFormat(StdDateFormat.DATE_FORMAT_STR_ISO8601), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"), new SimpleDateFormat(TIMESTAMP_PATTERN), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"), new SimpleDateFormat("yyyy-MM-dd'T'HH"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ"), new SimpleDateFormat("yyyy-MM-dd"), new SimpleDateFormat("yyyy-MM"), new SimpleDateFormat("yyyy")};
    public static final PeriodFormatter DAY_SECOND_FORMAT = new PeriodFormatterBuilder().appendDays().appendSuffix(" d").appendSeparator(SEP).appendHours().appendSuffix(" h").appendSeparator(SEP).appendMinutes().appendSuffix(" m").appendSeparator(SEP).appendSeconds().appendSuffix(" s").appendSeparator(SEP).toFormatter();
    public static String getAccessDateString(Date date) {
        return date != null ? ACCESS_DATE_FORMAT.format(date) : null;
    }

    public static String getLongGmtDateString(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(StdDateFormat.DATE_FORMAT_STR_ISO8601);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(date);
    }

    public static String getLongDateString(Date date) {
        return date != null ? LONG_DATE_FORMAT.format(date) : null;
    }

    public static String getLongDateString() {
        return getLongDateString(Calendar.getInstance().getTime());
    }

    public static String getMediumDateString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy-MM-dd");
        return date != null ? format.format(date) : null;
    }

    public static Date max(Date date1, Date date2) {
        if (date1 != null) {
            if (!(date2 == null || date1.after(date2))) {
                date1 = date2;
            }
            return date1;
        } else if (date2 != null) {
            return date2;
        } else {
            return null;
        }
    }

    public static Date max(Date... date) {
        Date latest = null;
        for (Date d : date) {
            latest = max(d, latest);
        }
        return latest;
    }

    public static String getMediumDateString(Date date, String defaultValue) {
        return date != null ? getMediumDateString(date) : defaultValue;
    }

    public static String getMediumDateString() {
        return getMediumDateString(Calendar.getInstance().getTime());
    }

    public static String getHttpDateString(Date date) {
        return HTTP_DATE_FORMAT.format(date) + " GMT";
    }

    public static String getExpiredHttpDateString() {
        Calendar cal = Calendar.getInstance();
        cal.add(6, -1);
        return getHttpDateString(cal.getTime());
    }

    public static Date getDefaultDate(String dateString) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date getMediumDate(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat();
            format.applyPattern("yyyy-MM-dd");
            return (dateString == null || !dateIsValid(dateString)) ? null : format.parse(dateString);
        } catch (ParseException ex) {
            throw new RuntimeException("Failed to parse medium date", ex);
        }
    }

    public static boolean between(Date baseDate, Date startDate, Date endDate) {
        if (startDate.equals(endDate) || endDate.before(startDate)) {
            return false;
        }
        if (!startDate.before(baseDate) && !startDate.equals(baseDate)) {
            return false;
        }
        if (endDate.after(baseDate) || endDate.equals(baseDate)) {
            return true;
        }
        return false;
    }

    public static boolean strictlyBetween(Date baseDate, Date startDate, Date endDate) {
        if (startDate.equals(endDate) || endDate.before(startDate) || !startDate.before(baseDate) || !endDate.after(baseDate)) {
            return false;
        }
        return true;
    }

    public static long getDays(Date date) {
        return date.getTime() / 86400000;
    }

    public static long getDays(Date startDate, Date endDate) {
        return (endDate.getTime() - startDate.getTime()) / 86400000;
    }

    public static long getDaysInclusive(Date startDate, Date endDate) {
        return getDays(startDate, endDate) + 1;
    }

    public static int daysBetween(Date startDate, Date endDate) {
        return Days.daysBetween(new DateTime((Object) startDate), new DateTime((Object) endDate)).getDays();
    }

    public static int monthsBetween(Date startDate, Date endDate) {
        return Months.monthsBetween(new DateTime((Object) startDate), new DateTime((Object) endDate)).getMonths();
    }

    public static int daysSince1900(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(1900, 0, 1);
        return daysBetween(calendar.getTime(), date);
    }

    public static Date getEpoch() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(1970, 0, 1);
        return calendar.getTime();
    }

    public static String getSqlDateString(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(1);
        int month = cal.get(2) + 1;
        int day = cal.get(5);
        String yearString = String.valueOf(year);
        return yearString + "-" + (month < 10 ? "0" + month : String.valueOf(month)) + "-" + (day < 10 ? "0" + day : String.valueOf(day));
    }

    public static boolean dateIsValid(String dateString) {
        return dateString.matches(DEFAULT_DATE_REGEX);
    }

    public static Date getDateAfterAddition(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(5, days);
        return cal.getTime();
    }

    public static boolean checkDates(String fromDate, String toDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (sdf.parse(fromDate).before(sdf.parse(toDate))) {
                return false;
            }
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static String getPrettyInterval(Date start, Date end) {
        if (start == null || end == null) {
            return null;
        }
        return DAY_SECOND_FORMAT.print(new Period(end.getTime() - start.getTime()));
    }

    public static Date parseDate(String dateString) {
        Date date = null;
        if (dateString != null) {
            SimpleDateFormat[] simpleDateFormatArr = SUPPORTED_DATE_FORMATS;
            int length = simpleDateFormatArr.length;
            int i = 0;
            while (i < length) {
                try {
                    date = simpleDateFormatArr[i].parse(dateString);
                    break;
                } catch (ParseException e) {
                    i++;
                }
            }
        }
        return date;
    }
}
