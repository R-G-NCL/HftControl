package Threads;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import Settings.Consts;

public abstract class WorkerThreadCoordinator {
	protected ArrayList<WorkerThread> m_workerThreads;
	private int m_initialNumberOfThreads;

	public WorkerThreadCoordinator(int numberOfThreads) {
		m_workerThreads = new ArrayList<WorkerThread>();
		m_initialNumberOfThreads = numberOfThreads;
	}

	public abstract WorkingState getState();

	protected abstract WorkerThread createWorkerThread();

	protected abstract String getName();

	public void setNumberOfWorkingThreads(int numberOfThreads) {

		if (numberOfThreads < 1 || numberOfThreads > 100) {
			if (numberOfThreads < 0 || numberOfThreads > 100)
				System.out.println(String.format("Strange number of threads for %s: %d", getName(), numberOfThreads));
			return;
		}
		System.out.println(String.format("Number of threads for %s: %d", getName(), numberOfThreads));

		synchronized (m_workerThreads) {
			int difference = m_workerThreads.size() - numberOfThreads;

			if (difference > 0) {
				for (int i = 0; i < difference; i++) {
					WorkerThread workerThread = m_workerThreads.remove(0);
					workerThread.stopWork();
				}
			}
			else if (difference < 0) {
				for (int i = 0; i < -difference; i++) {
					WorkerThread workerThread = createWorkerThread();
					m_workerThreads.add(workerThread);
					workerThread.start();
				}
			}
		}
	}

	public void incrementNumberOfThreads() {
		setNumberOfWorkingThreads(getNumberOfThreads() + 1);
	}

	public void decrementNumberOfThreads() {
		setNumberOfWorkingThreads(getNumberOfThreads() - 1);
	}

	public void start() {
		setNumberOfWorkingThreads(m_initialNumberOfThreads);
	}

	public int getNumberOfThreads() {
		synchronized (m_workerThreads) {
			return m_workerThreads.size();
		}
	}

	public void waitToFinish() {
		while (true) {
			if (getState() != WorkingState.Completed) {
				try {
					Thread.sleep(Consts.ThreadSleepTime);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			break;
		}
		synchronized (m_workerThreads) {
			for (WorkerThread workerThread : m_workerThreads) {
				workerThread.stopWork();
				if (workerThread.isAlive())
					try {
						workerThread.join();
					}
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
	}
}
