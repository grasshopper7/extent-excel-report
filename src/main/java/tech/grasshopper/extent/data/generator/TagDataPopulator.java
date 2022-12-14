package tech.grasshopper.extent.data.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Builder;
import tech.grasshopper.extent.data.SheetData.CountData;
import tech.grasshopper.extent.data.SheetData.TagCountData;
import tech.grasshopper.extent.data.pojo.Feature;
import tech.grasshopper.extent.data.pojo.Scenario;
import tech.grasshopper.extent.data.pojo.Status;

@Builder
public class TagDataPopulator {

	private List<Feature> features;

	public void populateTagCountData(List<TagCountData> tagData) {

		populateTagCountData(tagData, features);
	}

	public void populateFailAndSkipScenariosTagCountData(List<TagCountData> failSkipTagData) {

		List<Feature> statusFailAndSkippedFeatures = features.stream().filter(f -> f.getStatus() != Status.PASSED)
				.collect(Collectors.toList());

		populateTagCountData(failSkipTagData, statusFailAndSkippedFeatures);

		removeTagsWithOnlyPassedScenarios(failSkipTagData);
	}

	private void populateTagCountData(List<TagCountData> tagData, List<Feature> statusFilteredFeatures) {

		Map<String, TagCountData> tagDatas = new HashMap<>();

		for (Feature feature : statusFilteredFeatures) {
			for (Scenario scenario : feature.getScenarios()) {
				List<String> tags = scenario.getTags();

				for (String tag : tags) {
					TagCountData td = tagDatas.computeIfAbsent(tag,
							k -> TagCountData.builder().name(tag).scenarioCounts(CountData.builder().build()).build());

					if (!tagData.stream().anyMatch(t -> td.getName().equals(t.getName())))
						tagData.add(td);

					CountData scenarioCounts = td.getScenarioCounts();

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

	private void removeTagsWithOnlyPassedScenarios(List<TagCountData> tagData) {

		tagData.removeIf(td -> td.getScenarioCounts().getPassed() == td.getScenarioCounts().getTotal());
	}

	public void populateFailSkipFeatureScenarioData(Map<String, List<Feature>> data) {

		for (Feature feature : features) {
			if (feature.getStatus() == Status.PASSED)
				continue;

			for (Scenario scenario : feature.getScenarios()) {
				if (scenario.getStatus() == Status.PASSED)
					continue;

				populateTagFeatureScenarioData(data, feature, scenario);
			}
		}
	}

	public void populateFeatureScenarioData(Map<String, List<Feature>> data) {

		for (Feature feature : features) {

			for (Scenario scenario : feature.getScenarios()) {

				populateTagFeatureScenarioData(data, feature, scenario);
			}
		}
	}

	private void populateTagFeatureScenarioData(Map<String, List<Feature>> data, Feature feature, Scenario scenario) {

		for (String tag : scenario.getTags()) {
			List<Feature> feats = data.computeIfAbsent(tag, f -> new ArrayList<Feature>());

			Feature feat = feats.stream().filter(f -> f.getName().equals(feature.getName())).findAny().orElseGet(() -> {
				Feature f = Feature.builder().name(feature.getName()).status(feature.getStatus()).build();
				feats.add(f);
				return f;
			});

			feat.getScenarios().add(Scenario.builder().name(scenario.getName()).status(scenario.getStatus()).build());

			feat.setTotalScenarios(feat.getTotalScenarios() + 1);
		}
	}
}
