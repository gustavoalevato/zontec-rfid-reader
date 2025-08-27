package br.com.zontec.rfid.rfid_reader.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagInventoryRequest {

	private String epc;
	private String name;
	private String location;
	private Long rssi;
	private String antennaId;
	private long timestamp; // Para saber quando a tag foi lida
	
}
