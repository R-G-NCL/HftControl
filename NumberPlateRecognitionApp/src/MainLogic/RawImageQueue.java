package MainLogic;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.opencv.core.Mat;

public class RawImageQueue {
	private ConcurrentLinkedQueue<Mat> m_queue;
	private ConcurrentHashMap<Integer, Mat> m_imagesInProcessing;
	private AtomicInteger m_counter;

	public RawImageQueue() {
		m_queue = new ConcurrentLinkedQueue<Mat>();
		m_imagesInProcessing = new ConcurrentHashMap<Integer, Mat>();
		m_counter = new AtomicInteger();
	}

	public void addImage(Mat rawImage) {
		//rawImage.Index = m_counter.incrementAndGet();
		m_queue.add(rawImage);
	}

	public Mat getImage() {
		Mat result = m_queue.poll();
		/*if (result != null)
			m_imagesInProcessing.put(result.Index, result);*/

		return result;
	}

	public void returnImageToTheQueue(int index) {
		m_queue.add(m_imagesInProcessing.get(index));
	}
	
	public void removeImageFromWaitingQueue(int index) {
		m_imagesInProcessing.remove(index);
	}
}