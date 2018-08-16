package FaultTolerance;

import java.util.concurrent.atomic.AtomicLong;

public class PerformanceStatistics {

	private AtomicLong m_executionsNumber = new AtomicLong();
	private AtomicLong m_executionTimeSum = new AtomicLong();
	public long m_minExecutionTime;
	public long m_maxExecutionTime;

	private Object m_minExecutionTimeLock = new Object();
	private Object m_maxExecutionTimeLock = new Object();

	public float getAverageExecutionTime() {
		if (m_executionsNumber.get() == 0)
			return 0;

		return (float) m_executionTimeSum.get() / (float) m_executionsNumber.get();
	}

	public long getExecutionsNumber() {
		return m_executionsNumber.get();
	}

	public PerformanceStatistics() {
		clear();
	}

	public PerformanceStatistics(long executionTime) {
		clear();
		addExecutionTime(executionTime);
	}

	public void addExecutionTime(long executionTime) {
		if (executionTime < m_minExecutionTime)
			synchronized (m_minExecutionTimeLock) {
				if (executionTime < m_minExecutionTime)
					m_minExecutionTime = executionTime;
			}

		if (executionTime > m_maxExecutionTime)
			synchronized (m_maxExecutionTimeLock) {
				if (executionTime > m_maxExecutionTime)
					m_maxExecutionTime = executionTime;
			}

		m_executionsNumber.incrementAndGet();
		m_executionTimeSum.addAndGet(executionTime);
	}

	public void clear() {
		m_minExecutionTime = Long.MAX_VALUE;
		m_maxExecutionTime = 0;
		m_executionsNumber.set(0);
		m_executionTimeSum.set(0);
	}
}
