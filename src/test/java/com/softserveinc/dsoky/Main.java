package com.softserveinc.dsoky;

import com.softserveinc.dsoky.api.Author;
import com.softserveinc.dsoky.api.Book;
import com.softserveinc.dsoky.api.Publisher;
import com.softserveinc.dsoky.dto.BookDTO;
import com.softserveinc.dsoky.mappers.BookMapper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class Main {

    BookMapper bookMapper;
    AnnotationConfigApplicationContext context;

    public static void main(String[] args) {
        new Main().test();
    }

    public void test(){
        context = new AnnotationConfigApplicationContext(LibrarySpringConfiguration.class);
        bookMapper = context.getBean(BookMapper.class);

        Book book = new Book();
        book.setId(123);
        book.setName("Vasya Best");
        book.setIsbn("112223344455");
        book.setGenre("Horror");
        book.setPublisher(new Publisher(2,"Uva", "USA"));
        book.setPublishDate(LocalDate.now());
        book.setAuthors(new ArrayList<>(Arrays.asList(new Author(1,"Qww", "USA", LocalDate.now()))));

        BookDTO bookDTO = bookMapper.convertToDTO(book);
        System.out.println(bookDTO);

        Book book1 = bookMapper.convertToEntity(bookDTO);
        System.out.println(book1);
    }
}
