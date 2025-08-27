package br.com.zontec.rfid.rfid_reader.utils;
public class StringTool {


	/**
	 * Converte um array de bytes para uma String hexadecimal.
	 * Por exemplo: [0x01, 0x0A, 0xFF] -> "010AFF"
	 */
	public static String bytesToHex(byte[] bytes) {
	    if (bytes == null || bytes.length == 0) {
	        return "";
	    }
	    StringBuilder sb = new StringBuilder(bytes.length * 2);
	    for (byte b : bytes) {
	        sb.append(String.format("%02X", b));
	    }
	    return sb.toString();
	}


}
