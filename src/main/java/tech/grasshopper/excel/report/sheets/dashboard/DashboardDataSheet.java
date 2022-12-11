package tech.grasshopper.excel.report.sheets.dashboard;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.Builder;
import tech.grasshopper.excel.report.sheets.Sheet;
import tech.grasshopper.extent.data.ReportData;

@Builder
public class DashboardDataSheet implements Sheet {

	private ReportData reportData;

	private XSSFSheet sheet;

	@Override
	public void updateSheet() {

	}
}
