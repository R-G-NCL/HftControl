package IIP;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import FaultInjection.CpuException;
import FaultInjection.FaultInjector;
import Images.NumberPlateCutout;
import Images.Quality;
import Settings.Consts;

public class NumberPlateAreaHaarDetector {

	private CascadeClassifier m_numberPlatesCascade;

	public NumberPlateAreaHaarDetector() {
		m_numberPlatesCascade = new CascadeClassifier(Consts.NumberPlateCascadeClassifier);
	}

	public NumberPlateCutout[] GetPossibleNumberPlateArea(Mat originalImage) {
		Mat grayImage = new Mat();
		Imgproc.cvtColor(originalImage, grayImage, Imgproc.COLOR_BGR2GRAY);

		/*
		 * Mat equalizeHistImage = new Mat(); Imgproc.equalizeHist(grayImage,
		 * equalizeHistImage);
		 */

		MatOfRect numberPlates = new MatOfRect();
		//ToDo: Use settings
		m_numberPlatesCascade.detectMultiScale(grayImage, numberPlates, 1.1, 2, 0, new Size(10, 50), new Size());

		Rect[] platesArray = numberPlates.toArray();
		if (platesArray.length == 0)
			return new NumberPlateCutout[0];

		NumberPlateCutout[] results = new NumberPlateCutout[platesArray.length];
		for (int i = 0; i < results.length; i++) {
			Mat numberPlateArea = originalImage.submat(platesArray[i]);

			// ToDo
			int index = 7;
			Quality quality = Quality.Medium;
			results[i] = new NumberPlateCutout(index, numberPlateArea, quality);
		}
		return results;
	}
}
