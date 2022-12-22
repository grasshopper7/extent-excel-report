package tech.grasshopper.excel.report.table;

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

	public void writeTableValues() {

		CellOperations cellOperations = CellOperations.builder().sheet(sheet).build();
		CellReference cellRef = new CellReference(startCell);
		int startRow = cellRef.getRow();
		int startCol = cellRef.getCol();

		int rowCount = (int) failSkipFeatureAndScenarioData.stream().mapToLong(f -> f.getTotalScenarios()).sum();
		cellOperations.createCellsWithStyleInRange(startRow, startRow + rowCount, startCol, startCol + 4);

		int currentRow = startRow;

		for (Feature feature : failSkipFeatureAndScenarioData) {

			// Reset to feature name column
			int currentCol = startCol;

			if (feature.getTotalScenarios() > 1)
				cellOperations.mergeRows(currentRow, currentRow + (int) feature.getTotalScenarios() - 1, currentCol);

			cellOperations.writeStringValue(new CellReference(currentRow, currentCol), feature.getName());

			// Move to feature status column
			currentCol++;

			if (feature.getTotalScenarios() > 1)
				cellOperations.mergeRows(currentRow, currentRow + (int) feature.getTotalScenarios() - 1, currentCol);

			cellOperations.writeStringValue(new CellReference(currentRow, currentCol), feature.getStatus().toString());

			// Move to scenario name column
			currentCol++;

			for (Scenario scenario : feature.getScenarios()) {
				CellOperations.builder().sheet(sheet).build()
						.writeStringValue(new CellReference(currentRow, currentCol), scenario.getName());

				// Move to scenario status column
				currentCol++;

				CellOperations.builder().sheet(sheet).build()
						.writeStringValue(new CellReference(currentRow, currentCol), scenario.getStatus().toString());

				// Move BACK to scenario name column
				currentCol--;
				// Move to next scenario
				currentRow++;
			}

		}

		sheet.groupRow(startRow, startRow + rowCount - 1);
		sheet.setRowGroupCollapsed(startRow, true);
	}
}
