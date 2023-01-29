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
import tech.grasshopper.extent.data.pojo.Status;

@Builder
public class CellOperations {

	private XSSFSheet sheet;

	public void writePlainValue(String cellName, String cellValue) {

		CellReference cellReference = new CellReference(cellName);
		Cell cell = fetchOrCreateCell(cellReference);
		cell.setCellValue(cellValue);
	}

	public void writePlainPositiveNumberValue(String cellName, Long cellValue) {

		CellReference cellReference = new CellReference(cellName);
		Cell cell = fetchOrCreateCell(cellReference);
		if (cellValue > 0)
			cell.setCellValue(cellValue);
		else
			cell.setBlank();
	}

	public void writeValue(String cellName, String cellValue, CellValueOptions options) {

		CellReference cellReference = new CellReference(cellName);
		writeValue(cellReference, cellValue, options);
	}

	public void writeValue(CellReference cellReference, String cellValue, CellValueOptions options) {

		Cell cell = fetchOrCreateCell(cellReference);

		CellStyle style = cell.getCellStyle();
		XSSFFont font = sheet.getWorkbook().createFont();

		boolean numberPresent = options.isNumber() || options.isPositiveNumber();

		if (options.isBold())
			font.setBold(true);

		if (options.isItalic())
			font.setItalic(true);

		if (options.getTextColor() != null)
			font.setColor(new XSSFColor(options.getTextColor(), null));

		if (options.getHorizAlign() != null)
			style.setAlignment(options.getHorizAlign());

		if (options.getVertAlign() != null)
			style.setVerticalAlignment(options.getVertAlign());

		if (options.isStatus())
			font.setColor(new XSSFColor(Status.getStatusColor(Status.valueOf(cellValue)), null));

		style.setFont(font);

		if (numberPresent) {
			Long value = Long.parseLong(cellValue);

			if (options.isPositiveNumber() && value < 1)
				cell.setBlank();
			else
				cell.setCellValue(value);
		} else
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
