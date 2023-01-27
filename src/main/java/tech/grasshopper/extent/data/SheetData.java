package tech.grasshopper.extent.data;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import tech.grasshopper.extent.data.pojo.Status;

public class SheetData {

	@Data
	@Builder
	public static class BasicDashboardData {

		@Default
		private String title = "";

		private TimingData timingData;

		private CountData featureCounts;
		private CountData scenarioCounts;
		private CountData stepCounts;
	}

	@Data
	@Builder
	public static class TimingData {

		@Default
		private LocalDateTime startTime = LocalDateTime.now();
		@Default
		private LocalDateTime endTime = LocalDateTime.now();
		@Default
		private Duration duration = Duration.ZERO;
	}

	@Data
	@Builder
	public static class CountData {

		@Default
		private Long total = 0L;
		@Default
		private Long passed = 0L;
		@Default
		private Long failed = 0L;
		@Default
		private Long skipped = 0L;

		public String getPassPercent() {
			if (total == 0L || passed == 0L)
				return 0 + "%";
			return (Math.round((100.0 * passed) / total)) + "%";
		}
	}

	@Data
	@Builder
	public static class FailSkipData {

		private String scenarioName;
		private Status scenarioStatus;
		private String featureName;
		private Status featureStatus;
	}

	@Data
	@Builder
	public static class TagCountData {

		private String name;
		private CountData scenarioCounts;
	}

	@Data
	@Builder
	public static class FeatureData {

		private String name;
		private Status status;

		private TimingData timingData;

		private CountData scenarioCounts;
		private CountData stepCounts;
	}

	@Data
	@Builder
	public static class ScenarioData {

		private String name;
		private Status status;

		private String featureName;
		private Status featureStatus;

		private TimingData timingData;

		private CountData stepCounts;
	}
}
