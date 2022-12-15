package tech.grasshopper.excel.report.sheets.dashboard.components;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.extent.data.ReportData;

@Data
@SuperBuilder
public abstract class DBComponent {

	protected XSSFSheet dbSheet;

	protected XSSFSheet dbDataSheet;

	protected ReportData reportData;

	public abstract void createComponent();
}
