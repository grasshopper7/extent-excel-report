package tech.grasshopper.excel.report.table;

import java.util.List;
import java.util.function.Function;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.Builder;
import tech.grasshopper.excel.report.cell.CellOperations;

@Builder
public class TableOperations<T> {

	private XSSFSheet sheet;

	public void writeTableValues(String startCell, List<T> tableData, Function<T, List<String>> rowValueTransformer) {

		CellReference cellRef = new CellReference(startCell);
		int startRow = cellRef.getRow();
		int endRow = startRow + tableData.size();

		CellOperations dbDataCellOperations = CellOperations.builder().sheet(sheet).build();

		for (int i = startRow; i < endRow; i++) {

			List<String> rowValue = rowValueTransformer.apply(tableData.get(i - startRow));
			int startCol = cellRef.getCol();

			for (String value : rowValue) {
				dbDataCellOperations.writeStringValue(i, startCol, value);
				startCol++;
			}
		}
	}
}
