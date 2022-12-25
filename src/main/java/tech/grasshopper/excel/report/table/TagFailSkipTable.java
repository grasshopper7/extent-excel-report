package tech.grasshopper.excel.report.table;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.Builder;
import tech.grasshopper.excel.report.cell.CellOperations;
import tech.grasshopper.extent.data.pojo.Feature;
import tech.grasshopper.extent.data.pojo.Scenario;

@Builder
public class TagFailSkipTable {

	private XSSFSheet sheet;

	private String startCell;

	private Map<String, List<Feature>> failSkipFeatureAndScenarioTagData;

	private final int[] columnCellCount = { 1, 3, 1, 1 };

	public void writeTableValues() {

		CellOperations cellOperations = CellOperations.builder().sheet(sheet).build();
		CellReference cellRef = new CellReference(startCell);
		int startRow = cellRef.getRow();
		int startCol = cellRef.getCol();

		int rowCount = (int) failSkipFeatureAndScenarioTagData.values().stream().flatMap(Collection::stream)
				.mapToLong(f -> f.getTotalScenarios()).sum();
		cellOperations.createCellsWithStyleInRange(startRow, startRow + rowCount, startCol,
				startCol + Arrays.stream(columnCellCount).sum());

		int currentRow = startRow;

		for (Entry<String, List<Feature>> entry : failSkipFeatureAndScenarioTagData.entrySet()) {
			// Reset to tag name column
			int currentCol = startCol;

			String tag = entry.getKey();
			List<Feature> features = entry.getValue();

			long tagMergeRowCount = features.stream().mapToLong(f -> f.getTotalScenarios()).sum();

			cellOperations.mergeRows(currentRow, (int) tagMergeRowCount, currentCol, columnCellCount[0]);
			cellOperations.writeBoldStringValue(new CellReference(currentRow, currentCol), tag);

			// Move to feature name column
			currentCol = currentCol + columnCellCount[0];

			for (Feature feature : features) {
				cellOperations.mergeRows(currentRow, (int) feature.getTotalScenarios(), currentCol, columnCellCount[1]);
				cellOperations.writeNonExecutableName(new CellReference(currentRow, currentCol), feature.getName(),
						feature.getStatus());

				// Move to scenario name column
				currentCol = currentCol + columnCellCount[1];

				for (Scenario scenario : feature.getScenarios()) {

					cellOperations.mergeRows(currentRow, 1, currentCol, columnCellCount[2]);
					cellOperations.writeNonExecutableName(new CellReference(currentRow, currentCol), scenario.getName(),
							scenario.getStatus());

					// Move to scenario status column
					currentCol = currentCol + columnCellCount[2];

					cellOperations.mergeRows(currentRow, 1, currentCol, columnCellCount[3]);
					cellOperations.writeStatus(new CellReference(currentRow, currentCol), scenario.getStatus());

					// Move BACK to scenario name column
					currentCol = currentCol - columnCellCount[2];
					// Move to next scenario
					currentRow++;
				}

				// Move BACK to feature name column
				currentCol = currentCol - columnCellCount[1];
			}
		}

		sheet.groupRow(startRow, startRow + rowCount - 1);
		sheet.setRowGroupCollapsed(startRow, true);
	}
}
