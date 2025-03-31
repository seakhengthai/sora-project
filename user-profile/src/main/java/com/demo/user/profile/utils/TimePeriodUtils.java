package com.demo.user.profile.utils;

import com.demo.user.profile.domain.DrcrInd;
import com.demo.user.profile.dto.request.StatementDownloadRequestDTO;
import com.demo.user.profile.exception.ApplicationException;
import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

import static com.demo.user.profile.domain.StatementPeriod.*;

public class TimePeriodUtils {

    public static TimePeriod getDateTime(int year, int month) {
        YearMonth yearMonthObject = YearMonth.of(year, month);
        return TimePeriod.builder()
                .year(yearMonthObject.getYear())
                .month(yearMonthObject.getMonth().getValue())
                .daysOfMonth(yearMonthObject.lengthOfMonth())
                .build();
    }

    public static TimePeriod calculatePassedMonth(int passedMonth, Date date) {
        validPassedMonth(passedMonth);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (passedMonth == 0) {
            TimePeriod current = getTimePeriod(calendar);
            return TimePeriod.builder()
                    .fromDate(current.getFirstDayOfMonth())
                    .toDate(current.getLastDayOfMonth())
                    .build();
        }
        setDate(date);
        passedMonth = passedMonth - 1;
        setToLastMonth(calendar);
        setLastMonthTo(calendar, passedMonth);
        TimePeriod fromPeriod = getTimePeriod(calendar);

        calendar = setDate(date);
        setToLastMonth(calendar);
        TimePeriod toPeriod = getTimePeriod(calendar);
        return TimePeriod.builder()
                .fromDate(fromPeriod.getFirstDayOfMonth())
                .toDate(toPeriod.getLastDayOfMonth())
                .build();

    }

    public static TimePeriod getPeriod(StatementDownloadRequestDTO request) {
        TimePeriod timePeriod;
        Date now = new Date();
        switch (request.getPeriod()) {
            case THIS_MONTH:
                timePeriod = TimePeriodUtils.calculatePassedMonth(0, now);
                break;
            case LAST_1_MONTH:
                timePeriod = TimePeriodUtils.calculatePassedMonth(1, now);
                break;
            case LAST_2_MONTH:
                timePeriod = TimePeriodUtils.calculatePassedMonth(2, now);
                break;
            case LAST_3_MONTH:
                timePeriod = TimePeriodUtils.calculatePassedMonth(3, now);
                break;
            case LAST_4_MONTH:
                timePeriod = TimePeriodUtils.calculatePassedMonth(4, now);
                break;
            case LAST_5_MONTH:
                timePeriod = TimePeriodUtils.calculatePassedMonth(5, now);
                break;
            case LAST_6_MONTH:
                timePeriod = TimePeriodUtils.calculatePassedMonth(6, now);
                break;
            case CUSTOM:
                request.validateCustomPeriod();
                timePeriod = TimePeriod.builder()
                        .toDate(request.getToDate().trim() + " 23:59:59")
                        .fromDate(request.getFromDate().trim() + " 00:00:00")
                        .build();
                break;
            default:
                throw new ApplicationException("TP001", "Time period is invalid");
        }
        return timePeriod;
    }

    public static Date parseDate(String date) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            return formatter.parse(date);
        } catch (Exception e) {
            throw new ApplicationException("ET003", String.format("Cannot parse date: %s reason: %s", date, e.getMessage()));
        }
    }

    public static Date parseDateWithFormat(String date, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.parse(date);
        } catch (Exception e) {
            throw new ApplicationException("ET003", String.format("Cannot parse date: %s reason: %s", date, e.getMessage()));
        }
    }

    public static String formatDateStr(String strDate, String fromDateFormatStr, String toDateFormatStr)  {
        SimpleDateFormat fromDateFormat = new SimpleDateFormat(fromDateFormatStr);
        SimpleDateFormat toDateFormat = new SimpleDateFormat(toDateFormatStr);
        try {
            return toDateFormat.format(fromDateFormat.parse(strDate));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String dateToStrDateWithFormat(Date date, String format) {
        return formatDateStr(date.toString(),
                "EEE MMM dd HH:mm:ss z yyyy",
                format);
    }

    private static void validPassedMonth(int passedMonth) {
        if (passedMonth > 6) throw new ApplicationException("ET001", "Time period is over 6 months.");
    }


    public static Calendar setDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static void setToLastMonth(Calendar calendar) {
        calendar.add(Calendar.MONTH, -1);
    }

    public static void setToCurrentMonth(Calendar calendar) {
        calendar.add(Calendar.MONTH, 1);
    }

    public static void setLastMonthTo(Calendar calendar, int toMonth) {
        calendar.add(Calendar.MONTH, Math.negateExact(toMonth));
    }

    public static int getCurrentSetMonth(Calendar calendar) {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getCurrentSetYear(Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }

    public static TimePeriod getTimePeriod(Calendar calendar) {
        int month = getCurrentSetMonth(calendar);
        int year = getCurrentSetYear(calendar);
        return getDateTime(year, month);
    }

    public static Date getDateWithoutTime(Date date) {
        if(date==null) return null;
        date = DateUtils.setHours(date, 0);
        date = DateUtils.setMinutes(date, 0);
        date = DateUtils.setSeconds(date, 0);
        date = DateUtils.setMilliseconds(date, 0);
        return date;
    }
}
