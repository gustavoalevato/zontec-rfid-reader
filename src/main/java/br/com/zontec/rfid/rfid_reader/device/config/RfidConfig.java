package br.com.zontec.rfid.rfid_reader.device.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "rfid.reader")
@Data
public class RfidConfig {
    private String ip;
    private int port;
    private int singleScanTimeoutMs; // Tempo de espera para tags após uma leitura unitária
    private boolean buzzerEnabled; // Estado inicial do buzzer
    private int antennaPower; // Potência inicial da antena
}