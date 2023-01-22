package tech.grasshopper.excel.report.sheets.exceptions;

import org.apache.poi.ss.util.CellReference;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.sheets.Sheet;
import tech.grasshopper.excel.report.table.ExceptionsTable;

@SuperBuilder
public class ExceptionsSheet extends Sheet {

	private static final String EXCEPTIONS_TABLE_FEATURE_NAME_CELL = "B3";

	private static final int FREEZE_PANE_ROW = 2;

	@Override
	public void updateSheet() {

		sheet = workbook.getSheet(EXCEPTIONS_SHEET);

		if (reportData.getExceptionData().isEmpty()) {
			deleteSheet(EXCEPTIONS_SHEET);
			return;
		}

		updateExceptionsTableData(new CellReference(EXCEPTIONS_TABLE_FEATURE_NAME_CELL));

		sheet.createFreezePane(0, FREEZE_PANE_ROW);
	}

	private void updateExceptionsTableData(CellReference cellRef) {

		ExceptionsTable.builder().exceptionsData(reportData.getExceptionData()).sheet(sheet)
				.startCell(cellRef.formatAsString()).build().writeTableValues();
	}
}
