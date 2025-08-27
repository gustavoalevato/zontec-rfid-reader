package br.com.zontec.rfid.rfid_reader.infrastructure.factories;

import br.com.zontec.rfid.rfid_reader.device.config.RFIDReaderConnection;
import br.com.zontec.rfid.rfid_reader.infrastructure.services.IRFIDReader;
import br.com.zontec.rfid.rfid_reader.infrastructure.services.RFIDReadMultiTagsService;
import br.com.zontec.rfid.rfid_reader.infrastructure.services.RFIDReadSingleTagService;
import br.com.zontec.rfid.rfid_reader.infrastructure.types.RFIDReaderType;

public class RFIDReadFactory {
	
	public static IRFIDReader getReader(RFIDReaderConnection connection, RFIDReaderType type) {
		switch (type) {
		case RFIDReaderType.MULTIPLE: {
			return new RFIDReadMultiTagsService(connection);
		}
		case RFIDReaderType.SINGLE: {
			return new RFIDReadSingleTagService(connection);
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
	}
}
