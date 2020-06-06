package ledServer;

public class Utility {
	public static double Limit(double low, double high, double input) {
		if(input > high)
			return high;
		else if(input < low)
			return low;
		else
			return input;
	}
}
