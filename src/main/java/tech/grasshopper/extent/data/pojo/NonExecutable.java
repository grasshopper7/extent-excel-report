package tech.grasshopper.extent.data.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public abstract class NonExecutable extends BaseEntity {

	@Default
	protected List<String> tags = new ArrayList<>();
	@Default
	private List<String> authors = new ArrayList<>();
	@Default
	private List<String> devices = new ArrayList<>();
}
