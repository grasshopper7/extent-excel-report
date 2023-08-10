package tech.grasshopper.excel.report.cell;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import tech.grasshopper.excel.report.exception.ExcelReportException;
import tech.grasshopper.extent.data.pojo.Status;

public class CellStyles {

	public static final String EMPTY_CELL_STYLE = "EMPTY_CELL_STYLE";
	public static final String BOLD_CELL_STYLE = "BOLD_CELL_STYLE";
	public static final String ITALIC_CELL_STYLE = "ITALIC_CELL_STYLE";

	public static final String BOLD_VERTICAL_CENTER_CELL_STYLE = "BOLD_VERTICAL_CENTER_CELL_OPTIONS";
	public static final String BOLD_HORIZONTAL_CENTER_CELL_STYLE = "BOLD_HORIZONTAL_CENTER_CELL_OPTIONS";

	public static final String VERTICAL_CENTER_CELL_STYLE = "VERTICAL_CENTER_CELL_OPTIONS";
	public static final String HORIZONTAL_CENTER_CELL_STYLE = "HORIZONTAL_CENTER_CELL_OPTIONS";

	// Pass, Fail, Skip status counts display
	public static final String PASS_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE = "PASS_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE";
	public static final String FAIL_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE = "FAIL_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE";
	public static final String SKIP_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE = "SKIP_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE";

	// Marker string for status value display (PASS, FAIL, SKIP)
	public static final String STATUS_TEXT_BOLD_CELL_STYLE = "STATUS_TEXT_BOLD_CELL_STYLE";
	public static final String PASS_TEXT_BOLD_CELL_STYLE = "PASS_TEXT_BOLD_CELL_STYLE";
	public static final String FAIL_TEXT_BOLD_CELL_STYLE = "FAIL_TEXT_BOLD_CELL_STYLE";
	public static final String SKIP_TEXT_BOLD_CELL_STYLE = "SKIP_TEXT_BOLD_CELL_STYLE";

	// Marker string for text with status color display (Feat Name, Scen Name in
	// 'status' color)
	public static final String STATUS_TEXTCOLOR_CELL_STYLE = "STATUS_TEXTCOLOR_CELL_STYLE";
	public static final String PASS_TEXTCOLOR_CELL_STYLE = "PASS_TEXTCOLOR_CELL_STYLE";
	public static final String FAIL_TEXTCOLOR_CELL_STYLE = "FAIL_TEXTCOLOR_CELL_STYLE";
	public static final String SKIP_TEXTCOLOR_CELL_STYLE = "SKIP_TEXTCOLOR_CELL_STYLE";

	private static final Map<String, CellStyle> STYLES = new HashMap<>();

	public static CellStyle getCellStyle(XSSFSheet sheet, String cellStyle) {

		switch (cellStyle) {

		case EMPTY_CELL_STYLE:
		case STATUS_TEXT_BOLD_CELL_STYLE:
		case STATUS_TEXTCOLOR_CELL_STYLE:
			return emptyCellStyle(sheet);

		case BOLD_CELL_STYLE:
			return boldCellStyle(sheet);

		case ITALIC_CELL_STYLE:
			return italicCellStyle(sheet);

		case BOLD_VERTICAL_CENTER_CELL_STYLE:
			return boldVerticalCentreCellStyle(sheet);

		case BOLD_HORIZONTAL_CENTER_CELL_STYLE:
			return boldHorizontalCentreCellStyle(sheet);

		case VERTICAL_CENTER_CELL_STYLE:
			return verticalCentreCellStyle(sheet);

		case HORIZONTAL_CENTER_CELL_STYLE:
			return horizontalCentreCellStyle(sheet);

		case PASS_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE:
			return passTextColorHorizontalCentreCellStyle(sheet);

		case FAIL_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE:
			return failTextColorHorizontalCentreCellStyle(sheet);

		case SKIP_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE:
			return skipTextColorHorizontalCentreCellStyle(sheet);

		case PASS_TEXT_BOLD_CELL_STYLE:
			return passTextBoldCellStyle(sheet);

		case FAIL_TEXT_BOLD_CELL_STYLE:
			return failTextBoldCellStyle(sheet);

		case SKIP_TEXT_BOLD_CELL_STYLE:
			return skipTextBoldCellStyle(sheet);

		case PASS_TEXTCOLOR_CELL_STYLE:
			return passTextColorCellStyle(sheet);

		case FAIL_TEXTCOLOR_CELL_STYLE:
			return failTextColorCellStyle(sheet);

		case SKIP_TEXTCOLOR_CELL_STYLE:
			return skipTextColorCellStyle(sheet);

		default:
			throw new ExcelReportException("The cell style is not supported.");
		}
	}

