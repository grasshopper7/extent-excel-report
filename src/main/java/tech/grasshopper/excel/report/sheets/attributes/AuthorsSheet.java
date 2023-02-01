package tech.grasshopper.excel.report.sheets.attributes;

import java.util.List;
import java.util.Map;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.extent.data.SheetData.AttributeCountData;
import tech.grasshopper.extent.data.pojo.Feature;

@SuperBuilder
public class AuthorsSheet extends AttributesSheet {

	private static final String AUTHORS_CHART = "Authors";

	@Override
	protected List<AttributeCountData> getAttributeCountData() {
		return reportData.getAuthorData();
	}

	@Override
	protected String getAttributeSheetName() {
		return AUTHORS_SHEET;
	}

	@Override
	protected String getAttributeChartName() {
		return AUTHORS_CHART;
	}

	@Override
	protected Map<String, List<Feature>> getFeatureScenarioAttributeData() {
		return reportData.getFeatureAndScenarioAuthorData();
	}
}
