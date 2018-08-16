package OCR;

import java.awt.Rectangle;
import java.util.List;
import java.util.stream.Collectors;

import net.sourceforge.tess4j.Word;

public class RecognitionResult {
	private OcrAlgorithms m_ocrAlgorithm;
	private String m_text;
	private float m_confidence;
	private Rectangle m_boundingBox;
	private List<Rectangle> m_symbolBoundingBoxes;

	public RecognitionResult(OcrAlgorithms ocrAlgorithm, Word recognisedNumber, List<Word> symbols) {
		m_ocrAlgorithm = ocrAlgorithm;
		m_text = recognisedNumber.getText();
		m_confidence = recognisedNumber.getConfidence();
		m_boundingBox = recognisedNumber.getBoundingBox();
		m_symbolBoundingBoxes = symbols.stream().map(symbol -> symbol.getBoundingBox()).collect(Collectors.toList());
	}

	public OcrAlgorithms GetAlgorithm() {
		return m_ocrAlgorithm;
	}

	public String getText() {
		return m_text;
	}

	public float getConfidence() {
		return m_confidence;
	}

	public Rectangle getBoundingBox() {
		return m_boundingBox;
	}

	public List<Rectangle> getSymbolBoundingBoxes() {
		return m_symbolBoundingBoxes;
	}

	public void correctText(String newText) {
		m_text = newText;
	}
}
