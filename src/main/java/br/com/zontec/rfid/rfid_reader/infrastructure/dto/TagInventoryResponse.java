package br.com.zontec.rfid.rfid_reader.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class TagInventoryResponse {
	
    private String epc;
    private Long rssi;
    private String antennaId;
    private long timestamp; // Para saber quando a tag foi lida
    
}