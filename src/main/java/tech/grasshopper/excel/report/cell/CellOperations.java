package tech.grasshopper.excel.report.cell;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.Builder;
import tech.grasshopper.excel.report.util.TriConsumer;
import tech.grasshopper.extent.data.pojo.Status;

@Builder
public class CellOperations {

	private XSSFSheet sheet;

	public static final TriConsumer<CellOperations, CellReference, String> printString = CellOperations::writeStringValue;
	public static final TriConsumer<CellOperations, CellReference, String> printBoldString = CellOperations::writeBoldStringValue;
	public static final TriConsumer<CellOperations, CellReference, String> printLong = CellOperations::writePositiveNumericValue;
	public static final TriConsumer<CellOperations, CellReference, String> printStatus = CellOperations::writeStatus;

	public void writeStringValue(String cellName, String cellValue) {

		CellReference cellRef = new CellReference(cellName);
		writeStringValue(cellRef, cellValue);
	}

	public void writeStringValue(CellReference cellRef, String cellValue) {

		Cell cell = fetchOrCreateCell(cellRef);
		cell.setCellValue(cellValue);
	}

	public void writeBoldStringValue(CellReference cellRef, String cellValue) {

		Cell cell = fetchOrCreateCell(cellRef);

		CellStyle style = cell.getCellStyle();
		XSSFFont font = sheet.getWorkbook().createFont();
		font.setBold(true);
		style.setFont(font);

		cell.setCellValue(cellValue);
	}

	public void writeStatus(CellReference cellRef, String status) {

		writeStatus(cellRef, Status.valueOf(status));
	}

	public void writeStatus(CellReference cellRef, Status status) {

		Cell cell = fetchOrCreateCell(cellRef);

		CellStyle style = cell.getCellStyle();
		XSSFFont font = sheet.getWorkbook().createFont();
		font.setColor(new XSSFColor(Status.getStatusColor(status), null));
		font.setBold(true);
		style.setFont(font);

		cell.setCellValue(status.toString());
	}

	public void writeStringValueWithStatusColor(CellReference cellRef, String cellValue, Status status) {

		Cell cell = fetchOrCreateCell(cellRef);

		CellStyle style = cell.getCellStyle();
		XSSFFont font = sheet.getWorkbook().createFont();
		font.setColor(new XSSFColor(Status.getStatusColor(status), null));
		style.setFont(font);

		cell.setCellValue(cellValue);
	}

	public void writeNumericValue(CellReference cellRef, String cellValue) {

		writeNumericValue(cellRef, Long.parseLong(cellValue));
	}

	public void writePositiveNumericValue(CellReference cellRef, String cellValue) {

		Long value = Long.parseLong(cellValue);
		writePositiveNumericValue(cellRef, value);
	}

	public void writePositiveNumericValue(String cellName, Long cellValue) {

		CellReference cellRef = new CellReference(cellName);
		writePositiveNumericValue(cellRef, cellValue);
	}

	public void writePositiveNumericValue(CellReference cellRef, Long cellValue) {

		Cell cell = fetchOrCreateCell(cellRef);

		if (cellValue > 0)
			cell.setCellValue(cellValue);
		else
			cell.setBlank();
	}

	public void writeNumericValue(String cellName, Long cellValue) {

		CellReference cellRef = new CellReference(cellName);
		writeNumericValue(cellRef, cellValue);
	}

	public void writeNumericValue(CellReference cellRef, Long cellValue) {

		Cell cell = fetchOrCreateCell(cellRef);
		cell.setCellValue(cellValue);
	}

	private Cell fetchOrCreateCell(CellReference cellRef) {

		Row row = sheet.getRow(cellRef.getRow());
		if (row == null)
			row = sheet.createRow(cellRef.getRow());

		Cell cell = row.getCell(cellRef.getCol());
		if (cell == null)
			cell = row.createCell(cellRef.getCol());
		return cell;
	}

	public void mergeRows(int startRow, int rowsToMerge, int startColumn, int colsToMerge) {

		if (rowsToMerge == 1 && colsToMerge == 1)
			return;

		sheet.addMergedRegion(
				new CellRangeAddress(startRow, startRow + rowsToMerge - 1, startColumn, startColumn + colsToMerge - 1));
	}

	public CellStyle createCellStyle() {

		CellStyle style = sheet.getWorkbook().createCellStyle();
		style.setVerticalAlignment(VerticalAlignment.TOP);
		style.setBorderTop(BorderStyle.HAIR);
		style.setBorderRight(BorderStyle.HAIR);
		style.setBorderBottom(BorderStyle.HAIR);
		style.setBorderLeft(BorderStyle.HAIR);
		style.setWrapText(true);

		return style;
	}

	public void createCellsWithStyleInRange(int startRow, int endRow, int startCol, int endCol) {

		for (int i = startRow; i < endRow; i++) {
			for (int j = startCol; j < endCol; j++) {
				CellReference cellRef = new CellReference(i, j);
				Cell cell = fetchOrCreateCell(cellRef);
				CellStyle style = createCellStyle();
				cell.setCellStyle(style);
			}
		}
	}
}
