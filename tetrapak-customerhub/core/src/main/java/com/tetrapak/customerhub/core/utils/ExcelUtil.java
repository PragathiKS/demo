package com.tetrapak.customerhub.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.customerhub.core.beans.excel.ExcelFileData;
import com.tetrapak.customerhub.core.exceptions.ExcelReportRuntimeException;

/**
 * @author dhitiwar
 * @author swalamba
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
	 * @param sheet
	 * @param rowNum
	 * @return Row
	 */
	private static Row getRow(Sheet sheet, int rowNum) {
		Row row = null;
		if (Objects.nonNull(sheet)) {
			row = sheet.createRow(rowNum++);
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
		List<CellRangeAddress> regionList = sheet.getMergedRegions();
		for (CellRangeAddress cellRangeAddress : regionList) {
			RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
			RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
			RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
			RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
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
		if (Objects.nonNull(excelReportData)) {
			try {
				XSSFWorkbook workBook = new XSSFWorkbook();
				Sheet sheet = getExcelSheet(workBook, excelReportData.getExcelSheetName());
				if (Objects.nonNull(excelReportData.getData())) {
					sheet.setDisplayGridlines(false);
					prepareReportData(workBook, sheet, excelReportData.getData());
					setMarginsToSheet(sheet);
					resizeCellToFitContent(sheet, 10);
				}
				downloadExcel(response, workBook, excelReportData);
			} catch (IOException e) {
				LOGGER.error("\nA run-time exception occured while generating excel report.");
				throw new ExcelReportRuntimeException(
						"Something went wrong while generateExcelReport in ExcelUtil class", e);
			}
		} else {
			LOGGER.warn("ExcelReportData is null!!");
		}

	}

	private static void setMarginsToSheet(Sheet sheet) {
		int rowCount = sheet.getLastRowNum();
		int columnCount = sheet.getRow(0).getLastCellNum();

		CellRangeAddress region = new CellRangeAddress(0, rowCount, 0, columnCount);

		RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);

	}

	/**
	 * @param workBook
	 * @param sheet
	 * @param row
	 * @param String[][] reportData
	 * 
	 */
	private static void prepareReportData(Workbook workBook, Sheet sheet, String[][] reportData) {
		int rowCount = 0;
		Map<String, CellStyle> cellStyles = getCellStyleMap(workBook);

		for (String[] data : reportData) {
			Row row = getRow(sheet, rowCount);
			int columnCount = 0;
			if (Objects.nonNull(row)) {
				for (String field : data) {

					Cell cell = row.createCell(columnCount++);

					if (StringUtils.isNotBlank(field)) {
						if (field.contains("\n")) {
							row.setHeightInPoints(3 * sheet.getDefaultRowHeightInPoints());
						}
						List<String> tags = getTagsFromField(field);
						XSSFRichTextString richText = applyCustomStyles(field, tags, workBook);

						if (tags.contains("<mergerow>")) {
							sheet.addMergedRegion(new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(),
									cell.getColumnIndex(), reportData[0].length));
						}

						if (tags.contains("<lightGreyBG>")) {
							cell.setCellStyle(cellStyles.get("lightGreyBackgroudStyle"));
						} else if (tags.contains("<aligncenter>")) {
							cell.setCellStyle(cellStyles.get("regularCenterStyle"));
						} else if (tags.contains("<darkGreyBG>")) {
							cell.setCellStyle(cellStyles.get("darkGreyBackgroudStyle"));
						} else {
							cell.setCellStyle(cellStyles.get("regularStyle"));
						}

						cell.setCellValue(richText);
					} else {
						cell.setCellStyle(cellStyles.get("emptyCellStyle"));
					}

				}
			}
			++rowCount;
		}
	}

	/**
	 * get list of CellStyles in form a map
	 * 
	 * @param workBook
	 * @return getCellStyleMap
	 */
	private static Map<String, CellStyle> getCellStyleMap(Workbook workBook) {
		Map<String, CellStyle> cellStyles = new HashMap<String, CellStyle>();

		CellStyle borderStyle = workBook.createCellStyle();
		borderStyle.setBorderLeft(BorderStyle.THIN);
		borderStyle.setBorderRight(BorderStyle.THIN);
		borderStyle.setBorderTop(BorderStyle.THIN);
		borderStyle.setBorderBottom(BorderStyle.THIN);
		borderStyle.setWrapText(true);
		cellStyles.put("regularStyle", borderStyle);

		CellStyle emptyCellStyle = workBook.createCellStyle();
		emptyCellStyle.setBorderLeft(BorderStyle.NONE);
		emptyCellStyle.setBorderRight(BorderStyle.NONE);
		emptyCellStyle.setBorderTop(BorderStyle.NONE);
		emptyCellStyle.setBorderBottom(BorderStyle.NONE);
		emptyCellStyle.setWrapText(true);
		cellStyles.put("emptyCellStyle", emptyCellStyle);

		CellStyle regularCenterStyle = workBook.createCellStyle();
		regularCenterStyle.setBorderLeft(BorderStyle.THIN);
		regularCenterStyle.setBorderRight(BorderStyle.THIN);
		regularCenterStyle.setBorderTop(BorderStyle.THIN);
		regularCenterStyle.setBorderBottom(BorderStyle.THIN);
		regularCenterStyle.setAlignment(CellStyle.ALIGN_CENTER);
		regularCenterStyle.setWrapText(true);
		cellStyles.put("regularCenterStyle", regularCenterStyle);

		CellStyle lightGreyBackgroudStyle = workBook.createCellStyle();
		lightGreyBackgroudStyle.setBorderLeft(BorderStyle.THIN);
		lightGreyBackgroudStyle.setBorderRight(BorderStyle.THIN);
		lightGreyBackgroudStyle.setBorderTop(BorderStyle.THIN);
		lightGreyBackgroudStyle.setBorderBottom(BorderStyle.THIN);
		lightGreyBackgroudStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		lightGreyBackgroudStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		lightGreyBackgroudStyle.setAlignment(CellStyle.ALIGN_CENTER);
		lightGreyBackgroudStyle.setWrapText(true);
		cellStyles.put("lightGreyBackgroudStyle", lightGreyBackgroudStyle);

		CellStyle darkGreyBackgroudStyle = workBook.createCellStyle();
		Font whiteFont = workBook.createFont();
		whiteFont.setColor(IndexedColors.WHITE.index);
		darkGreyBackgroudStyle.setFont(whiteFont);
		darkGreyBackgroudStyle.setBorderLeft(BorderStyle.THIN);
		darkGreyBackgroudStyle.setBorderRight(BorderStyle.THIN);
		darkGreyBackgroudStyle.setBorderTop(BorderStyle.THIN);
		darkGreyBackgroudStyle.setBorderBottom(BorderStyle.THIN);
		darkGreyBackgroudStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
		darkGreyBackgroudStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		darkGreyBackgroudStyle.setAlignment(CellStyle.ALIGN_CENTER);
		darkGreyBackgroudStyle.setWrapText(true);
		cellStyles.put("darkGreyBackgroudStyle", darkGreyBackgroudStyle);

		return cellStyles;
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

	/**
	 * Remove all the custom tags
	 * 
	 * @param string
	 * @return
	 */
	private static String stripTags(String string) {
		if (StringUtils.isBlank(string)) {
			return string;
		}
		Matcher m = REMOVE_TAGS.matcher(string);
		return m.replaceAll("");
	}

	private static XSSFRichTextString applyCustomStyles(String field, List<String> tags, Workbook workBook) {
		field = stripTags(field);
		XSSFRichTextString richTextString = new XSSFRichTextString(field);
		if (!tags.isEmpty()) {
			Font customFont = workBook.createFont();
			if (field.contains("<whiteFontColor>")) {
				customFont.setColor(IndexedColors.BLUE.index);
			}

			if (tags.contains("<bold>")) {
				customFont.setBold(true);
			}

			customFont.setFontHeightInPoints((short) 9);
			richTextString.applyFont(customFont);

			if (tags.contains("<halfBold>")) {
				customFont.setBold(true);
				int index = 1;
				if (field.contains(":")) {
					index = field.indexOf(":");
				} else {
					index = field.length() - 1;
				}
				richTextString.applyFont(0, index, customFont);
			}
		}

		return richTextString;
	}

	/**
	 * 
	 * get the tags set in the field and return as a list
	 * 
	 * @param field
	 * @return
	 */
	private static List<String> getTagsFromField(String field) {
		List<String> tagList = new ArrayList<>();
		if (field.contains("<bold>")) {
			tagList.add("<bold>");
		}
		if (field.contains("<whiteFontColor>")) {
			tagList.add("<whiteFontColor>");
		}
		if (field.contains("<lightGreyBG>")) {
			tagList.add("<lightGreyBG>");
		}
		if (field.contains("<halfBold>")) {
			tagList.add("<halfBold>");
		}
		if (field.contains("<darkGreyBG>")) {
			tagList.add("<darkGreyBG>");
		}
		if (field.contains("<mergerow>")) {
			tagList.add("<mergerow>");
		}
		if (field.contains("<aligncenter>")) {
			tagList.add("<aligncenter>");
		}
		return tagList;
	}

}