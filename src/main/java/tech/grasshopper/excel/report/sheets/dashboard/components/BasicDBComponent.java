package tech.grasshopper.excel.report.sheets.dashboard.components;

import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.CURRENT_DATE_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.DURATION_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.END_DATE_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.FEATURES_FAILED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.FEATURES_PASSED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.FEATURES_SKIPPED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.FEATURES_TOTAL_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.SCENARIOS_FAILED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.SCENARIOS_PASSED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.SCENARIOS_SKIPPED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.SCENARIOS_TOTAL_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.START_DATE_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.STEPS_FAILED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.STEPS_PASSED_CELL;
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
		dbDataCellOperations.writeNumericValue(FEATURES_PASSED_CELL, featureCounts.getPassed());
		dbDataCellOperations.writeNumericValue(FEATURES_FAILED_CELL, featureCounts.getFailed());
		dbDataCellOperations.writeNumericValue(FEATURES_SKIPPED_CELL, featureCounts.getSkipped());
		dbDataCellOperations.writeNumericValue(FEATURES_TOTAL_CELL, featureCounts.getTotal());
	}

	private void updateScenarioCounts() {

		CountData scenarioCounts = reportData.getDashboardData().getScenarioCounts();
		dbDataCellOperations.writeNumericValue(SCENARIOS_PASSED_CELL, scenarioCounts.getPassed());
		dbDataCellOperations.writeNumericValue(SCENARIOS_FAILED_CELL, scenarioCounts.getFailed());
		dbDataCellOperations.writeNumericValue(SCENARIOS_SKIPPED_CELL, scenarioCounts.getSkipped());
		dbDataCellOperations.writeNumericValue(SCENARIOS_TOTAL_CELL, scenarioCounts.getTotal());

	}

	private void updateStepCounts() {

		CountData stepCounts = reportData.getDashboardData().getStepCounts();
		dbDataCellOperations.writeNumericValue(STEPS_PASSED_CELL, stepCounts.getPassed());
		dbDataCellOperations.writeNumericValue(STEPS_FAILED_CELL, stepCounts.getFailed());
		dbDataCellOperations.writeNumericValue(STEPS_SKIPPED_CELL, stepCounts.getSkipped());
		dbDataCellOperations.writeNumericValue(STEPS_TOTAL_CELL, stepCounts.getTotal());
	}
}
