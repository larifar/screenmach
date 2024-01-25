package br.com.alura.screenmach.principal;

import br.com.alura.screenmach.model.DadosEpisodio;
import br.com.alura.screenmach.model.DadosSerie;
import br.com.alura.screenmach.model.DadosTemporada;
import br.com.alura.screenmach.model.Epidodio;
import br.com.alura.screenmach.service.ApiKey;
import br.com.alura.screenmach.service.ConsumoAPI;
import br.com.alura.screenmach.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

        temporadas.forEach(t -> t.episodios().forEach(e ->
                        System.out.println("Temporada: "+t.numero() + " - Episodio: " + e.titulo())
                ));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

//        System.out.println("\n Top 10 epidodios: ");
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .limit(10)
//                .map(e -> e.titulo().toUpperCase())
//                .forEach(System.out::println);

        List<Epidodio> epidodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Epidodio(t.numero(), d))
                ).collect(Collectors.toList());

        epidodios.forEach(System.out::println);

//        System.out.println("Digite o nome do ep que gostaria de encontrar: ");
//        var trechoTitulo = leitor.nextLine();
//
//        Optional<Epidodio> episodioBuscado = epidodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .peek(e -> System.out.println("episodio encontrado: " + e))
//                .findFirst();
//
//        if (episodioBuscado.isPresent()){
//            System.out.println("episodio encontrado: ");
//            System.out.println( "Temporada: "+episodioBuscado.get().getTemporada());
//        } else {
//            System.out.println("Episodio não encontrado.");
//        }
//
//        System.out.println("A partir de que ano você deseja ver os episódios? ");
//        var ano = leitor.nextInt();
//        leitor.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        epidodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                " Epidosio: " + e.getTitulo() +
//                                "Data de lançamento: " + e.getDataLancamento().format(formatador)
//                ));

//        Map<Integer, Double> avaliacoesPorTemporada = epidodios.stream()
//                .filter(e -> e.getAvaliacao() > 0.0)
//                .collect(Collectors.groupingBy(Epidodio::getTemporada,
//                        Collectors.averagingDouble(Epidodio::getAvaliacao)));
//        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = epidodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Epidodio::getAvaliacao));
        System.out.println("Média: " + est.getAverage() +
                "\nMelhor ep: " + est.getMax() +
                "\nPior ep: " + est.getMin()+
                "\nQuantidade ep: " + est.getCount());
    }
}
