package tech.grasshopper.extent.data.generator;

import java.util.List;

import lombok.Builder;
import tech.grasshopper.extent.data.pojo.Executable;
import tech.grasshopper.extent.data.pojo.Executable.ExecutableType;
import tech.grasshopper.extent.data.pojo.Feature;
import tech.grasshopper.extent.data.pojo.Hook;
import tech.grasshopper.extent.data.pojo.Scenario;
import tech.grasshopper.extent.data.pojo.Status;
import tech.grasshopper.extent.data.pojo.Step;

@Builder
public class ExceptionDataPopulator {

	private List<Feature> features;

	public void populateExceptionData(List<Feature> data) {

		for (Feature feature : features) {
			if (feature.getStatus() == Status.PASSED)
				continue;

			Feature feat = Feature.builder().name(feature.getName()).status(feature.getStatus()).build();

			for (Scenario scenario : feature.getScenarios()) {
				if (scenario.getStatus() == Status.PASSED)
					continue;

				Scenario scen = Scenario.builder().name(scenario.getName()).status(scenario.getStatus()).build();

				boolean presentStackExec = false;

				for (Executable executable : scenario.getStepsAndHooks()) {
					if (executable.getStatus() == Status.PASSED
							|| (executable.getStatus() == Status.SKIPPED && executable.getErrorMessage().isEmpty()))
						continue;

					presentStackExec = true;

					if (executable.getExecutableType() == ExecutableType.STEP) {
						scen.getStackTraceExecutables()
								.add(Step.builder().name(executable.getName()).executableType(ExecutableType.STEP)
										.status(executable.getStatus()).errorMessage(executable.getErrorMessage())
										.build());

					} else if (executable.getExecutableType() == ExecutableType.HOOK) {
						scen.getStackTraceExecutables()
								.add(Hook.builder().name(executable.getName()).location(executable.getLocation())
										.executableType(ExecutableType.HOOK).status(executable.getStatus())
										.errorMessage(executable.getErrorMessage()).build());
					}

					feat.setTotalSteps(feat.getTotalSteps() + 1);
					scen.setTotalSteps(scen.getTotalSteps() + 1);
				}

				if (presentStackExec) {
					feat.getScenarios().add(scen);
					feat.setTotalScenarios(feat.getTotalScenarios() + 1);
				}
			}

			if (!feat.getScenarios().isEmpty())
				data.add(feat);
		}
	}
}
