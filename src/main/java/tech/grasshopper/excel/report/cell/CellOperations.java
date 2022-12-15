package tech.grasshopper.excel.report.cell;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.Builder;

@Builder
public class CellOperations {

	private XSSFSheet sheet;

	public void writeStringValue(int row, int col, String cellValue) {

		CellReference cellRef = new CellReference(row, col);
		writeStringValue(cellRef, cellValue);
	}

	public void writeStringValue(String cellName, String cellValue) {

		CellReference cellRef = new CellReference(cellName);
		writeStringValue(cellRef, cellValue);
	}

	public void writeStringValue(CellReference cellRef, String cellValue) {

		Cell cell = fetchOrCreateCell(cellRef);
		cell.setCellValue(cellValue);
	}

	public void writeNumericValue(int row, int col, int cellValue) {

		CellReference cellRef = new CellReference(row, col);
		writeNumericValue(cellRef, cellValue);
	}

	public void writeNumericValue(String cellName, int cellValue) {

		CellReference cellRef = new CellReference(cellName);
		writeNumericValue(cellRef, cellValue);
	}

	public void writeNumericValue(CellReference cellRef, int cellValue) {

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
}
