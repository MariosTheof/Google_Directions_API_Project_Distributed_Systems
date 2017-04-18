import java.io.Serializable;
public class Routes implements Serializable{
	Point start;
	Point destination;
	Directions direction;
	Routes(Point a, Point b, Directions d){
		start = a;
		destination = b;
		direction = d;
	}

}
