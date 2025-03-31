package com.demo.user.profile.dto.request;

import com.demo.user.profile.domain.FileType;
import com.demo.user.profile.domain.StatementPeriod;
import com.demo.user.profile.exception.ApplicationException;
import com.demo.user.profile.utils.TimePeriodUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Calendar;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class StatementDownloadRequestDTO {
    String cif;
    @NotBlank
    String accountNo;
    @NotNull
    StatementPeriod period;
    @JsonProperty("from_date")
    String fromDate;
    @JsonProperty("to_date")
    String toDate;
    @JsonProperty("file_type")
    FileType fileType;

    public void validateCustomPeriod() {
        try {
            Date fDate = TimePeriodUtils.parseDate(getFromDate().toUpperCase());
            Date tDate = TimePeriodUtils.parseDate(getToDate().toUpperCase());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fDate);
            calendar.add(Calendar.MONTH, 6);
            if (fDate.after(tDate)) {
                throw new ApplicationException("ET002", "Time period from date cannot be grater than to date.");
            }
        } catch (ApplicationException ax) {
            throw ax;
        } catch (Exception e) {
            throw new ApplicationException("ED002", String.format("Invalid date request: From date: %s to date: %s, %s", getFromDate(), getToDate(), e.getMessage()));
        }
    }

    public boolean isWithinRange(Date checkDate, Date startDate, Date endDate) {
        return checkDate.getTime() >= startDate.getTime() &&
                checkDate.getTime() <= endDate.getTime();
    }
}
