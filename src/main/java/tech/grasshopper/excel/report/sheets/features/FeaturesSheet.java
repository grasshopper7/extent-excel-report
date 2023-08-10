package tech.grasshopper.excel.report.sheets.features;

import static tech.grasshopper.excel.report.cell.CellStyles.BOLD_CELL_STYLE;
import static tech.grasshopper.excel.report.cell.CellStyles.BOLD_HORIZONTAL_CENTER_CELL_STYLE;
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

		List<String> styles = new ArrayList<>();

		styles.add(BOLD_CELL_STYLE);
		styles.add(STATUS_TEXT_BOLD_CELL_STYLE);
		styles.add(ITALIC_CELL_STYLE);
		styles.add(HORIZONTAL_CENTER_CELL_STYLE);
		styles.add(PASS_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE);
		styles.add(FAIL_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE);
		styles.add(SKIP_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE);
		styles.add(BOLD_HORIZONTAL_CENTER_CELL_STYLE);
		styles.add(HORIZONTAL_CENTER_CELL_STYLE);
		styles.add(PASS_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE);
		styles.add(FAIL_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE);
		styles.add(SKIP_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE);

		List<ValueOption> options = new ArrayList<>();

		options.add(VALUE);
		options.add(STATUS_TEXT);
		options.add(VALUE);
		options.add(POSITIVE_NUMBER);
		options.add(POSITIVE_NUMBER);
		options.add(POSITIVE_NUMBER);
		options.add(POSITIVE_NUMBER);
		options.add(VALUE);
		options.add(POSITIVE_NUMBER);
		options.add(POSITIVE_NUMBER);
		options.add(POSITIVE_NUMBER);
		options.add(POSITIVE_NUMBER);

		featureTableOperations.writeTableCellValues(FEATURES_TABLE_NAME_CELL, reportData.getFeatureData(),
				rowValueTransformer, styles, options);
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
