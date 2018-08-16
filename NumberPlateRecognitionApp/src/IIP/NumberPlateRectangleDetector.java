package IIP;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import Images.NumberPlateCutout;

public class NumberPlateRectangleDetector {
	public NumberPlateCutout[] GetNumberPlateRectangles(Mat rawImage) {
		Mat grayImage = new Mat();
		Imgproc.cvtColor(rawImage, grayImage, Imgproc.COLOR_BGR2GRAY);
	
		
		Mat blurImage = new Mat();
		Imgproc.medianBlur(grayImage, blurImage, 3);
		
		
		Mat normalizedImage = new Mat();
		Imgproc.threshold(blurImage, normalizedImage, 70, 210, Imgproc.THRESH_BINARY);

		
		double threshold = 70;
		Mat canny1 = new Mat();
		Imgproc.Canny(normalizedImage, canny1, threshold, threshold * 3);
		return null;
	}
}
