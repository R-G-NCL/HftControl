package OCR;

import java.awt.image.BufferedImage;
import java.util.List;

import Settings.Consts;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Word;
import net.sourceforge.tess4j.ITessAPI.TessPageIteratorLevel;

public class TesseractRecognition {
	private Tesseract m_tesseract;

	public TesseractRecognition() {
		m_tesseract = new Tesseract();
		m_tesseract.setLanguage(Consts.GbNumberPlatesLanguage);
		
		// Specify m_tesseract.setPageSegMode(10); for single symbol recognition.
	}

	public RecognitionResult recogniseNumberPlate(BufferedImage numberPlateImage) {
		List<Word> words = m_tesseract.getWords(numberPlateImage, TessPageIteratorLevel.RIL_TEXTLINE);
		int resultListSize = words.size();
		if (resultListSize == 0)
			return null;

		List<Word> symbols = m_tesseract.getWords(numberPlateImage, TessPageIteratorLevel.RIL_SYMBOL);
		return new RecognitionResult(OcrAlgorithms.Tesseract, words.get(0), symbols);
	}
}
