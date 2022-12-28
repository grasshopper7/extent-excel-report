package tech.grasshopper.excel.report.sheets;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.extent.data.ReportData;

@Data
@SuperBuilder
public abstract class Sheet {

	protected static final String SCENARIOS_SHEET = "Scenarios";

	protected static final String TAGS_SHEET = "Tags";

	protected static final String FEATURES_SHEET = "Features";

	protected static final String EXECUTION_SHEET = "Execution";
	
	protected static final int FREEZE_PANE_ROW = 20;

	protected ReportData reportData;

	protected XSSFWorkbook workbook;
	
	protected XSSFSheet sheet;

	public abstract void updateSheet();

	public void deleteSheet(String sheet) {
		workbook.removeSheetAt(workbook.getSheetIndex(sheet));
	}
}
