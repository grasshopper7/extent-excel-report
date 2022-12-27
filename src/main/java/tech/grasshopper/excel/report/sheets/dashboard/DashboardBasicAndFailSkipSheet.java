package tech.grasshopper.excel.report.sheets.dashboard;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.sheets.dashboard.components.BasicDBComponent;
import tech.grasshopper.excel.report.sheets.dashboard.components.FeatureScenarioFailSkipDBComponent;

@SuperBuilder
public class DashboardBasicAndFailSkipSheet extends DashboardSheet {

	public static final String FEATURE_SCENARIO_FAIL_SKIP_TABLE_CELL = "B39";

	@Override
	public void updateSheet() {

		XSSFSheet dbSheet = xssfWorkbook.getSheet(DASHBOARD_SHEET);

		XSSFSheet dbDataSheet = xssfWorkbook.getSheet(DASHBOARD_DATA_SHEET);

		BasicDBComponent.builder().dbSheet(dbSheet).dbDataSheet(dbDataSheet).reportData(reportData).build()
				.createComponent();

		FeatureScenarioFailSkipDBComponent.builder().dbSheet(dbSheet).dbDataSheet(dbDataSheet).reportData(reportData)
				.featureBarChartIndex(3).scenarioBarChartIndex(4)
				.failSkipTableStartCell(FEATURE_SCENARIO_FAIL_SKIP_TABLE_CELL).build().createComponent();
	}
}
