package tech.grasshopper.reporter.excel;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aventstack.extentreports.model.Report;
import com.aventstack.extentreports.observer.ReportObserver;
import com.aventstack.extentreports.observer.entity.ReportEntity;
import com.aventstack.extentreports.reporter.AbstractFileReporter;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import tech.grasshopper.excel.report.workbook.ReportWorkbook;
import tech.grasshopper.extent.data.ReportData;
import tech.grasshopper.extent.data.pojo.Feature;

public class ExtentExcelCucumberReporter extends AbstractFileReporter implements ReportObserver<ReportEntity> {

	private static final Logger logger = Logger.getLogger(ExtentExcelCucumberReporter.class.getName());
	// private static final String REPORTER_NAME = "xlsx";
	private static final String FILE_NAME = "report.xlsx";

	private Disposable disposable;
	private Report report;

	public ExtentExcelCucumberReporter(String path) {
		super(new File(path));
	}

	protected ExtentExcelCucumberReporter(File f) {
		super(f);
	}

	@Override
	public Observer<ReportEntity> getReportObserver() {
		return new Observer<ReportEntity>() {
			@Override
			public void onSubscribe(Disposable d) {
				start(d);
			}

			@Override
			public void onNext(ReportEntity value) {
				flush(value);
			}

			@Override
			public void onError(Throwable e) {
			}

			@Override
			public void onComplete() {
			}
		};
	}

	private void start(Disposable d) {
		disposable = d;
	}

	private void flush(ReportEntity value) {
		try {
			report = value.getReport();
			final String reportXls = getFileNameAsExt(FILE_NAME, new String[] { ".xlsx" });

			ReportData reportData = new ReportData();
			reportData.createData(report);

			ReportWorkbook.createReport(reportData, reportXls);

		} catch (Exception e) {
			disposable.dispose();
			logger.log(Level.SEVERE, "An exception occurred", e);
		}
	}
}