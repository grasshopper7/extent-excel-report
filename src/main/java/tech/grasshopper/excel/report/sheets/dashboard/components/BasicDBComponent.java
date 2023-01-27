package tech.grasshopper.excel.report.sheets.dashboard.components;

import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.CURRENT_DATE_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.DURATION_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.END_DATE_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.FEATURES_FAILED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.FEATURES_PASSED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.FEATURES_PASS_PERCENT_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.FEATURES_SKIPPED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.FEATURES_TOTAL_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.SCENARIOS_FAILED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.SCENARIOS_PASSED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.SCENARIOS_PASS_PERCENT_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.SCENARIOS_SKIPPED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.SCENARIOS_TOTAL_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.START_DATE_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.STEPS_FAILED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.STEPS_PASSED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.STEPS_PASS_PERCENT_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.STEPS_SKIPPED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.STEPS_TOTAL_CELL;

import java.time.LocalDateTime;

import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.cell.CellOperations;
import tech.grasshopper.excel.report.util.DateUtil;
import tech.grasshopper.extent.data.SheetData.CountData;
import tech.grasshopper.extent.data.SheetData.TimingData;

@SuperBuilder
public class BasicDBComponent extends DBComponent {

	private CellOperations dbDataCellOperations;

	@Default
	private int featurePieChartIndex = 0;

	@Default
	private int scenarioPieChartIndex = 1;

	@Default
	private int stepPieChartIndex = 2;

	@Override
	public void createComponent() {

		dbDataCellOperations = CellOperations.builder().sheet(dbDataSheet).build();

		// update data in dbdata sheet
		updateBasicValues();
		updateFeatureCounts();
		updateScenarioCounts();
		updateStepCounts();
	}

	private void updateBasicValues() {

		TimingData timingData = reportData.getDashboardData().getTimingData();

		// To be done later
		// dbDataCellOperations.writeValue(TITLE_CELL, null);

		// Not used just added for FUN
		dbDataCellOperations.writeStringValue(CURRENT_DATE_CELL, DateUtil.formatDateTime(LocalDateTime.now()));

		dbDataCellOperations.writeStringValue(START_DATE_CELL, DateUtil.formatDateTime(timingData.getStartTime()));
		dbDataCellOperations.writeStringValue(END_DATE_CELL, DateUtil.formatDateTime(timingData.getEndTime()));
		dbDataCellOperations.writeStringValue(DURATION_CELL, DateUtil.durationValue(timingData.getDuration()));
	}

	private void updateFeatureCounts() {

		CountData featureCounts = reportData.getDashboardData().getFeatureCounts();
		dbDataCellOperations.writePositiveNumericValue(FEATURES_PASSED_CELL, featureCounts.getPassed());
		dbDataCellOperations.writePositiveNumericValue(FEATURES_FAILED_CELL, featureCounts.getFailed());
		dbDataCellOperations.writePositiveNumericValue(FEATURES_SKIPPED_CELL, featureCounts.getSkipped());
		dbDataCellOperations.writePositiveNumericValue(FEATURES_TOTAL_CELL, featureCounts.getTotal());
		dbDataCellOperations.writeStringValue(FEATURES_PASS_PERCENT_CELL, featureCounts.getPassPercent());
	}

	private void updateScenarioCounts() {

		CountData scenarioCounts = reportData.getDashboardData().getScenarioCounts();
		dbDataCellOperations.writePositiveNumericValue(SCENARIOS_PASSED_CELL, scenarioCounts.getPassed());
		dbDataCellOperations.writePositiveNumericValue(SCENARIOS_FAILED_CELL, scenarioCounts.getFailed());
		dbDataCellOperations.writePositiveNumericValue(SCENARIOS_SKIPPED_CELL, scenarioCounts.getSkipped());
		dbDataCellOperations.writePositiveNumericValue(SCENARIOS_TOTAL_CELL, scenarioCounts.getTotal());
		dbDataCellOperations.writeStringValue(SCENARIOS_PASS_PERCENT_CELL, scenarioCounts.getPassPercent());
	}

	private void updateStepCounts() {

		CountData stepCounts = reportData.getDashboardData().getStepCounts();
		dbDataCellOperations.writePositiveNumericValue(STEPS_PASSED_CELL, stepCounts.getPassed());
		dbDataCellOperations.writePositiveNumericValue(STEPS_FAILED_CELL, stepCounts.getFailed());
		dbDataCellOperations.writePositiveNumericValue(STEPS_SKIPPED_CELL, stepCounts.getSkipped());
		dbDataCellOperations.writePositiveNumericValue(STEPS_TOTAL_CELL, stepCounts.getTotal());
		dbDataCellOperations.writeStringValue(STEPS_PASS_PERCENT_CELL, stepCounts.getPassPercent());
	}
}
