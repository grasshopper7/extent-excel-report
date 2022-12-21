package tech.grasshopper.excel.report.table;

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

	public void writeTableValues() {

		CellOperations cellOperations = CellOperations.builder().sheet(sheet).build();
		CellReference cellRef = new CellReference(startCell);
		int startRow = cellRef.getRow();
		int startCol = cellRef.getCol();

		int rowCount = (int) failSkipFeatureAndScenarioTagData.values().stream().flatMap(Collection::stream)
				.mapToLong(f -> f.getTotalScenarios()).sum();
		cellOperations.createCellsWithStyleInRange(startRow, startRow + rowCount, startCol, startCol + 4);

		int currentRow = startRow;

		for (Entry<String, List<Feature>> entry : failSkipFeatureAndScenarioTagData.entrySet()) {
			// Reset to tag name column
			int currentCol = startCol;

			String tag = entry.getKey();
			List<Feature> features = entry.getValue();

			long tagMergeRowCount = features.stream().mapToLong(f -> f.getTotalScenarios()).sum();

			if (tagMergeRowCount > 1)
				cellOperations.mergeRows(currentRow, currentRow + (int) tagMergeRowCount - 1, currentCol);

			cellOperations.writeStringValue(new CellReference(currentRow, currentCol), tag);

			// Move to feature name column
			currentCol++;

			for (Feature feature : features) {
				if (feature.getTotalScenarios() > 1)
					cellOperations.mergeRows(currentRow, currentRow + (int) feature.getTotalScenarios() - 1,
							currentCol);

				cellOperations.writeStringValue(new CellReference(currentRow, currentCol), feature.getName());

				// Move to scenario name column
				currentCol++;

				for (Scenario scenario : feature.getScenarios()) {
					CellOperations.builder().sheet(sheet).build()
							.writeStringValue(new CellReference(currentRow, currentCol), scenario.getName());

					// Move to scenario status column
					currentCol++;

					CellOperations.builder().sheet(sheet).build().writeStringValue(
							new CellReference(currentRow, currentCol), scenario.getStatus().toString());

					// Move BACK to scenario name column
					currentCol--;
					// Move to next scenario
					currentRow++;
				}

				// Move BACK to feature name column
				currentCol--;
			}
		}

		sheet.groupRow(startRow, startRow + rowCount - 1);
		sheet.setRowGroupCollapsed(startRow, true);
	}
}
