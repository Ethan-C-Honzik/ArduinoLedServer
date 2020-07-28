package ledServer;

import java.net.UnknownHostException;

public class Main {
	private static SerialCommandManager command;
	private static ScreenRecorder screen = new ScreenRecorder();
	public static boolean debug = false;

	public static void main(String args[]) {
		while (true) {
			NetworkLED(GenWARLS(50, screenCapToTable()));
		}
	}

	public static ColorLookUpTable screenCapToTable() {
		screen.UpdateColorSections();
		ColorLookUpTable table = new ColorLookUpTable(screen.colorSecs.length);
		for (int i = 0; i < screen.colorSecs.length; i++) {
			table.setKey(i, screen.colorSecs[i]);
		}
		return table;
	}

	public static byte[] GenWARLS(int ledCount, ColorLookUpTable table) {
		byte[] message = new byte[ledCount * 4 + 2];
		message[0] = 1;
		message[1] = 2;
		for (int i = 0; i < ledCount; i++) {
			message[2 + i * 4] = (byte) i;
			message[3 + i * 4] = (byte) (table.getColor((float) i / ledCount).getRed());
			message[4 + i * 4] = (byte) (table.getColor((float) i / ledCount).getGreen());
			message[5 + i * 4] = (byte) (table.getColor((float) i / ledCount).getBlue());
		}

		return message;
	}

	public static void NetworkLED(byte data[]) {
		try {
			UDPStreamer ledConnection = new UDPStreamer("192.168.0.30", 21324);
			// byte[] data = { 1, 2, 0, 0, (byte) 255, (byte) 255, 1, (byte) 255, 0, 0};
			ledConnection.sendData(data);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void SerialLed() {
		command = new SerialCommandManager();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// command.PrimeLeds();
		// LEDtest();
		for (int j = 0; j < 50000000; j++) {
			screen.UpdateColorSections();
			for (int i = 0; i < 16; i++) {
				// String hex = Integer.toHexString(screen.colorSecs[i].getRGB() & 0xffffff);
				String hex = String.format("%1$02x", screen.colorSecs[i].getRGB());
				hex = hex.substring(2);
				// System.out.println("input RGB: " + screen.colorSecs[i].toString() + "output
				// hex: " + hex);
				// System.out.println("settingColor:" + hex);
				command.SetHex(i, hex);
				// Thread.yield();
			}
			// System.out.println("Updating Color");
			command.UpdateLeds();
		}
	}

	public static void LEDtest() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long x = System.currentTimeMillis();
		for (int j = 0; j < 16; j++) {
			for (int i = 0; i < 16; i++) {
				// System.out.println(i);
				command.SetHex(i, "000000");
			}
			command.UpdateLeds();
			for (int i = 0; i < 16; i++) {
				// System.out.println(i);
				command.SetHex(i, "00ff00");
			}
			command.UpdateLeds();

		}
		System.out.println("Serial Limited fps: " + ((16.0 / (System.currentTimeMillis() - x)) * 1000.0));
	}

}
