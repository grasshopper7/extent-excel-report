package tech.grasshopper.excel.report.sheets.dashboard.components;

import static tech.grasshopper.excel.report.cell.CellStyles.EMPTY_CELL_STYLE;
import static tech.grasshopper.excel.report.cell.CellStyles.HORIZONTAL_CENTER_CELL_STYLE;
import static tech.grasshopper.excel.report.cell.ValueOption.POSITIVE_NUMBER;
import static tech.grasshopper.excel.report.cell.ValueOption.VALUE;
import static tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange.convertCellReferenceToChartDataRange;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.FEATURES_FAIL_SKIP_SCENARIO_CHART;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.FEATURE_FAIL_SKIP_TABLE_NAME_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.FEATURE_FAIL_SKIP_TABLE_SCENARIO_FAILED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.FEATURE_FAIL_SKIP_TABLE_SCENARIO_PASSED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.FEATURE_FAIL_SKIP_TABLE_SCENARIO_SKIPPED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.SCENARIOS_FAIL_SKIP_STEP_CHART;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.SCENARIO_FAIL_SKIP_TABLE_NAME_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.SCENARIO_FAIL_SKIP_TABLE_STEP_FAILED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.SCENARIO_FAIL_SKIP_TABLE_STEP_PASSED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.SCENARIO_FAIL_SKIP_TABLE_STEP_SKIPPED_CELL;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.cell.ValueOption;
import tech.grasshopper.excel.report.chart.ChartOperations;
import tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange;
import tech.grasshopper.excel.report.table.FeatureScenarioFailSkipTable;
import tech.grasshopper.excel.report.table.SimpleTableOperations;
import tech.grasshopper.extent.data.SheetData.CountData;
import tech.grasshopper.extent.data.SheetData.FeatureData;
import tech.grasshopper.extent.data.SheetData.ScenarioData;
import tech.grasshopper.extent.data.pojo.Status;

@SuperBuilder
public class FeatureScenarioFailSkipDBComponent extends DBComponent {

	private String failSkipTableStartCell;

	@Override
	public void createComponent() {

		List<FeatureData> failSkipFeatures = reportData.getFeatureData().stream()
				.filter(f -> f.getStatus() == Status.FAILED || f.getStatus() == Status.SKIPPED)
				.collect(Collectors.toList());

		updateFeatureFailSkipTableData(failSkipFeatures);
		refreshFeatureFailSkipChartPlot(failSkipFeatures);

		List<ScenarioData> failSkipScenarios = reportData.getScenarioData().stream()
				.filter(f -> f.getStatus() == Status.FAILED || f.getStatus() == Status.SKIPPED)
				.collect(Collectors.toList());

		updateScenarioFailSkipTableData(failSkipScenarios);
		refreshScenarioFailSkipChartPlot(failSkipScenarios);

		updateDBScenarioFeatureFailSkipTableData();
	}

	private void updateFeatureFailSkipTableData(List<FeatureData> failSkipFeatures) {

		SimpleTableOperations<FeatureData> dbDataTableOperations = SimpleTableOperations.<FeatureData>builder()
				.sheet(dbDataSheet).build();

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

		List<String> styles = new ArrayList<>();

		styles.add(EMPTY_CELL_STYLE);
		styles.add(EMPTY_CELL_STYLE);
		styles.add(HORIZONTAL_CENTER_CELL_STYLE);
		styles.add(HORIZONTAL_CENTER_CELL_STYLE);
		styles.add(HORIZONTAL_CENTER_CELL_STYLE);

		List<ValueOption> options = new ArrayList<>();

		options.add(VALUE);
		options.add(VALUE);
		options.add(POSITIVE_NUMBER);
		options.add(POSITIVE_NUMBER);
		options.add(POSITIVE_NUMBER);

		dbDataTableOperations.writeTableCellValues(FEATURE_FAIL_SKIP_TABLE_NAME_CELL, failSkipFeatures,
				rowValueTransformer, styles, options);
	}

	private void refreshFeatureFailSkipChartPlot(List<FeatureData> failSkipFeatures) {

		ChartOperations dbChartOperations = ChartOperations.builder().dataSheet(dbDataSheet).chartSheet(dbSheet)
				.build();

		int rows = failSkipFeatures.size();

		ChartDataSeriesRange categoryRange = convertCellReferenceToChartDataRange(FEATURE_FAIL_SKIP_TABLE_NAME_CELL,
				rows);

		List<ChartDataSeriesRange> valueRanges = new ArrayList<>();
		valueRanges.add(convertCellReferenceToChartDataRange(FEATURE_FAIL_SKIP_TABLE_SCENARIO_PASSED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(FEATURE_FAIL_SKIP_TABLE_SCENARIO_SKIPPED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(FEATURE_FAIL_SKIP_TABLE_SCENARIO_FAILED_CELL, rows));

		dbChartOperations.updateBarChartPlot(FEATURES_FAIL_SKIP_SCENARIO_CHART, categoryRange, valueRanges);
	}

	private void updateScenarioFailSkipTableData(List<ScenarioData> failSkipScenarios) {

		SimpleTableOperations<ScenarioData> dbDataTableOperations = SimpleTableOperations.<ScenarioData>builder()
				.sheet(dbDataSheet).build();

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

		List<String> styles = new ArrayList<>();

		styles.add(EMPTY_CELL_STYLE);
		styles.add(EMPTY_CELL_STYLE);
		styles.add(HORIZONTAL_CENTER_CELL_STYLE);
		styles.add(HORIZONTAL_CENTER_CELL_STYLE);
		styles.add(HORIZONTAL_CENTER_CELL_STYLE);

		List<ValueOption> options = new ArrayList<>();

		options.add(VALUE);
		options.add(VALUE);
		options.add(POSITIVE_NUMBER);
		options.add(POSITIVE_NUMBER);
		options.add(POSITIVE_NUMBER);

		dbDataTableOperations.writeTableCellValues(SCENARIO_FAIL_SKIP_TABLE_NAME_CELL, failSkipScenarios,
				rowValueTransformer, styles, options);
	}

	private void refreshScenarioFailSkipChartPlot(List<ScenarioData> failSkipScenarios) {

		ChartOperations dbChartOperations = ChartOperations.builder().dataSheet(dbDataSheet).chartSheet(dbSheet)
				.build();

		int rows = failSkipScenarios.size();

		ChartDataSeriesRange categoryRange = convertCellReferenceToChartDataRange(SCENARIO_FAIL_SKIP_TABLE_NAME_CELL,
				rows);

		List<ChartDataSeriesRange> valueRanges = new ArrayList<>();
		valueRanges.add(convertCellReferenceToChartDataRange(SCENARIO_FAIL_SKIP_TABLE_STEP_PASSED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(SCENARIO_FAIL_SKIP_TABLE_STEP_SKIPPED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(SCENARIO_FAIL_SKIP_TABLE_STEP_FAILED_CELL, rows));

		dbChartOperations.updateBarChartPlot(SCENARIOS_FAIL_SKIP_STEP_CHART, categoryRange, valueRanges);
	}

	private void updateDBScenarioFeatureFailSkipTableData() {

		FeatureScenarioFailSkipTable.builder()
				.failSkipFeatureAndScenarioData(reportData.getFailSkipFeatureAndScenarioData()).sheet(dbSheet)
				.startCell(failSkipTableStartCell).build().writeTableValues();
	}
}
