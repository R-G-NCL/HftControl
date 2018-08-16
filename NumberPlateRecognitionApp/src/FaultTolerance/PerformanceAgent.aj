package FaultTolerance;

import org.opencv.core.Mat;

import IIP.IipAlgorithms;
import IIP.InitialImageProcessing;
import OCR.OcrAlgorithms;
import OCR.OpticalCharacterRecognition;
import OCR.RecognitionResult;
import StubImages.NumberPlateCutout;

public aspect PerformanceAgent {
	private static IHolisticFaultToleranceController m_hftController;

	public static void setHftComponent(IHolisticFaultToleranceController hftComponent) {
		m_hftController = hftComponent;
	}

	NumberPlateCutout[] around(InitialImageProcessing iipLogic, Mat rawImage, IipAlgorithms iipAlgorithm) : 
		Pointcuts.processImage(iipLogic, rawImage, iipAlgorithm){

		long startTime = System.currentTimeMillis();
		NumberPlateCutout[] result = proceed(iipLogic, rawImage, iipAlgorithm);

		long executionTime = System.currentTimeMillis() - startTime;
		m_hftController.updateIipPerformanceInfo(iipAlgorithm, executionTime);

		return result;
	}

	RecognitionResult around(OpticalCharacterRecognition ocrLogic, Mat numberPlateCutoutImage, OcrAlgorithms ocrAlgorithms) : 
		Pointcuts.recognizeNumberPlate(ocrLogic, numberPlateCutoutImage, ocrAlgorithms){

		long startTime = System.currentTimeMillis();
		RecognitionResult result = proceed(ocrLogic, numberPlateCutoutImage, ocrAlgorithms);

		long executionTime = System.currentTimeMillis() - startTime;
		m_hftController.updateOcrPerformanceInfo(ocrAlgorithms, executionTime);

		return result;
	}
}
