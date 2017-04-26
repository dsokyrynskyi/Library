package com.softserveinc.dsoky.service;

import com.softserveinc.dsoky.dao.AuthorDAO;
import com.softserveinc.dsoky.dao.BookDAO;
import com.softserveinc.dsoky.dto.AuthorDTO;
import com.softserveinc.dsoky.mappers.AuthorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class AuthorService{

    private static final Logger log = LoggerFactory.getLogger(AuthorService.class);
    private final AuthorDAO authorDAO;
    private final BookDAO bookDAO;
    private final AuthorMapper authorMapper;

    @Autowired
    public AuthorService(AuthorDAO authorDAO, BookDAO bookDAO, AuthorMapper authorMapper) {
        this.authorDAO = authorDAO;
        this.bookDAO = bookDAO;
        this.authorMapper = authorMapper;
    }

    public List<AuthorDTO> getAllDTOs() {
        log.debug("Mapping all Author entities to DTOs...");
        return authorDAO.getAll()
                .stream()
                .map(authorMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public AuthorDTO getDTO(long id) {
        log.debug(format("Mapping the Author #%d to DTO...", id));
        return authorMapper.convertToDTO(authorDAO.get(id));
    }

    public List<AuthorDTO> getDTOByBook(long bookId) {
        log.debug(format("Mapping all Author entities of Book #%d to DTOs...", bookId));
        return authorDAO.getByBook(bookId)
                .stream()
                .map(authorMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public void save(AuthorDTO authorDTO) {
        log.debug("Mapping Author's DTO to entity before saving...");
        authorDAO.save(authorMapper.convertToEntity(authorDTO));
    }

    @Transactional
    public void remove(long id) {
        authorDAO.remove(id);
    }

    public void update(AuthorDTO authorDTO) {
        log.debug(format("Mapping Author's DTO #%d to Entity before updating...", authorDTO.getId()));
        authorDAO.update(authorMapper.convertToEntity(authorDTO));
    }

    public void removeRelation(long bookId, long authId){
        bookDAO.removeFromBooksAuthors(bookId, authId);
    }

    public void insertForBook(long bookId, long authorId){
        authorDAO.insertAuthorForBook(bookId, authorId);
    }
}
