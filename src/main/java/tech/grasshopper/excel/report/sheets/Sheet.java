package tech.grasshopper.excel.report.sheets;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.extent.data.ReportData;

@Data
@SuperBuilder
public abstract class Sheet {

	protected ReportData reportData;

	protected XSSFWorkbook xssfWorkbook;

	protected abstract void updateSheet();
}
