package tech.grasshopper.extent.data.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.exception.ExcelReportException;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Hook extends Executable {

	private HookType hookType;

	public static enum HookType {
		BEFORE, AFTER, BEFORE_STEP, AFTER_STEP;
	}

	@Override
	public String getName() {
		return location;
	}

	public void checkData() {

		if (location == null || location.isEmpty())
			throw new ExcelReportException("Location is null or empty for hook - " + getName());

		if (status == null)
			throw new ExcelReportException("No status present for hook - " + getName());

		super.checkData();
	}
}
