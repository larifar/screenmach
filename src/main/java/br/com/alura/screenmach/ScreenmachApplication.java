package br.com.alura.screenmach;

import br.com.alura.screenmach.model.DadosSerie;
import br.com.alura.screenmach.service.ConsumoAPI;
import br.com.alura.screenmach.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmachApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmachApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=b588e316");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
	}
}
