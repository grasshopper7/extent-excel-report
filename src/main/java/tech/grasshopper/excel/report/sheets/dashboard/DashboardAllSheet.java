package tech.grasshopper.excel.report.sheets.dashboard;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.sheets.dashboard.components.BasicDBComponent;
import tech.grasshopper.excel.report.sheets.dashboard.components.FailSkipDBComponent;
import tech.grasshopper.excel.report.sheets.dashboard.components.TagDBComponent;

@SuperBuilder
public class DashboardAllSheet extends DashboardSheet {

	@Override
	public void updateSheet() {

		XSSFSheet dbSheet = xssfWorkbook.getSheet(DASHBOARD_ALL_SHEET);

		XSSFSheet dbDataSheet = xssfWorkbook.getSheet(DASHBOARD_DATA_SHEET);

		BasicDBComponent.builder().dbSheet(dbSheet).dbDataSheet(dbDataSheet).reportData(reportData).build()
				.createComponent();

		TagDBComponent.builder().dbSheet(dbSheet).dbDataSheet(dbDataSheet).reportData(reportData).tagBarChartIndex(3)
				.build().createComponent();

		FailSkipDBComponent.builder().dbSheet(dbSheet).dbDataSheet(dbDataSheet).reportData(reportData)
				.featureBarChartIndex(4).scenarioBarChartIndex(5).failSkipTableStartCell("B51").build().createComponent();
	}
}