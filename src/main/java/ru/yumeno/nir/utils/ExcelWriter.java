package ru.yumeno.nir.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import ru.yumeno.nir.entity.News;
import ru.yumeno.nir.security.entity.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class ExcelWriter {
    private Workbook workbook;

    public ExcelWriter() {
        this.workbook = new XSSFWorkbook();
    }

    public void writeNews(List<News> news, String sheetName) {
        Sheet sheet = workbook.createSheet(sheetName);
        sheet.setColumnWidth(0, 2000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 8000);
        sheet.setColumnWidth(4, 15000);
        sheet.setColumnWidth(5, 8000);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Id");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Header");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Body");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Create Date");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("Image URL");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(5);
        headerCell.setCellValue("Tags");
        headerCell.setCellStyle(headerStyle);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        for (int i = 0; i < news.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Cell cell = row.createCell(0);
            cell.setCellValue(news.get(i).getId());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(news.get(i).getHeader());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellValue(news.get(i).getBody());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellValue(news.get(i).getCreateDate().toString());
            cell.setCellStyle(style);

            cell = row.createCell(4);
            cell.setCellValue(news.get(i).getImageUrl());
            cell.setCellStyle(style);

            cell = row.createCell(5);
            cell.setCellValue(news.get(i).getTags().toString());
            cell.setCellStyle(style);
        }
    }

    public void writeUser(User user) {
        Sheet sheet = workbook.createSheet("currentUser");
        sheet.setColumnWidth(0, 2000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 10000);
        sheet.setColumnWidth(3, 8000);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Id");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Username");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Password");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Roles");
        headerCell.setCellStyle(headerStyle);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        Row row = sheet.createRow(1);
        Cell cell = row.createCell(0);
        cell.setCellValue(user.getId());
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue(user.getUsername());
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue(user.getPassword());
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue(user.getRoles().toString());
        cell.setCellStyle(style);
    }

    public void writeFile() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "report.xlsx";

        try {
            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
            workbook.close();
            workbook = new XSSFWorkbook();
            outputStream.close();
        }
        catch (IOException e) {
            log.warn("IOException, message: " + e.getMessage());
        }
    }
}
