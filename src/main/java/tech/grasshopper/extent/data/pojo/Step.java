package tech.grasshopper.extent.data.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.exception.ExcelReportException;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Step extends Executable {

	private String keyword;

	@Default
	private List<Hook> before = new ArrayList<>();
	@Default
	private List<Hook> after = new ArrayList<>();

	public List<Hook> getBeforeAfterHooks() {
		List<Hook> hooks = new ArrayList<>();
		hooks.addAll(before);
		hooks.addAll(after);
		return hooks;
	}

	public void addBeforeStepHook(Hook hook) {
		before.add(hook);
	}

	public void addAfterStepHook(Hook hook) {
		after.add(hook);
	}

	public void checkData() {

		if (name == null || name.isEmpty())
			throw new ExcelReportException("Step text is null or empty.");

		if (keyword == null || keyword.isEmpty())
			throw new ExcelReportException("Keyword is null or empty for step - " + getName());

		if (status == null)
			throw new ExcelReportException("No status present for step - " + getName());

		super.checkData();
	}
}
