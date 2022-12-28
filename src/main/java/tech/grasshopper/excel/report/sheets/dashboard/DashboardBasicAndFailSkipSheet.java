package tech.grasshopper.excel.report.sheets.dashboard;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.sheets.dashboard.components.BasicDBComponent;
import tech.grasshopper.excel.report.sheets.dashboard.components.FeatureScenarioFailSkipDBComponent;

@SuperBuilder
public class DashboardBasicAndFailSkipSheet extends DashboardSheet {

	private static final String FEATURE_SCENARIO_FAIL_SKIP_TABLE_CELL = "B39";

	@Override
	public void updateSheet() {

		super.updateSheet();

		BasicDBComponent.builder().dbSheet(sheet).dbDataSheet(dbDataSheet).reportData(reportData).build()
				.createComponent();

		FeatureScenarioFailSkipDBComponent.builder().dbSheet(sheet).dbDataSheet(dbDataSheet).reportData(reportData)
				.featureBarChartIndex(3).scenarioBarChartIndex(4)
				.failSkipTableStartCell(FEATURE_SCENARIO_FAIL_SKIP_TABLE_CELL).build().createComponent();
	}
}
