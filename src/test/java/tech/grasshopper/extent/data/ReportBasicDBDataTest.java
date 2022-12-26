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

import tech.grasshopper.reporter.excel.ExtentExcelCucumberReporter;

public class ReportBasicDBDataTest {

	@Test
	public void test() {
		ExtentReports extent = new ExtentReports();
		extent.setAnalysisStrategy(AnalysisStrategy.BDD);

		ExtentExcelCucumberReporter excel = new ExtentExcelCucumberReporter("test-output/excel/ExtentExcelBasicDB.xlsx");

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
		scenarioSO.createNode(When.class, "When SO step.").pass("");
		scenarioSO.createNode(Then.class, "Then SO step.").pass("");

		extent.flush();
	}
}
