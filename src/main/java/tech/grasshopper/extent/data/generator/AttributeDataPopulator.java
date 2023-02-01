package tech.grasshopper.extent.data.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.extent.data.SheetData.AttributeCountData;
import tech.grasshopper.extent.data.SheetData.CountData;
import tech.grasshopper.extent.data.pojo.Feature;
import tech.grasshopper.extent.data.pojo.Scenario;
import tech.grasshopper.extent.data.pojo.Status;

@SuperBuilder
public abstract class AttributeDataPopulator {

	protected List<Feature> features;

	protected abstract List<String> getAttributeNames(Scenario scenario);

	// Attribute Common
	public void populateAttributeCountData(List<AttributeCountData> attributeData) {

		populateAttributeCountData(attributeData, features);
	}

	protected void populateAttributeCountData(List<AttributeCountData> attributeData,
			List<Feature> statusFilteredFeatures) {

		Map<String, AttributeCountData> attributeDatas = new HashMap<>();

		for (Feature feature : statusFilteredFeatures) {
			for (Scenario scenario : feature.getScenarios()) {
				List<String> attributes = getAttributeNames(scenario);

				for (String attribute : attributes) {
					AttributeCountData acd = attributeDatas.computeIfAbsent(attribute, k -> AttributeCountData.builder()
							.name(attribute).scenarioCounts(CountData.builder().build()).build());

					if (!attributeData.stream().anyMatch(a -> acd.getName().equals(a.getName())))
						attributeData.add(acd);

					CountData scenarioCounts = acd.getScenarioCounts();

					scenarioCounts.setTotal(scenarioCounts.getTotal() + 1);
					if (scenario.getStatus() == Status.PASSED)
						scenarioCounts.setPassed(scenarioCounts.getPassed() + 1);
					else if (scenario.getStatus() == Status.FAILED)
						scenarioCounts.setFailed(scenarioCounts.getFailed() + 1);
					else if (scenario.getStatus() == Status.SKIPPED)
						scenarioCounts.setSkipped(scenarioCounts.getSkipped() + 1);
				}
			}
		}
	}

	// Attribute Common
	public void populateAttributeFeatureScenarioData(Map<String, List<Feature>> data) {

		for (Feature feature : features) {

			for (Scenario scenario : feature.getScenarios()) {

				populateAttributeFeatureScenarioData(data, feature, scenario);
			}
		}
	}

	protected void populateAttributeFeatureScenarioData(Map<String, List<Feature>> data, Feature feature,
			Scenario scenario) {

		for (String attribute : getAttributeNames(scenario)) {
			List<Feature> feats = data.computeIfAbsent(attribute, f -> new ArrayList<Feature>());

			Feature feat = feats.stream().filter(f -> f.getName().equals(feature.getName())).findAny().orElseGet(() -> {
				Feature f = Feature.builder().name(feature.getName()).status(feature.getStatus()).build();
				feats.add(f);
				return f;
			});

			feat.getScenarios().add(Scenario.builder().name(scenario.getName()).status(scenario.getStatus()).build());

			feat.setTotalScenarios(feat.getTotalScenarios() + 1);
		}
	}

	@SuperBuilder
	public static class AuthorDataPopulator extends AttributeDataPopulator {
		@Override
		protected List<String> getAttributeNames(Scenario scenario) {
			return scenario.getAuthors();
		}
	}

	@SuperBuilder
	public static class DeviceDataPopulator extends AttributeDataPopulator {
		@Override
		protected List<String> getAttributeNames(Scenario scenario) {
			return scenario.getDevices();
		}
	}
}
