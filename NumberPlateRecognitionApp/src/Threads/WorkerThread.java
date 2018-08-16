package Threads;
public abstract class WorkerThread extends Thread {

	protected boolean m_isStopped = false;

	public void run() {
		while (!m_isStopped) {
			doWork();
		}
		m_isStopped = true;
	}

	protected abstract void doWork();

	public void stopWork() {
		m_isStopped = true;
	}
}
