package tech.grasshopper.excel.report.table;

import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.Builder;
import tech.grasshopper.excel.report.cell.CellOperations;
import tech.grasshopper.extent.data.pojo.Executable;
import tech.grasshopper.extent.data.pojo.Feature;
import tech.grasshopper.extent.data.pojo.Scenario;

@Builder
public class ExceptionsTable {

	private XSSFSheet sheet;

	private String startCell;

	private List<Feature> exceptionsData;

	private final int[] columnCellCount = { 1, 1, 1, 1 };

	public void writeTableValues() {

		CellOperations cellOperations = CellOperations.builder().sheet(sheet).build();
		CellReference cellRef = new CellReference(startCell);
		int startRow = cellRef.getRow();
		int startCol = cellRef.getCol();

		int rowCount = (int) exceptionsData.stream().mapToLong(f -> f.getTotalSteps()).sum();
		cellOperations.createCellsWithStyleInRange(startRow, startRow + rowCount, startCol,
				startCol + Arrays.stream(columnCellCount).sum());

		int currentRow = startRow;

		for (Feature feature : exceptionsData) {

			// Reset to feature name column
			int currentCol = startCol;

			cellOperations.mergeRows(currentRow, (int) feature.getTotalSteps(), currentCol, columnCellCount[0]);
			cellOperations.writeStringValueWithStatusColor(new CellReference(currentRow, currentCol), feature.getName(),
					feature.getStatus());

			// Move to scenario name column
			currentCol = currentCol + columnCellCount[0];

			for (Scenario scenario : feature.getScenarios()) {

				cellOperations.mergeRows(currentRow, (int) scenario.getTotalSteps(), currentCol, columnCellCount[1]);
				cellOperations.writeStringValueWithStatusColor(new CellReference(currentRow, currentCol),
						scenario.getName(), scenario.getStatus());

				// Move to step\hook name column
				currentCol = currentCol + columnCellCount[1];

				for (Executable executable : scenario.getStackTraceExecutables()) {

					cellOperations.mergeRows(currentRow, 1, currentCol, columnCellCount[2]);
					cellOperations.writeStringValueWithStatusColor(new CellReference(currentRow, currentCol),
							executable.getName(), executable.getStatus());

					// Move to stacktrace column
					currentCol = currentCol + columnCellCount[2];

					cellOperations.mergeRows(currentRow, 1, currentCol, columnCellCount[3]);
					cellOperations.writeStringValueWithStatusColor(new CellReference(currentRow, currentCol),
							executable.getErrorMessage(), executable.getStatus());

					// Move BACK to step\hook name column
					currentCol = currentCol - columnCellCount[2];
					// Move to next scenario
					currentRow++;
				}

				// Move BACK to scenario name column
				currentCol = currentCol - columnCellCount[1];
			}
		}
	}
}
