package Threads;

import java.util.concurrent.ConcurrentLinkedQueue;

import MainLogic.NumberPlateCutoutQueue;
import OCR.RecognitionResult;

public class OcrCoordinator extends WorkerThreadCoordinator {
	private NumberPlateCutoutQueue m_numberPlateCutoutQueue;
	private ConcurrentLinkedQueue<RecognitionResult> m_resultsQueue;

	public OcrCoordinator(NumberPlateCutoutQueue numberPlateCutoutQueue, int numberOfThreads) {
		super(numberOfThreads);
		m_numberPlateCutoutQueue = numberPlateCutoutQueue;
		m_resultsQueue = new ConcurrentLinkedQueue<RecognitionResult>();
	}

	public ConcurrentLinkedQueue<RecognitionResult> getResultsQueue() {
		return m_resultsQueue;
	}

	@Override
	public WorkingState getState() {
		if (m_numberPlateCutoutQueue.getQueueSize() > 0)
			return WorkingState.Processing;

		if (m_numberPlateCutoutQueue.newItemsAreExpected())
			return WorkingState.WaitingForTasks;

		return WorkingState.Completed;
	}

	@Override
	protected WorkerThread createWorkerThread() {
		return new OcrWorkerThread(m_numberPlateCutoutQueue, m_resultsQueue);
	}

	@Override
	protected String getName() {
		return "OCR";
	}
}
