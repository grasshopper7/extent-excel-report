package tech.grasshopper.excel.report.sheets.tags;

import static tech.grasshopper.excel.report.cell.CellOperations.printBoldString;
import static tech.grasshopper.excel.report.cell.CellOperations.printLong;
import static tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange.convertCellReferenceToChartDataRange;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.logging.log4j.util.TriConsumer;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.cell.CellOperations;
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

	private static final String TAGS_COUNT_TABLE_NAME_CELL = "B21";
	private static final String TAGS_COUNT_TABLE_SCENARIO_PASSED_CELL = "D21";
	private static final String TAGS_COUNT_TABLE_SCENARIO_FAILED_CELL = "E21";
	private static final String TAGS_COUNT_TABLE_SCENARIO_SKIPPED_CELL = "F21";

	private static final String TAGS_TABLE_NAME_CELL = "B25";

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

			return row;
		};

		List<TriConsumer<CellOperations, CellReference, String>> printFunctions = new ArrayList<>();

		printFunctions.add(printBoldString);
		printFunctions.add(printLong);
		printFunctions.add(printLong);
		printFunctions.add(printLong);
		printFunctions.add(printLong);

		scenarioTableOperations.writeTableValues(TAGS_COUNT_TABLE_NAME_CELL, reportData.getTagData(),
				rowValueTransformer, printFunctions);
	}

	private void refreshTagsChartPlot() {

		ChartOperations chartOperations = ChartOperations.builder().dataSheet(sheet).chartSheet(sheet).build();

		int rows = reportData.getTagData().size();

		ChartDataSeriesRange categoryRange = convertCellReferenceToChartDataRange(TAGS_COUNT_TABLE_NAME_CELL, rows);

		List<ChartDataSeriesRange> valueRanges = new ArrayList<>();
		valueRanges.add(convertCellReferenceToChartDataRange(TAGS_COUNT_TABLE_SCENARIO_PASSED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(TAGS_COUNT_TABLE_SCENARIO_SKIPPED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(TAGS_COUNT_TABLE_SCENARIO_FAILED_CELL, rows));

		chartOperations.updateBarChartPlot(0, categoryRange, valueRanges);
	}

	private void updateTagsFeatureScenarioTableData(CellReference cellRef) {

		TagFeatureScenarioTable.builder().featureAndScenarioTagData(reportData.getFeatureAndScenarioTagData())
				.sheet(sheet).startCell(cellRef.formatAsString()).columnCellCount(new int[] { 1, 3, 2, 1 }).build()
				.writeTableValues();
	}
}
