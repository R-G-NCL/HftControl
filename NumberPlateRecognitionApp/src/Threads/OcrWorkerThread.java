package Threads;

import java.util.concurrent.ConcurrentLinkedQueue;

import Images.NumberPlateCutout;
import MainLogic.NumberPlateCutoutQueue;
import OCR.OpticalCharacterRecognition;
import OCR.RecognitionResult;
import Settings.Consts;

public class OcrWorkerThread extends WorkerThread {
	private NumberPlateCutoutQueue m_numberPlateCutoutQueue;
	private OpticalCharacterRecognition m_opticalCharacterRecognition;
	private ConcurrentLinkedQueue<RecognitionResult> m_resultsQueue;

	public OcrWorkerThread(NumberPlateCutoutQueue numberPlateCutoutQueue, ConcurrentLinkedQueue<RecognitionResult> resultsQueue) {
		m_opticalCharacterRecognition = new OpticalCharacterRecognition();
		m_numberPlateCutoutQueue = numberPlateCutoutQueue;
		m_resultsQueue = resultsQueue;
	}

	@Override
	protected void doWork() {
		try {
			NumberPlateCutout numberPlateCutout = m_numberPlateCutoutQueue.getNumberPlateCutout();

			if (numberPlateCutout == null) {
				Thread.sleep(Consts.ThreadSleepTime);
				return;
			}

			RecognitionResult result = m_opticalCharacterRecognition.recognizeNumberPlate(numberPlateCutout.Image);
			if (result != null)
				m_resultsQueue.add(result);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
