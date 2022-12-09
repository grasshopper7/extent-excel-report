package tech.grasshopper.extent.data.generator;

import java.util.List;

import lombok.Builder;
import tech.grasshopper.extent.data.SheetData.FailSkipData;
import tech.grasshopper.extent.data.pojo.Feature;
import tech.grasshopper.extent.data.pojo.Scenario;
import tech.grasshopper.extent.data.pojo.Status;

@Builder
public class FailSkipDataPopulator {

	private List<Feature> features;

	public void populateFailSkipData(List<FailSkipData> failSkipData) {

		for (Feature feature : features) {
			for (Scenario scenario : feature.getScenarios()) {
				if (scenario.getStatus() != Status.PASSED)
					failSkipData.add(
							FailSkipData.builder().featureName(feature.getName()).featureStatus(feature.getStatus())
									.scenarioName(scenario.getName()).scenarioStatus(scenario.getStatus()).build());
			}
		}
	}
}
