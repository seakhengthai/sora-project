package com.demo.user.profile.utils;

import com.demo.user.profile.exception.ApplicationException;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@ToString
public class TimePeriod {
    Integer year;
    Integer month;
    Integer daysOfMonth;

    String fromDate;

    String toDate;

    public String getLastDayOfMonth() {
        return daysOfMonth + "-" + getMonthName(month) + "-" + year + " 23:59:59";
    }

    public String getFirstDayOfMonth() {
        return "1-" + getMonthName(month) + "-" + year + " 00:00:00";
    }


    public static String getMonthName(int month) {
        if (month <= 0 || month > 12) throw new ApplicationException("TP002", "Month is invalid.");
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return monthNames[month - 1];
    }
}
