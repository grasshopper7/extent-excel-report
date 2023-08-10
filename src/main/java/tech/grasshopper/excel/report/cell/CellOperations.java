package tech.grasshopper.excel.report.cell;

import static tech.grasshopper.excel.report.cell.CellStyles.FAIL_TEXT_BOLD_CELL_STYLE;
import static tech.grasshopper.excel.report.cell.CellStyles.PASS_TEXT_BOLD_CELL_STYLE;
import static tech.grasshopper.excel.report.cell.CellStyles.SKIP_TEXT_BOLD_CELL_STYLE;
import static tech.grasshopper.excel.report.cell.CellStyles.getCellStyle;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
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

	public void writeValue(String cellName, String cellValue, String style, ValueOption option) {

		CellReference cellReference = new CellReference(cellName);
		writeValue(cellReference, cellValue, style, option);
	}

	public void writeValue(CellReference cellReference, String cellValue, String style, ValueOption option) {
		Cell cell = fetchOrCreateCell(cellReference);
		CellStyle cellStyle = getCellStyle(sheet, style);

		if (option == ValueOption.POSITIVE_NUMBER) {
			Long value = Long.parseLong(cellValue);
			cell.setCellStyle(cellStyle);

			if (value < 1) {
				cell.setBlank();
			} else {
				cell.setCellValue(value);
			}

			return;
		} else if (option == ValueOption.STATUS_TEXT) {
			Status status = Status.valueOf(cellValue);

			if (status == Status.PASSED)
				cellStyle = getCellStyle(sheet, PASS_TEXT_BOLD_CELL_STYLE);
			else if (status == Status.FAILED)
				cellStyle = getCellStyle(sheet, FAIL_TEXT_BOLD_CELL_STYLE);
			else
				cellStyle = getCellStyle(sheet, SKIP_TEXT_BOLD_CELL_STYLE);
		}

		cell.setCellStyle(cellStyle);
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

	public void createCellsWithStyleInRange(int startRow, int endRow, int startCol, int endCol) {

		for (int i = startRow; i < endRow; i++) {
			for (int j = startCol; j < endCol; j++) {
				CellReference cellRef = new CellReference(i, j);
				Cell cell = fetchOrCreateCell(cellRef);
				cell.setCellStyle(CellStyles.getCellStyle(sheet, CellStyles.EMPTY_CELL_STYLE));
			}
		}
	}
}
