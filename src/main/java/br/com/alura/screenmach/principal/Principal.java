package br.com.alura.screenmach.principal;

import br.com.alura.screenmach.model.DadosEpisodio;
import br.com.alura.screenmach.model.DadosSerie;
import br.com.alura.screenmach.model.DadosTemporada;
import br.com.alura.screenmach.service.ApiKey;
import br.com.alura.screenmach.service.ConsumoAPI;
import br.com.alura.screenmach.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner leitor = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String API_KEY = "&apikey=" + ApiKey.pegaKey();
    private final String URL = "https://www.omdbapi.com/?t=";
    public void exibeMenu(){
        System.out.println("Digite o nome da série para buscar: ");
        String nomeSerie = leitor.nextLine();
        var url = URL + nomeSerie.replace(" ", "+") + API_KEY;
        var json = consumoAPI.obterDados(url);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas= new ArrayList<>();

		for (int i = 1; i<=dados.totalTemporadas(); i++) {
			json = consumoAPI.obterDados(url + "&season=" + i);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);

//        for (int i = 0; i < dados.totalTemporadas(); i++) {
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (DadosEpisodio dadosEpisodio : episodiosTemporada) {
//                System.out.println("Temporada: " + temporadas.get(i).numero());
//                System.out.println("Nome episodio: " + dadosEpisodio.titulo());
//            }
//        }

        temporadas.forEach(t -> t.episodios().forEach(e ->
                        System.out.println("Temporada: "+t.numero() + " - Episodio: " + e.titulo())
                ));
    }
}
