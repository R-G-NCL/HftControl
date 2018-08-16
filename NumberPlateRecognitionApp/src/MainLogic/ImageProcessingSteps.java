package MainLogic;

import java.util.ArrayList;

import org.opencv.core.Mat;

import OCR.RecognitionResult;

public class ImageProcessingSteps {
	private String m_imageFileName;
	private Mat m_originalImage;
	private ArrayList<Mat> m_imageProcessingSteps;
	private Mat m_numberPlateCutout;
	private Mat m_numberPlateCutoutWithOcr;
	private RecognitionResult m_recognitionResult;

	public ImageProcessingSteps(String imageFileName, Mat originalImage){
		m_imageFileName = imageFileName;
		m_originalImage = originalImage;
	}
	
	
	
}
