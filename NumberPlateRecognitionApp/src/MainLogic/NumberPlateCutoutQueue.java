package MainLogic;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import Images.NumberPlateCutout;

public class NumberPlateCutoutQueue {
	private ConcurrentLinkedQueue<NumberPlateCutout> m_queue;
	private AtomicInteger m_queueSize;
	private boolean m_newItemsAreExpected;

	public NumberPlateCutoutQueue() {
		m_queue = new ConcurrentLinkedQueue<NumberPlateCutout>();
		m_queueSize = new AtomicInteger(0);
		m_newItemsAreExpected = true;
	}

	public void addNumberPlateCutout(NumberPlateCutout numberPlateCutout) {
		m_queue.add(numberPlateCutout);
		m_queueSize.incrementAndGet();
	}

	public NumberPlateCutout getNumberPlateCutout() {
		NumberPlateCutout numberPlateCutout = m_queue.poll();
		if (numberPlateCutout != null)
			m_queueSize.decrementAndGet();
		return numberPlateCutout;
	}

	public int getQueueSize() {
		return m_queueSize.get();
	}
	
	public void sealQueue() {
		m_newItemsAreExpected = false;
	}
	
	public boolean newItemsAreExpected() {
		return m_newItemsAreExpected;
	}
}