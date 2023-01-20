package tech.grasshopper.excel.report.sheets.exceptions;

import org.apache.poi.ss.util.CellReference;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.sheets.Sheet;
import tech.grasshopper.excel.report.table.ExceptionsTable;
import tech.grasshopper.extent.data.pojo.Executable;
import tech.grasshopper.extent.data.pojo.Feature;
import tech.grasshopper.extent.data.pojo.Scenario;

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

		for (Feature feature : reportData.getExceptionData()) {

			System.out.println("Feature - " + feature.getName() + " - " + feature.getTotalScenarios() + " - "
					+ feature.getTotalSteps());

			for (Scenario scenario : feature.getScenarios()) {

				System.out.println("Scenario - " + scenario.getName() + " - " + scenario.getTotalSteps());

				for (Executable exec : scenario.getStackTraceExecutables()) {

					System.out.println("Executable - " + exec.getName() + " - " + exec.getStatus() + " - "
							+ exec.getExecutableType());
					System.out.println(exec.getErrorMessage());
				}
			}
		}

		ExceptionsTable.builder().exceptionsData(reportData.getExceptionData()).sheet(sheet)
				.startCell(cellRef.formatAsString()).build().writeTableValues();
	}
}
