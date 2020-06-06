package ledServer;

import java.io.IOException;
import java.util.Scanner;

import com.fazecast.jSerialComm.*;

public class SerialCom {

	private final String DEFAULT_PORT_NAME = "Arduino";
	private String devicePortName;
	private SerialPort arduinoPort = null;
	private Scanner sc;

	public SerialCom() {
		devicePortName = DEFAULT_PORT_NAME;
		initConnection();
	}

	public SerialCom(String DeviceName) {
		devicePortName = DeviceName;
		initConnection();
	}

	// used to attempt to connect after connection is lost or when initial
	// connection fails
	public void Reconnect() {
		arduinoPort = null;
		initConnection();
	}

	private void initConnection() {
		// gets the amount of ports currently plugged in
		int portCount = SerialPort.getCommPorts().length;
		SerialPort serialPorts[] = new SerialPort[portCount];
		serialPorts = SerialPort.getCommPorts();

		for (int i = 0; i < portCount; i++) {

			String portName = serialPorts[i].getDescriptivePortName();
			System.out.println(serialPorts[i].getSystemPortName() + ": " + portName + ": " + i);

			if (portName.contains(devicePortName)) {
				arduinoPort = serialPorts[i];
				arduinoPort.openPort();
				System.out.println("connected to: " + portName + "[" + i + "]");
				break;
			}
		}

		if (arduinoPort != null) {
			arduinoPort.setComPortParameters(500000, 8, 1, 0); // default connection settings for Arduino
			arduinoPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0); // block until bytes can be written
			sc = new Scanner(arduinoPort.getInputStream());
		} else {
			System.out.println("Could Not find arduino");
		}
	}

	public void sendData(String data) throws InterruptedException  {
		try {
			//System.out.println(data);
			arduinoPort.getOutputStream().write(data.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//sc.next line freezes all code until the Arduino has confirmed it has received the message
		if(data.contains("ref")) {
			String time = sc.nextLine();
			System.out.println(time);
		}

	}

	public String ReciveData() {
		return sc.nextLine();
	}
}
