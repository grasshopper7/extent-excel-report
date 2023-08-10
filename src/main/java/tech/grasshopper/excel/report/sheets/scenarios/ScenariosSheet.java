package tech.grasshopper.excel.report.sheets.scenarios;

import static tech.grasshopper.excel.report.cell.CellStyles.BOLD_CELL_STYLE;
import static tech.grasshopper.excel.report.cell.CellStyles.FAIL_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE;
import static tech.grasshopper.excel.report.cell.CellStyles.HORIZONTAL_CENTER_CELL_STYLE;
import static tech.grasshopper.excel.report.cell.CellStyles.ITALIC_CELL_STYLE;
import static tech.grasshopper.excel.report.cell.CellStyles.PASS_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE;
import static tech.grasshopper.excel.report.cell.CellStyles.SKIP_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE;
import static tech.grasshopper.excel.report.cell.CellStyles.STATUS_TEXT_BOLD_CELL_STYLE;
import static tech.grasshopper.excel.report.cell.ValueOption.POSITIVE_NUMBER;
import static tech.grasshopper.excel.report.cell.ValueOption.STATUS_TEXT;
import static tech.grasshopper.excel.report.cell.ValueOption.VALUE;
import static tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange.convertCellReferenceToChartDataRange;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.cell.ValueOption;
import tech.grasshopper.excel.report.chart.ChartOperations;
import tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange;
import tech.grasshopper.excel.report.sheets.Sheet;
import tech.grasshopper.excel.report.table.SimpleTableOperations;
import tech.grasshopper.excel.report.util.DateUtil;
import tech.grasshopper.extent.data.SheetData.CountData;
import tech.grasshopper.extent.data.SheetData.ScenarioData;

@SuperBuilder
public class ScenariosSheet extends Sheet {

	private static final String SCENARIOS_TABLE_NAME_CELL = "B22";
	private static final String SCENARIOS_TABLE_STEP_PASSED_CELL = "H22";
	private static final String SCENARIOS_TABLE_STEP_FAILED_CELL = "I22";
	private static final String SCENARIOS_TABLE_STEP_SKIPPED_CELL = "J22";
	private static final String SCENARIOS_CHART = "Scenarios";

	@Override
	public void updateSheet() {

		sheet = workbook.getSheet(SCENARIOS_SHEET);

		if (reportData.getScenarioData().isEmpty()) {
			deleteSheet(SCENARIOS_SHEET);
			return;
		}

		updateScenariosTableData();
		refreshScenariosChartPlot();

		sheet.createFreezePane(0, FREEZE_PANE_ROW);
	}

	private void updateScenariosTableData() {

		SimpleTableOperations<ScenarioData> scenarioTableOperations = SimpleTableOperations.<ScenarioData>builder()
				.sheet(sheet).build();

		Function<ScenarioData, List<String>> rowValueTransformer = (ScenarioData s) -> {
			List<String> row = new ArrayList<>();
			CountData counts = s.getStepCounts();

			row.add(s.getName());
			row.add(s.getStatus().toString());
			row.add(DateUtil.durationValue(s.getTimingData().getDuration()));
			row.add(s.getFeatureName());
			row.add(s.getFeatureStatus().toString());
			row.add(String.valueOf(counts.getTotal()));
			row.add(String.valueOf(counts.getPassed()));
			row.add(String.valueOf(counts.getFailed()));
			row.add(String.valueOf(counts.getSkipped()));

			return row;
		};

		List<String> styles = new ArrayList<>();

		styles.add(BOLD_CELL_STYLE);
		styles.add(STATUS_TEXT_BOLD_CELL_STYLE);
		styles.add(ITALIC_CELL_STYLE);
		styles.add(BOLD_CELL_STYLE);
		styles.add(STATUS_TEXT_BOLD_CELL_STYLE);
		styles.add(HORIZONTAL_CENTER_CELL_STYLE);
		styles.add(PASS_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE);
		styles.add(FAIL_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE);
		styles.add(SKIP_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE);

		List<ValueOption> options = new ArrayList<>();

		options.add(VALUE);
		options.add(STATUS_TEXT);
		options.add(VALUE);
		options.add(VALUE);
		options.add(STATUS_TEXT);
		options.add(POSITIVE_NUMBER);
		options.add(POSITIVE_NUMBER);
		options.add(POSITIVE_NUMBER);
		options.add(POSITIVE_NUMBER);

		scenarioTableOperations.writeTableCellValues(SCENARIOS_TABLE_NAME_CELL, reportData.getScenarioData(),
				rowValueTransformer, styles, options);
	}

	private void refreshScenariosChartPlot() {

		ChartOperations chartOperations = ChartOperations.builder().dataSheet(sheet).chartSheet(sheet).build();

		int rows = reportData.getScenarioData().size();

		ChartDataSeriesRange categoryRange = convertCellReferenceToChartDataRange(SCENARIOS_TABLE_NAME_CELL, rows);

		List<ChartDataSeriesRange> valueRanges = new ArrayList<>();
		valueRanges.add(convertCellReferenceToChartDataRange(SCENARIOS_TABLE_STEP_PASSED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(SCENARIOS_TABLE_STEP_SKIPPED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(SCENARIOS_TABLE_STEP_FAILED_CELL, rows));

		chartOperations.updateBarChartPlot(SCENARIOS_CHART, categoryRange, valueRanges);
	}
}
