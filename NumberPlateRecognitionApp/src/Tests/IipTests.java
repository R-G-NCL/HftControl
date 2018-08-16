package Tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.opencv.core.Core;

import MainLogic.NumberPlateCutoutQueue;
import Settings.Consts;
import Threads.IipCoordinator;

public class IipTests {

	@Test
	public void test() {
		assertEquals(1, 1);
	}
	
	@Test
	public void testIipCoordinator()
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		NumberPlateCutoutQueue cutoutQueue = new NumberPlateCutoutQueue();
		IipCoordinator iipCoordinator = new IipCoordinator(Consts.FolderWithImages, cutoutQueue, 12);
		iipCoordinator.start();
		iipCoordinator.waitToFinish();
		assertEquals(1, 1);
	}
}
