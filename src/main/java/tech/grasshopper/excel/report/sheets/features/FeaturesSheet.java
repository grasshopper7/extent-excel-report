package tech.grasshopper.excel.report.sheets.features;

import static tech.grasshopper.excel.report.cell.CellOperations.printBoldString;
import static tech.grasshopper.excel.report.cell.CellOperations.printLong;
import static tech.grasshopper.excel.report.cell.CellOperations.printStatus;
import static tech.grasshopper.excel.report.cell.CellOperations.printString;
import static tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange.convertCellReferenceToChartDataRange;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.logging.log4j.util.TriConsumer;
import org.apache.poi.ss.util.CellReference;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.cell.CellOperations;
import tech.grasshopper.excel.report.chart.ChartOperations;
import tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange;
import tech.grasshopper.excel.report.sheets.Sheet;
import tech.grasshopper.excel.report.table.SimpleTableOperations;
import tech.grasshopper.excel.report.util.DateUtil;
import tech.grasshopper.extent.data.SheetData.CountData;
import tech.grasshopper.extent.data.SheetData.FeatureData;

@SuperBuilder
public class FeaturesSheet extends Sheet {

	private static final String FEATURES_TABLE_NAME_CELL = "B21";
	private static final String FEATURES_TABLE_SCENARIO_PASSED_CELL = "F21";
	private static final String FEATURES_TABLE_SCENARIO_FAILED_CELL = "G21";
	private static final String FEATURES_TABLE_SCENARIO_SKIPPED_CELL = "H21";

	@Override
	public void updateSheet() {

		sheet = workbook.getSheet(FEATURES_SHEET);

		if (reportData.getFeatureData().isEmpty()) {
			deleteSheet(FEATURES_SHEET);
			return;
		}

		updateFeaturesTableData();
		refreshFeaturesChartPlot();

		sheet.createFreezePane(0, FREEZE_PANE_ROW);
	}

	private void updateFeaturesTableData() {

		SimpleTableOperations<FeatureData> scenarioTableOperations = SimpleTableOperations.<FeatureData>builder()
				.sheet(sheet).build();

		Function<FeatureData, List<String>> rowValueTransformer = (FeatureData f) -> {
			List<String> row = new ArrayList<>();
			CountData scenarioCounts = f.getScenarioCounts();
			CountData stepCounts = f.getStepCounts();

			row.add(f.getName());
			row.add(f.getStatus().toString());
			row.add(DateUtil.durationValue(f.getTimingData().getDuration()));
			row.add(String.valueOf(scenarioCounts.getTotal()));
			row.add(String.valueOf(scenarioCounts.getPassed()));
			row.add(String.valueOf(scenarioCounts.getFailed()));
			row.add(String.valueOf(scenarioCounts.getSkipped()));
			row.add(String.valueOf(stepCounts.getTotal()));
			row.add(String.valueOf(stepCounts.getPassed()));
			row.add(String.valueOf(stepCounts.getFailed()));
			row.add(String.valueOf(stepCounts.getSkipped()));

			return row;
		};

		List<TriConsumer<CellOperations, CellReference, String>> printFunctions = new ArrayList<>();

		printFunctions.add(printBoldString);
		printFunctions.add(printStatus);
		printFunctions.add(printString);
		printFunctions.add(printLong);
		printFunctions.add(printLong);
		printFunctions.add(printLong);
		printFunctions.add(printLong);
		printFunctions.add(printLong);
		printFunctions.add(printLong);
		printFunctions.add(printLong);
		printFunctions.add(printLong);

		scenarioTableOperations.writeTableValues(FEATURES_TABLE_NAME_CELL, reportData.getFeatureData(),
				rowValueTransformer, printFunctions);
	}

	private void refreshFeaturesChartPlot() {

		ChartOperations chartOperations = ChartOperations.builder().dataSheet(sheet).chartSheet(sheet).build();

		int rows = reportData.getFeatureData().size();

		ChartDataSeriesRange categoryRange = convertCellReferenceToChartDataRange(FEATURES_TABLE_NAME_CELL, rows);

		List<ChartDataSeriesRange> valueRanges = new ArrayList<>();
		valueRanges.add(convertCellReferenceToChartDataRange(FEATURES_TABLE_SCENARIO_PASSED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(FEATURES_TABLE_SCENARIO_SKIPPED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(FEATURES_TABLE_SCENARIO_FAILED_CELL, rows));

		chartOperations.updateBarChartPlot(0, categoryRange, valueRanges);
	}
}
