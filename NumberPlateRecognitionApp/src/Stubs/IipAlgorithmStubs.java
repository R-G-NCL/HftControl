package Stubs;

import java.util.Random;

import org.opencv.core.Mat;

import FaultInjection.CpuException;
import IIP.IipAlgorithms;
import Images.NumberPlateCutout;
import Images.Quality;

public final class IipAlgorithmStubs {
	public static NumberPlateCutout[] processImage(Mat rawImage, IipAlgorithms iipAlgorithm) throws CpuException {
		// FaultInjector.TryInject(Consts.IipFaultInjectionProbability);
		NumberPlateCutout[] result;
		Random random = new Random();
		result = new NumberPlateCutout[] { new NumberPlateCutout(1, null, Quality.Medium) };
		int sleepTime = 0;
		if (iipAlgorithm == IipAlgorithms.RectangleDetection) {
			sleepTime = 70 + random.nextInt(30);
		}
		else if (iipAlgorithm == IipAlgorithms.HaarCascade) {
			sleepTime = 60 + random.nextInt(30);
		}
		if (sleepTime == 0)
			result = null;

		try {
			Thread.sleep(sleepTime);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

		return result;
	}
}
