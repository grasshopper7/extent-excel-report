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
import tech.grasshopper.excel.report.sheets.features.FeaturesSheet;
import tech.grasshopper.excel.report.sheets.scenarios.ScenariosSheet;
import tech.grasshopper.excel.report.sheets.tags.TagsSheet;
import tech.grasshopper.extent.data.ReportData;

@Data
@SuperBuilder
public abstract class ReportWorkbook {

	protected ReportData reportData;

	protected XSSFWorkbook xssfWorkbook;

	public static void createReport(ReportData reportData, String reportFile) throws IOException {

		ReportWorkbook reportWorkbook = createReportType(reportData);

		Path copied = Paths.get(reportFile);
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
		boolean tagFailSkipDataPresent = !reportData.getFailSkipTagCountData().isEmpty();

		if (tagFailSkipDataPresent & failSkipDataPresent)
			return ExecutionAndTagAndFailSkipDataReport.builder().reportData(reportData).build();
		else if (failSkipDataPresent)
			return ExecutionAndFailSkipDataReport.builder().reportData(reportData).build();
		else
			return ExecutionDataReport.builder().reportData(reportData).build();
	}

	protected abstract String templateReportLocation();

	protected void updateSheets() {

		// Scenarios sheet
		ScenariosSheet.builder().reportData(reportData).xssfWorkbook(xssfWorkbook).build().updateSheet();

		// Tags sheet
		TagsSheet.builder().reportData(reportData).xssfWorkbook(xssfWorkbook).build().updateSheet();

		// Features sheet
		FeaturesSheet.builder().reportData(reportData).xssfWorkbook(xssfWorkbook).build().updateSheet();

		// Execution sheet (?)
	}
}
