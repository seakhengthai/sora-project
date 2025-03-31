package com.demo.user.profile.export;

import com.demo.user.profile.domain.DrcrInd;
import com.demo.user.profile.domain.entity.AccountTransaction;
import com.demo.user.profile.domain.entity.UserAccount;
import com.demo.user.profile.dto.request.StatementDownloadRequestDTO;
import com.demo.user.profile.exception.ApplicationException;
import com.demo.user.profile.utils.TimePeriod;
import com.demo.user.profile.utils.TimePeriodUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class ExportAccountStatementExcel {

    private final String FONT_NAME = "SF UI Text";

    private void cellValid(Row row, int columnCount, Object values, CellStyle style, SXSSFSheet sheet) {
        Cell cell = row.createCell(columnCount);
        if (values instanceof Integer) {
            cell.setCellValue((Integer) values);
        } else if (values instanceof Boolean) {
            cell.setCellValue((boolean) values);
        } else {
            cell.setCellValue((String) values);
        }

        cell.setCellStyle(style);
    }

    private void header(SXSSFWorkbook workbook, SXSSFSheet sheet, StatementDownloadRequestDTO request) throws IOException {
        Row row = sheet.createRow(4);
        Cell cell = row.createCell(0);
        CellStyle cellStyleTitle = workbook.createCellStyle();
        Font fontTitle = workbook.createFont();
        fontTitle.setBold(true);
        fontTitle.setFontHeightInPoints((short) 18);
        fontTitle.setColor(IndexedColors.GREEN.getIndex());
        cellStyleTitle.setFont(fontTitle);
        cell.setCellStyle(cellStyleTitle);
        cell.setCellValue("ACCOUNT STATEMENT");

        TimePeriod timePeriod = TimePeriodUtils.getPeriod(request);

        SXSSFCell cellD;
        SXSSFRow rowD;
        Font fontD = workbook.createFont();
        fontD.setFontHeightInPoints((short) 12);
        fontD.setFontName(FONT_NAME);
        CellStyle cellStyleD = workbook.createCellStyle();
        cellStyleD.setFont(fontD);
        rowD = sheet.createRow(5);
        cellD = rowD.createCell(0);
        cellD.setCellValue("From: " + timePeriod.getFromDate() + " To " + timePeriod.getToDate());
        cellD.setCellStyle(cellStyleD);

        row = sheet.createRow(7);
        Cell cellAccountDetail = row.createCell(0);
        CellStyle cellStyleAccountDetail = workbook.createCellStyle();
        Font fontAccountDetail = workbook.createFont();
        fontAccountDetail.setFontHeightInPoints((short) 12);
        fontAccountDetail.setBold(true);
        cellStyleAccountDetail.setFont(fontAccountDetail);
        cellAccountDetail.setCellStyle(cellStyleAccountDetail);
        cellAccountDetail.setCellValue("ACCOUNT DETAIL");

        row = sheet.createRow(13);

        Cell cellTransactionDetailTitle = row.createCell(0);
        CellStyle cellStyleTransactionDetailTitle = workbook.createCellStyle();
        Font fontCellTransactionDetail = workbook.createFont();
        fontCellTransactionDetail.setFontHeightInPoints((short) 12);
        fontCellTransactionDetail.setBold(true);
        cellStyleTransactionDetailTitle.setFont(fontCellTransactionDetail);
        cellTransactionDetailTitle.setCellStyle(cellStyleTransactionDetailTitle);
        cellTransactionDetailTitle.setCellValue("TRANSACTION DETAIL");

        row = sheet.createRow(14);
        CellStyle cellStyle = workbook.createCellStyle();
        Font fontTableHeader = workbook.createFont();
        fontTableHeader.setFontHeightInPoints((short) 10);
        fontTableHeader.setBold(true);
        fontTableHeader.setFontHeightInPoints((short) 10);

        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFont(fontTableHeader);

        cellValid(row, 0, "Date", cellStyle, sheet);
        cellValid(row, 1, "Description", cellStyle, sheet);
        cellValid(row, 2, "Money Out", cellStyle, sheet);
        cellValid(row, 3, "Money In", cellStyle, sheet);
        cellValid(row, 4, "Balance", cellStyle, sheet);
    }

    private void accountDetail(UserAccount userAccount, List<AccountTransaction> accountTransactions, SXSSFWorkbook workbook, SXSSFSheet sheet){

        SXSSFCell cellAccountDetail;
        SXSSFRow row;
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName(FONT_NAME);
        font.setBold(true);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);

        row = sheet.createRow(8);
        cellAccountDetail = row.createCell(0);
        cellAccountDetail.setCellValue("Account Name ");
        cellAccountDetail.setCellStyle(cellStyle);
        cellAccountDetail = row.createCell(1);
        cellAccountDetail.setCellValue(userAccount.getAccountName());

        row = sheet.createRow(9);
        cellAccountDetail = row.createCell(0);
        cellAccountDetail.setCellValue("Account Number ");
        cellAccountDetail.setCellStyle(cellStyle);
        cellAccountDetail = row.createCell(1);
        cellAccountDetail.setCellValue(userAccount.getAccountNo());

        row = sheet.createRow(10);
        cellAccountDetail = row.createCell(0);
        cellAccountDetail.setCellValue("Account Currency ");
        cellAccountDetail.setCellStyle(cellStyle);
        cellAccountDetail = row.createCell(1);
        cellAccountDetail.setCellValue(userAccount.getCurrency());

    }

    private void writeData(List<AccountTransaction> accountTransactions, SXSSFWorkbook workbook, SXSSFSheet sheet) {
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 9);
        font.setFontName(FONT_NAME);


        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyle.setFont(font);

        CellStyle cellStyleR = workbook.createCellStyle();
        cellStyleR.setBorderBottom(BorderStyle.THIN);
        cellStyleR.setWrapText(true);
        cellStyleR.setAlignment(HorizontalAlignment.RIGHT);
        cellStyleR.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyleR.setFont(font);

        ObjectMapper om = new ObjectMapper();

        AtomicInteger rowCount = new AtomicInteger(15);
        accountTransactions.forEach(statement -> {
            SXSSFRow row = sheet.createRow(rowCount.getAndIncrement());
            int columnCount = 0;
            cellValid(row, columnCount++, statement.getTxnDate(), cellStyle, sheet);
            cellValid(row, columnCount++, statement.getDescription(), cellStyle, sheet);
            cellValid(row, columnCount++, DrcrInd.DEBIT.equals(statement.getDrcrInd()) ? statement.getAmount()  + " " + statement.getCurrency() : "", cellStyleR, sheet);
            cellValid(row, columnCount++, DrcrInd.CREDIT.equals(statement.getDrcrInd()) ? statement.getAmount() + " " + statement.getCurrency() : "", cellStyleR, sheet);
            cellValid(row, columnCount, statement.getAmount() + " " + statement.getCurrency(), cellStyleR, sheet);
        });
    }

    public void export(HttpServletResponse response, UserAccount userAccount, StatementDownloadRequestDTO request, List<AccountTransaction> accountTransactions) {
        try {
            final SXSSFWorkbook workbook = new SXSSFWorkbook();
            SXSSFSheet sheet = workbook.createSheet("Account Statements");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd_HHmmss");//_HH:mm:ss
            String currentDate = dateFormat.format(new Date());
            String fileName = "Account_Statement_" + currentDate + ".xlsx";
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename="+ fileName;
            response.setHeader(headerKey, headerValue);
            response.addHeader("file-name", fileName);

            header(workbook, sheet, request);
            accountDetail(userAccount, accountTransactions, workbook, sheet);
            writeData(accountTransactions, workbook, sheet);

            sheet.setColumnWidth(0, 5500);
            sheet.setColumnWidth(1, 8000);
            sheet.setColumnWidth(2, 3000);
            sheet.setColumnWidth(3, 3000);
            sheet.setColumnWidth(4, 3500);
            sheet.getPrintSetup().setPaperSize(PrintSetup.A4_EXTRA_PAPERSIZE);
            ServletOutputStream stream = response.getOutputStream();
            workbook.write(stream);
            stream.close();
        } catch (Exception e) {
            log.error("Failed to generate PDF file", e);
            throw new ApplicationException("EA002", "Failed to generate account statement.");
        }
    }
}
