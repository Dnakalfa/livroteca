package com.fireSkill.livroteca.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fireSkill.livroteca.model.Livro;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class LivrotecaService {

    private final String GUTENDEX_URL = "https://gutendex.com/books/?search=";

    public List<Livro> buscarLivros(String query) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        List<Livro> livros = new ArrayList<>();

        try {
            String response = restTemplate.getForObject(GUTENDEX_URL + query, String.class);
            JsonNode root = mapper.readTree(response);

            for (JsonNode item : root.path("results")) {
                Livro livro = new Livro();
                livro.setTitulo(item.path("title").asText());
                livro.setAutor(item.path("authors").get(0).path("name").asText());
                livro.setIdiomas(item.path("languages").toString());
                livro.setFormatoTexto(item.path("formats").path("text/html").asText());
                livro.setCapaUrl(item.path("formats").path("image/jpeg").asText());
                livro.setDownloads(item.path("download_count").asInt());
                livros.add(livro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return livros;
    }
}
