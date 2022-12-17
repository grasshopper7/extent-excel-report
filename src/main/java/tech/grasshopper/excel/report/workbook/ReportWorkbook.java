package tech.grasshopper.excel.report.workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

		/*
		 * try (XSSFWorkbook templateXls = new
		 * XSSFWorkbook(reportWorkbook.templateReportLocation())) {
		 * reportWorkbook.setXssfWorkbook(templateXls);
		 * 
		 * reportWorkbook.updateSheets();
		 * 
		 * try (FileOutputStream fileOut = new FileOutputStream(new
		 * File("src/main/resources/report.xlsx"))) { templateXls.write(fileOut); } }
		 */

		Path copied = Paths.get("src/main/resources/report.xlsx");
		Path originalPath = Paths.get(reportWorkbook.templateReportLocation());
		Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);

		File xlsxFile = copied.toFile();
		FileInputStream inputStream = new FileInputStream(xlsxFile);
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		reportWorkbook.setXssfWorkbook(workbook);
		reportWorkbook.updateSheets();
		inputStream.close();

		FileOutputStream os = new FileOutputStream(xlsxFile);
		workbook.write(os);
		workbook.close();
		os.close();

	}

	private static ReportWorkbook createReportType(ReportData reportData) {

		boolean failSkipDataPresent = !reportData.getFailSkipFeatureAndScenarioData().isEmpty();
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
