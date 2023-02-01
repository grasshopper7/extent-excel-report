package tech.grasshopper.excel.report.sheets.attributes;

import java.util.List;
import java.util.Map;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.extent.data.SheetData.AttributeCountData;
import tech.grasshopper.extent.data.pojo.Feature;

@SuperBuilder
public class DevicesSheet extends AttributesSheet {

	private static final String DEVICES_CHART = "Devices";

	@Override
	protected List<AttributeCountData> getAttributeCountData() {
		return reportData.getDeviceData();
	}

	@Override
	protected String getAttributeSheetName() {
		return DEVICES_SHEET;
	}

	@Override
	protected String getAttributeChartName() {
		return DEVICES_CHART;
	}

	@Override
	protected Map<String, List<Feature>> getFeatureScenarioAttributeData() {
		return reportData.getFeatureAndScenarioDeviceData();
	}
}
