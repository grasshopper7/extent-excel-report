package tech.grasshopper.excel.report.table;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.Builder;
import tech.grasshopper.extent.data.pojo.Feature;

@Builder
public class TagFailSkipTable {
	
	private XSSFSheet sheet;
	
	private String startCell;
	
	private Map<String, Feature> failSkipFeatureAndScenarioTagData;
	
	public void writeTableValues() {
	
		
	}
}
