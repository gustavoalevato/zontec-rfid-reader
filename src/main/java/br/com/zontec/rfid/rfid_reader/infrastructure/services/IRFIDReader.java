package br.com.zontec.rfid.rfid_reader.infrastructure.services;

import java.util.List;

import com.jty.common.callback.CallbackTagUhf;
import com.jty.common.controller.HandleReader;
import com.jty.service.ReaderBasicService;
import com.jty.service.impl.ReaderBasicServiceImpl;

import br.com.zontec.rfid.rfid_reader.exception.ReaderConnectionException;
import br.com.zontec.rfid.rfid_reader.infrastructure.dto.TagDataResponse;

public interface IRFIDReader {
	
	public ReaderBasicService readerBasicService = new ReaderBasicServiceImpl();
	
	public List<TagDataResponse> read() throws ReaderConnectionException;
		
	public CallbackTagUhf readCallback();
	
	public void customAjdusts();
	
	/**
	 * Inicia o inventário contínuo de tags.
	 */
	public default boolean startContinuousInventory(HandleReader reader) {
		return readerBasicService.beginInventory(reader);
	}

	/**
	 * Para o inventário contínuo de tags.
	 */
	public default boolean stopContinuousInventory(HandleReader reader) {
		return readerBasicService.stopInventory(reader);
	}

	/**
	 * Inicia o inventário unico de tag.
	 */
	public default boolean startSingleInventory(HandleReader reader) {
		return readerBasicService.singleInventory(reader);
	}

}
