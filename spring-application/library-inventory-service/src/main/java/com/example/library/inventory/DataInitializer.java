package com.example.library.inventory;

import com.example.library.inventory.dataaccess.Book;
import com.example.library.inventory.dataaccess.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Store initial users and books in mongodb.
 */
@Component
public class DataInitializer implements CommandLineRunner {

  public static final UUID BOOK_CLEAN_CODE_IDENTIFIER =
      UUID.fromString("f9bf70d6-e56d-4cab-be6b-294cd05f599f");
  public static final UUID BOOK_CLOUD_NATIVE_IDENTIFIER =
      UUID.fromString("3038627d-627e-448d-8422-0a5705c9e8f1");
  public static final UUID BOOK_SPRING_ACTION_IDENTIFIER =
      UUID.fromString("081314cb-4abf-43e5-9b38-7d7261edb10d");
  public static final UUID BOOK_DEVOPS_IDENTIFIER =
      UUID.fromString("02c3d1fb-ca32-46bd-818f-704012b3fe9c");

  private final BookRepository bookRepository;

  @Autowired
  public DataInitializer(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Override
  public void run(String... args) {
    createBooks();
  }

  @Transactional
  void createBooks() {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    logger.info("Creating some initial books...");
    List<Book> bookList =
        Stream.of(
            new Book(
                BOOK_CLEAN_CODE_IDENTIFIER,
                "9780132350884",
                "Clean Code",
                "Even bad code can function. But if code isn’t clean, it can bring a development "
                    + "organization to its knees. Every year, countless hours and significant resources are "
                    + "lost because of poorly written code. But it doesn’t have to be that way. "
                    + "Noted software expert Robert C. Martin presents a revolutionary paradigm with Clean Code: "
                    + "A Handbook of Agile Software Craftsmanship . Martin has teamed up with his colleagues from "
                    + "Object Mentor to distill their best agile practice of cleaning code “on the fly” into a book "
                    + "that will instill within you the values of a software craftsman and make you a better "
                    + "programmer—but only if you work at it.",
                Collections.singletonList("Bob C. Martin")),
            new Book(
                BOOK_CLOUD_NATIVE_IDENTIFIER,
                "9781449374648",
                "Cloud Native Java",
                "What separates the traditional enterprise from the likes of Amazon, Netflix, "
                    + "and Etsy? Those companies have refined the art of cloud native development to "
                    + "maintain their competitive edge and stay well ahead of the competition. "
                    + "This practical guide shows Java/JVM developers how to build better software, "
                    + "faster, using Spring Boot, Spring Cloud, and Cloud Foundry.",
                Arrays.asList("Josh Long", "Kenny Bastiani")),
            new Book(
                BOOK_SPRING_ACTION_IDENTIFIER,
                "9781617291203",
                "Spring in Action: Covers Spring 4",
                "Spring in Action, Fourth Edition is a hands-on guide to the Spring Framework, "
                    + "updated for version 4. It covers the latest features, tools, and practices "
                    + "including Spring MVC, REST, Security, Web Flow, and more. You'll move between "
                    + "short snippets and an ongoing example as you learn to build simple and efficient "
                    + "J2EE applications. Author Craig Walls has a special knack for crisp and "
                    + "entertaining examples that zoom in on the features and techniques you really need.",
                Collections.singletonList("Craig Walls")),
            new Book(
                BOOK_DEVOPS_IDENTIFIER,
                "9781942788003",
                "The DevOps Handbook",
                "Wondering if The DevOps Handbook is for you? Authors, Gene Kim, Jez Humble, "
                    + "Patrick Debois and John Willis developed this book for anyone looking to transform "
                    + "their IT organization—especially those who want to make serious changes through the "
                    + "DevOps methodology to increase productivity, profitability and win the marketplace.",
                Arrays.asList("Gene Kim", "Jez Humble", "Patrick Debois")))
            .map(bookRepository::save)
            .collect(Collectors.toList());
    logger.info("Created {} books", bookList.size());
  }
}
