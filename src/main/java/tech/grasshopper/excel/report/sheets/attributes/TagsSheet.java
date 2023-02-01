package tech.grasshopper.excel.report.sheets.attributes;

import java.util.List;
import java.util.Map;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.extent.data.SheetData.AttributeCountData;
import tech.grasshopper.extent.data.pojo.Feature;

@SuperBuilder
public class TagsSheet extends AttributesSheet {

	private static final String TAGS_CHART = "Tags";

	@Override
	protected List<AttributeCountData> getAttributeCountData() {
		return reportData.getTagData();
	}

	@Override
	protected String getAttributeSheetName() {
		return TAGS_SHEET;
	}

	@Override
	protected String getAttributeChartName() {
		return TAGS_CHART;
	}

	@Override
	protected Map<String, List<Feature>> getFeatureScenarioAttributeData() {
		return reportData.getFeatureAndScenarioTagData();
	}
}
