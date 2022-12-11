package tech.grasshopper.excel.report.workbook;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.sheets.dashboard.DashboardAllSheet;

@SuperBuilder
public class ExecutionAndTagAndFailSkipDataReport extends ReportWorkbook {

	protected String templateReportLocation() {
		return null;
	}

	@Override
	protected void updateSheets() {

		DashboardAllSheet.builder().reportData(reportData).xssfWorkbook(xssfWorkbook).build().updateSheet();
	}
}
