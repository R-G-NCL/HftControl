package Stubs;

import java.util.ArrayList;
import java.util.Random;

import org.opencv.core.Mat;

import FaultInjection.CpuException;
import OCR.OcrAlgorithms;
import OCR.RecognitionResult;
import net.sourceforge.tess4j.Word;

public final class OcrAlgorithmStubs {

	public static RecognitionResult recogniseNumberPlate(Mat numberPlateCutoutImage, OcrAlgorithms ocrAlgorithm)
			throws CpuException {
		// FaultInjector.TryInject(Consts.OcrFaultInjectionProbability);
		RecognitionResult result;

		Random random = new Random();
		result = new RecognitionResult(OcrAlgorithms.Tesseract, new Word("word", 0.85f, null), new ArrayList<Word>());
		int sleepTime = 0;

		if (ocrAlgorithm == OcrAlgorithms.HaarCascade)
			sleepTime = 25 + random.nextInt(10);
		else if (ocrAlgorithm == OcrAlgorithms.Tesseract)
			sleepTime = 25 + random.nextInt(20);
		else if (ocrAlgorithm == OcrAlgorithms.TesseractSingleSymbols)
			sleepTime = 30 + random.nextInt(20);
		else
			result = null;

		if (sleepTime == 0)
			return null;

		try {
			Thread.sleep(sleepTime);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

		return result;
	}
}
