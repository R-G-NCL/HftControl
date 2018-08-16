package Tests;

import static org.junit.Assert.*;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Test;
import org.opencv.core.Core;

import MainLogic.NumberPlateCutoutQueue;
import OCR.RecognitionResult;
import Settings.Consts;
import StubImages.NumberPlateCutout;
import StubImages.Quality;
import Threads.OcrCoordinator;
import net.sourceforge.tess4j.Tesseract;

public class OcrTests {

	@Test
	public void test() {
		assertEquals(1, 1);
	}

	@Test
	public void testOcrCoordinator()
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		NumberPlateCutoutQueue cutoutQueue = new NumberPlateCutoutQueue();
		for (int i = 0; i < 50; i++) {
			cutoutQueue.addNumberPlateCutout(new NumberPlateCutout(1, null, Quality.Medium));
		}
		cutoutQueue.sealQueue();
		
		OcrCoordinator ocrCoordinator = new OcrCoordinator(cutoutQueue, 5);
		ocrCoordinator.start();
		ocrCoordinator.waitToFinish();
		assertEquals(1, 1);
		assertFalse(ocrCoordinator.getResultsQueue().isEmpty());
	}
}
