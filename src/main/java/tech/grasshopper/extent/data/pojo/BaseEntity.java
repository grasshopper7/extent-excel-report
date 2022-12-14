package tech.grasshopper.extent.data.pojo;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.exception.ExcelReportException;

@Data
@SuperBuilder
public abstract class BaseEntity {

	protected String name;

	protected Status status;

	protected LocalDateTime startTime;
	protected LocalDateTime endTime;

	public Duration calculatedDuration() {

		return Duration.between(startTime, endTime);
	}

	protected void checkTimeData() {

		if (startTime == null)
			throw new ExcelReportException(
					"Start Time not present for " + this.getClass().getName() + " - " + getName());

		if (endTime == null)
			throw new ExcelReportException("End Time not present for " + this.getClass().getName() + " - " + getName());

		if (startTime.compareTo(endTime) > 0)
			throw new ExcelReportException(
					"Start Time is later than End time for " + this.getClass().getName() + " - " + getName());
	}

	public void checkData() {
		checkTimeData();
	}
}
