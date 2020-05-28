package hello_world;

import javacard.framework.*;

public class hello_world extends Applet{

	private static final byte[] helloWorld = {'H', 'e', 'l', 'l', 'o'};
	private static final byte HW_CLA = (byte) 0x80;
	private static final byte HW_INS = (byte) 0x00;

	public static void install(byte[] bArray, short bOffset, byte bLength) {
		new hello_world().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
	}

	public void process(APDU apdu){
		if (selectingApplet()){
			return;
		}

		byte[] buf = apdu.getBuffer();
		byte CLA = buf[ISO7816.OFFSET_CLA];
		byte INS = buf[ISO7816.OFFSET_INS];
		
		if (CLA != HW_CLA){
			ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
		}
		
		if (INS == HW_INS){
			getHelloWorld(apdu);
		}
		else {
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}
	private void getHelloWorld(APDU apdu){
		byte[] buffer = apdu.getBuffer();
		short length = (short) helloWorld.length;
		
		//Copies an array from the specified source array
		Util.arrayCopyNonAtomic(helloWorld, (short) 0, buffer, (short) 0, length);
		
		//"convenience" send method
		apdu.setOutgoingAndSend((short) 0, length);
	}
}
