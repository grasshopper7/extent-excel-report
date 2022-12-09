package tech.grasshopper.extent.data.generator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.aventstack.extentreports.model.Report;
import com.aventstack.extentreports.model.ReportStats;

import lombok.Builder;
import tech.grasshopper.excel.report.util.DateUtil;
import tech.grasshopper.extent.data.SheetData.BasicDashboardData;
import tech.grasshopper.extent.data.SheetData.CountData;
import tech.grasshopper.extent.data.SheetData.TimingData;
import tech.grasshopper.extent.data.pojo.Feature;
import tech.grasshopper.extent.data.pojo.Scenario;

@Builder
public class DashboardDataPopulator {

	private List<Feature> features;

	public BasicDashboardData populateDashboardData(Report report) {

		ReportStats stats = report.getStats();
		LocalDateTime startTime = DateUtil.convertToLocalDateTimeFromDate(report.getStartTime());
		LocalDateTime endTime = DateUtil.convertToLocalDateTimeFromDate(report.getEndTime());
		TimingData timingData = TimingData.builder().startTime(startTime).endTime(endTime)
				.duration(Duration.between(startTime, endTime)).build();

		BasicDashboardData dashboardData = BasicDashboardData.builder().timingData(timingData)
				.featureCounts(createCountData(stats.getParent())).scenarioCounts(createCountData(stats.getChild()))
				.build();

		CountData stepCounts = CountData.builder().build();
		for (Feature feature : features) {
			for (Scenario scenario : feature.getScenarios()) {
				stepCounts.setPassed(stepCounts.getPassed() + scenario.getPassedSteps());
				stepCounts.setFailed(stepCounts.getFailed() + scenario.getFailedSteps());
				stepCounts.setSkipped(stepCounts.getSkipped() + scenario.getSkippedSteps());
				stepCounts.setTotal(stepCounts.getTotal() + scenario.getTotalSteps());
			}
		}
		dashboardData.setStepCounts(stepCounts);
		return dashboardData;
	}

	private CountData createCountData(Map<com.aventstack.extentreports.Status, Long> statusCounts) {

		Long total = statusCounts.getOrDefault(com.aventstack.extentreports.Status.PASS, 0L)
				+ statusCounts.getOrDefault(com.aventstack.extentreports.Status.FAIL, 0L)
				+ statusCounts.getOrDefault(com.aventstack.extentreports.Status.SKIP, 0L);

		return CountData.builder().total(total)
				.passed(statusCounts.getOrDefault(com.aventstack.extentreports.Status.PASS, 0L))
				.failed(statusCounts.getOrDefault(com.aventstack.extentreports.Status.FAIL, 0L))
				.skipped(statusCounts.getOrDefault(com.aventstack.extentreports.Status.SKIP, 0L)).build();
	}
}
