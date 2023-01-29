package tech.grasshopper.excel.report.table;

import java.util.List;
import java.util.function.Function;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.Builder;
import tech.grasshopper.excel.report.cell.CellOperations;
import tech.grasshopper.excel.report.cell.CellValueOptions;

@Builder
public class SimpleTableOperations<T> {

	private XSSFSheet sheet;

	public void writeTableCellValues(String startCell, List<T> tableData, Function<T, List<String>> rowValueTransformer,
			List<CellValueOptions> valueOptions) {

		CellReference cellRef = new CellReference(startCell);
		int startRow = cellRef.getRow();
		int endRow = startRow + tableData.size();
		int startColumn = cellRef.getCol();
		int endColumn = startColumn + valueOptions.size();

		CellOperations cellOperations = CellOperations.builder().sheet(sheet).build();

		cellOperations.createCellsWithStyleInRange(startRow, endRow, startColumn, endColumn);

		for (int i = startRow; i < endRow; i++) {

			List<String> rowValue = rowValueTransformer.apply(tableData.get(i - startRow));
			int startCol = cellRef.getCol();

			for (int j = 0; j < rowValue.size(); j++) {

				cellOperations.writeValue(new CellReference(i, startCol), rowValue.get(j), valueOptions.get(j));
				startCol++;
			}
		}
	}
}
