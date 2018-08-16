package FaultTolerance;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import FaultInjection.CpuException;
import IIP.IipAlgorithms;
import MainLogic.NumberPlateCutoutQueue;
import OCR.OcrAlgorithms;
import Settings.Consts;
import Threads.IipCoordinator;
import Threads.WorkingState;
import Threads.OcrCoordinator;

public class HolisticFaultToleranceController implements IHolisticFaultToleranceController {
	private IipCoordinator m_initialImageProcessing;
	private OcrCoordinator m_opticalCharacterRecognition;
	private NumberPlateCutoutQueue m_numberPlateCutoutQueue;
	private Timer m_timer;
	
	private OperationMode m_operationMode;

	private HashMap<IipAlgorithms, PerformanceStatistics> m_iipAlgorithmsPerformance = new HashMap<IipAlgorithms, PerformanceStatistics>();
	private HashMap<OcrAlgorithms, PerformanceStatistics> m_ocrAlgorithmsPerformance = new HashMap<OcrAlgorithms, PerformanceStatistics>();

	public HolisticFaultToleranceController(IipCoordinator initialImageProcessing,
			OcrCoordinator opticalCharacterRecognition, NumberPlateCutoutQueue numberPlateCutoutQueue,
			OperationMode operationMode) {
		m_initialImageProcessing = initialImageProcessing;
		m_opticalCharacterRecognition = opticalCharacterRecognition;
		m_numberPlateCutoutQueue = numberPlateCutoutQueue;
		m_operationMode = operationMode;

		for (IipAlgorithms iipAlgorithm : IipAlgorithms.values())
			m_iipAlgorithmsPerformance.put(iipAlgorithm, new PerformanceStatistics());
		for (OcrAlgorithms ocrAlgorithm : OcrAlgorithms.values())
			m_ocrAlgorithmsPerformance.put(ocrAlgorithm, new PerformanceStatistics());

		PerformanceAgent.setHftComponent(this);
		ErrorHandlingAgent.setHftController(this);

		m_initialImageProcessing.start();
		m_opticalCharacterRecognition.start();

		m_timer = new Timer(true);
		m_timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				try {
					checkNumberPlatesQueueSize();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}, Consts.HftTimerInterval * 8, Consts.HftTimerInterval);
	}

	private void checkNumberPlatesQueueSize() {
		int currentQueueSize = m_numberPlateCutoutQueue.getQueueSize();
		System.out.printf("Number plates queue size:%d\r\n", currentQueueSize);
		if (currentQueueSize >= Consts.NumberPlatesQueueUpperSize) {
			m_initialImageProcessing.decrementNumberOfThreads();
			m_opticalCharacterRecognition.incrementNumberOfThreads();
		} else if (currentQueueSize <= Consts.NumberPlatesQueueLowerSize
				&& m_initialImageProcessing.getState() != WorkingState.Completed) {
			m_initialImageProcessing.incrementNumberOfThreads();
			m_opticalCharacterRecognition.decrementNumberOfThreads();
		}
	}

	public void waitToFinish() {
		m_initialImageProcessing.waitToFinish();
		m_opticalCharacterRecognition.waitToFinish();
		m_timer.purge();
	}

	@Override
	public void errorDetected(ErrorType Type, int indexId) {
		// TODO Auto-generated method stub

	}

	private double getIipSuccessRate(IipAlgorithms iipAlgorithm) {
		return 0.7;
	}

	private double getOcrSuccessRate(OcrAlgorithms ocrAlgorithm) {
		return 0.8;
	}

	private RecoveryAction GetRecoveryAction(Exception exception, int attemptNumber, int numberOfRetries, double currentSuccessRate, double successRateThreshold){
		if (currentSuccessRate > successRateThreshold)
			return RecoveryAction.Skip;

		if (exception instanceof CpuException) {
			if (attemptNumber <= numberOfRetries)
				return RecoveryAction.Retry;
		} else
			return RecoveryAction.TryNextAlgorithm;

		return RecoveryAction.Skip;
	}
	
	@Override
	public RecoveryAction getIipRecoveryAction(IipAlgorithms iipAlgorithm, Exception exception, int attemptNumber) {
		return GetRecoveryAction(exception, attemptNumber, Consts.NumberOfRetriesForIip, getIipSuccessRate(iipAlgorithm), Consts.IipSuccessRateThreshold);
	}

	@Override
	public RecoveryAction getOcrRecoveryAction(OcrAlgorithms ocrAlgorithm, Exception exception, int attemptNumber) {
		return GetRecoveryAction(exception, attemptNumber, Consts.NumberOfRetriesForOcr, getOcrSuccessRate(ocrAlgorithm), Consts.OcrSuccessRateThreshold);
	}

	@Override
	public void updateIipPerformanceInfo(IipAlgorithms iipAlgorithms, long executionTime) {
		m_iipAlgorithmsPerformance.get(iipAlgorithms).addExecutionTime(executionTime);
	}

	@Override
	public void updateOcrPerformanceInfo(OcrAlgorithms ocrAlgorithms, long executionTime) {
		m_ocrAlgorithmsPerformance.get(ocrAlgorithms).addExecutionTime(executionTime);
	}

	@Override
	public int getNumberOfRetriesForIip() {
		return Consts.NumberOfRetriesForIip;
	}

	@Override
	public int getNumberOfRetriesForOcr() {
		return Consts.NumberOfRetriesForOcr;
	}
}
