package com.softserveinc.dsoky.service;

import com.softserveinc.dsoky.dao.BookDAO;
import com.softserveinc.dsoky.dto.BookDTO;
import com.softserveinc.dsoky.mappers.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookDAO bookDAO;
    private final BookMapper bookMapper;

    @Autowired
    public BookService(BookDAO bookDAO, BookMapper bookMapper) {
        this.bookDAO = bookDAO;
        this.bookMapper = bookMapper;
    }

    public List<BookDTO> getAllBookDTOs() {
        return bookDAO.getAll().stream()
                .map(bookMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBookDTO(long id){
        return bookMapper.convertToDTO(bookDAO.get(id));
    }

    public List<BookDTO> getDTOByAuthor(long author) {
        return bookDAO.getByAuthor(author).stream()
                .map(bookMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> getDTOByPublisher(long id) {
        return bookDAO.getByPublisher(id).stream()
                .map(bookMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void remove(long id) {
        bookDAO.remove(id);
    }

    @Transactional
    public void update(BookDTO bookDTO) {
        bookDAO.update(bookMapper.convertToEntity(bookDTO));
    }

    @Transactional
    public void saveDTO(BookDTO bookDTO) {
        bookDAO.save(bookMapper.convertToEntity(bookDTO));
    }

    public BookDTO getBookDTOByName(String bookName) {
        return bookMapper.convertToDTO(bookDAO.getByName(bookName));
    }
}
