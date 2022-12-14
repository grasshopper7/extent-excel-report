package tech.grasshopper.excel.report.workbook;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.sheets.dashboard.DashboardBasicAndFailSkipSheet;

@SuperBuilder
public class ExecutionAndFailSkipDataReport extends ReportWorkbook {
	@Override

	protected String templateReportLocation() {
		//return "src/main/resources/templates/report template - Fail Skip DB.xlsx";
		return "/templates/report template - Fail Skip DB.xlsx";
	}

	@Override
	protected void updateSheets() {

		// Dashboard sheet
		DashboardBasicAndFailSkipSheet.builder().reportData(reportData).workbook(xssfWorkbook).build().updateSheet();

		super.updateSheets();
	}
}
