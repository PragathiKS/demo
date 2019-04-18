package com.tetrapak.customerhub.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.customerhub.core.beans.excel.ExcelFileData;
import com.tetrapak.customerhub.core.exceptions.ExcelReportRuntimeException;

/**
 * @author dhitiwar
 *
 */
public class ExcelUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);
	private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");

	private ExcelUtil() {
		// Disallowed to create Object out of class
	}

	/**
	 * 
	 * @return Workbook
	 * 
	 */
	private static Workbook getExcelWorkBook() {
		return new XSSFWorkbook();
	}

	/**
	 * 
	 * @param workBook
	 * @param sheetName
	 * @return Sheet
	 * 
	 */
	private static Sheet getExcelSheet(Workbook workBook, String sheetName) {
		Sheet sheet = null;
		if (Objects.nonNull(workBook) && Objects.nonNull(sheetName)) {
			sheet = workBook.createSheet(sheetName);
		}
		return sheet;
	}

	/**
	 * Generates the font provided a workbook
	 * 
	 * @param workBook wb
	 * @return Font font
	 */
	public static Font getFont() {
		Workbook workBook = getExcelWorkBook();
		Font font = null;
		if (Objects.nonNull(workBook)) {
			font = workBook.createFont();
		}
		return font;
	}

	/**
	 * @param workBook
	 * @param excelReportData
	 * @return
	 */
	private static Font getFontStylingForHeader(Workbook workBook, ExcelFileData excelReportData) {
		Font headerFont = null;
		if (Objects.nonNull(workBook) && Objects.nonNull(excelReportData)) {
			headerFont = workBook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints(excelReportData.getFontHeight());
			headerFont.setColor(excelReportData.getFontColor());
		}
		return headerFont;
	}

	/**
	 * @param workBook
	 * @param headerFont
	 * @return CellStyle
	 */
	private static CellStyle getHeaderCellStyle(Workbook workBook, Font headerFont) {
		CellStyle headerCellStyle = null;
		if (Objects.nonNull(workBook) && Objects.nonNull(headerFont)) {
			headerCellStyle = workBook.createCellStyle();
			headerCellStyle.setFont(headerFont);
		}
		return headerCellStyle;
	}

	/**
	 * @param sheet
	 * @param headerCellStyle
	 * @param headerColumnLabels
	 * @return Row
	 */
	private static Row getHeaderLabels(Sheet sheet, CellStyle headerCellStyle, List<String> headerColumnLabels) {
		Row headerRow = null;
		if (Objects.nonNull(sheet) && Objects.nonNull(headerCellStyle) && Objects.nonNull(headerColumnLabels)) {
			headerRow = getRow(sheet, 0);
			// Create cells
			if (Objects.nonNull(headerRow)) {
				for (int i = 0; i < headerColumnLabels.size(); i++) {
					Cell cell = headerRow.createCell(i);
					cell.setCellValue(headerColumnLabels.get(i));
					cell.setCellStyle(headerCellStyle);
				}
			}
		}

		return headerRow;
	}

	/**
	 * @param sheet
	 * @param rowNum
	 * @return Row
	 */
	private static Row getRow(Sheet sheet, int rowNum) {
		Row row = null;
		if (Objects.nonNull(sheet)) {
			row = sheet.createRow(++rowNum);
		}
		return row;
	}

	/**
	 * @param sheet
	 * @param cloumnCount
	 */
	private static void resizeCellToFitContent(Sheet sheet, int cloumnCount) {
		// Resize all columns to fit the content size
		for (int i = 0; i < cloumnCount; i++) {
			sheet.autoSizeColumn(i);
		}
	}

	/**
	 * @param workBook
	 * @throws IOException
	 */
	private static void closeWorkbook(Workbook workBook) throws IOException {
		if (Objects.nonNull(workBook)) {
			workBook.close();
		}
	}

	/**
	 * Generate the excel file using the below parameters
	 * 
	 * @param response        Response
	 * @param excelReportData Excel Bean Data
	 */
	public static void generateExcelReport(SlingHttpServletResponse response, ExcelFileData excelReportData) {
		try {
			Workbook workBook = getExcelWorkBook();
			Sheet sheet = getExcelSheet(workBook, excelReportData.getExcelSheetName());
			if (Objects.nonNull(sheet)) {
				sheet.autoSizeColumn(33);
				sheet.setDisplayGridlines(false);
			}
			Font headerFont = getFontStylingForHeader(workBook, excelReportData);
			CellStyle headerCellStyle = getHeaderCellStyle(workBook, headerFont);
			Row headerRow = getHeaderLabels(sheet, headerCellStyle, excelReportData.getColumns());
			if (Objects.nonNull(excelReportData)) {
				prepareReportData(workBook, sheet, headerRow, excelReportData.getData(), excelReportData);
			}
			resizeCellToFitContent(sheet, 10);
			downloadExcel(response, workBook, excelReportData);
		} catch (IOException e) {
			LOGGER.error("\nA run-time exception occured while generating excel report.");
			throw new ExcelReportRuntimeException("Something went wrong while generateExcelReport in ExcelUtil class",
					e);
		}
	}

	/**
	 * @param workBook 
	 * @param sheet
	 * @param row
	 * @param String[][] reportData
	 * 
	 */
	private static void prepareReportData(Workbook workBook, Sheet sheet, Row row, String[][] reportData, ExcelFileData ed) {
		int rowCount = row.getRowNum();
		CellRangeAddress region = CellRangeAddress.valueOf("A1:J44");
		RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
		for (String[] data : reportData) {
			row = getRow(sheet, rowCount);
			int columnCount = 0;
			if (Objects.nonNull(row)) {
				for (String field : data) {
					Cell cell = row.createCell(columnCount++);
					CellStyle style = workBook.createCellStyle();
					style.setBorderLeft(BorderStyle.THIN);
					style.setBorderRight(BorderStyle.THIN);
					style.setBorderTop(BorderStyle.THIN);
					style.setBorderBottom(BorderStyle.THIN);
					//style.setWrapText(true);
					Font regularFont = workBook.createFont();
					//regularFont.setFontHeightInPoints((short) 30);
					//regularFont.setFontName("IMPACT");
					//regularFont.setItalic(true);
					//style.setFont(regularFont);
					if (StringUtils.isNotBlank(field)) {
						field = stripTags(field);
						XSSFRichTextString richText = new XSSFRichTextString(field);
						Font f = getFontStylingForHeader(getExcelWorkBook(), ed);
						f.setBold(true);
						//add proper index
						//richText.applyFont(0, 2, f);
						cell.setCellValue(richText);
						cell.setCellStyle(style);
					}
				}
			}
			++rowCount;
		}
		sheet.getLastRowNum();
		sheet.getRow(0).getLastCellNum();
	}

	private static String applyCustomStyles(String field, Cell cell, CellStyle style) {
		if (field.contains("<b>")) {
			field = stripTags(field);
			XSSFRichTextString richTextString = new XSSFRichTextString(field);
			Font customFont = getExcelWorkBook().createFont();
			customFont.setBold(true);
			customFont.setFontHeightInPoints((short)16);
			cell.setCellValue(richTextString);
			cell.setCellStyle(style);	
		} else if (field.contains("<w>")) {
			field = stripTags(field);
			XSSFRichTextString richTextString = new XSSFRichTextString(field);
			Font customFont = getExcelWorkBook().createFont();
			customFont.setColor(IndexedColors.WHITE.index);
			customFont.setFontHeightInPoints((short)9);
			cell.setCellValue(richTextString);
			cell.setCellStyle(style);
		}
		
		return field;
	}
	
	/**
	 * @param response
	 * @param workBook
	 * @param ExcelFileData excelReportData
	 * 
	 */
	private static void downloadExcel(SlingHttpServletResponse response, Workbook workBook,
			ExcelFileData excelReportData) throws IOException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workBook.write(out);
		byte[] docBytes = out.toByteArray();
		ByteArrayInputStream in = new ByteArrayInputStream(docBytes);
		response.setContentType("application/x-ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=" + excelReportData.getFileName() + ".xlsx");
		int read;
		OutputStream os = response.getOutputStream();
		while ((read = in.read(docBytes)) != -1) {
			os.write(docBytes, 0, read);
		}
		os.flush();
		os.close();
		closeWorkbook(workBook);

	}
	
	private static String stripTags(String string) {
	    if (StringUtils.isBlank(string)) {
	        return string;
	    }
	    Matcher m = REMOVE_TAGS.matcher(string);
	    return m.replaceAll("");
	}
	
	private XSSFRichTextString getStyledRichText(String text, Cell cell) {

		Font blankText = getBlankTextFont();
		if (text.contains("<blank>")) {

		}
		return null;
	}

	private Font getBlankTextFont() {
		Font blankTextFont = null;
		blankTextFont = getExcelWorkBook().createFont();
		// blankTextFont.set
		return blankTextFont;
	}

}