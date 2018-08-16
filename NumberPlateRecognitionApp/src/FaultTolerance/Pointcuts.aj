package FaultTolerance;

import org.opencv.core.Mat;

import IIP.IipAlgorithms;
import IIP.InitialImageProcessing;
import Images.NumberPlateCutout;
import MainLogic.NumberPlateCutoutQueue;
import OCR.OcrAlgorithms;
import OCR.OpticalCharacterRecognition;
import Threads.IipCoordinator;
import Threads.OcrCoordinator;
import Threads.WorkerThreadCoordinator;

public aspect Pointcuts {
	//IIP: getNumberPlatesRectangle
	pointcut getNumberPlatesRectangle(InitialImageProcessing iipLogic, Mat rawImage) : 
		target(iipLogic) && args(rawImage) && call(NumberPlateCutout[] getNumberPlateRectangles(Mat));

	//IIP: getGetNumberPlateArea
	pointcut getGetNumberPlateArea(InitialImageProcessing iipLogic, Mat rawImage) : 
		target(iipLogic) && args(rawImage) && call(NumberPlateCutout[] getNumberPlateArea(Mat));
	
	//IIP: processImage
	//processImage function makes calls of getNumberPlatesRectangle and getGetNumberPlateArea
	pointcut processImage(InitialImageProcessing iipLogic, Mat rawImage, IipAlgorithms iipAlgorithm) : 
		target(iipLogic) && args(rawImage, iipAlgorithm) && call(NumberPlateCutout[] processImage(Mat, IipAlgorithms));

	//OCR: recognizeByHaarCascade
	pointcut recognizeByHaarCascade(OpticalCharacterRecognition ocrLogic, Mat numberPlateCutoutImage) : 
		target(ocrLogic) && args(numberPlateCutoutImage) && call(RecognitionResult recognizeByHaarCascade(Mat));

	//OCR: recognizeByTesseract
	pointcut recognizeByTesseract(OpticalCharacterRecognition ocrLogic, Mat numberPlateCutoutImage) : 
		target(ocrLogic) && args(numberPlateCutoutImage) && call(RecognitionResult recognizeByTesseract(Mat));

	//OCR recognizeByTesseractSingleSymbols
	pointcut recognizeByTesseractSingleSymbols(OpticalCharacterRecognition ocrLogic, Mat numberPlateCutoutImage) : 
		target(ocrLogic) && args(numberPlateCutoutImage) && call(RecognitionResult recognizeByTesseractSingleSymbols(Mat));
	
	//OCR: recognizeByTesseract
	//recognizeNumberPlate function makes calls of recognizeByHaarCascade, recognizeByTesseract and recognizeByTesseractSingleSymbols
	pointcut recognizeNumberPlate(OpticalCharacterRecognition ocrLogic, Mat numberPlateCutoutImage, OcrAlgorithms ocrAlgorithms) : 
		target(ocrLogic) && args(numberPlateCutoutImage, ocrAlgorithms) && call(RecognitionResult recognizeNumberPlate(Mat, OcrAlgorithms));
	
	
	// --------------------------- Diagnostics ---------------------------
	
	pointcut addNumberPlateCutout(NumberPlateCutoutQueue npcq, NumberPlateCutout numberPlateCutout) : 
		target(npcq) && args(numberPlateCutout) && call(void addNumberPlateCutout(NumberPlateCutout));
	
	pointcut getNumberPlateCutout(NumberPlateCutoutQueue npcq) : 
		target(npcq) && call(NumberPlateCutout getNumberPlateCutout());
	
	pointcut getQueueSize(NumberPlateCutoutQueue npcq) : 
		target(npcq) && call(int getQueueSize());
	
	// --------------------------- Threads coordinators --------------------
	
	pointcut setNumberOfWorkingThreads(WorkerThreadCoordinator workerThreadCoordinator, int numberOfThreads) : 
		target(workerThreadCoordinator) && args(numberOfThreads) && call(void setNumberOfWorkingThreads(int));
	pointcut createIipWorkerThread(IipCoordinator iipCoordinator) : target(iipCoordinator) && call(WorkerThread createWorkerThread());
	pointcut createOcrWorkerThread(OcrCoordinator ocrCoordinator) : target(ocrCoordinator) && call(WorkerThread createWorkerThread());
}
