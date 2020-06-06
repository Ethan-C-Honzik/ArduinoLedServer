package ledServer;

public class Main {
	private static CommandManager command = new CommandManager();
	private static ScreenRecorder screen = new ScreenRecorder();

	public static void main(String args[]) {
					
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//command.PrimeLeds(); 
		
		LEDtest();
		for(int j = 0; j< 5000000; j++) {
			screen.UpdateColorSections();
			for(int i = 0; i < 16; i++) {
				//String hex = Integer.toHexString(screen.colorSecs[i].getRGB() & 0xffffff);
				String hex = String.format("%1$02x", screen.colorSecs[i].getRGB());
				//idk why two 00's makes it faster but it does ok.
				hex = hex.substring(2);
				//System.out.println("input RGB: " + screen.colorSecs[i].toString() + "output hex: " + hex);
				//System.out.println("settingColor:" + hex);
				command.SetHex(i, hex);
				//Thread.yield();
			}
			//System.out.println("Updating Color");
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
			for (int i = 0; i < 16; i ++) {
				//System.out.println(i);
				command.SetHex(i, "000000");				
			}
			command.UpdateLeds();
			for (int i = 0; i < 16; i ++) {
				//System.out.println(i);
				command.SetHex(i, "00ff00");				
			}
			command.UpdateLeds();


		}
		System.out.println("Serial Limited fps: " + ((16.0/(System.currentTimeMillis() - x)) * 1000.0));
	}

}
