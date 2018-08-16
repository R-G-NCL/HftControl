package FaultInjection;

import java.util.Random;

public final class FaultInjector {
	private static Random m_random = new Random();

	public static void TryInject(float probability) throws CpuException {
		if (m_random.nextFloat() < probability)
			throw new CpuException("CPU error");
	}
}
