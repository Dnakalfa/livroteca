package com.fireSkill.livroteca.controller;

import com.fireSkill.livroteca.model.Favorito;
import com.fireSkill.livroteca.model.Livro;
import com.fireSkill.livroteca.repository.FavoritoRepository;
import com.fireSkill.livroteca.repository.LivroRepository;
import com.fireSkill.livroteca.service.LivrotecaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Scanner;

@Controller
public class LivroController {

    @Autowired
    private LivrotecaService service;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private FavoritoRepository favoritoRepository;

    public void exibeMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Buscar Livros");
            System.out.println("2. Ver Histórico");
            System.out.println("3. Ver Favoritos");
            System.out.println("4. Favoritar Livro");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir nova linha

            switch (opcao) {
                case 1:
                    buscarLivros(scanner);
                    break;
                case 2:
                    listarHistorico();
                    break;
                case 3:
                    listarFavoritos();
                    break;
                case 4:
                    favoritarLivro(scanner);
                    break;
                case 5:
                    continuar = false;
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

        scanner.close();
    }

    private void buscarLivros(Scanner scanner) {
        System.out.print("Digite o título ou autor para buscar: ");
        String query = scanner.nextLine();

        List<Livro> livros = service.buscarLivros(query);
        livroRepository.saveAll(livros); // Salva os resultados no histórico

        System.out.println("\n=== Resultados da Busca ===");
        for (Livro livro : livros) {
            System.out.println("ID: " + livro.getId());
            System.out.println("Título: " + livro.getTitulo());
            System.out.println("Autor(es): " + livro.getAutor());
            System.out.println("Idiomas: " + livro.getIdiomas());
            System.out.println("Formato: " + livro.getFormatoTexto());
            System.out.println("Downloads: " + livro.getDownloads());
            System.out.println("---------------");
        }
    }

    private void listarHistorico() {
        List<Livro> historico = livroRepository.findAll();

        System.out.println("\n=== Histórico de Buscas ===");
        for (Livro livro : historico) {
            System.out.println("Título: " + livro.getTitulo());
            System.out.println("Autor(es): " + livro.getAutor());
            System.out.println("---------------");
        }
    }

    private void listarFavoritos() {
        List<Long> favoritos = favoritoRepository.findAll()
                .stream()
                .map(fav -> fav.getLivroId())
                .toList();

        System.out.println("\n=== Livros Favoritos ===");
        for (Long livroId : favoritos) {
            Livro livro = livroRepository.findById(livroId).orElse(null);
            if (livro != null) {
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Autor(es): " + livro.getAutor());
                System.out.println("---------------");
            }
        }
    }

    private void favoritarLivro(Scanner scanner) {
        System.out.print("Digite o ID do livro que deseja favoritar: ");
        Long livroId = scanner.nextLong();

        if (livroRepository.existsById(livroId)) {
            favoritoRepository.save(new Favorito(livroId));
            System.out.println("Livro favoritado com sucesso!");
        } else {
            System.out.println("ID do livro não encontrado.");
        }
    }
}