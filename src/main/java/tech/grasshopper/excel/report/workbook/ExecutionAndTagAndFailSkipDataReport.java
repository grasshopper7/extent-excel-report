package tech.grasshopper.excel.report.workbook;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.sheets.dashboard.DashboardAllSheet;
import tech.grasshopper.excel.report.sheets.features.FeaturesSheet;
import tech.grasshopper.excel.report.sheets.scenarios.ScenariosSheet;
import tech.grasshopper.excel.report.sheets.tags.TagsSheet;

@SuperBuilder
public class ExecutionAndTagAndFailSkipDataReport extends ReportWorkbook {

	protected String templateReportLocation() {
		return "src/main/resources/report template.xlsx";
	}

	@Override
	protected void updateSheets() {

		// Dashboard sheet
		DashboardAllSheet.builder().reportData(reportData).xssfWorkbook(xssfWorkbook).build().updateSheet();

		// Scenarios sheet
		ScenariosSheet.builder().reportData(reportData).xssfWorkbook(xssfWorkbook).build().updateSheet();

		// Tags sheet
		TagsSheet.builder().reportData(reportData).xssfWorkbook(xssfWorkbook).build().updateSheet();

		// Features sheet
		FeaturesSheet.builder().reportData(reportData).xssfWorkbook(xssfWorkbook).build().updateSheet();

		// Execution sheet (?)
	}
}
