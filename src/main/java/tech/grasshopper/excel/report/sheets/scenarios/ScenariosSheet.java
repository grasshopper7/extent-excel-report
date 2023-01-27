package tech.grasshopper.excel.report.sheets.scenarios;

import static tech.grasshopper.excel.report.cell.CellOperations.printBoldString;
import static tech.grasshopper.excel.report.cell.CellOperations.printLong;
import static tech.grasshopper.excel.report.cell.CellOperations.printStatus;
import static tech.grasshopper.excel.report.cell.CellOperations.printString;
import static tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange.convertCellReferenceToChartDataRange;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.poi.ss.util.CellReference;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.cell.CellOperations;
import tech.grasshopper.excel.report.chart.ChartOperations;
import tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange;
import tech.grasshopper.excel.report.sheets.Sheet;
import tech.grasshopper.excel.report.table.SimpleTableOperations;
import tech.grasshopper.excel.report.util.DateUtil;
import tech.grasshopper.excel.report.util.TriConsumer;
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

		List<TriConsumer<CellOperations, CellReference, String>> printFunctions = new ArrayList<>();

		printFunctions.add(printBoldString);
		printFunctions.add(printStatus);
		printFunctions.add(printString);
		printFunctions.add(printBoldString);
		printFunctions.add(printStatus);
		printFunctions.add(printLong);
		printFunctions.add(printLong);
		printFunctions.add(printLong);
		printFunctions.add(printLong);

		scenarioTableOperations.writeTableValues(SCENARIOS_TABLE_NAME_CELL, reportData.getScenarioData(),
				rowValueTransformer, printFunctions);
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