	public static String getStatusColorStyle(Status status) {

		if (status == Status.PASSED)
			return PASS_TEXTCOLOR_CELL_STYLE;
		else if (status == Status.FAILED)
			return FAIL_TEXTCOLOR_CELL_STYLE;
		else
			return SKIP_TEXTCOLOR_CELL_STYLE;
	}

	private static CellStyle emptyCellStyle(XSSFSheet sheet) {
		return STYLES.computeIfAbsent(EMPTY_CELL_STYLE, k -> baseCellStyle(sheet));
	}

	private static CellStyle boldCellStyle(XSSFSheet sheet) {
		return STYLES.computeIfAbsent(BOLD_CELL_STYLE, k -> setFont(sheet, f -> f.setBold(true)));
	}

	private static CellStyle italicCellStyle(XSSFSheet sheet) {
		return STYLES.computeIfAbsent(ITALIC_CELL_STYLE, k -> setFont(sheet, f -> f.setItalic(true)));
	}

	private static CellStyle boldVerticalCentreCellStyle(XSSFSheet sheet) {
		return STYLES.computeIfAbsent(BOLD_VERTICAL_CENTER_CELL_STYLE, k -> setStyleAndFont(sheet,
				s -> s.setVerticalAlignment(VerticalAlignment.CENTER), f -> f.setBold(true)));
	}

	private static CellStyle boldHorizontalCentreCellStyle(XSSFSheet sheet) {
		return STYLES.computeIfAbsent(BOLD_HORIZONTAL_CENTER_CELL_STYLE,
				k -> setStyleAndFont(sheet, s -> s.setAlignment(HorizontalAlignment.CENTER), f -> f.setBold(true)));
	}

	private static CellStyle verticalCentreCellStyle(XSSFSheet sheet) {
		return STYLES.computeIfAbsent(VERTICAL_CENTER_CELL_STYLE,
				k -> setStyle(sheet, s -> s.setVerticalAlignment(VerticalAlignment.CENTER)));
	}

	private static CellStyle horizontalCentreCellStyle(XSSFSheet sheet) {
		return STYLES.computeIfAbsent(HORIZONTAL_CENTER_CELL_STYLE,
				k -> setStyle(sheet, s -> s.setAlignment(HorizontalAlignment.CENTER)));
	}

	private static CellStyle passTextColorHorizontalCentreCellStyle(XSSFSheet sheet) {
		return STYLES.computeIfAbsent(PASS_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE,
				k -> setStyleAndFont(sheet, s -> s.setAlignment(HorizontalAlignment.CENTER),
						f -> f.setColor(new XSSFColor(Status.getStatusColor(Status.PASSED), null))));
	}

	private static CellStyle failTextColorHorizontalCentreCellStyle(XSSFSheet sheet) {
		return STYLES.computeIfAbsent(FAIL_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE,
				k -> setStyleAndFont(sheet, s -> s.setAlignment(HorizontalAlignment.CENTER),
						f -> f.setColor(new XSSFColor(Status.getStatusColor(Status.FAILED), null))));
	}

