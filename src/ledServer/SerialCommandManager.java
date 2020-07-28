package ledServer;

public class SerialCommandManager {
	private SerialCom serial;
	public SerialCommandManager() {
		serial = new SerialCom();
	}
	
	public SerialCommandManager(String name) {
		serial = new SerialCom(name);
	}
	
	public void SetHex(int index, String hex) {
		try {
			//System.out.println("RAW SENT DATA: " + "hex:" + index + ";" + hex + ">");
			serial.sendData("hex:" + index + ";" + hex + '>');
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void UpdateLeds() {
		try {
			serial.sendData("ref" + '>');
		} catch(InterruptedException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//I honestly have literally no clue. apparently sending this speeds up communication though so ¯\_(-.-)_/¯
	public void PrimeLeds() {
		try {
			for (int j = 0; j < 1; j++) {
				for (int i = 0; i < 16; i ++) {
					//System.out.println(i);
					serial.sendData("hex:" + i + ";" + "ff0000" + '>');		
				}
				serial.sendData("ref" + ">");
				for (int i = 0; i < 16; i ++) {
					//System.out.println(i);
					serial.sendData("hex:" + i + ";" + "000000" + '>');			
				}
				serial.sendData("ref" + ">");


			}
		} catch(InterruptedException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
