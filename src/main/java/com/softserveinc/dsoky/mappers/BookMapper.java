package com.softserveinc.dsoky.mappers;

import com.softserveinc.dsoky.api.Book;
import com.softserveinc.dsoky.dao.AuthorDAO;
import com.softserveinc.dsoky.dao.PublisherDAO;
import com.softserveinc.dsoky.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class BookMapper {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private final PublisherDAO publisherDAO;
    private final AuthorDAO authorDAO;

    @Autowired
    public BookMapper(PublisherDAO publisherDAO, AuthorDAO authorDAO) {
        this.publisherDAO = publisherDAO;
        this.authorDAO = authorDAO;
    }

    public BookDTO convertToDTO(Book book) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setName(book.getName());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setGenre(book.getGenre());
        bookDTO.setPublishDate(book.getPublishDate().format(formatter));
        return bookDTO;
    }

    public Book convertToEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setName(bookDTO.getName());
        book.setIsbn(bookDTO.getIsbn());
        book.setGenre(bookDTO.getGenre());
        book.setPublishDate(LocalDate.parse(bookDTO.getPublishDate()));
        return book;
    }
}
