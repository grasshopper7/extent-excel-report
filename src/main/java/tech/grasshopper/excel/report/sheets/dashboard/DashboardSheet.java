package tech.grasshopper.excel.report.sheets.dashboard;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.extent.data.ReportData;

@Data
@SuperBuilder
public abstract class DashboardSheet {

	protected static final String DASHBOARD_ALL_SHEET = "";

	protected static final String DASHBOARD_BASIC_SHEET = "";

	protected static final String DASHBOARD_BASIC_TAG_SHEET = "";

	protected static final String DASHBOARD_BASIC_FAIL_SKIP__SHEET = "";

	protected static final String DASHBOARD_DATA_SHEET = "";

	protected ReportData reportData;

	protected XSSFWorkbook xssfWorkbook;

	public abstract void createDashboard();
}
