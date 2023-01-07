package tech.grasshopper.excel.report.workbook;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.sheets.dashboard.DashboardBasicSheet;

@SuperBuilder
public class ExecutionDataReport extends ReportWorkbook {

	protected String templateReportLocation() {
		//return "src/main/resources/templates/report template - Basic DB.xlsx";
		return "/templates/report template - Basic DB.xlsx";
	}

	@Override
	protected void updateSheets() {

		// Dashboard sheet
		DashboardBasicSheet.builder().reportData(reportData).workbook(xssfWorkbook).build().updateSheet();

		super.updateSheets();
	}
}
