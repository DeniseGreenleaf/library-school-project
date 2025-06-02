package com.example.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		File dbFile = new File("bibblan.db");
		System.out.println(">>> Laddar databas från: " + dbFile.getAbsolutePath());
		SpringApplication.run(LibraryApplication.class, args);
	}

}


