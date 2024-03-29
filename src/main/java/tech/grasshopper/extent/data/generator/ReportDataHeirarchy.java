package tech.grasshopper.extent.data.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.aventstack.extentreports.gherkin.model.Asterisk;
import com.aventstack.extentreports.gherkin.model.ScenarioOutline;
import com.aventstack.extentreports.model.Log;
import com.aventstack.extentreports.model.NamedAttribute;
import com.aventstack.extentreports.model.Report;
import com.aventstack.extentreports.model.Test;

import lombok.Builder;
import tech.grasshopper.excel.report.util.DateUtil;
import tech.grasshopper.extent.data.pojo.Executable.ExecutableType;
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

			List<String> featureTags = attributesData(featureTest.getCategorySet());
			List<String> featureAuthors = attributesData(featureTest.getAuthorSet());
			List<String> featureDevices = attributesData(featureTest.getDeviceSet());

			List<Scenario> scenarios = new ArrayList<>();
			Feature feature = Feature.builder().name(featureTest.getName())
					.status(convertStatus(featureTest.getStatus())).tags(featureTags).authors(featureAuthors)
					.devices(featureDevices).scenarios(scenarios)
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

		List<String> scenarioTags = attributesData(scenarioTest.getCategorySet());
		List<String> scenarioAuthors = attributesData(scenarioTest.getAuthorSet());
		List<String> scenarioDevices = attributesData(scenarioTest.getDeviceSet());

		Scenario scenario = Scenario.builder().name(scenarioTest.getName())
				.status(convertStatus(scenarioTest.getStatus())).tags(scenarioTags).authors(scenarioAuthors)
				.devices(scenarioDevices).steps(steps).before(beforeHook).after(afterHook)
				.startTime(DateUtil.convertToLocalDateTimeFromDate(scenarioTest.getStartTime()))
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
						step = Step.builder().executableType(ExecutableType.STEP).build();
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
					step = Step.builder().executableType(ExecutableType.STEP).build();
				}

				addStepData(step, stepTest);
				steps.add(step);
				loopObject = LoopObject.STEP;
			}
		}
	}

	private List<String> attributesData(Set<? extends NamedAttribute> attrs) {
		List<String> attributes = new ArrayList<>();
		attrs.stream().forEach(c -> attributes.add(c.getName()));
		return attributes;
	}

	private void addStepData(Step step, Test stepTest) {
		step.setName(stepTest.getName());
		step.setStatus(convertStatus(stepTest.getStatus()));
		step.setKeyword(stepTest.getBddType().getSimpleName());
		step.setErrorMessage(getStackTrace(stepTest));
		step.setStartTime(DateUtil.convertToLocalDateTimeFromDate(stepTest.getStartTime()));
		step.setEndTime(DateUtil.convertToLocalDateTimeFromDate(stepTest.getEndTime()));
	}

	private void addHookData(List<Hook> hooks, Test hookTest) {
		hooks.add(createHook(hookTest));
	}

	private Hook createHook(Test hookTest) {
		return Hook.builder().executableType(ExecutableType.HOOK).location(hookTest.getName())
				.hookType(HookType.valueOf(hookTest.getDescription())).status(convertStatus(hookTest.getStatus()))
				.errorMessage(getStackTrace(hookTest))
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

	public String getStackTrace(Test test) {

		List<Log> failAndSkipLogs = test.getLogs().stream()
				.filter(l -> l.getStatus() == com.aventstack.extentreports.Status.FAIL
						|| l.getStatus() == com.aventstack.extentreports.Status.SKIP)
				.collect(Collectors.toList());

		for (Log log : failAndSkipLogs) {

			if (log.getException() != null)
				return log.getException().getStackTrace();
		}
		return "";
	}
}