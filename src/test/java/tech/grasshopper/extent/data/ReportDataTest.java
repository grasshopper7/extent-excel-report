package tech.grasshopper.extent.data;

import org.junit.jupiter.api.Test;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.gherkin.model.Asterisk;
import com.aventstack.extentreports.gherkin.model.Feature;
import com.aventstack.extentreports.gherkin.model.Given;
import com.aventstack.extentreports.gherkin.model.Scenario;
import com.aventstack.extentreports.gherkin.model.ScenarioOutline;
import com.aventstack.extentreports.gherkin.model.Then;
import com.aventstack.extentreports.gherkin.model.When;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import tech.grasshopper.reporter.excel.ExtentExcelCucumberReporter;

public class ReportDataTest {

	@Test
	public void test() {
		ExtentReports extent = new ExtentReports();
		extent.setAnalysisStrategy(AnalysisStrategy.BDD);

		ExtentExcelCucumberReporter excel = new ExtentExcelCucumberReporter("test-output/excel/ExtentExcel.xlsx");

		extent.attachReporter(excel);

		ExtentTest feature = extent.createTest(Feature.class, "Feature");
		ExtentTest scenario = feature.createNode(Scenario.class, "Scenario");
		scenario.assignCategory("@tag1", "@tag2");
		scenario.assignCategory("@tag3");
		scenario.assignCategory("simple");
		scenario.createNode(Asterisk.class, "Before Scenario", "BEFORE");
		scenario.createNode(Given.class, "Given step.").pass("");
		scenario.createNode(Asterisk.class, "Before Step", "BEFORE_STEP");
		scenario.createNode(When.class, "When step.").pass("");
		scenario.createNode(Asterisk.class, "After Step", "AFTER_STEP");
		scenario.createNode(Then.class, "Then step.").pass("");
		scenario.createNode(Asterisk.class, "After Scenario", "AFTER");

		ExtentTest scenarioOutline = feature.createNode(ScenarioOutline.class, "Scenario Outline");
		ExtentTest scenarioSO = scenarioOutline.createNode(Scenario.class, "SO Scenario Outline",
				"Scenario in a Scenario Outline");
		scenarioSO.createNode(Given.class, "Given SO step.").pass("");
		scenarioSO.createNode(When.class, "When SO step.").pass("");
		scenarioSO.createNode(Then.class, "Then SO step.").pass("");

		ExtentTest featureFail = extent.createTest(Feature.class, "Feature Fail");
		ExtentTest scenarioFail = featureFail.createNode(Scenario.class, "Scenario Fail");
		scenarioFail.assignCategory("simpleFail");
		scenarioFail.createNode(Given.class, "Given step.").pass("");
		scenarioFail.createNode(When.class, "When step.").pass("");
		scenarioFail.createNode(Then.class, "Then step.").fail("");

		ExtentTest featureSkip = extent.createTest(Feature.class, "Feature Skip");
		ExtentTest scenarioSkip = featureSkip.createNode(Scenario.class, "Scenario Skip");
		scenarioSkip.assignCategory("simpleSkip");
		scenarioSkip.createNode(Given.class, "Given step.").pass("");
		scenarioSkip.createNode(When.class, "When step.").pass("");
		scenarioSkip.createNode(Then.class, "Then step.").skip("");

		extent.flush();
	}
}