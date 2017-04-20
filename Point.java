import java.io.Serializable;
//import java.lang.Math;
public class Point implements Serializable{
	double Lat, Long;
	Point(double Latitude, double Longitude){
		this.Lat = Latitude;
		this.Long = Longitude;
	}
	public boolean equals(Point b){
		return (Math.abs(this.Lat - b.Lat) < 0.001) && (Math.abs(this.Long - b.Long) < 0.001);
	}
}