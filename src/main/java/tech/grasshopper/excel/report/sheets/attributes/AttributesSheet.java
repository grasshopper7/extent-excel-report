package tech.grasshopper.excel.report.sheets.attributes;

import static tech.grasshopper.excel.report.cell.CellValueOptions.BOLD_CELL_OPTIONS;
import static tech.grasshopper.excel.report.cell.CellValueOptions.BOLD_HORIZCENTER_CELL_OPTIONS;
import static tech.grasshopper.excel.report.cell.CellValueOptions.POSITIVENUMBER_HORIZCENTER_CELL_OPTIONS;
import static tech.grasshopper.excel.report.cell.CellValueOptions.POSITIVENUMBER_STATUSFAILEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS;
import static tech.grasshopper.excel.report.cell.CellValueOptions.POSITIVENUMBER_STATUSPASSEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS;
import static tech.grasshopper.excel.report.cell.CellValueOptions.POSITIVENUMBER_STATUSSKIPPEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS;
import static tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange.convertCellReferenceToChartDataRange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.poi.ss.util.CellReference;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.cell.CellValueOptions;
import tech.grasshopper.excel.report.chart.ChartOperations;
import tech.grasshopper.excel.report.chart.ChartOperations.ChartDataSeriesRange;
import tech.grasshopper.excel.report.sheets.ComponentShifter;
import tech.grasshopper.excel.report.sheets.Sheet;
import tech.grasshopper.excel.report.table.SimpleTableOperations;
import tech.grasshopper.excel.report.table.AttributeFeatureScenarioTable;
import tech.grasshopper.extent.data.SheetData.AttributeCountData;
import tech.grasshopper.extent.data.SheetData.CountData;
import tech.grasshopper.extent.data.pojo.Feature;

@SuperBuilder
public abstract class AttributesSheet extends Sheet {

	private static final String ATTRIBUTES_COUNT_TABLE_NAME_CELL = "B22";
	private static final String ATTRIBUTES_COUNT_TABLE_SCENARIO_PASSED_CELL = "D22";
	private static final String ATTRIBUTES_COUNT_TABLE_SCENARIO_FAILED_CELL = "E22";
	private static final String ATTRIBUTES_COUNT_TABLE_SCENARIO_SKIPPED_CELL = "F22";

	private static final String ATTRIBUTES_TABLE_NAME_CELL = "B26";

	@Override
	public void updateSheet() {

		sheet = workbook.getSheet(getAttributeSheetName());

		if (getAttributeCountData().isEmpty()) {
			deleteSheet(getAttributeSheetName());
			return;
		}

		moveDownAttributesFeatureScenarioTable();

		updateAttributesTableData();
		refreshAtributesChartPlot();

		CellReference origCellRef = new CellReference(ATTRIBUTES_TABLE_NAME_CELL);
		CellReference cellRef = new CellReference(origCellRef.getRow() + getAttributeCountData().size(),
				origCellRef.getCol());

		updateAttributesFeatureScenarioTableData(cellRef);

		sheet.createFreezePane(0, FREEZE_PANE_ROW);
	}

	private void moveDownAttributesFeatureScenarioTable() {

		CellReference celRef = new CellReference(ATTRIBUTES_COUNT_TABLE_NAME_CELL);
		ComponentShifter.shiftRows(sheet, celRef.getRow() + 1, getAttributeCountData().size());
	}

	private void updateAttributesTableData() {

		SimpleTableOperations<AttributeCountData> scenarioTableOperations = SimpleTableOperations
				.<AttributeCountData>builder().sheet(sheet).build();

		Function<AttributeCountData, List<String>> rowValueTransformer = (AttributeCountData t) -> {
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

		scenarioTableOperations.writeTableCellValues(ATTRIBUTES_COUNT_TABLE_NAME_CELL, getAttributeCountData(),
				rowValueTransformer, cellOptions);
	}

	private void refreshAtributesChartPlot() {

		ChartOperations chartOperations = ChartOperations.builder().dataSheet(sheet).chartSheet(sheet).build();

		int rows = getAttributeCountData().size();

		ChartDataSeriesRange categoryRange = convertCellReferenceToChartDataRange(ATTRIBUTES_COUNT_TABLE_NAME_CELL,
				rows);

		List<ChartDataSeriesRange> valueRanges = new ArrayList<>();
		valueRanges.add(convertCellReferenceToChartDataRange(ATTRIBUTES_COUNT_TABLE_SCENARIO_PASSED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(ATTRIBUTES_COUNT_TABLE_SCENARIO_SKIPPED_CELL, rows));
		valueRanges.add(convertCellReferenceToChartDataRange(ATTRIBUTES_COUNT_TABLE_SCENARIO_FAILED_CELL, rows));

		chartOperations.updateBarChartPlot(getAttributeChartName(), categoryRange, valueRanges);
	}

	private void updateAttributesFeatureScenarioTableData(CellReference cellReference) {

		AttributeFeatureScenarioTable.builder().featureAndScenarioAttributeData(getFeatureScenarioAttributeData())
				.sheet(sheet).startCell(cellReference.formatAsString()).columnCellCount(new int[] { 1, 5, 1, 1 })
				.build().writeTableValues();
	}

	protected abstract List<AttributeCountData> getAttributeCountData();

	protected abstract String getAttributeSheetName();

	protected abstract String getAttributeChartName();

	protected abstract Map<String, List<Feature>> getFeatureScenarioAttributeData();
}
