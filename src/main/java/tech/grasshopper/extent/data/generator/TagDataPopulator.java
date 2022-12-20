package tech.grasshopper.extent.data.generator;

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

	public void populateFailSkipFeatureScenarioData(Map<String, Feature> data) {

		for (Feature feature : features) {
			if (feature.getStatus() == Status.PASSED)
				continue;

			for (Scenario scenario : feature.getScenarios()) {
				if (scenario.getStatus() == Status.PASSED)
					continue;

				List<String> tags = scenario.getTags();

				for (String tag : tags) {

					Feature feat = data.computeIfAbsent(tag,
							f -> Feature.builder().name(feature.getName()).status(feature.getStatus()).build());

					feat.getScenarios()
							.add(Scenario.builder().name(scenario.getName()).status(scenario.getStatus()).build());

					feat.setTotalScenarios(feat.getTotalScenarios() + 1);
				}
			}
		}
	}
}
