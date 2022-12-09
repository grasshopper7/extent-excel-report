package tech.grasshopper.extent.data.generator;

import java.util.List;

import lombok.Builder;
import tech.grasshopper.excel.report.exception.ExcelReportException;
import tech.grasshopper.extent.data.pojo.Feature;
import tech.grasshopper.extent.data.pojo.Scenario;
import tech.grasshopper.extent.data.pojo.Status;
import tech.grasshopper.extent.data.pojo.Step;

@Builder
public class HeirarchyDataPopulator {

	private List<Feature> features;

	public void populateCountsAndParentDetails() {

		if (features == null || features.size() == 0)
			throw new ExcelReportException("No features present in test execution.");

		for (Feature feature : features) {

			for (Scenario scenario : feature.getScenarios()) {

				for (Step step : scenario.getSteps()) {

					if (step.getStatus() == Status.PASSED) {
						scenario.setPassedSteps(scenario.getPassedSteps() + 1);
						feature.setPassedSteps(feature.getPassedSteps() + 1);
					} else if (step.getStatus() == Status.FAILED) {
						scenario.setFailedSteps(scenario.getFailedSteps() + 1);
						feature.setFailedSteps(feature.getFailedSteps() + 1);
					} else {
						scenario.setSkippedSteps(scenario.getSkippedSteps() + 1);
						feature.setSkippedSteps(feature.getSkippedSteps() + 1);
					}

					scenario.setTotalSteps(scenario.getTotalSteps() + 1);
					feature.setTotalSteps(feature.getTotalSteps() + 1);

					step.setScenario(scenario);
					step.setFeature(feature);
				}

				if (scenario.getStatus() == Status.PASSED)
					feature.setPassedScenarios(feature.getPassedScenarios() + 1);
				else if (scenario.getStatus() == Status.FAILED)
					feature.setFailedScenarios(feature.getFailedScenarios() + 1);
				else
					feature.setSkippedScenarios(feature.getSkippedScenarios() + 1);

				feature.setTotalScenarios(feature.getTotalScenarios() + 1);

				scenario.setFeature(feature);
			}
		}
	}
}
