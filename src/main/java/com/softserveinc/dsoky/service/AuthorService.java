package com.softserveinc.dsoky.service;

import com.softserveinc.dsoky.dao.AuthorDAO;
import com.softserveinc.dsoky.dto.AuthorDTO;
import com.softserveinc.dsoky.mappers.AuthorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService{

    private final AuthorDAO authorDAO;
    private final AuthorMapper authorMapper;

    @Autowired
    public AuthorService(AuthorDAO authorDAO, AuthorMapper authorMapper) {
        this.authorDAO = authorDAO;
        this.authorMapper = authorMapper;
    }

    public List<AuthorDTO> getAllDTOs() {
        return authorDAO.getAll()
                .stream()
                .map(authorMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public AuthorDTO getDTO(long id) {
        return authorMapper.convertToDTO(authorDAO.get(id));
    }

    public List<AuthorDTO> getDTOByBook(long id) {
        return authorDAO.getByBook(id)
                .stream()
                .map(authorMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public void save(AuthorDTO authorDTO) {
        authorDAO.save(authorMapper.convertToEntity(authorDTO));
    }

    @Transactional
    public void remove(long id) {
        authorDAO.remove(id);
    }

    public void update(AuthorDTO authorDTO) {
        authorDAO.update(authorMapper.convertToEntity(authorDTO));
    }
}
