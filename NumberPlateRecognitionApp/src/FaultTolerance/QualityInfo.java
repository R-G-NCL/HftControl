package FaultTolerance;

public class QualityInfo {
	public long ExecutionTime;
	public StubImages.Quality Quality;
	
	public QualityInfo(long executionTime, StubImages.Quality quality){
		ExecutionTime = executionTime;
		Quality = quality;
	}
}
