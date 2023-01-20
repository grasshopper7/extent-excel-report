package tech.grasshopper.extent.data.pojo;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public abstract class Executable extends BaseEntity {

	protected Feature feature;
	protected Scenario scenario;

	protected Status status;

	@Default
	protected String errorMessage = "";

	@Default
	protected String location = "";

	private ExecutableType executableType;

	public static enum ExecutableType {
		STEP, HOOK;
	}
}
