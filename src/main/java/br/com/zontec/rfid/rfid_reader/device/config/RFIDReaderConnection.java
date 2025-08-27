package br.com.zontec.rfid.rfid_reader.device.config;

import com.jty.common.controller.HandleReader;

import br.com.zontec.rfid.rfid_reader.exception.ReaderConnectionException;

public class RFIDReaderConnection {
	
	private HandleReader reader = new HandleReader();
	
	public RFIDReaderConnection(String readerIp, int readerPort) throws ReaderConnectionException {
		this.connectToReader(readerIp, readerPort);
	}
	
	/**
	 * Conecta ao leitor RFID usando o IP e a porta fornecidos.
	 * @throws ReaderConnectionException 
	 */
	private void connectToReader(String readerIp, int readerPort) throws ReaderConnectionException {
		
		String readerName = String.format("%s:%s", readerIp, readerPort);

		System.out.println("connectando a " + readerName);

		boolean result = reader.connectDev(readerName);
	
		if (result) {
			System.out.println("Status: Leitor RFID conectado.");
		} else {
			System.out.println("Status: Falha ao conectar Leitor RFID.");
			throw new ReaderConnectionException();
		}
		
	}

	/**
	 * Desconecta o leitor RFID.
	 */
	public void disconnectReader() {
		System.out.println("Desconectando do leitor...");
		boolean disconnected = reader.disconnectDev();
		if (disconnected) {
			System.out.println("Status: Leitor RFID desconectado.");
		} else {
			System.out.println("Status: Falha ao desconectar Leitor RFID.");
		}
	}
	
	public HandleReader getReader() {
		return this.reader;
	}
	
	public Boolean isConnected() {
		return this.reader != null;
	}
	
}
