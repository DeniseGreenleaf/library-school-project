package com.example.library;

import com.example.library.Books.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		File dbFile = new File("bibblan.db");
		System.out.println(">>> Laddar databas från: " + dbFile.getAbsolutePath());
		SpringApplication.run(LibraryApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner testData(BookRepository bookRepository) {
//		return args -> {
//			System.out.println(">>> Böcker i databasen:");
//			bookRepository.findAll().forEach(book ->
//					System.out.println(book.getBookId() + ": " + book.getTitle()));
//		};
//	}
}


//@SpringBootApplication
//public class LibraryApplication {
//
//	public static void main(String[] args) {
//		File dbFile = new File("bibblan.db");
//		System.out.println(">>> Laddar databas från: " + dbFile.getAbsolutePath());
//		SpringApplication.run(LibraryApplication.class, args);
//	}

