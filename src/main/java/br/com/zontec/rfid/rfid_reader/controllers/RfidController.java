package br.com.zontec.rfid.rfid_reader.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zontec.rfid.rfid_reader.infrastructure.dto.TagDataResponse;
import br.com.zontec.rfid.rfid_reader.infrastructure.services.IRFIDReader;
import br.com.zontec.rfid.rfid_reader.infrastructure.services.RFIDReadBaseService;
import br.com.zontec.rfid.rfid_reader.infrastructure.types.RFIDReaderType;
import cn.hutool.http.HttpStatus;

@RestController
@RequestMapping("/api/rfid")
public class RfidController {

    private final RFIDReadBaseService rfdReadService;

    public RfidController(RFIDReadBaseService rfdReadService) {
        this.rfdReadService = rfdReadService;
    }

    @GetMapping("/readTags")
    public ResponseEntity<List<TagDataResponse>> readTags() {
        try {
        	
        	IRFIDReader reader = this.rfdReadService.getRFIDReaderService(RFIDReaderType.MULTIPLE);
        	        	
        	List<TagDataResponse> tags = reader.read();
            
            if (tags.isEmpty()) {
                return ResponseEntity.status(HttpStatus.HTTP_NO_CONTENT).body(null); // 204 No Content se não encontrar tags
            }
        	
        	
        	
            return ResponseEntity.ok(tags); // 200 OK com a lista de tags
        } catch (IllegalStateException e) {
            // Se o leitor não estiver conectado ou estiver ocupado
            return ResponseEntity.status(HttpStatus.HTTP_UNAVAILABLE).body(null); // 503 Service Unavailable
        } catch (Exception e) {
            // Captura outras exceções inesperadas
            System.err.println("Erro ao ler tags: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.HTTP_INTERNAL_ERROR).body(null); // 500 Internal Server Error
        }
    }
    
    @GetMapping("/readSingleTag")
    public ResponseEntity<TagDataResponse> readTag() {
        try {
        	IRFIDReader reader = this.rfdReadService.getRFIDReaderService(RFIDReaderType.SINGLE);
        	
        	List<TagDataResponse> tags = reader.read();

            if (tags.isEmpty()) {
                return ResponseEntity.status(HttpStatus.HTTP_NO_CONTENT).build(); // 204 No Content se não encontrar tags
            }
            
            return ResponseEntity.ok(tags.get(0)); // 200 OK com a lista de tags
        } catch (IllegalStateException e) {
            // Se o leitor não estiver conectado ou estiver ocupado
            return ResponseEntity.status(HttpStatus.HTTP_UNAVAILABLE).body(null); // 503 Service Unavailable
        } catch (Exception e) {
            // Captura outras exceções inesperadas
            System.err.println("Erro ao ler tags: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.HTTP_INTERNAL_ERROR).body(null); // 500 Internal Server Error
        }
    }
    
}
