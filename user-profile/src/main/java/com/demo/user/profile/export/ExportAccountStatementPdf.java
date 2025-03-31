package com.demo.user.profile.export;

import com.demo.user.profile.domain.entity.AccountTransaction;
import com.demo.user.profile.domain.entity.UserAccount;
import com.demo.user.profile.dto.request.StatementDownloadRequestDTO;
import com.demo.user.profile.exception.ApplicationException;
import com.demo.user.profile.utils.TimePeriod;
import com.demo.user.profile.utils.TimePeriodUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExportAccountStatementPdf {

    public void createPdfReport(HttpServletResponse response, StatementDownloadRequestDTO request, UserAccount userAccount,
                                List<AccountTransaction> accountTransactions) {
        try {
            final InputStream stream = this.getClass().getResourceAsStream("/account_statement.jrxml");
            final JasperReport report = JasperCompileManager.compileReport(stream);
            final JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(accountTransactions);
            Map<String, Object> accountDetail = this.getAccountDetails(userAccount, request);

            final JasperPrint dataGenerate = JasperFillManager.fillReport(report, accountDetail, source);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd_HHmmss");
            String currentDate = dateFormat.format(new Date());
            String fileName = "Account_Statement_" + currentDate + ".pdf";
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.addHeader("file-name", fileName);
            OutputStream outputStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(dataGenerate, outputStream);
        } catch (Exception e) {
            log.error("Failed to generate PDF file", e);
            throw new ApplicationException("EA002", "Failed to generate account statement.");
        }
    }

    private Map<String, Object> getAccountDetails(UserAccount userAccount, StatementDownloadRequestDTO request) {
        TimePeriod timePeriod = TimePeriodUtils.getPeriod(request);
        Map<String, Object> accountDetail = new HashMap<>();
        accountDetail.put("accountName", userAccount.getAccountName());
        accountDetail.put("accountNumber", userAccount.getAccountNo());
        accountDetail.put("accountCurrency", userAccount.getCurrency());
        accountDetail.put("accountBalance", userAccount.getBalance());
        accountDetail.put("fromDate", timePeriod.getFromDate());
        accountDetail.put("toDate", timePeriod.getToDate());
        return accountDetail;
    }
}
