package Threads;

import java.io.File;
import java.util.Queue;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import IIP.InitialImageProcessing;
import Images.NumberPlateCutout;
import MainLogic.NumberPlateCutoutQueue;
import Settings.Consts;

public class IipWorkerThread extends WorkerThread {
	private Queue<File> m_imageFilesQueue;
	private NumberPlateCutoutQueue m_numberPlateCutoutQueue;
	private InitialImageProcessing m_initialImageProcessing;

	public IipWorkerThread(Queue<File> imageFilesQueue, NumberPlateCutoutQueue numberPlateCutoutQueue) {
		m_imageFilesQueue = imageFilesQueue;
		m_numberPlateCutoutQueue = numberPlateCutoutQueue;
		m_initialImageProcessing = new InitialImageProcessing();
	}

	@Override
	protected void doWork() {

		try {
			File file = m_imageFilesQueue.poll();
			if (file == null) {
				Thread.sleep(Consts.ThreadSleepTime);
				return;
			}

			Mat rawImage = Imgcodecs.imread(file.getPath());

			NumberPlateCutout[] numberPlateCutouts = m_initialImageProcessing.processImage(rawImage);
			if (numberPlateCutouts != null && numberPlateCutouts.length > 0)
				for (NumberPlateCutout numberPlateCutout : numberPlateCutouts) {
					m_numberPlateCutoutQueue.addNumberPlateCutout(numberPlateCutout);	
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
