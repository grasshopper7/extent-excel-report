package tech.grasshopper.extent.data.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.aventstack.extentreports.gherkin.model.Asterisk;
import com.aventstack.extentreports.gherkin.model.ScenarioOutline;
import com.aventstack.extentreports.model.Report;
import com.aventstack.extentreports.model.Test;

import lombok.Builder;
import tech.grasshopper.excel.report.util.DateUtil;
import tech.grasshopper.extent.data.pojo.Feature;
import tech.grasshopper.extent.data.pojo.Hook;
import tech.grasshopper.extent.data.pojo.Hook.HookType;
import tech.grasshopper.extent.data.pojo.Scenario;
import tech.grasshopper.extent.data.pojo.Status;
import tech.grasshopper.extent.data.pojo.Step;

@Builder
public class ReportDataHeirarchy {

	private Report report;

	public List<Feature> createFeatureHeirarchy() {

		List<Feature> features = new ArrayList<>();

		for (Test featureTest : report.getTestList()) {

			List<String> featureTags = new ArrayList<>();
			featureTest.getCategorySet().stream().forEach(c -> featureTags.add(c.getName()));

			List<Scenario> scenarios = new ArrayList<>();
			Feature feature = Feature.builder().name(featureTest.getName())
					.status(convertStatus(featureTest.getStatus())).tags(featureTags).scenarios(scenarios)
					.startTime(DateUtil.convertToLocalDateTimeFromDate(featureTest.getStartTime()))
					.endTime(DateUtil.convertToLocalDateTimeFromDate(featureTest.getEndTime())).build();
			features.add(feature);

			for (Test scenarioTest : featureTest.getChildren()) {

				if (scenarioTest.getBddType() == ScenarioOutline.class) {
					for (Test soScenarioTest : scenarioTest.getChildren())
						createScenarioHookSteps(soScenarioTest, scenarios);
				} else
					createScenarioHookSteps(scenarioTest, scenarios);
			}
		}

		return features;
	}

	private void createScenarioHookSteps(Test scenarioTest, List<Scenario> scenarios) {

		List<Step> steps = new ArrayList<>();
		List<Hook> beforeHook = new ArrayList<>();
		List<Hook> afterHook = new ArrayList<>();

		List<String> scenarioTags = new ArrayList<>();
		scenarioTest.getCategorySet().stream().forEach(c -> scenarioTags.add(c.getName()));

		Scenario scenario = Scenario.builder().name(scenarioTest.getName())
				.status(convertStatus(scenarioTest.getStatus())).tags(scenarioTags).steps(steps).before(beforeHook)
				.after(afterHook).startTime(DateUtil.convertToLocalDateTimeFromDate(scenarioTest.getStartTime()))
				.endTime(DateUtil.convertToLocalDateTimeFromDate(scenarioTest.getEndTime())).build();
		scenarios.add(scenario);

		Step step = null;
		LoopObject loopObject = LoopObject.INITIAL;
		for (Test stepTest : scenarioTest.getChildren()) {

			if (stepTest.getBddType() == Asterisk.class && isValidHook(stepTest)) {
				HookType type = HookType.valueOf(stepTest.getDescription().toUpperCase());
				switch (type) {
				case BEFORE:
					addHookData(beforeHook, stepTest);
					break;
				case AFTER:
					addHookData(afterHook, stepTest);
					break;
				case BEFORE_STEP:
					if (loopObject == LoopObject.INITIAL || loopObject == LoopObject.STEP
							|| loopObject == LoopObject.AFTER_STEP) {
						step = Step.builder().build();
					}

					step.addBeforeStepHook(createHook(stepTest));
					loopObject = LoopObject.BEFORE_STEP;
					break;
				case AFTER_STEP:
					step.addAfterStepHook(createHook(stepTest));
					loopObject = LoopObject.AFTER_STEP;
					break;
				}
			} else {
				if (loopObject == LoopObject.INITIAL || loopObject == LoopObject.STEP
						|| loopObject == LoopObject.AFTER_STEP) {
					step = Step.builder().build();
				}

				addStepData(step, stepTest);
				steps.add(step);
				loopObject = LoopObject.STEP;
			}
		}
	}

	private void addStepData(Step step, Test stepTest) {
		step.setName(stepTest.getName());
		step.setStatus(convertStatus(stepTest.getStatus()));
		step.setKeyword(stepTest.getBddType().getSimpleName());
		step.setStartTime(DateUtil.convertToLocalDateTimeFromDate(stepTest.getStartTime()));
		step.setEndTime(DateUtil.convertToLocalDateTimeFromDate(stepTest.getEndTime()));
	}

	private void addHookData(List<Hook> hooks, Test hookTest) {
		hooks.add(createHook(hookTest));
	}

	private Hook createHook(Test hookTest) {
		return Hook.builder().location(hookTest.getName()).hookType(HookType.valueOf(hookTest.getDescription()))
				.status(convertStatus(hookTest.getStatus()))
				.startTime(DateUtil.convertToLocalDateTimeFromDate(hookTest.getStartTime()))
				.endTime(DateUtil.convertToLocalDateTimeFromDate(hookTest.getEndTime())).build();
	}

	private boolean isValidHook(Test test) {
		return Arrays.stream(HookType.values()).anyMatch((h) -> h.name().equals(test.getDescription().toUpperCase()));
	}

	private static enum LoopObject {
		INITIAL, BEFORE_STEP, STEP, AFTER_STEP;
	}

	private Status convertStatus(com.aventstack.extentreports.Status extentStatus) {
		Status status = Status.SKIPPED;
		if (extentStatus == com.aventstack.extentreports.Status.PASS)
			status = Status.PASSED;
		else if (extentStatus == com.aventstack.extentreports.Status.FAIL)
			status = Status.FAILED;
		return status;
	}
}