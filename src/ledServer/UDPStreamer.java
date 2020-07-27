package ledServer;
import java.net.DatagramSocket;
import java.net.DatagramPacket;

public class UDPStreamer {
	private int ip;
	private int port;
	
	public UDPStreamer(int ip, int port) {
		this.ip = ip;
		this.port = port;
	}
}
