package tech.grasshopper.excel.report.workbook;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.sheets.dashboard.DashboardAllSheet;

@SuperBuilder
public class ExecutionAndTagAndFailSkipDataReport extends ReportWorkbook {

	protected String templateReportLocation() {
		return "src/main/resources/templates/report template - All DB.xlsx";
	}

	@Override
	protected void updateSheets() {

		// Dashboard sheet
		DashboardAllSheet.builder().reportData(reportData).xssfWorkbook(xssfWorkbook).build().updateSheet();

		super.updateSheets();
	}
}
