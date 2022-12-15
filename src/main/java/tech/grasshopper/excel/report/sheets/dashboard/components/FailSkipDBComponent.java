package tech.grasshopper.excel.report.sheets.dashboard.components;

import static tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange.convertCellReferenceToChartDataRange;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.FEATURE_FAIL_SKIP_TABLE_NAME_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.FEATURE_FAIL_SKIP_TABLE_SCENARIO_FAILED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.FEATURE_FAIL_SKIP_TABLE_SCENARIO_PASSED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.FEATURE_FAIL_SKIP_TABLE_SCENARIO_SKIPPED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.SCENARIO_FAIL_SKIP_TABLE_NAME_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.SCENARIO_FAIL_SKIP_TABLE_STEP_FAILED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.SCENARIO_FAIL_SKIP_TABLE_STEP_PASSED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.SCENARIO_FAIL_SKIP_TABLE_STEP_SKIPPED_CELL;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.chart.ChartOperations;
import tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange;
import tech.grasshopper.excel.report.table.TableOperations;
import tech.grasshopper.extent.data.SheetData.CountData;
import tech.grasshopper.extent.data.SheetData.FailSkipData;
import tech.grasshopper.extent.data.SheetData.FeatureData;
import tech.grasshopper.extent.data.SheetData.ScenarioData;
import tech.grasshopper.extent.data.pojo.Status;

@SuperBuilder
public class FailSkipDBComponent extends DBComponent {

	private int featureBarChartIndex;

	private int scenarioBarChartIndex;

	private String failSkipTableStartCell;

	@Override
	public void createComponent() {

		updateFeatureFailSkipTableData();
		refreshFeatureFailSkipChartPlot();
		updateScenarioFailSkipTableData();
		refreshScenarioFailSkipChartPlot();
		updateDBScenarioFeatureFailSkipTableData();
	}

	private void updateFeatureFailSkipTableData() {

		List<FeatureData> failSkipFeatures = reportData.getFeatureData().stream()
				.filter(f -> f.getStatus() == Status.FAILED || f.getStatus() == Status.SKIPPED)
				.collect(Collectors.toList());

		TableOperations<FeatureData> dbDataTableOperations = TableOperations.<FeatureData>builder().sheet(dbDataSheet)
				.build();

		Function<FeatureData, List<String>> rowValueTransformer = (FeatureData f) -> {
			List<String> row = new ArrayList<>();
			CountData counts = f.getScenarioCounts();

			row.add(f.getName());
			row.add(f.getStatus().toString());
			row.add(String.valueOf(counts.getPassed()));
			row.add(String.valueOf(counts.getFailed()));
			row.add(String.valueOf(counts.getSkipped()));

			return row;
		};

		dbDataTableOperations.writeTableValues(FEATURE_FAIL_SKIP_TABLE_NAME_CELL, failSkipFeatures,
				rowValueTransformer);
	}

	private void refreshFeatureFailSkipChartPlot() {

		ChartOperations dbChartOperations = ChartOperations.builder().dataSheet(dbDataSheet).chartSheet(dbSheet)
				.build();

		int rows = reportData.getFeatureData().size();

		ChartDataSeriesRange categoryRange = convertCellReferenceToChartDataRange(FEATURE_FAIL_SKIP_TABLE_NAME_CELL,
				rows);

		List<ChartDataSeriesRange> valueRanges = new ArrayList<>();
		valueRanges.add(convertCellReferenceToChartDataRange(FEATURE_FAIL_SKIP_TABLE_SCENARIO_PASSED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(FEATURE_FAIL_SKIP_TABLE_SCENARIO_SKIPPED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(FEATURE_FAIL_SKIP_TABLE_SCENARIO_FAILED_CELL, rows));

		dbChartOperations.updateBarChartPlot(featureBarChartIndex, categoryRange, valueRanges);
	}

	private void updateScenarioFailSkipTableData() {

		List<ScenarioData> failSkipScenarios = reportData.getScenarioData().stream()
				.filter(f -> f.getStatus() == Status.FAILED || f.getStatus() == Status.SKIPPED)
				.collect(Collectors.toList());

		TableOperations<ScenarioData> dbDataTableOperations = TableOperations.<ScenarioData>builder().sheet(dbDataSheet)
				.build();

		Function<ScenarioData, List<String>> rowValueTransformer = (ScenarioData s) -> {
			List<String> row = new ArrayList<>();
			CountData counts = s.getStepCounts();

			row.add(s.getName());
			row.add(s.getStatus().toString());
			row.add(String.valueOf(counts.getPassed()));
			row.add(String.valueOf(counts.getFailed()));
			row.add(String.valueOf(counts.getSkipped()));

			return row;
		};

		dbDataTableOperations.writeTableValues(SCENARIO_FAIL_SKIP_TABLE_NAME_CELL, failSkipScenarios,
				rowValueTransformer);
	}

	private void refreshScenarioFailSkipChartPlot() {

		ChartOperations dbChartOperations = ChartOperations.builder().dataSheet(dbDataSheet).chartSheet(dbSheet)
				.build();

		int rows = reportData.getScenarioData().size();

		ChartDataSeriesRange categoryRange = convertCellReferenceToChartDataRange(SCENARIO_FAIL_SKIP_TABLE_NAME_CELL,
				rows);

		List<ChartDataSeriesRange> valueRanges = new ArrayList<>();
		valueRanges.add(convertCellReferenceToChartDataRange(SCENARIO_FAIL_SKIP_TABLE_STEP_PASSED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(SCENARIO_FAIL_SKIP_TABLE_STEP_SKIPPED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(SCENARIO_FAIL_SKIP_TABLE_STEP_FAILED_CELL, rows));

		dbChartOperations.updateBarChartPlot(scenarioBarChartIndex, categoryRange, valueRanges);
	}

	private void updateDBScenarioFeatureFailSkipTableData() {

		TableOperations<FailSkipData> dbDataTableOperations = TableOperations.<FailSkipData>builder().sheet(dbSheet)
				.build();

		Function<FailSkipData, List<String>> rowValueTransformer = (FailSkipData fs) -> {
			List<String> row = new ArrayList<>();

			row.add(fs.getScenarioName());
			row.add(fs.getScenarioStatus().toString());
			row.add(fs.getFeatureName());
			row.add(fs.getFeatureStatus().toString());

			return row;
		};

		dbDataTableOperations.writeTableValues(failSkipTableStartCell, reportData.getFailSkipData(),
				rowValueTransformer);
	}
}
