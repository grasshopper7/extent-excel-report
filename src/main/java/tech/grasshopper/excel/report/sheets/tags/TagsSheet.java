package tech.grasshopper.excel.report.sheets.tags;

import static tech.grasshopper.excel.report.cell.CellValueOptions.BOLD_CELL_OPTIONS;
import static tech.grasshopper.excel.report.cell.CellValueOptions.BOLD_HORIZCENTER_CELL_OPTIONS;
import static tech.grasshopper.excel.report.cell.CellValueOptions.POSITIVENUMBER_HORIZCENTER_CELL_OPTIONS;
import static tech.grasshopper.excel.report.cell.CellValueOptions.POSITIVENUMBER_STATUSFAILEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS;
import static tech.grasshopper.excel.report.cell.CellValueOptions.POSITIVENUMBER_STATUSPASSEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS;
import static tech.grasshopper.excel.report.cell.CellValueOptions.POSITIVENUMBER_STATUSSKIPPEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS;
import static tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange.convertCellReferenceToChartDataRange;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.cell.CellValueOptions;
import tech.grasshopper.excel.report.chart.ChartOperations;
import tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange;
import tech.grasshopper.excel.report.sheets.ComponentShifter;
import tech.grasshopper.excel.report.sheets.Sheet;
import tech.grasshopper.excel.report.table.SimpleTableOperations;
import tech.grasshopper.excel.report.table.TagFeatureScenarioTable;
import tech.grasshopper.extent.data.SheetData.CountData;
import tech.grasshopper.extent.data.SheetData.TagCountData;

@SuperBuilder
public class TagsSheet extends Sheet {

	private static final String TAGS_COUNT_TABLE_NAME_CELL = "B22";
	private static final String TAGS_COUNT_TABLE_SCENARIO_PASSED_CELL = "D22";
	private static final String TAGS_COUNT_TABLE_SCENARIO_FAILED_CELL = "E22";
	private static final String TAGS_COUNT_TABLE_SCENARIO_SKIPPED_CELL = "F22";

	private static final String TAGS_TABLE_NAME_CELL = "B26";
	private static final String TAGS_CHART = "Tags";

	@Override
	public void updateSheet() {

		sheet = workbook.getSheet(TAGS_SHEET);

		if (reportData.getTagData().isEmpty()) {
			deleteSheet(TAGS_SHEET);
			return;
		}

		moveDownTagsFeatureScenarioTable(sheet, reportData.getTagData().size());

		updateTagsTableData();
		refreshTagsChartPlot();

		CellReference origCellRef = new CellReference(TAGS_TABLE_NAME_CELL);
		CellReference cellRef = new CellReference(origCellRef.getRow() + reportData.getTagData().size(),
				origCellRef.getCol());

		updateTagsFeatureScenarioTableData(cellRef);

		sheet.createFreezePane(0, FREEZE_PANE_ROW);
	}

	private void moveDownTagsFeatureScenarioTable(XSSFSheet sheet, int tagRowCount) {

		CellReference celRef = new CellReference(TAGS_COUNT_TABLE_NAME_CELL);

		ComponentShifter.shiftRows(sheet, celRef.getRow() + 1, tagRowCount);
	}

	private void updateTagsTableData() {

		SimpleTableOperations<TagCountData> scenarioTableOperations = SimpleTableOperations.<TagCountData>builder()
				.sheet(sheet).build();

		Function<TagCountData, List<String>> rowValueTransformer = (TagCountData t) -> {
			List<String> row = new ArrayList<>();
			CountData counts = t.getScenarioCounts();

			row.add(t.getName());
			row.add(String.valueOf(counts.getTotal()));
			row.add(String.valueOf(counts.getPassed()));
			row.add(String.valueOf(counts.getFailed()));
			row.add(String.valueOf(counts.getSkipped()));
			row.add(counts.getPassPercent());

			return row;
		};

		List<CellValueOptions> cellOptions = new ArrayList<>();

		cellOptions.add(BOLD_CELL_OPTIONS);
		cellOptions.add(POSITIVENUMBER_HORIZCENTER_CELL_OPTIONS);
		cellOptions.add(POSITIVENUMBER_STATUSPASSEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS);
		cellOptions.add(POSITIVENUMBER_STATUSFAILEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS);
		cellOptions.add(POSITIVENUMBER_STATUSSKIPPEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS);
		cellOptions.add(BOLD_HORIZCENTER_CELL_OPTIONS);

		scenarioTableOperations.writeTableCellValues(TAGS_COUNT_TABLE_NAME_CELL, reportData.getTagData(),
				rowValueTransformer, cellOptions);
	}

	private void refreshTagsChartPlot() {

		ChartOperations chartOperations = ChartOperations.builder().dataSheet(sheet).chartSheet(sheet).build();

		int rows = reportData.getTagData().size();

		ChartDataSeriesRange categoryRange = convertCellReferenceToChartDataRange(TAGS_COUNT_TABLE_NAME_CELL, rows);

		List<ChartDataSeriesRange> valueRanges = new ArrayList<>();
		valueRanges.add(convertCellReferenceToChartDataRange(TAGS_COUNT_TABLE_SCENARIO_PASSED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(TAGS_COUNT_TABLE_SCENARIO_SKIPPED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(TAGS_COUNT_TABLE_SCENARIO_FAILED_CELL, rows));

		chartOperations.updateBarChartPlot(TAGS_CHART, categoryRange, valueRanges);
	}

	private void updateTagsFeatureScenarioTableData(CellReference cellRef) {

		TagFeatureScenarioTable.builder().featureAndScenarioTagData(reportData.getFeatureAndScenarioTagData())
				.sheet(sheet).startCell(cellRef.formatAsString()).columnCellCount(new int[] { 1, 5, 1, 1 }).build()
				.writeTableValues();
	}
}
