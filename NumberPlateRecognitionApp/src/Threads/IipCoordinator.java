package Threads;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import MainLogic.NumberPlateCutoutQueue;

public class IipCoordinator extends WorkerThreadCoordinator {
	private ConcurrentLinkedQueue<File> m_imageFilesQueue;
	private NumberPlateCutoutQueue m_numberPlateCutoutQueue;

	public IipCoordinator(String folderName, NumberPlateCutoutQueue numberPlateCutoutQueue, int numberOfThreads) {
		super(numberOfThreads);

		File folderWithImages = new File(folderName);
		m_imageFilesQueue = new ConcurrentLinkedQueue<File>();
		for (int i = 0; i < 10; i++) {
			for (File imageFile : folderWithImages.listFiles()) {
				if (!imageFile.isFile())
					continue;
				m_imageFilesQueue.add(imageFile);
			}
		}
		m_numberPlateCutoutQueue = numberPlateCutoutQueue;
	}

	@Override
	public WorkingState getState() {
		if (m_imageFilesQueue.size() > 0)
			return WorkingState.Processing;
		// ToDo: Add logic for WorkingState.WaitingForTasks if necessary
		// (if there is a stream of images rather than just a folder)
		m_numberPlateCutoutQueue.sealQueue();
		return WorkingState.Completed;
	}

	@Override
	protected WorkerThread createWorkerThread() {
		return new IipWorkerThread(m_imageFilesQueue, m_numberPlateCutoutQueue);
	}

	@Override
	protected String getName() {
		return "IIP";
	}
}
