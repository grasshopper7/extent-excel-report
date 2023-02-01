package tech.grasshopper.extent.data.generator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.extent.data.SheetData.AttributeCountData;
import tech.grasshopper.extent.data.pojo.Feature;
import tech.grasshopper.extent.data.pojo.Scenario;
import tech.grasshopper.extent.data.pojo.Status;

@SuperBuilder
public class TagDataPopulator extends AttributeDataPopulator {

	public void populateFailAndSkipScenariosTagCountData(List<AttributeCountData> failSkipTagData) {

		List<Feature> statusFailAndSkippedFeatures = features.stream().filter(f -> f.getStatus() != Status.PASSED)
				.collect(Collectors.toList());

		populateAttributeCountData(failSkipTagData, statusFailAndSkippedFeatures);

		removeTagsWithOnlyPassedScenarios(failSkipTagData);
	}

	private void removeTagsWithOnlyPassedScenarios(List<AttributeCountData> tagData) {

		tagData.removeIf(td -> td.getScenarioCounts().getPassed() == td.getScenarioCounts().getTotal());
	}

	public void populateFailSkipFeatureScenarioData(Map<String, List<Feature>> data) {

		for (Feature feature : features) {
			if (feature.getStatus() == Status.PASSED)
				continue;

			for (Scenario scenario : feature.getScenarios()) {
				if (scenario.getStatus() == Status.PASSED)
					continue;

				populateAttributeFeatureScenarioData(data, feature, scenario);
			}
		}
	}

	@Override
	protected List<String> getAttributeNames(Scenario scenario) {
		return scenario.getTags();
	}
}
