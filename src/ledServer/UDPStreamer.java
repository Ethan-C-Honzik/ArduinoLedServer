package ledServer;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.IOException;
import java.net.DatagramPacket;

public class UDPStreamer {
	private InetAddress ip;
	private int port;
	private DatagramSocket socket;
	
	public UDPStreamer(String ip, int port) throws UnknownHostException {
		this.ip = InetAddress.getByName(ip);
		this.port = port;
		
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendData(byte[] data) {
		DatagramPacket packet 
        = new DatagramPacket(data, data.length, ip, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
