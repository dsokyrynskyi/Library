package com.softserveinc.dsoky.service;

import com.softserveinc.dsoky.dao.BookDAO;
import com.softserveinc.dsoky.dto.BookDTO;
import com.softserveinc.dsoky.dto.RichBookDTO;
import com.softserveinc.dsoky.mappers.BookMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class BookService{

    private Logger log = LoggerFactory.getLogger(BookService.class);
    private final BookDAO bookDAO;
    private final BookMapper bookMapper;

    @Autowired
    public BookService(BookDAO bookDAO, BookMapper bookMapper) {
        this.bookDAO = bookDAO;
        this.bookMapper = bookMapper;
    }

    public List<BookDTO> getAllBookDTOs() {
        log.debug("Mapping all Book entities to DTOs...");
        return bookDAO.getAll().stream()
                .map(bookMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBookDTO(long id){
        log.debug(format("Mapping the Book #%d to DTO...", id));
        return bookMapper.convertToDTO(bookDAO.get(id));
    }

    public List<BookDTO> getDTOByAuthor(long authorId) {
        log.debug(format("Mapping the Book entities of Author #%d to DTOs...", authorId));
        return bookDAO.getByAuthor(authorId).stream()
                .map(bookMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> getDTOByPublisher(long pubId) {
        log.debug(format("Mapping the Book entities of Publisher #%d to DTOs...", pubId));
        return bookDAO.getByPublisher(pubId).stream()
                .map(bookMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void remove(long id) {
        bookDAO.remove(id);
    }

    @Transactional
    public void update(RichBookDTO bookDTO) {
        log.debug(format("Mapping the Book DTO #%d to Entity before updating...", bookDTO.getId()));
        bookDAO.update(bookMapper.convertToEntity(bookDTO));
    }

    @Transactional
    public void saveDTO(RichBookDTO bookDTO) {
        log.debug("Mapping the Book DTO to entity before saving...");
        bookDAO.save(bookMapper.convertToEntity(bookDTO));
    }

    public BookDTO getBookDTOByName(String bookName) {
        log.debug(format("Mapping the Book entity with name %s to DTO...", bookName));
        return bookMapper.convertToDTO(bookDAO.getByName(bookName));
    }

    public void removeRelation(long bookId, long authId){
        bookDAO.removeFromBooksAuthors(bookId, authId);
    }
}
