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
public class Scenario extends NonExecutable {

	private Feature feature;

	@Default
	private List<Hook> before = new ArrayList<>();
	@Default
	private List<Step> steps = new ArrayList<>();
	@Default
	private List<Hook> after = new ArrayList<>();

	@Default
	private long passedSteps = 0;
	@Default
	private long failedSteps = 0;
	@Default
	private long skippedSteps = 0;
	@Default
	private long totalSteps = 0;

	@Default
	private List<Executable> stackTraceExecutables = new ArrayList<>();

	public List<Hook> getBeforeAfterHooks() {
		List<Hook> hooks = new ArrayList<>();
		hooks.addAll(before);
		hooks.addAll(after);
		return hooks;
	}

	public List<Executable> getStepsAndHooks() {
		List<Executable> executables = new ArrayList<>();

		before.forEach(h -> executables.add(h));
		steps.forEach(s -> {
			s.getBefore().forEach(h -> executables.add(h));
			executables.add(s);
			s.getAfter().forEach(h -> executables.add(h));
		});
		after.forEach(h -> executables.add(h));

		return executables;
	}

	@Override
	public void checkData() {

		if (name == null || name.isEmpty())
			throw new ExcelReportException("Scenario name is null or empty.");

		if (feature == null)
			throw new ExcelReportException("No feature present for scenario - " + getName());

		if (steps == null || steps.isEmpty())
			throw new ExcelReportException("No steps present for scenario - " + getName());

		if (status == null)
			throw new ExcelReportException("No status present for scenario - " + getName());

		super.checkData();
	}
}
