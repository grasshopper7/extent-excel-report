package tech.grasshopper.excel.report.cell;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.Builder;

@Builder
public class CellOperations {

	private XSSFSheet sheet;

	public void writeStringValue(String cellName, String cellValue) {

		CellReference cellRef = new CellReference(cellName);
		writeStringValue(cellRef, cellValue);
	}

	public void writeStringValue(CellReference cellRef, String cellValue) {

		Cell cell = fetchOrCreateCell(cellRef);
		cell.setCellValue(cellValue);
	}

	public void writeNumericValue(CellReference cellRef, String cellValue) {

		writeNumericValue(cellRef, Long.parseLong(cellValue));
	}

	public void writePositiveNumericValue(CellReference cellRef, String cellValue) {

		Long value = Long.parseLong(cellValue);
		Cell cell = fetchOrCreateCell(cellRef);

		if (value > 0)
			cell.setCellValue(value);
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
}
