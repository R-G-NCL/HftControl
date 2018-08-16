package Settings;

public final class Consts {
	public static final String FolderWithImages = "E:\\ThesisExperiments\\CarPhotos";
	
	public static final int ThreadSleepTime = 10;
	
	//Fault injection
	public static final float IipFaultInjectionProbability = 0.05f;
	public static final float OcrFaultInjectionProbability = 0.05f;
	
	//IIP HAAR detection
	public static final String NumberPlateCascadeClassifier = "NumberPlate.xml";
	
	//IIP Rectangle detection
	
	//OCR Tesseract
	public static final String GbNumberPlatesLanguage = "gb_number_plates";
	
	//HFT controller	
	public static final int HftTimerInterval = 50;
	public static final int NumberPlatesQueueUpperSize = 5;
	public static final int NumberPlatesQueueLowerSize = 3;
	public static final int NumberOfRetriesForIip = 3;
	public static final int NumberOfRetriesForOcr = 3;

	public static final double IipSuccessRateThreshold = 0.55;
	public static final double OcrSuccessRateThreshold = 0.62;
	
	
	public static final int InitialNumberOfThreadsIip = 16;
	public static final int InitialNumberOfThreadsOcr = 7;
}
