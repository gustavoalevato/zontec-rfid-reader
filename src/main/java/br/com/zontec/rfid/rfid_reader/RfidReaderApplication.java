package br.com.zontec.rfid.rfid_reader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan({
	"br.com.zontec.rfid"
})
public class RfidReaderApplication{

	public static void main(String[] args) {
		SpringApplication.run(RfidReaderApplication.class, args);
	}
	

}
