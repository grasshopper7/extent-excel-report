package tech.grasshopper.excel.report.sheets.dashboard.components;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class FailSkipDBComponent extends DBComponent {

	private int featureBarChartIndex;

	private int scenarioBarChartIndex;

	@Override
	public void createComponent() {

	}

}
