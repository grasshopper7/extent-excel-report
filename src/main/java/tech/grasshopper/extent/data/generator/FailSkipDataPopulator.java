package tech.grasshopper.extent.data.generator;

import java.util.List;

import lombok.Builder;
import tech.grasshopper.extent.data.pojo.Feature;
import tech.grasshopper.extent.data.pojo.Scenario;
import tech.grasshopper.extent.data.pojo.Status;

@Builder
public class FailSkipDataPopulator {

	private List<Feature> features;

	public void populateFailSkipFeatureScenarioData(List<Feature> data) {

		for (Feature feature : features) {
			if (feature.getStatus() == Status.PASSED)
				continue;

			Feature feat = Feature.builder().name(feature.getName()).status(feature.getStatus()).build();
			data.add(feat);

			for (Scenario scenario : feature.getScenarios()) {
				if (scenario.getStatus() == Status.PASSED)
					continue;

				feat.getScenarios()
						.add(Scenario.builder().name(scenario.getName()).status(scenario.getStatus()).build());

				feat.setTotalScenarios(feat.getTotalScenarios() + 1);
			}
		}
	}
}
