package Images;

import java.util.List;

import org.opencv.core.Mat;

public class NumberPlateCutout {
	public int Index;
	public List<Mat> OperationHistory;
	public Mat Image;
	public Quality Quality;

	public NumberPlateCutout(int index, Mat image, Quality quality) {
		Index = index;
		Image = image;
		Quality = quality;
	}
}
