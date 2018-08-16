package IIP;

import org.opencv.core.Mat;

import FaultInjection.CpuException;
import FaultInjection.FaultInjector;
import Settings.Consts;
import StubImages.NumberPlateCutout;
import Stubs.IipAlgorithmStubs;

public class InitialImageProcessing {

	private NumberPlateRectangleDetector m_numberPlateRectangleDetector;
	private NumberPlateAreaHaarDetector m_numberPlateAreaHaarDetector;
	private static IipAlgorithms m_currentIipAlgorithm;

	public InitialImageProcessing() {
		m_numberPlateRectangleDetector = new NumberPlateRectangleDetector();
		m_numberPlateAreaHaarDetector = new NumberPlateAreaHaarDetector();
		m_currentIipAlgorithm = IipAlgorithms.RectangleDetection;
	}

	private NumberPlateCutout[] getNumberPlateRectangles(Mat rawImage) {
		return m_numberPlateRectangleDetector.GetNumberPlateRectangles(rawImage);
	}

	private NumberPlateCutout[] getNumberPlateArea(Mat rawImage) {
		return m_numberPlateAreaHaarDetector.GetPossibleNumberPlateArea(rawImage);
	}

	private NumberPlateCutout[] processImageOriginal(Mat rawImage, IipAlgorithms iipAlgorithm) throws CpuException {
		FaultInjector.TryInject(Consts.IipFaultInjectionProbability);
		NumberPlateCutout[] result;
		if (iipAlgorithm == IipAlgorithms.RectangleDetection)
			result = getNumberPlateRectangles(rawImage);
		else if (iipAlgorithm == IipAlgorithms.HaarCascade)
			result = getNumberPlateArea(rawImage);
		else
			result = null;

		return result;
	}
	
	public static void SetConfiguration(IipAlgorithms currentIipAlgorithm) {
		m_currentIipAlgorithm = currentIipAlgorithm;
	}

	public NumberPlateCutout[] processImage(Mat rawImage) throws CpuException {
		return IipAlgorithmStubs.processImage(rawImage, m_currentIipAlgorithm);
	}
}
