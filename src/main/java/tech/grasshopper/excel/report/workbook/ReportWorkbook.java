package tech.grasshopper.excel.report.workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.extent.data.ReportData;

@Data
@SuperBuilder
public abstract class ReportWorkbook {

	protected ReportData reportData;

	protected XSSFWorkbook xssfWorkbook;

	public static void createReport(ReportData reportData) throws IOException {

		ReportWorkbook reportWorkbook = createReportType(reportData);

		try (XSSFWorkbook templateXls = new XSSFWorkbook(reportWorkbook.templateReportLocation())) {
			reportWorkbook.setXssfWorkbook(templateXls);

			reportWorkbook.updateSheets();

			try (FileOutputStream fileOut = new FileOutputStream(new File("src/main/resources/report.xlsx"))) {
				templateXls.write(fileOut);
			}
		}
	}

	private static ReportWorkbook createReportType(ReportData reportData) {

		boolean failSkipDataPresent = !reportData.getFailSkipData().isEmpty();
		boolean tagDataPresent = !reportData.getTagData().isEmpty();

		if (tagDataPresent & failSkipDataPresent)
			return ExecutionAndTagAndFailSkipDataReport.builder().reportData(reportData).build();
		else if (tagDataPresent)
			return ExecutionAndTagDataReport.builder().reportData(reportData).build();
		else if (failSkipDataPresent)
			return ExecutionAndFailSkipDataReport.builder().reportData(reportData).build();
		else
			return ExecutionDataReport.builder().reportData(reportData).build();
	}

	protected abstract String templateReportLocation();

	protected abstract void updateSheets();
}
