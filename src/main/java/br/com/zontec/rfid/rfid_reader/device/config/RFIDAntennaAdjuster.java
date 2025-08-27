package br.com.zontec.rfid.rfid_reader.device.config;
import com.jty.common.controller.HandleReader;
import com.jty.service.ReaderUhfService;
import com.jty.service.impl.ReaderUhfServiceImpl;
import com.jty.uhf.entity.AntennaChannel;
import com.jty.uhf.entity.AntennaConfigUhf;

public class RFIDAntennaAdjuster {

	private HandleReader reader; // O objeto HandleReader já conectado
	private ReaderUhfService readerUhfService; // Serviço UHF


	public RFIDAntennaAdjuster(HandleReader reader) {
		this.reader = reader;
		this.readerUhfService = new ReaderUhfServiceImpl();
	}
	
	
	public void changeAntennaPower(int desiredPower) {
		// Imprimindo configurações atuais
		this.queryAndPrintAntennaConfig();

		// Atualizando potencia da antena
		this.updateAntennaPower(desiredPower);

		// Imprimindo informações para confirmar mudança
		this.queryAndPrintAntennaConfig();
	}
	
	/**
	 * Consulta e imprime a configuração atual das antenas.
	 */
	private void queryAndPrintAntennaConfig() {
		System.out.println("Consultando configuração atual da antena...");
		AntennaChannel antennaChannel = readerUhfService.queryAntennaConfig(reader);

		if (antennaChannel != null && antennaChannel.getAntennaConfigs() != null) {
			for (AntennaConfigUhf config : antennaChannel.getAntennaConfigs()) {
				// ASSUMindo que AntennaConfigUhf tenha um método getAntennaId() e
				// getOutputPower()
				// Você deve verificar isso na descompilação!
				System.out.println("  Antena ID: " + config.getAntennaId());
				System.out.println("  Potência de Saída: " + config.getPower());
				
				// Imprima outros parâmetros se desejar (ex: attenuationCoefficient, etc.)
			}
		} else {
			System.out.println("Nenhuma configuração de antena encontrada ou objeto nulo.");
		}
	}

	/**
	 * Atualiza a potência de saída da primeira antena. Você precisará ajustar para
	 * o ID da antena correto se tiver múltiplas.
	 *
	 * @param newPower O novo valor de potência a ser configurado. Você terá que
	 *                 experimentar valores dentro do range suportado pelo seu
	 *                 leitor.
	 */
	private boolean updateAntennaPower(int newPower) {
		System.out.println("Tentando ajustar a potência da antena para: " + newPower);

		// Primeiro, obtenha a configuração atual
		AntennaChannel antennaChannel = readerUhfService.queryAntennaConfig(reader);

		if (antennaChannel != null && antennaChannel.getAntennaConfigs() != null
				&& antennaChannel.getAntennaConfigs().length > 0) {
			// Supondo que você queira ajustar a primeira antena (índice 0)
			// Se você tem múltiplas antenas e quer ajustar uma específica,
			// precisará iterar sobre antennaChannel.getAntennaConfigs()
			// e encontrar a antena pelo ID (se houver um getAntennaId() nela).
			AntennaConfigUhf firstAntennaConfig = antennaChannel.getAntennaConfigs()[0];

			// ASSUMindo que AntennaConfigUhf tenha um método setOutputPower()
			// Verifique o nome real do método na descompilação!
			firstAntennaConfig.setPower(newPower);

			// Agora, atualize a configuração no leitor
			boolean success = readerUhfService.updateAntennaConfig(reader, antennaChannel);

			if (success) {
				System.out.println("Potência da antena atualizada com sucesso para " + newPower);
			} else {
				System.out.println("Falha ao atualizar a potência da antena.");
			}
			return success;
		} else {
			System.out.println("Erro: Não foi possível obter a configuração da antena para atualizar.");
			return false;
		}
	}



}