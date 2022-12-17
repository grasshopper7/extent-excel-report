package tech.grasshopper.extent.data;

import java.util.ArrayList;
import java.util.List;

import com.aventstack.extentreports.model.Report;

import lombok.Getter;
import tech.grasshopper.excel.report.exception.ExcelReportException;
import tech.grasshopper.extent.data.SheetData.BasicDashboardData;
import tech.grasshopper.extent.data.SheetData.FailSkipData;
import tech.grasshopper.extent.data.SheetData.FeatureData;
import tech.grasshopper.extent.data.SheetData.ScenarioData;
import tech.grasshopper.extent.data.SheetData.TagData;
import tech.grasshopper.extent.data.generator.DashboardDataPopulator;
import tech.grasshopper.extent.data.generator.FailSkipDataPopulator;
import tech.grasshopper.extent.data.generator.HeirarchyDataPopulator;
import tech.grasshopper.extent.data.generator.NonExecutableDataPopulator;
import tech.grasshopper.extent.data.generator.ReportDataHeirarchy;
import tech.grasshopper.extent.data.generator.TagDataPopulator;
import tech.grasshopper.extent.data.pojo.Executable;
import tech.grasshopper.extent.data.pojo.Feature;
import tech.grasshopper.extent.data.pojo.Scenario;

@Getter
public class ReportData {

	private List<Feature> features;

	private BasicDashboardData dashboardData;

	private List<TagData> failSkipTagData = new ArrayList<>();

	private List<FailSkipData> failSkipFeatureAndScenarioData = new ArrayList<>();

	private List<TagData> tagData = new ArrayList<>();

	private List<FeatureData> featureData = new ArrayList<>();

	private List<ScenarioData> scenarioData = new ArrayList<>();

	public void createData(Report report) {

		features = ReportDataHeirarchy.builder().report(report).build().createFeatureHeirarchy();

		populateCounts();

		checkData();

		populateDashboardData(report);

		populateFailSkipTagData();

		populateFailSkipFeatureAndScenarioData();

		populateTagData();

		populateFeatureData();

		populateScenarioData();
	}

	private void checkData() {

		if (features == null || features.size() == 0)
			throw new ExcelReportException("No features present in test execution.");

		for (Feature feature : features) {
			feature.checkData();

			for (Scenario scenario : feature.getScenarios()) {
				scenario.checkData();

				for (Executable executable : scenario.getStepsAndHooks()) {
					executable.checkData();
				}
			}
		}
	}

	private void populateCounts() {

		HeirarchyDataPopulator.builder().features(features).build().populateCountsAndParentDetails();
	}

	private void populateDashboardData(Report report) {

		dashboardData = DashboardDataPopulator.builder().features(features).build().populateDashboardData(report);
	}

	private void populateFailSkipTagData() {

		TagDataPopulator.builder().features(features).build().populateFailAndSkipScenariosTagData(failSkipTagData);
	}

	private void populateFailSkipFeatureAndScenarioData() {

		FailSkipDataPopulator.builder().features(features).build().populateFailSkipData(failSkipFeatureAndScenarioData);
	}

	private void populateTagData() {

		TagDataPopulator.builder().features(features).build().populateTagData(tagData);
	}

	private void populateFeatureData() {

		NonExecutableDataPopulator.builder().features(features).build().populateFeatureData(featureData);
	}

	private void populateScenarioData() {

		NonExecutableDataPopulator.builder().features(features).build().populateScenarioData(scenarioData);
	}
}
