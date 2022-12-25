package tech.grasshopper.extent.data.pojo;

import java.awt.Color;

public enum Status {

	PASSED, SKIPPED, PENDING, UNDEFINED, AMBIGUOUS, FAILED, UNUSED;

	public static Color getStatusColor(Status status) {

		if (status == PASSED)
			return Color.GREEN;
		else if (status == SKIPPED)
			return Color.YELLOW;
		if (status == FAILED)
			return Color.RED;

		return Color.BLACK;
	}
}
