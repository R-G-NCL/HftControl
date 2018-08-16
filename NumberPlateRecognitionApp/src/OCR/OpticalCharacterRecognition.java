package OCR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.opencv.core.Mat;

import FaultInjection.CpuException;
import FaultInjection.FaultInjector;
import IIP.IipAlgorithms;
import Settings.Consts;
import StubImages.MatHelper;
import Stubs.OcrAlgorithmStubs;
import net.sourceforge.tess4j.Word;

public class OpticalCharacterRecognition {
	private TesseractRecognition m_tesseractRecognition;
	private Map<Character, Character> m_similarSymbols;
	private Map<Character, Character> m_similarDigits;

	private static OcrAlgorithms m_currentOcrAlgorithm;

	public OpticalCharacterRecognition() {
		//m_tesseractRecognition = new TesseractRecognition();
		initialiseSimilarSymbols();
		m_currentOcrAlgorithm = OcrAlgorithms.Tesseract;
	}

	private void initialiseSimilarSymbols() {
		m_similarSymbols = new HashMap<Character, Character>();
		m_similarSymbols.put('0', 'O');
		m_similarSymbols.put('1', 'I');
		m_similarSymbols.put('2', 'Z');
		m_similarSymbols.put('5', 'S');
		m_similarSymbols.put('8', 'B');

		m_similarDigits = new HashMap<Character, Character>();
		for (Character key : m_similarSymbols.keySet()) {
			m_similarDigits.put(m_similarSymbols.get(key), key);
		}
	}

	private void correctResult(RecognitionResult result) {
		String text = result.getText();
		if (text.length() != 7)
			return;

		char[] textChars = text.toCharArray();
		for (int i = 0; i < 7; i++) {
			if (i == 2 || i == 3) {
				if (m_similarDigits.containsKey(textChars[i]))
					textChars[i] = m_similarDigits.get(textChars[i]);
				continue;
			}

			if (m_similarSymbols.containsKey(textChars[i]))
				textChars[i] = m_similarSymbols.get(textChars[i]);
		}

		String newText = new String(textChars);
		if (newText.equals(text))
			result.correctText(newText);
	}

	private RecognitionResult recogniseByHaarCascade(Mat numberPlateCutoutImage) {
		return null;
	}

	private RecognitionResult recogniseByTesseract(Mat numberPlateCutoutImage) {
		return null;
	}

	private RecognitionResult recogniseByTesseractSingleSymbols(Mat numberPlateCutoutImage) {
		return m_tesseractRecognition.recogniseNumberPlate(MatHelper.matToBufferedImage(numberPlateCutoutImage));
	}

	public static void SetConfiguration(OcrAlgorithms currentOcrAlgorithm) {
		m_currentOcrAlgorithm = currentOcrAlgorithm;
	}

	private RecognitionResult recogniseNumberPlate(Mat numberPlateCutoutImage, OcrAlgorithms ocrAlgorithms) throws CpuException {
		FaultInjector.TryInject(Consts.OcrFaultInjectionProbability);
		RecognitionResult result;
		
		if (m_currentOcrAlgorithm == OcrAlgorithms.HaarCascade)
			result = recogniseByHaarCascade(numberPlateCutoutImage);
		else if (m_currentOcrAlgorithm == OcrAlgorithms.Tesseract)
			result = recogniseByTesseract(numberPlateCutoutImage);
		else if (m_currentOcrAlgorithm == OcrAlgorithms.TesseractSingleSymbols)
			result = recogniseByTesseractSingleSymbols(numberPlateCutoutImage);
		else
			result = null;

		if (result != null)
			correctResult(result);

		return result;
	}
	
	public RecognitionResult recognizeNumberPlate(Mat numberPlateCutoutImage) throws CpuException {
		return OcrAlgorithmStubs.recogniseNumberPlate(numberPlateCutoutImage, m_currentOcrAlgorithm);
	}
}
