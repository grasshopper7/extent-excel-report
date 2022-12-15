package tech.grasshopper.excel.report.sheets.dashboard.components;

import static tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange.convertCellReferenceToChartDataRange;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.TAG_TABLE_NAME_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.TAG_TABLE_SCENARIO_FAILED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.TAG_TABLE_SCENARIO_PASSED_CELL;
import static tech.grasshopper.excel.report.sheets.dashboard.DashboardSheet.TAG_TABLE_SCENARIO_SKIPPED_CELL;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.chart.ChartOperations;
import tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange;
import tech.grasshopper.excel.report.table.TableOperations;
import tech.grasshopper.extent.data.SheetData.CountData;
import tech.grasshopper.extent.data.SheetData.TagData;

@SuperBuilder
public class TagDBComponent extends DBComponent {

	private int tagBarChartIndex;

	@Override
	public void createComponent() {

		updateTagTableData();
		refreshTagChartPlot();
	}

	private void updateTagTableData() {

		TableOperations<TagData> dbDataTableOperations = TableOperations.<TagData>builder().sheet(dbDataSheet).build();

		Function<TagData, List<String>> rowValueTransformer = (TagData t) -> {
			List<String> row = new ArrayList<>();
			CountData counts = t.getScenarioCounts();

			row.add(t.getName());
			row.add(String.valueOf(counts.getPassed()));
			row.add(String.valueOf(counts.getFailed()));
			row.add(String.valueOf(counts.getSkipped()));

			return row;
		};

		dbDataTableOperations.writeTableValues(TAG_TABLE_NAME_CELL, reportData.getTagData(), rowValueTransformer);
	}

	private void refreshTagChartPlot() {

		ChartOperations dbChartOperations = ChartOperations.builder().dataSheet(dbDataSheet).chartSheet(dbSheet)
				.build();

		int rows = reportData.getTagData().size();

		ChartDataSeriesRange categoryRange = convertCellReferenceToChartDataRange(TAG_TABLE_NAME_CELL, rows);

		List<ChartDataSeriesRange> valueRanges = new ArrayList<>();
		valueRanges.add(convertCellReferenceToChartDataRange(TAG_TABLE_SCENARIO_PASSED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(TAG_TABLE_SCENARIO_SKIPPED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(TAG_TABLE_SCENARIO_FAILED_CELL, rows));

		dbChartOperations.updateBarChartPlot(tagBarChartIndex, categoryRange, valueRanges);
	}
}