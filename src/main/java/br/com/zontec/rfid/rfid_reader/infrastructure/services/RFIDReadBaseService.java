package br.com.zontec.rfid.rfid_reader.infrastructure.services;

import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

import com.jty.common.callback.CallbackDisconnected;
import com.jty.common.controller.HandleReader;
import com.jty.service.ReaderBasicService;
import com.jty.service.impl.ReaderBasicServiceImpl;

import br.com.zontec.rfid.rfid_reader.device.config.RFIDReaderConnection;
import br.com.zontec.rfid.rfid_reader.device.config.RfidConfig;
import br.com.zontec.rfid.rfid_reader.exception.ReaderConnectionException;
import br.com.zontec.rfid.rfid_reader.infrastructure.factories.RFIDReadFactory;
import br.com.zontec.rfid.rfid_reader.infrastructure.types.RFIDReaderType;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class RFIDReadBaseService {

	protected ReaderBasicService readerBasicService;
	protected RFIDReaderConnection readerConnection;

	protected final RfidConfig rfidConfig;

	// Lock para garantir que apenas uma operação de leitura/configuração ocorra por vez
	protected final ReentrantLock readerLock = new ReentrantLock();

	public RFIDReadBaseService(RfidConfig rfidConfig) {
		this.rfidConfig = rfidConfig;
		this.readerBasicService = new ReaderBasicServiceImpl();
	}

	
	@PostConstruct
	public void init() {
		connectToReader();
	}

	@PreDestroy
	public void destroy() {
		System.out.println("Encerrando serviço RFID. Desconectando do leitor.");
		disconnectReader();
	}

	protected void connectToReader() {
		readerLock.lock(); // Adquire o lock antes de tentar conectar
		try {
			this.readerConnection = new RFIDReaderConnection(rfidConfig.getIp(), rfidConfig.getPort());
			
			this.readerConnection.getReader().setCallbackDisconnected(new CallbackDisconnected() {

				@Override
				public void info(HandleReader arg0) {
					System.out.println("O leitor se desconectou! Bora conectar de novo");
					connectToReader();
				}
				
			});
			
		} catch (ReaderConnectionException e) {
			System.out.println("Ocorreu um erro ao conectar ao leitor");
			e.printStackTrace();
		} finally {
			readerLock.unlock(); // Libera o lock
		}
	}

	protected void disconnectReader() {
		readerLock.lock();
		try {
			if(readerConnection != null)
				readerConnection.disconnectReader();
		} finally {
			readerLock.unlock();
		}
	}
	
	public IRFIDReader getRFIDReaderService(RFIDReaderType type) {
		
		return RFIDReadFactory.getReader(readerConnection, type);
		
	}
}
