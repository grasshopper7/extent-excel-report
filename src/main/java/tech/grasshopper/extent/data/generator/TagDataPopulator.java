package tech.grasshopper.extent.data.generator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		Map<String, TagData> tagDatas = new HashMap<>();

		for (Feature feature : features) {
			for (Scenario scenario : feature.getScenarios()) {
				List<String> tags = scenario.getTags();

				for (String tag : tags) {
					TagData td = tagDatas.computeIfAbsent(tag,
							k -> TagData.builder().name(tag).scenarioCounts(CountData.builder().build()).build());

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
