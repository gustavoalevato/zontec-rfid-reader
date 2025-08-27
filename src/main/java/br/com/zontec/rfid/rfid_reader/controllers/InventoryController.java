package br.com.zontec.rfid.rfid_reader.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zontec.rfid.rfid_reader.infrastructure.dto.TagDataResponse;
import br.com.zontec.rfid.rfid_reader.infrastructure.dto.TagInventoryResponse;
import br.com.zontec.rfid.rfid_reader.infrastructure.services.IRFIDReader;
import br.com.zontec.rfid.rfid_reader.infrastructure.services.RFIDReadBaseService;
import br.com.zontec.rfid.rfid_reader.infrastructure.types.RFIDReaderType;
import cn.hutool.http.HttpStatus;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final RFIDReadBaseService rfdReadService;
    
    public InventoryController(RFIDReadBaseService rfdReadService) {
        this.rfdReadService = rfdReadService;
    }

    @GetMapping("/readTags")
    public ResponseEntity<List<TagInventoryResponse>> readTags() {
        try {
        	
        	IRFIDReader reader = this.rfdReadService.getRFIDReaderService(RFIDReaderType.MULTIPLE);
        	        	
        	List<TagDataResponse> tags = reader.read();
            
            if (tags.isEmpty()) {
                return ResponseEntity.status(HttpStatus.HTTP_NO_CONTENT).body(null); // 204 No Content se não encontrar tags
            }
        	
            List<TagInventoryResponse> objetos = new ArrayList<TagInventoryResponse>();
            tags.forEach(tag -> {
            	objetos.add(new TagInventoryResponse(tag.getEpc(), tag.getRssi(), tag.getAntennaId(), tag.getTimestamp()));
            });
        	
        	
            return ResponseEntity.ok(objetos); // 200 OK com a lista de tags
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
    public ResponseEntity<TagInventoryResponse> readTag() {
        try {
        	IRFIDReader reader = this.rfdReadService.getRFIDReaderService(RFIDReaderType.SINGLE);
        	
        	List<TagDataResponse> tags = reader.read();

            if (tags.isEmpty()) {
                return ResponseEntity.status(HttpStatus.HTTP_NO_CONTENT).build(); // 204 No Content se não encontrar tags
            }
            
            TagDataResponse tag = tags.get(0);
            
            TagInventoryResponse retorno;
            
            retorno = new TagInventoryResponse(tag.getEpc(), tag.getRssi(), tag.getAntennaId(), tag.getTimestamp());
            
            return ResponseEntity.ok(retorno); // 200 OK com a lista de tags
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

