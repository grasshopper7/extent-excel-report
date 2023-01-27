package tech.grasshopper.excel.report.sheets.dashboard;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.sheets.dashboard.components.BasicDBComponent;

@SuperBuilder
public class DashboardBasicSheet extends DashboardSheet {

	@Override
	public void updateSheet() {

		super.updateSheet();

		BasicDBComponent.builder().dbSheet(sheet).dbDataSheet(dbDataSheet).reportData(reportData).build()
				.createComponent();

		lockSheet();
	}
}
