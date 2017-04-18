import java.io.Serializable;
public class Point implements Serializable{
	double Lat, Long;
	Point(double Latitude, double Longitude){
		this.Lat = Latitude;
		this.Long = Longitude;
	}
	
}
