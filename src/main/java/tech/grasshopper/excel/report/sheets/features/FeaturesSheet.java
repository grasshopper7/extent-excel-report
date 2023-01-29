package tech.grasshopper.excel.report.sheets.features;

import static tech.grasshopper.excel.report.cell.CellValueOptions.BOLD_CELL_OPTIONS;
import static tech.grasshopper.excel.report.cell.CellValueOptions.BOLD_HORIZCENTER_CELL_OPTIONS;
import static tech.grasshopper.excel.report.cell.CellValueOptions.ITALIC_CELL_OPTIONS;
import static tech.grasshopper.excel.report.cell.CellValueOptions.POSITIVENUMBER_HORIZCENTER_CELL_OPTIONS;
import static tech.grasshopper.excel.report.cell.CellValueOptions.POSITIVENUMBER_STATUSFAILEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS;
import static tech.grasshopper.excel.report.cell.CellValueOptions.POSITIVENUMBER_STATUSPASSEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS;
import static tech.grasshopper.excel.report.cell.CellValueOptions.POSITIVENUMBER_STATUSSKIPPEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS;
import static tech.grasshopper.excel.report.cell.CellValueOptions.STATUS_BOLD_CELL_OPTIONS;
import static tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange.convertCellReferenceToChartDataRange;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.cell.CellValueOptions;
import tech.grasshopper.excel.report.chart.ChartOperations;
import tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange;
import tech.grasshopper.excel.report.sheets.Sheet;
import tech.grasshopper.excel.report.table.SimpleTableOperations;
import tech.grasshopper.excel.report.util.DateUtil;
import tech.grasshopper.extent.data.SheetData.CountData;
import tech.grasshopper.extent.data.SheetData.FeatureData;

@SuperBuilder
public class FeaturesSheet extends Sheet {

	private static final String FEATURES_TABLE_NAME_CELL = "B22";
	private static final String FEATURES_TABLE_SCENARIO_PASSED_CELL = "F22";
	private static final String FEATURES_TABLE_SCENARIO_FAILED_CELL = "G22";
	private static final String FEATURES_TABLE_SCENARIO_SKIPPED_CELL = "H22";
	private static final String FEATURES_CHART = "Features";

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

		SimpleTableOperations<FeatureData> featureTableOperations = SimpleTableOperations.<FeatureData>builder()
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
			row.add(scenarioCounts.getPassPercent());
			row.add(String.valueOf(stepCounts.getTotal()));
			row.add(String.valueOf(stepCounts.getPassed()));
			row.add(String.valueOf(stepCounts.getFailed()));
			row.add(String.valueOf(stepCounts.getSkipped()));

			return row;
		};

		List<CellValueOptions> cellOptions = new ArrayList<>();

		cellOptions.add(BOLD_CELL_OPTIONS);
		cellOptions.add(STATUS_BOLD_CELL_OPTIONS);
		cellOptions.add(ITALIC_CELL_OPTIONS);
		cellOptions.add(POSITIVENUMBER_HORIZCENTER_CELL_OPTIONS);
		cellOptions.add(POSITIVENUMBER_STATUSPASSEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS);
		cellOptions.add(POSITIVENUMBER_STATUSFAILEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS);
		cellOptions.add(POSITIVENUMBER_STATUSSKIPPEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS);
		cellOptions.add(BOLD_HORIZCENTER_CELL_OPTIONS);
		cellOptions.add(POSITIVENUMBER_HORIZCENTER_CELL_OPTIONS);
		cellOptions.add(POSITIVENUMBER_STATUSPASSEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS);
		cellOptions.add(POSITIVENUMBER_STATUSFAILEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS);
		cellOptions.add(POSITIVENUMBER_STATUSSKIPPEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS);

		featureTableOperations.writeTableCellValues(FEATURES_TABLE_NAME_CELL, reportData.getFeatureData(),
				rowValueTransformer, cellOptions);
	}

	private void refreshFeaturesChartPlot() {

		ChartOperations chartOperations = ChartOperations.builder().dataSheet(sheet).chartSheet(sheet).build();

		int rows = reportData.getFeatureData().size();

		ChartDataSeriesRange categoryRange = convertCellReferenceToChartDataRange(FEATURES_TABLE_NAME_CELL, rows);

		List<ChartDataSeriesRange> valueRanges = new ArrayList<>();
		valueRanges.add(convertCellReferenceToChartDataRange(FEATURES_TABLE_SCENARIO_PASSED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(FEATURES_TABLE_SCENARIO_SKIPPED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(FEATURES_TABLE_SCENARIO_FAILED_CELL, rows));

		chartOperations.updateBarChartPlot(FEATURES_CHART, categoryRange, valueRanges);
	}
}
