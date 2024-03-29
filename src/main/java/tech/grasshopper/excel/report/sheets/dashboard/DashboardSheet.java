package tech.grasshopper.excel.report.sheets.dashboard;

import org.apache.poi.ss.usermodel.SheetVisibility;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.sheets.Sheet;

@SuperBuilder
public abstract class DashboardSheet extends Sheet {

	public static final String TITLE_CELL = "B2";
	public static final String CURRENT_DATE_CELL = "B3";
	public static final String START_DATE_CELL = "B4";
	public static final String END_DATE_CELL = "B5";
	public static final String DURATION_CELL = "B6";
	public static final String FEATURES_PASSED_CELL = "D2";
	public static final String FEATURES_FAILED_CELL = "D3";
	public static final String FEATURES_SKIPPED_CELL = "D4";
	public static final String FEATURES_TOTAL_CELL = "D5";
	public static final String FEATURES_PASS_PERCENT_CELL = "D6";
	public static final String SCENARIOS_PASSED_CELL = "F2";
	public static final String SCENARIOS_FAILED_CELL = "F3";
	public static final String SCENARIOS_SKIPPED_CELL = "F4";
	public static final String SCENARIOS_TOTAL_CELL = "F5";
	public static final String SCENARIOS_PASS_PERCENT_CELL = "F6";
	public static final String STEPS_PASSED_CELL = "H2";
	public static final String STEPS_FAILED_CELL = "H3";
	public static final String STEPS_SKIPPED_CELL = "H4";
	public static final String STEPS_TOTAL_CELL = "H5";
	public static final String STEPS_PASS_PERCENT_CELL = "H6";

	public static final String TAG_TABLE_NAME_CELL = "A20";
	public static final String TAG_TABLE_SCENARIO_PASSED_CELL = "B20";
	public static final String TAG_TABLE_SCENARIO_FAILED_CELL = "C20";
	public static final String TAG_TABLE_SCENARIO_SKIPPED_CELL = "D20";

	public static final String FEATURE_FAIL_SKIP_TABLE_NAME_CELL = "H20";
	public static final String FEATURE_FAIL_SKIP_TABLE_STATUS_CELL = "I20";
	public static final String FEATURE_FAIL_SKIP_TABLE_SCENARIO_PASSED_CELL = "J20";
	public static final String FEATURE_FAIL_SKIP_TABLE_SCENARIO_FAILED_CELL = "K20";
	public static final String FEATURE_FAIL_SKIP_TABLE_SCENARIO_SKIPPED_CELL = "L20";

	public static final String SCENARIO_FAIL_SKIP_TABLE_NAME_CELL = "P20";
	public static final String SCENARIO_FAIL_SKIP_TABLE_STATUS_CELL = "Q20";
	public static final String SCENARIO_FAIL_SKIP_TABLE_STEP_PASSED_CELL = "R20";
	public static final String SCENARIO_FAIL_SKIP_TABLE_STEP_FAILED_CELL = "S20";
	public static final String SCENARIO_FAIL_SKIP_TABLE_STEP_SKIPPED_CELL = "T20";

	public static final String FEATURES_CHART = "Features";
	public static final String SCENARIOS_CHART = "Scenarios";
	public static final String STEPS_CHART = "Steps";
	public static final String TAGS_FAIL_SKIP_SCENARIO_CHART = "Tags with Failed & Skipped Scenarios";
	public static final String FEATURES_FAIL_SKIP_SCENARIO_CHART = "Features with Failed & Skipped Scenarios";
	public static final String SCENARIOS_FAIL_SKIP_STEP_CHART = "Scenarios with Failed & Skipped Steps";

	protected XSSFSheet dbDataSheet;

	@Override
	public void updateSheet() {

		sheet = workbook.getSheet(DASHBOARD_SHEET);

		dbDataSheet = workbook.getSheet(DASHBOARD_DATA_SHEET);

		workbook.setSheetVisibility(workbook.getSheetIndex(dbDataSheet), SheetVisibility.VERY_HIDDEN);
	}
}
