package tech.grasshopper.reporter;

import java.io.File;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.aventstack.extentreports.model.Report;
import com.aventstack.extentreports.observer.ReportObserver;
import com.aventstack.extentreports.observer.entity.ReportEntity;
import com.aventstack.extentreports.reporter.AbstractFileReporter;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import tech.grasshopper.excel.report.workbook.ReportWorkbook;
import tech.grasshopper.extent.data.ReportData;

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
		final String reportXls = getFileNameAsExt(FILE_NAME, new String[] { ".xlsx" });
		try {
			report = value.getReport();

			ReportData reportData = new ReportData();
			reportData.createData(report);

			ReportWorkbook.createReport(reportData, reportXls);
		} catch (Exception e) {
			disposable.dispose();

			File reportFile = Paths.get(reportXls).toFile();
			if (reportFile.exists())
				reportFile.delete();

			logger.log(Level.SEVERE, "An exception occurred", e);
		}
	}
}