package tech.grasshopper.excel.report.sheets.dashboard.components;

import static tech.grasshopper.excel.report.cell.CellStyles.EMPTY_CELL_STYLE;
import static tech.grasshopper.excel.report.cell.CellStyles.HORIZONTAL_CENTER_CELL_STYLE;
import static tech.grasshopper.excel.report.cell.ValueOption.POSITIVE_NUMBER;
import static tech.grasshopper.excel.report.cell.ValueOption.VALUE;
import static tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange.convertCellReferenceToChartDataRange;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.TAGS_FAIL_SKIP_SCENARIO_CHART;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.TAG_TABLE_NAME_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.TAG_TABLE_SCENARIO_FAILED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.TAG_TABLE_SCENARIO_PASSED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.TAG_TABLE_SCENARIO_SKIPPED_CELL;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.cell.ValueOption;
import tech.grasshopper.excel.report.chart.ChartOperations;
import tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange;
import tech.grasshopper.excel.report.table.AttributeFeatureScenarioTable;
import tech.grasshopper.excel.report.table.SimpleTableOperations;
import tech.grasshopper.extent.data.SheetData.AttributeCountData;
import tech.grasshopper.extent.data.SheetData.CountData;

@SuperBuilder
public class TagFailSkipDBComponent extends DBComponent {

	private String failSkipTableStartCell;

	@Override
	public void createComponent() {

		updateTagTableData();
		refreshTagChartPlot();

		updateDBScenarioFeatureFailSkipTagTableData();
	}

	private void updateTagTableData() {

		SimpleTableOperations<AttributeCountData> dbDataTableOperations = SimpleTableOperations
				.<AttributeCountData>builder().sheet(dbDataSheet).build();

		Function<AttributeCountData, List<String>> rowValueTransformer = (AttributeCountData t) -> {
			List<String> row = new ArrayList<>();
			CountData counts = t.getScenarioCounts();

			row.add(t.getName());
			row.add(String.valueOf(counts.getPassed()));
			row.add(String.valueOf(counts.getFailed()));
			row.add(String.valueOf(counts.getSkipped()));

			return row;
		};

		List<String> styles = new ArrayList<>();

		styles.add(EMPTY_CELL_STYLE);
		styles.add(HORIZONTAL_CENTER_CELL_STYLE);
		styles.add(HORIZONTAL_CENTER_CELL_STYLE);
		styles.add(HORIZONTAL_CENTER_CELL_STYLE);

		List<ValueOption> options = new ArrayList<>();

		options.add(VALUE);
		options.add(POSITIVE_NUMBER);
		options.add(POSITIVE_NUMBER);
		options.add(POSITIVE_NUMBER);

		dbDataTableOperations.writeTableCellValues(TAG_TABLE_NAME_CELL, reportData.getFailSkipTagCountData(),
				rowValueTransformer, styles, options);
	}

	private void refreshTagChartPlot() {

		ChartOperations dbChartOperations = ChartOperations.builder().dataSheet(dbDataSheet).chartSheet(dbSheet)
				.build();

		int rows = reportData.getFailSkipTagCountData().size();

		ChartDataSeriesRange categoryRange = convertCellReferenceToChartDataRange(TAG_TABLE_NAME_CELL, rows);

		List<ChartDataSeriesRange> valueRanges = new ArrayList<>();
		valueRanges.add(convertCellReferenceToChartDataRange(TAG_TABLE_SCENARIO_PASSED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(TAG_TABLE_SCENARIO_SKIPPED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(TAG_TABLE_SCENARIO_FAILED_CELL, rows));

		dbChartOperations.updateBarChartPlot(TAGS_FAIL_SKIP_SCENARIO_CHART, categoryRange, valueRanges);
	}

	private void updateDBScenarioFeatureFailSkipTagTableData() {

		AttributeFeatureScenarioTable.builder()
				.featureAndScenarioAttributeData(reportData.getFailSkipFeatureAndScenarioTagData()).sheet(dbSheet)
				.startCell(failSkipTableStartCell).columnCellCount(new int[] { 1, 3, 1, 1 })
				/* .groupRows(true) */.build().writeTableValues();
	}
}