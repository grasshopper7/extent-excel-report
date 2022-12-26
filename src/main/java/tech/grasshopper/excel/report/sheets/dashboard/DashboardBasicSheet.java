package tech.grasshopper.excel.report.sheets.dashboard;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.sheets.dashboard.components.BasicDBComponent;

@SuperBuilder
public class DashboardBasicSheet extends DashboardSheet {

	@Override
	public void updateSheet() {

		XSSFSheet dbSheet = xssfWorkbook.getSheet(DASHBOARD_ALL_SHEET);

		XSSFSheet dbDataSheet = xssfWorkbook.getSheet(DASHBOARD_DATA_SHEET);

		BasicDBComponent.builder().dbSheet(dbSheet).dbDataSheet(dbDataSheet).reportData(reportData).build()
				.createComponent();
	}
}
