package br.com.zontec.rfid.rfid_reader.infrastructure.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import com.jty.common.callback.CallbackTagUhf;
import com.jty.common.controller.HandleReader;
import com.jty.enums.BuzzerAlarmType;
import com.jty.enums.BuzzerStatus;
import com.jty.product.entity.BasicVariableMessage;
import com.jty.uhf.entity.DataOutputUhfMultiple;
import com.jty.uhf.entity.DataOutputUhfStandard;

import br.com.zontec.rfid.rfid_reader.device.config.RFIDAntennaAdjuster;
import br.com.zontec.rfid.rfid_reader.device.config.RFIDBuzzerAdjuster;
import br.com.zontec.rfid.rfid_reader.device.config.RFIDReaderConnection;
import br.com.zontec.rfid.rfid_reader.exception.ReaderConnectionException;
import br.com.zontec.rfid.rfid_reader.infrastructure.dto.TagDataResponse;

public class RFIDReadSingleTagService implements IRFIDReader {

	private static final Integer ANTENNA_POWER_DEFAULT = 5;

	private static final Integer SCAN_TIMEOUT = 1000; //milisegundos


	// Lista temporária para coletar tags de uma única varredura
	private Map<String, TagDataResponse> currentScanTags;

	RFIDReaderConnection readerConnection;

	// Lock para garantir que apenas uma operação de leitura/configuração ocorra por vez
	private final ReentrantLock readerLock = new ReentrantLock();

	public RFIDReadSingleTagService(RFIDReaderConnection readerConnection) {
		this.readerConnection = readerConnection;
		this.currentScanTags = Collections.synchronizedMap(new HashMap<String, TagDataResponse>());

		/**
		 * Registra o callback da leitura das tags
		 */
		this.readerConnection.getReader().setCallbackTagUhf(readCallback());
	}

	public List<TagDataResponse> read() throws ReaderConnectionException {

		// Verifica se o leitor está conectado antes de tentar ler
		if (!this.readerConnection.isConnected()) {

			System.err.println("Leitor não conectado. Tentando reconectar...");

			if (!this.readerConnection.isConnected()) {
				throw new IllegalStateException("Não foi possível conectar ao leitor RFID para realizar a leitura.");
			}
		}

		customAjdusts();


		// Adquire o lock para garantir que apenas uma leitura por vez
		if (!readerLock.tryLock()) {
			System.out.println("Leitor ocupado, aguarde a operação anterior terminar.");
			throw new IllegalStateException("Leitor RFID está atualmente ocupado com outra operação.");
		}

		try {

			//currentScanTags.clear(); // Limpa a lista de tags da varredura anterior
			System.out.println("Disparando leitura multipla de tags...");

			boolean scanTriggered = startContinuousInventory(this.readerConnection.getReader());

			if (!scanTriggered) {
				System.err.println("Falha ao disparar o comando singleInventory.");
				return Collections.emptyList();
			}

			// Espera por um tempo configurável para o leitor enviar os dados das tags
			try {
				Thread.sleep(SCAN_TIMEOUT);

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				System.err.println("Leitura continua interrompida durante a espera: " + e.getMessage());

			}

			List<TagDataResponse> tagsRetorno = new ArrayList<TagDataResponse>();

			currentScanTags.forEach((id, tag) -> {
				tagsRetorno.add(tag);
			});

			// Retorna as tags coletadas durante o período de espera
			return new ArrayList<TagDataResponse>(tagsRetorno); // Retorna uma cópia para evitar modificações externas
		} finally {
			readerLock.unlock(); // Libera o lock
			stopContinuousInventory(this.readerConnection.getReader());
		}
	}

	@Override
	public CallbackTagUhf readCallback() {
		return new CallbackTagUhf() {
			@Override
			public void tagType08(HandleReader r, DataOutputUhfStandard dataOutputUhfStandard) {
				// Lógica de coleta de tags UHF padrão
				if (dataOutputUhfStandard != null && dataOutputUhfStandard.getMessage() != null) {
					BasicVariableMessage basicMessage = dataOutputUhfStandard.getMessage();

					BasicVariableMessage message = dataOutputUhfStandard.getMessage();

					String epc = dataOutputUhfStandard.getTagId();
					Long rssi = Long.valueOf(message.getRssiValue());
					String antennaId = String.valueOf(message.getAntennaId());

					TagDataResponse tag = new TagDataResponse(epc, rssi, antennaId, System.currentTimeMillis());
					currentScanTags.put(epc, tag);

					System.out.println("Tag coletada no callback: " + tag.getEpc()); // Para depuração
				}else {
					System.err.println("Erro ao coletar tag no callback");
				}
			}

			@Override
			public void tagType0D(HandleReader r, DataOutputUhfMultiple dataOutputUhfMultiple) {
				// Implemente a lógica para tags tipo 0D se necessário
				System.out.println("Tag do tipo 0D lida, processamento não implementado no exemplo.");
			}
		};
	}

	@Override
	public void customAjdusts() {

		/**
		 * Configurando potencia da antena para leitura
		 */
		RFIDAntennaAdjuster antennaAdjuster = new RFIDAntennaAdjuster(this.readerConnection.getReader());
		antennaAdjuster.changeAntennaPower(ANTENNA_POWER_DEFAULT);

		/**
		 * Habilitando o som de leitura do módulo
		 */
		RFIDBuzzerAdjuster buzzerAdjuster = new RFIDBuzzerAdjuster(this.readerConnection.getReader());
		buzzerAdjuster.changeBuzzerStatus(BuzzerStatus.OPEN);
		buzzerAdjuster.changeBuzzerAlarmType(BuzzerAlarmType.SOUND1);
		//buzzerAdjuster.changeBuzzerBurningTime(1000);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
