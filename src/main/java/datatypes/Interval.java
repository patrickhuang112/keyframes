package datatypes;

public class Interval {
	
	private Integer start;
	private Integer end;
	
	public Interval() {
		
	}
	
	public Interval(Integer start, Integer end) {
		this.start = start;
		this.end = end;
	}
	
	public Integer getStart() {
		return this.start;
	}
	
	public Integer getEnd() {
		return this.end;
	}
	
	public void setStart(Integer start) {
		this.start = start;
	}
	
	public void setEnd(Integer end) {
		this.end = end;
	}
}
