package br.com.zontec.rfid.rfid_reader.device.config;
import com.jty.common.controller.HandleReader;
import com.jty.enums.BuzzerAlarmType;
import com.jty.enums.BuzzerStatus;
import com.jty.service.ReaderBasicService;
import com.jty.service.impl.ReaderBasicServiceImpl;

public class RFIDBuzzerAdjuster {

	private HandleReader reader; // O objeto HandleReader já conectado
	private ReaderBasicService readerBasicService;


	public RFIDBuzzerAdjuster(HandleReader reader) {
		this.reader = reader;
		this.readerBasicService = new ReaderBasicServiceImpl();
	}

	public void changeBuzzerStatus(BuzzerStatus status) {
		try {
			boolean buzzerResult = readerBasicService.updateBuzzerStatus(reader, status);
			if (buzzerResult) {
				System.out.println("Buzzer configurado para: " + (status.getStatus().equals(BuzzerStatus.OPEN.getStatus()) ? "ATIVADO" : "DESATIVADO"));
			} else {
				System.err.println("Falha ao configurar o buzzer.");
			}
		} catch (Exception e) {
			System.err.println("Erro ao configurar o buzzer: " + e.getMessage());
		}

	}

	public void changeBuzzerAlarmType(BuzzerAlarmType alarmType) {
		try {
			boolean alarmToneResult = readerBasicService.updateBuzzerAlartToneType(reader, alarmType);
			if (alarmToneResult) {
				System.out.println("BuzzerAlarmType configurado para: " + alarmType.toString());
			} else {
				System.err.println("Falha ao configurar o buzzer alarm type.");
			}
		} catch (Exception e) {
			System.err.println("Erro ao configurar o buzzer alarm type: " + e.getMessage());
		}
	}

	public void changeBuzzerBurningTime(int burningTime) {
		try {
			boolean burningTimeResult = readerBasicService.updateBuzzerBurningTime(reader, burningTime);
			if (burningTimeResult) {
				System.out.println("Buzzer configurado para: " + burningTime);
			} else {
				System.err.println("Falha ao configurar o buzzer burning time.");
			}
		} catch (Exception e) {
			System.err.println("Erro ao configurar o buzzer burning time: " + e.getMessage());
		}
	}


}