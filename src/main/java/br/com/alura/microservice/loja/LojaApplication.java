package br.com.alura.microservice.loja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.alura.microservice.loja.service.CompraService;

@SpringBootApplication
public class LojaApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(LojaApplication.class, args);
	}

}
