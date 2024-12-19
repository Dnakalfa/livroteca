package com.fireSkill.livroteca;

import com.fireSkill.livroteca.controller.LivroController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LivrotecaApplication implements CommandLineRunner {

	@Autowired
	private LivroController livroController;

	public static void main(String[] args) {
		SpringApplication.run(LivrotecaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		livroController.exibeMenu();
	}
}
