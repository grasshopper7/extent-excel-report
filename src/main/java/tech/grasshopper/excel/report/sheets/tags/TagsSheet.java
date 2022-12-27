package tech.grasshopper.excel.report.sheets.tags;

import lombok.experimental.SuperBuilder;
import tech.grasshopper.excel.report.sheets.Sheet;

@SuperBuilder
public class TagsSheet extends Sheet {

	@Override
	public void updateSheet() {

		if (reportData.getTagData().isEmpty()) {
			deleteSheet(TAGS_SHEET);
			return;
		}
	}
}
