package tech.grasshopper.extent.data.generator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Builder;
import tech.grasshopper.extent.data.SheetData.CountData;
import tech.grasshopper.extent.data.SheetData.TagData;
import tech.grasshopper.extent.data.pojo.Feature;
import tech.grasshopper.extent.data.pojo.Scenario;
import tech.grasshopper.extent.data.pojo.Status;

@Builder
public class TagDataPopulator {

	private List<Feature> features;

	public void populateTagData(List<TagData> tagData) {

		populateTagData(tagData, features);
	}

	public void populateFailAndSkipScenariosTagData(List<TagData> failSkipTagData) {

		List<Feature> statusFailAndSkippedFeatures = features.stream().filter(f -> f.getStatus() != Status.PASSED)
				.collect(Collectors.toList());

		populateTagData(failSkipTagData, statusFailAndSkippedFeatures);
	}

	private void populateTagData(List<TagData> tagData, List<Feature> statusFilteredFeatures) {

		Map<String, TagData> tagDatas = new HashMap<>();

		for (Feature feature : statusFilteredFeatures) {
			for (Scenario scenario : feature.getScenarios()) {
				List<String> tags = scenario.getTags();

				for (String tag : tags) {
					TagData td = tagDatas.computeIfAbsent(tag,
							k -> TagData.builder().name(tag).scenarioCounts(CountData.builder().build()).build());

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
}
