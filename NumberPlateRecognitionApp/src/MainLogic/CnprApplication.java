package MainLogic;
import java.util.ArrayList;

import org.opencv.core.Core;

import FaultTolerance.HolisticFaultToleranceController;
import FaultTolerance.OperationMode;
import Settings.Consts;
import Threads.IipCoordinator;
import Threads.OcrCoordinator;

public class CnprApplication {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		System.out.println("App started!");
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		String folderName = Consts.FolderWithImages;
		OperationMode operationMode = OperationMode.Reliability;
		
		//ArrayList<ImageProcessingSteps> imageProcessingSteps = new ArrayList<ImageProcessingSteps>();
		
		NumberPlateCutoutQueue cutoutQueue = new NumberPlateCutoutQueue();
		IipCoordinator initialImageProcessing = new IipCoordinator(folderName, cutoutQueue, Consts.InitialNumberOfThreadsIip);
		OcrCoordinator opticalCharacterRecognition = new OcrCoordinator(cutoutQueue, Consts.InitialNumberOfThreadsOcr);
		
		HolisticFaultToleranceController hftController = 
				new HolisticFaultToleranceController(initialImageProcessing, opticalCharacterRecognition, 
						cutoutQueue, operationMode);
		hftController.waitToFinish();
		
		long executionTime = System.currentTimeMillis() - startTime;
		System.out.printf("App finished in %d ms! \r\n", executionTime);
	}
}
