package tech.grasshopper.excel.report.sheets.dashboard.components;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class DBComponent {

	protected XSSFSheet dbSheet;

	protected XSSFSheet dbDataSheet;

	protected abstract void createComponent();
}
