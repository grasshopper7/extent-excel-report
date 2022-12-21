package tech.grasshopper.excel.report.sheets.dashboard;

import java.util.Collection;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.sheets.ComponentShifter;
import tech.grasshopper.excel.report.sheets.dashboard.components.BasicDBComponent;
import tech.grasshopper.excel.report.sheets.dashboard.components.FailSkipDBComponent;
import tech.grasshopper.excel.report.sheets.dashboard.components.TagFailSkipDBComponent;

@SuperBuilder
public class DashboardAllSheet extends DashboardSheet {

	public static final String TAG_FAIL_SKIP_TABLE_CELL = "B38";
	public static final String FEATURE_SCENARIO_FAIL_SKIP_TABLE_CELL = "B60";

	@Override
	public void updateSheet() {

		XSSFSheet dbSheet = xssfWorkbook.getSheet(DASHBOARD_ALL_SHEET);

		XSSFSheet dbDataSheet = xssfWorkbook.getSheet(DASHBOARD_DATA_SHEET);

		BasicDBComponent.builder().dbSheet(dbSheet).dbDataSheet(dbDataSheet).reportData(reportData).build()
				.createComponent();

		int tagRowCount = (int) reportData.getFailSkipFeatureAndScenarioTagData().values().stream()
				.flatMap(Collection::stream).mapToLong(f -> f.getTotalScenarios()).sum();

		moveDownFailSkipDBComponent(dbSheet, tagRowCount);

		TagFailSkipDBComponent.builder().dbSheet(dbSheet).dbDataSheet(dbDataSheet).reportData(reportData)
				.tagBarChartIndex(3).failSkipTableStartCell(TAG_FAIL_SKIP_TABLE_CELL).build().createComponent();

		CellReference origCellRef = new CellReference(FEATURE_SCENARIO_FAIL_SKIP_TABLE_CELL);
		CellReference cellRef = new CellReference(origCellRef.getRow() + tagRowCount, origCellRef.getCol());

		FailSkipDBComponent.builder().dbSheet(dbSheet).dbDataSheet(dbDataSheet).reportData(reportData)
				.featureBarChartIndex(4).scenarioBarChartIndex(5).failSkipTableStartCell(cellRef.formatAsString())
				.build().createComponent();
	}

	private void moveDownFailSkipDBComponent(XSSFSheet sheet, int tagRowCount) {

		CellReference celRef = new CellReference(TAG_FAIL_SKIP_TABLE_CELL);

		ComponentShifter.shiftRows(sheet, celRef.getRow(), tagRowCount);
	}
}