package tech.grasshopper.excel.report.table;

import java.util.List;
import java.util.function.Function;

import org.apache.logging.log4j.util.TriConsumer;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.Builder;
import tech.grasshopper.excel.report.cell.CellOperations;

@Builder
public class SimpleTableOperations<T> {

	private XSSFSheet sheet;

	public void writeTableValues(String startCell, List<T> tableData, Function<T, List<String>> rowValueTransformer,
			List<TriConsumer<CellOperations, CellReference, String>> printFunctions) {

		CellReference cellRef = new CellReference(startCell);
		int startRow = cellRef.getRow();
		int endRow = startRow + tableData.size();

		CellOperations dbDataCellOperations = CellOperations.builder().sheet(sheet).build();

		for (int i = startRow; i < endRow; i++) {

			List<String> rowValue = rowValueTransformer.apply(tableData.get(i - startRow));
			int startCol = cellRef.getCol();

			for (int j = 0; j < rowValue.size(); j++) {
				// dbDataCellOperations.writeValue(i, startCol, value);
				printFunctions.get(j).accept(dbDataCellOperations, new CellReference(i, startCol), rowValue.get(j));
				startCol++;
			}
		}
	}
}
