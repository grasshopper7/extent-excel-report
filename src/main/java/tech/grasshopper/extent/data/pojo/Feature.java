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
public class Feature extends NonExecutable {

	@Default
	private List<Scenario> scenarios = new ArrayList<>();

	@Default
	private long passedScenarios = 0;
	@Default
	private long failedScenarios = 0;
	@Default
	private long skippedScenarios = 0;
	@Default
	private long totalScenarios = 0;

	@Default
	private long passedSteps = 0;
	@Default
	private long failedSteps = 0;
	@Default
	private long skippedSteps = 0;
	@Default
	private long totalSteps = 0;

	@Override
	public void checkData() {

		if (name == null || name.isEmpty())
			throw new ExcelReportException("Feature name is null or empty.");

		if (scenarios == null || scenarios.isEmpty())
			throw new ExcelReportException("No scenarios present for feature - " + getName());

		if (status == null)
			throw new ExcelReportException("No status present for feature - " + getName());

		super.checkData();
	}
}