	private static CellStyle skipTextColorHorizontalCentreCellStyle(XSSFSheet sheet) {
		return STYLES.computeIfAbsent(SKIP_TEXTCOLOR_HORIZONTAL_CENTER_CELL_STYLE,
				k -> setStyleAndFont(sheet, s -> s.setAlignment(HorizontalAlignment.CENTER),
						f -> f.setColor(new XSSFColor(Status.getStatusColor(Status.SKIPPED), null))));
	}

	private static CellStyle passTextBoldCellStyle(XSSFSheet sheet) {
		return STYLES.computeIfAbsent(PASS_TEXT_BOLD_CELL_STYLE, k -> setFont(sheet, f -> {
			f.setColor(new XSSFColor(Status.getStatusColor(Status.PASSED), null));
			f.setBold(true);
		}));
	}

	private static CellStyle failTextBoldCellStyle(XSSFSheet sheet) {
		return STYLES.computeIfAbsent(FAIL_TEXT_BOLD_CELL_STYLE, k -> setFont(sheet, f -> {
			f.setColor(new XSSFColor(Status.getStatusColor(Status.FAILED), null));
			f.setBold(true);
		}));
	}

	private static CellStyle skipTextBoldCellStyle(XSSFSheet sheet) {
		return STYLES.computeIfAbsent(SKIP_TEXT_BOLD_CELL_STYLE, k -> setFont(sheet, f -> {
			f.setColor(new XSSFColor(Status.getStatusColor(Status.SKIPPED), null));
			f.setBold(true);
		}));
	}

	private static CellStyle passTextColorCellStyle(XSSFSheet sheet) {
		return STYLES.computeIfAbsent(PASS_TEXTCOLOR_CELL_STYLE, k -> setFont(sheet, f -> {
			f.setColor(new XSSFColor(Status.getStatusColor(Status.PASSED), null));
		}));
	}

	private static CellStyle failTextColorCellStyle(XSSFSheet sheet) {
		return STYLES.computeIfAbsent(FAIL_TEXTCOLOR_CELL_STYLE, k -> setFont(sheet, f -> {
			f.setColor(new XSSFColor(Status.getStatusColor(Status.FAILED), null));
		}));
	}

	private static CellStyle skipTextColorCellStyle(XSSFSheet sheet) {
		return STYLES.computeIfAbsent(SKIP_TEXTCOLOR_CELL_STYLE, k -> setFont(sheet, f -> {
			f.setColor(new XSSFColor(Status.getStatusColor(Status.SKIPPED), null));
		}));
	}

	private static CellStyle setFont(XSSFSheet sheet, Consumer<XSSFFont> function) {

		CellStyle style = baseCellStyle(sheet);
		XSSFFont font = sheet.getWorkbook().createFont();
		function.accept(font);
		style.setFont(font);
		return style;
	}

	private static CellStyle setStyle(XSSFSheet sheet, Consumer<CellStyle> function) {

		CellStyle style = baseCellStyle(sheet);
		function.accept(style);
		return style;
	}

	private static CellStyle setStyleAndFont(XSSFSheet sheet, Consumer<CellStyle> funcStyle,
			Consumer<XSSFFont> funcFont) {

		CellStyle style = baseCellStyle(sheet);
		XSSFFont font = sheet.getWorkbook().createFont();
		funcFont.accept(font);
		style.setFont(font);
		funcStyle.accept(style);
		return style;
	}

	private static CellStyle baseCellStyle(XSSFSheet sheet) {

		CellStyle style = sheet.getWorkbook().createCellStyle();
		style.setVerticalAlignment(VerticalAlignment.TOP);
		style.setBorderTop(BorderStyle.HAIR);
		style.setBorderRight(BorderStyle.HAIR);
		style.setBorderBottom(BorderStyle.HAIR);
		style.setBorderLeft(BorderStyle.HAIR);
		style.setWrapText(true);
		return style;
	}
}
