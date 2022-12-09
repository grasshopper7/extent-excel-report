package tech.grasshopper.excel.report.exception;

public class ExcelReportException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExcelReportException() {
		super();
	}

	public ExcelReportException(String message) {
		super(message);
	}

	public ExcelReportException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExcelReportException(Throwable cause) {
		super(cause);
	}

}
