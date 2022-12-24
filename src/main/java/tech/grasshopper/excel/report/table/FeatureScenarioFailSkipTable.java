package tech.grasshopper.excel.report.table;

import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.Builder;
import tech.grasshopper.excel.report.cell.CellOperations;
import tech.grasshopper.extent.data.pojo.Feature;
import tech.grasshopper.extent.data.pojo.Scenario;

@Builder
public class FeatureScenarioFailSkipTable {

	private XSSFSheet sheet;

	private String startCell;

	private List<Feature> failSkipFeatureAndScenarioData;

	private final int[] columnCellCount = { 2, 1, 2, 1 };

	public void writeTableValues() {

		CellOperations cellOperations = CellOperations.builder().sheet(sheet).build();
		CellReference cellRef = new CellReference(startCell);
		int startRow = cellRef.getRow();
		int startCol = cellRef.getCol();

		int rowCount = (int) failSkipFeatureAndScenarioData.stream().mapToLong(f -> f.getTotalScenarios()).sum();
		cellOperations.createCellsWithStyleInRange(startRow, startRow + rowCount, startCol,
				startCol + Arrays.stream(columnCellCount).sum());

		int currentRow = startRow;

		for (Feature feature : failSkipFeatureAndScenarioData) {

			// Reset to feature name column
			int currentCol = startCol;

			cellOperations.mergeRows(currentRow, (int) feature.getTotalScenarios(), currentCol, columnCellCount[0]);
			cellOperations.writeStringValue(new CellReference(currentRow, currentCol), feature.getName());

			// Move to feature status column
			currentCol = currentCol + columnCellCount[0];

			cellOperations.mergeRows(currentRow, (int) feature.getTotalScenarios(), currentCol, columnCellCount[1]);
			cellOperations.writeStringValue(new CellReference(currentRow, currentCol), feature.getStatus().toString());

			// Move to scenario name column
			currentCol = currentCol + columnCellCount[1];

			for (Scenario scenario : feature.getScenarios()) {

				cellOperations.mergeRows(currentRow, 1, currentCol, columnCellCount[2]);
				cellOperations.writeStringValue(new CellReference(currentRow, currentCol), scenario.getName());

				// Move to scenario status column
				currentCol = currentCol + columnCellCount[2];

				cellOperations.mergeRows(currentRow, 1, currentCol, columnCellCount[3]);
				cellOperations.writeStringValue(new CellReference(currentRow, currentCol),
						scenario.getStatus().toString());

				// Move BACK to scenario name column
				currentCol = currentCol - columnCellCount[2];
				// Move to next scenario
				currentRow++;
			}
		}

		sheet.groupRow(startRow, startRow + rowCount - 1);
		sheet.setRowGroupCollapsed(startRow, true);
	}
}
