package FaultTolerance;

public class QualityInfo {
	public long ExecutionTime;
	public Images.Quality Quality;
	
	public QualityInfo(long executionTime, Images.Quality quality){
		ExecutionTime = executionTime;
		Quality = quality;
	}
}
