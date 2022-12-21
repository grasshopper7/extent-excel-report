package tech.grasshopper.excel.report.sheets;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor;

public class ComponentShifter {

	public static void shiftRows(XSSFSheet sheet, int startRow, int shiftRowCnt) {

		List<CTTwoCellAnchor> drawingAnchors = sheet.getDrawingPatriarch().getCTDrawing().getTwoCellAnchorList();

		for (CTTwoCellAnchor drawingAnchor : drawingAnchors) {
			int fromRow = drawingAnchor.getFrom().getRow();
			int toRow = drawingAnchor.getTo().getRow();
			if (fromRow >= startRow) {
				drawingAnchor.getFrom().setRow(fromRow + shiftRowCnt);
				drawingAnchor.getTo().setRow(toRow + shiftRowCnt);
			}
		}
		sheet.shiftRows(startRow, sheet.getLastRowNum(), shiftRowCnt);
	}
}
