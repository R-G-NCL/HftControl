package FaultTolerance;

import IIP.IipAlgorithms;
import OCR.OcrAlgorithms;

public interface IHolisticFaultToleranceController {
	public void errorDetected(ErrorType Type, int indexId);
	public RecoveryAction getIipRecoveryAction(IipAlgorithms iipAlgorithm, Exception exception, int attemptNumber);
	public RecoveryAction getOcrRecoveryAction(OcrAlgorithms ocrAlgorithm, Exception exception, int attemptNumber);
	void updateIipPerformanceInfo(IipAlgorithms iipAlgorithms, long executionTime);
	void updateOcrPerformanceInfo(OcrAlgorithms ocrAlgorithms, long executionTime);
	int getNumberOfRetriesForIip();
	int getNumberOfRetriesForOcr();
}
