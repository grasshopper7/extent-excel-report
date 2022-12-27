package tech.grasshopper.extent.data.generator;

import java.time.Duration;
import java.util.List;

import lombok.Builder;
import tech.grasshopper.extent.data.SheetData.CountData;
import tech.grasshopper.extent.data.SheetData.FeatureData;
import tech.grasshopper.extent.data.SheetData.ScenarioData;
import tech.grasshopper.extent.data.SheetData.TimingData;
import tech.grasshopper.extent.data.pojo.Feature;
import tech.grasshopper.extent.data.pojo.Scenario;

@Builder
public class NonExecutableDataPopulator {

	private List<Feature> features;

	public void populateFeatureData(List<FeatureData> featureData) {

		for (Feature feature : features) {
			TimingData timingData = TimingData.builder().startTime(feature.getStartTime()).endTime(feature.getEndTime())
					.duration(Duration.between(feature.getStartTime(), feature.getEndTime())).build();

			CountData scenarioCounts = CountData.builder().total(feature.getTotalScenarios())
					.passed(feature.getPassedScenarios()).failed(feature.getFailedScenarios())
					.skipped(feature.getSkippedScenarios()).build();

			CountData stepCounts = CountData.builder().total(feature.getTotalSteps()).passed(feature.getPassedSteps())
					.failed(feature.getFailedSteps()).skipped(feature.getSkippedSteps()).build();

			featureData.add(FeatureData.builder().name(feature.getName()).status(feature.getStatus())
					.timingData(timingData).scenarioCounts(scenarioCounts).stepCounts(stepCounts).build());
		}
	}

	public void populateScenarioData(List<ScenarioData> scenarioData) {

		for (Feature feature : features) {
			for (Scenario scenario : feature.getScenarios()) {
				TimingData timingData = TimingData.builder().startTime(scenario.getStartTime())
						.endTime(scenario.getEndTime())
						.duration(Duration.between(scenario.getStartTime(), scenario.getEndTime())).build();

				CountData stepCounts = CountData.builder().total(scenario.getTotalSteps())
						.passed(scenario.getPassedSteps()).failed(scenario.getFailedSteps())
						.skipped(scenario.getSkippedSteps()).build();

				scenarioData.add(ScenarioData.builder().name(scenario.getName()).status(scenario.getStatus())
						.featureName(feature.getName()).featureStatus(feature.getStatus()).timingData(timingData)
						.stepCounts(stepCounts).build());
			}
		}
	}
}
