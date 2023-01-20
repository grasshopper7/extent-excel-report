package tech.grasshopper.extent.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.aventstack.extentreports.model.Report;

import lombok.Getter;
import tech.grasshopper.excel.report.exception.ExcelReportException;
import tech.grasshopper.extent.data.SheetData.BasicDashboardData;
import tech.grasshopper.extent.data.SheetData.FeatureData;
import tech.grasshopper.extent.data.SheetData.ScenarioData;
import tech.grasshopper.extent.data.SheetData.TagCountData;
import tech.grasshopper.extent.data.generator.DashboardDataPopulator;
import tech.grasshopper.extent.data.generator.ExceptionDataPopulator;
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

	private List<TagCountData> failSkipTagCountData = new ArrayList<>();

	private Map<String, List<Feature>> failSkipFeatureAndScenarioTagData = new LinkedHashMap<>();

	private List<Feature> failSkipFeatureAndScenarioData = new ArrayList<>();

	private List<TagCountData> tagData = new ArrayList<>();

	private Map<String, List<Feature>> featureAndScenarioTagData = new LinkedHashMap<>();

	private List<FeatureData> featureData = new ArrayList<>();

	private List<ScenarioData> scenarioData = new ArrayList<>();

	private List<Feature> exceptionData = new ArrayList<>();

	public void createData(Report report) {

		features = ReportDataHeirarchy.builder().report(report).build().createFeatureHeirarchy();

		populateCounts();

		checkData();

		populateDashboardData(report);

		populateFailSkipTagCountData();

		populateFailSkipFeatureAndScenarioTagData();

		populateFailSkipFeatureAndScenarioData();

		populateTagData();

		populateFeatureAndScenarioTagData();

		populateFeatureData();

		populateScenarioData();

		populateExceptionData();
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

	private void populateFailSkipTagCountData() {

		TagDataPopulator.builder().features(features).build()
				.populateFailAndSkipScenariosTagCountData(failSkipTagCountData);
	}

	private void populateFailSkipFeatureAndScenarioTagData() {

		TagDataPopulator.builder().features(features).build()
				.populateFailSkipFeatureScenarioData(failSkipFeatureAndScenarioTagData);
	}

	private void populateFailSkipFeatureAndScenarioData() {

		FailSkipDataPopulator.builder().features(features).build()
				.populateFailSkipFeatureScenarioData(failSkipFeatureAndScenarioData);
	}

	private void populateTagData() {

		TagDataPopulator.builder().features(features).build().populateTagCountData(tagData);
	}

	private void populateFeatureAndScenarioTagData() {

		TagDataPopulator.builder().features(features).build().populateFeatureScenarioData(featureAndScenarioTagData);
	}

	private void populateFeatureData() {

		NonExecutableDataPopulator.builder().features(features).build().populateFeatureData(featureData);
	}

	private void populateScenarioData() {

		NonExecutableDataPopulator.builder().features(features).build().populateScenarioData(scenarioData);
	}

	private void populateExceptionData() {

		ExceptionDataPopulator.builder().features(features).build().populateExceptionData(exceptionData);
	}
}
