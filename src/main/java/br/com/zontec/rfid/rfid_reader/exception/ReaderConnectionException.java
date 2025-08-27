package br.com.zontec.rfid.rfid_reader.exception;

public class ReaderConnectionException extends Exception {
	
	
	public ReaderConnectionException(String message) {
		super(message);
	}
	
	public ReaderConnectionException() {
		super("Ocorreu um erro ao conectar ao leitor RFID");
	}

}
