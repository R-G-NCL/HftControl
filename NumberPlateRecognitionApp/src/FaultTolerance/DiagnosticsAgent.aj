package FaultTolerance;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.opencv.core.Mat;

import IIP.IipAlgorithms;
import IIP.InitialImageProcessing;
import MainLogic.NumberPlateCutoutQueue;
import OCR.OcrAlgorithms;
import OCR.OpticalCharacterRecognition;
import StubImages.NumberPlateCutout;
import Threads.WorkerThreadCoordinator;
import Threads.IipCoordinator;
import Threads.OcrCoordinator;

public aspect DiagnosticsAgent {
	private static boolean m_logInfo = false;

	public static void setLogInfo(boolean logInfo){
		m_logInfo = logInfo;
	}
	
	private void PrintDiagnosticsInformation(String info) {
		if (!m_logInfo)
			return;
		System.out.println(String.format("%s: %s", LocalDateTime.now().format(DateTimeFormatter.ISO_TIME), info));
	}
	
	private void printExceptionInfo(String methodName, Exception exception){
		PrintDiagnosticsInformation(String.format("Exception in method %s. %s", methodName, exception));
	}

	// ---------- NumberPlateCutoutQueue ----------
	before(NumberPlateCutoutQueue npcq, NumberPlateCutout numberPlateCutout) : Pointcuts.addNumberPlateCutout(npcq, numberPlateCutout) {
		String info = String.format("NumberPlateCutout.addNumberPlateCutout called with arguments: Index = %d",
				numberPlateCutout.Index);
		PrintDiagnosticsInformation(info);
	}

	before(NumberPlateCutoutQueue npcq) : Pointcuts.getNumberPlateCutout(npcq) {
		String info = "NumberPlateCutout.getNumberPlateCutout was called";
		PrintDiagnosticsInformation(info);
	}

	before(NumberPlateCutoutQueue npcq) : Pointcuts.getQueueSize(npcq) {
		String info = "NumberPlateCutout.getQueueSize was called";
		PrintDiagnosticsInformation(info);
	}

	// ---------- InitialImageProcessing ----------
	before(InitialImageProcessing iipLogic, Mat rawImage) : Pointcuts.getNumberPlatesRectangle(iipLogic, rawImage){
		String info = "InitialImageProcessing.getNumberPlatesRectangle was called";
		PrintDiagnosticsInformation(info);
	}

	before(InitialImageProcessing iipLogic, Mat rawImage) : Pointcuts.getGetNumberPlateArea(iipLogic, rawImage){
		String info = "InitialImageProcessing.getGetNumberPlateArea was called";
		PrintDiagnosticsInformation(info);
	}

	before(InitialImageProcessing iipLogic, Mat rawImage, IipAlgorithms iipAlgorithm) : Pointcuts.processImage(iipLogic, rawImage, iipAlgorithm){
		String info = "InitialImageProcessing.processImage was called";
		PrintDiagnosticsInformation(info);
	}
	
	after(InitialImageProcessing iipLogic, Mat rawImage, IipAlgorithms iipAlgorithm) throwing (Exception exception) : 
		Pointcuts.processImage(iipLogic, rawImage, iipAlgorithm){
		printExceptionInfo("InitialImageProcessing.processImage", exception);
	}
	
	// ---------- OpticalCharacterRecognition ----------
	before(OpticalCharacterRecognition ocrLogic, Mat numberPlateCutoutImage) : Pointcuts.recognizeByHaarCascade(ocrLogic, numberPlateCutoutImage){
		String info = "OpticalCharacterRecognition.recognizeByHaarCascade was called";
		PrintDiagnosticsInformation(info);
	}

	before(OpticalCharacterRecognition ocrLogic, Mat numberPlateCutoutImage) : Pointcuts.recognizeByTesseract(ocrLogic, numberPlateCutoutImage){
		String info = "OpticalCharacterRecognition.recognizeByTesseract was called";
		PrintDiagnosticsInformation(info);
	}

	before(OpticalCharacterRecognition ocrLogic, Mat numberPlateCutoutImage) : Pointcuts.recognizeByTesseractSingleSymbols(ocrLogic, numberPlateCutoutImage){
		String info = "OpticalCharacterRecognition.recognizeByTesseractSingleSymbols was called";
		PrintDiagnosticsInformation(info);
	}
	
	before(OpticalCharacterRecognition ocrLogic, Mat numberPlateCutoutImage, OcrAlgorithms ocrAlgorithms) : Pointcuts.recognizeNumberPlate(ocrLogic, numberPlateCutoutImage, ocrAlgorithms){
		String info = "OpticalCharacterRecognition.recognizeNumberPlate was called";
		PrintDiagnosticsInformation(info);
	}
	
	after(OpticalCharacterRecognition ocrLogic, Mat numberPlateCutoutImage, OcrAlgorithms ocrAlgorithms) throwing (Exception exception) : 
		Pointcuts.recognizeNumberPlate(ocrLogic, numberPlateCutoutImage, ocrAlgorithms){
		printExceptionInfo("OpticalCharacterRecognition.recognizeNumberPlate", exception);
	}
		
	// ---------- Threads coordinators ----------
	before(WorkerThreadCoordinator workerThreadCoordinator, int numberOfThreads) : Pointcuts.setNumberOfWorkingThreads(workerThreadCoordinator, numberOfThreads){
		String info = String.format("WorkerThreadCoordinator.setNumberOfWorkingThreads(%d) was called", numberOfThreads);
		PrintDiagnosticsInformation(info);
	}
	
	before(IipCoordinator iipCoordinator) : Pointcuts.createIipWorkerThread(iipCoordinator){
		String info = "IipCoordinator.createWorkerThread was called";
		PrintDiagnosticsInformation(info);
	}
	
	before(OcrCoordinator ocrCoordinator) : Pointcuts.createOcrWorkerThread(ocrCoordinator){
		String info = "OcrCoordinator.createWorkerThread was called";
		PrintDiagnosticsInformation(info);
	}
}
