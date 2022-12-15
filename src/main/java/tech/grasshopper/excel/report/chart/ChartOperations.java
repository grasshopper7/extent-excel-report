package tech.grasshopper.excel.report.chart;

import java.util.List;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import lombok.Builder;
import lombok.Data;

@Builder
public class ChartOperations {

	private XSSFSheet chartSheet;

	private XSSFSheet dataSheet;

	public void updateBarChartPlot(int chartIndex, ChartDataSeriesRange categoryRange,
			List<ChartDataSeriesRange> valueRanges) {

		XSSFDrawing drawing = chartSheet.getDrawingPatriarch();
		XSSFChart chart = drawing.getCharts().get(chartIndex);

		List<XDDFChartData> data = chart.getChartSeries();

		XDDFDataSource<String> category = XDDFDataSourcesFactory.fromStringCellRange(dataSheet,
				categoryRange.convertToCellRange());

		for (int i = 0; i < valueRanges.size(); i++) {
			XDDFNumericalDataSource<Double> value = XDDFDataSourcesFactory.fromNumericCellRange(dataSheet,
					valueRanges.get(i).convertToCellRange());

			data.get(0).getSeries(i).replaceData(category, value);
		}

		chart.plot(data.get(0));
	}

	@Builder
	@Data
	public static class ChartDataSeriesRange {

		private int firstRow;

		private int lastRow;

		private int firstColumn;

		private int lastColumn;

		public CellRangeAddress convertToCellRange() {

			return new CellRangeAddress(firstRow, lastRow, firstColumn, lastColumn);
		}

		public static ChartDataSeriesRange convertCellReferenceToChartDataRange(String cellStr, int rows) {

			CellReference cellRef = new CellReference(cellStr);

			return ChartDataSeriesRange.builder().firstRow(cellRef.getRow()).lastRow(cellRef.getRow() + (rows - 1))
					.firstColumn(cellRef.getCol()).lastColumn(cellRef.getCol()).build();
		}
	}
}
