package com.softserveinc.dsoky.mappers;

import com.softserveinc.dsoky.api.Book;
import com.softserveinc.dsoky.api.Publisher;
import com.softserveinc.dsoky.dao.AuthorDAO;
import com.softserveinc.dsoky.dao.PublisherDAO;
import com.softserveinc.dsoky.dto.BookDTO;
import com.softserveinc.dsoky.dto.RichBookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

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

    public Book convertToEntity(RichBookDTO bookDTO) {
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setName(bookDTO.getName());
        book.setIsbn(bookDTO.getIsbn());
        book.setGenre(bookDTO.getGenre());
        book.setPublishDate(LocalDate.parse(bookDTO.getPublishDate()));
        book.setPublisher(new Publisher(bookDTO.getPublisher()));
        book.setAuthors(bookDTO.getAuthors().stream()
                .map(authorDAO::get)
                .collect(Collectors.toList()));
        return book;
    }
}
