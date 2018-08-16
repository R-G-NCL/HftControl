package FaultTolerance;
import org.opencv.core.Mat;
import IIP.IipAlgorithms;
import IIP.InitialImageProcessing;
import OCR.OcrAlgorithms;
import OCR.OpticalCharacterRecognition;
import OCR.RecognitionResult;
import StubImages.NumberPlateCutout;

public privileged aspect ErrorHandlingAgent {
	private static IHolisticFaultToleranceController m_hftController;

	public static void setHftController(IHolisticFaultToleranceController hftController) {
		m_hftController = hftController;
	}

	public static void addError(ErrorType errorType, int indexId) {
		m_hftController.errorDetected(errorType, indexId);
	}

	NumberPlateCutout[] around(InitialImageProcessing iipLogic, Mat rawImage, IipAlgorithms iipAlgorithm) : Pointcuts.processImage(iipLogic, rawImage, iipAlgorithm){
		int attemptNumber = 0;

		while (true) {
			try {
				attemptNumber++;
				NumberPlateCutout[] result = proceed(iipLogic, rawImage, iipAlgorithm);
				return result;
			}
			catch (Exception exception) {
				RecoveryAction recoveryAction = m_hftController.getIipRecoveryAction(iipAlgorithm, exception, attemptNumber);
				if (recoveryAction == RecoveryAction.Retry)
					continue;
				else if (recoveryAction == RecoveryAction.TryNextAlgorithm) {
					try {
						if (attemptNumber <= m_hftController.getNumberOfRetriesForIip()) {
							if (iipAlgorithm == IipAlgorithms.RectangleDetection)
								return iipLogic.getNumberPlateArea(rawImage);
							else if (iipAlgorithm == IipAlgorithms.HaarCascade)
								return iipLogic.getNumberPlateRectangles(rawImage);
						}
					}
					catch (Exception retryException) {
						continue;
					}
				}
				return null;
			}
		}
	}
	
	RecognitionResult around(OpticalCharacterRecognition ocrLogic, Mat numberPlateCutoutImage, OcrAlgorithms ocrAlgorithm) : 
		Pointcuts.recognizeNumberPlate(ocrLogic, numberPlateCutoutImage, ocrAlgorithm){
		int attemptNumber = 0;

		while (true) {
			try {
				attemptNumber++;
				RecognitionResult result = proceed(ocrLogic, numberPlateCutoutImage, ocrAlgorithm);
				return result;
			}
			catch (Exception exception) {
				RecoveryAction recoveryAction = m_hftController.getOcrRecoveryAction(ocrAlgorithm, exception, attemptNumber);
				if (recoveryAction == RecoveryAction.Retry)
					continue;
				else if (recoveryAction == RecoveryAction.TryNextAlgorithm) {
					try {
						if (attemptNumber <= m_hftController.getNumberOfRetriesForIip()) {
							if (ocrAlgorithm == OcrAlgorithms.HaarCascade)
								return ocrLogic.recogniseByTesseract(numberPlateCutoutImage);
							else if (ocrAlgorithm == OcrAlgorithms.Tesseract)
								return ocrLogic.recogniseByTesseractSingleSymbols(numberPlateCutoutImage);
							else if (ocrAlgorithm == OcrAlgorithms.TesseractSingleSymbols)
								return ocrLogic.recogniseByHaarCascade(numberPlateCutoutImage);
						}
					}
					catch (Exception retryException) {
						continue;
					}
				}
				return null;
			}
		}
	}
}
