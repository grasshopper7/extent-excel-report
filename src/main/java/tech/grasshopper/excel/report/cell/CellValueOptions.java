package tech.grasshopper.excel.report.cell;

import java.awt.Color;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import lombok.Builder;
import lombok.Builder.Default;
import tech.grasshopper.extent.data.pojo.Status;
import lombok.Data;

@Builder
@Data
public class CellValueOptions {

	public static final CellValueOptions EMPTY_CELL_OPTIONS = CellValueOptions.builder().build();
	public static final CellValueOptions BOLD_CELL_OPTIONS = CellValueOptions.builder().bold(true).build();
	public static final CellValueOptions ITALIC_CELL_OPTIONS = CellValueOptions.builder().italic(true).build();
	public static final CellValueOptions BOLD_HORIZCENTER_CELL_OPTIONS = CellValueOptions.builder().bold(true)
			.horizAlign(HorizontalAlignment.CENTER).build();
	public static final CellValueOptions POSITIVENUMBER_HORIZCENTER_CELL_OPTIONS = CellValueOptions.builder()
			.positiveNumber(true).horizAlign(HorizontalAlignment.CENTER).build();
	public static final CellValueOptions POSITIVENUMBER_STATUSPASSEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS = CellValueOptions
			.builder().positiveNumber(true).horizAlign(HorizontalAlignment.CENTER)
			.textColor(Status.getStatusColor(Status.PASSED)).build();
	public static final CellValueOptions POSITIVENUMBER_STATUSFAILEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS = CellValueOptions
			.builder().positiveNumber(true).horizAlign(HorizontalAlignment.CENTER)
			.textColor(Status.getStatusColor(Status.FAILED)).build();
	public static final CellValueOptions POSITIVENUMBER_STATUSSKIPPEDTEXTCOLOR_HORIZCENTER_CELL_OPTIONS = CellValueOptions
			.builder().positiveNumber(true).horizAlign(HorizontalAlignment.CENTER)
			.textColor(Status.getStatusColor(Status.SKIPPED)).build();
	public static final CellValueOptions STATUS_BOLD_CELL_OPTIONS = CellValueOptions.builder().status(true).bold(true)
			.build();

	@Default
	private boolean bold = false;

	@Default
	private boolean italic = false;

	@Default
	private Color textColor = Color.BLACK;

	private HorizontalAlignment horizAlign;

	private VerticalAlignment vertAlign;

	@Default
	private boolean status = false;

	@Default
	private boolean number = false;

	@Default
	private boolean positiveNumber = false;

	public static CellValueOptions getStatusColorCellValueOption(Status status) {

		return CellValueOptions.builder().textColor(Status.getStatusColor(status)).build();
	}

}
