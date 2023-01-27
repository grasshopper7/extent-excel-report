package tech.grasshopper.excel.report.sheets.dashboard;

import java.util.Collection;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.sheets.ComponentShifter;
import tech.grasshopper.excel.report.sheets.dashboard.components.BasicDBComponent;
import tech.grasshopper.excel.report.sheets.dashboard.components.FeatureScenarioFailSkipDBComponent;
import tech.grasshopper.excel.report.sheets.dashboard.components.TagFailSkipDBComponent;

@SuperBuilder
public class DashboardAllSheet extends DashboardSheet {

	private static final String TAG_FAIL_SKIP_TABLE_CELL = "B39";
	private static final String FEATURE_SCENARIO_FAIL_SKIP_TABLE_CELL = "B61";

	@Override
	public void updateSheet() {

		super.updateSheet();

		BasicDBComponent.builder().dbSheet(sheet).dbDataSheet(dbDataSheet).reportData(reportData).build()
				.createComponent();

		int tagRowCount = (int) reportData.getFailSkipFeatureAndScenarioTagData().values().stream()
				.flatMap(Collection::stream).mapToLong(f -> f.getTotalScenarios()).sum();

		moveDownFeatureScenarioFailSkipDBComponent(sheet, tagRowCount);

		TagFailSkipDBComponent.builder().dbSheet(sheet).dbDataSheet(dbDataSheet).reportData(reportData)
				.failSkipTableStartCell(TAG_FAIL_SKIP_TABLE_CELL).build().createComponent();

		CellReference origCellRef = new CellReference(FEATURE_SCENARIO_FAIL_SKIP_TABLE_CELL);
		CellReference cellRef = new CellReference(origCellRef.getRow() + tagRowCount, origCellRef.getCol());

		FeatureScenarioFailSkipDBComponent.builder().dbSheet(sheet).dbDataSheet(dbDataSheet).reportData(reportData)
				.failSkipTableStartCell(cellRef.formatAsString()).build().createComponent();

		lockSheet();
	}

	private void moveDownFeatureScenarioFailSkipDBComponent(XSSFSheet sheet, int tagRowCount) {

		CellReference celRef = new CellReference(TAG_FAIL_SKIP_TABLE_CELL);

		ComponentShifter.shiftRows(sheet, celRef.getRow(), tagRowCount);
	}
}