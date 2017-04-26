package com.softserveinc.dsoky.service;

import com.softserveinc.dsoky.dao.PublisherDAO;
import com.softserveinc.dsoky.dto.PublisherDTO;
import com.softserveinc.dsoky.mappers.PublisherMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class PublisherService{
    private static final Logger log = LoggerFactory.getLogger(PublisherService.class);
    private final PublisherMapper publisherMapper;
    private final PublisherDAO publisherDAO;
    @Autowired
    public PublisherService(PublisherMapper publisherMapper, PublisherDAO publisherDAO) {
        this.publisherMapper = publisherMapper;
        this.publisherDAO = publisherDAO;
    }

    public List<PublisherDTO> getAllDTOs() {
        log.debug("Mapping all Publisher entities to DTOs...");
        return publisherDAO.getAll()
                .stream()
                .map(publisherMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public PublisherDTO getDTO(long id) {
        log.debug(format("Mapping the Publisher #%d to DTO...", id));
        return publisherMapper.convertToDTO(publisherDAO.get(id));
    }

    public PublisherDTO getDTOByBook(long bookId) {
        log.debug(format("Mapping the Publisher entity of Book #%d to DTO...", bookId));
        return publisherMapper.convertToDTO(publisherDAO.getByBook(bookId));
    }

    public void save(PublisherDTO publisherDTO) {
        log.debug("Mapping Publisher's DTO to entity before saving...");
        publisherDAO.save(publisherMapper.convertToEntity(publisherDTO));
    }

    public void remove(long id) {
        publisherDAO.remove(id);
    }

    public void update(PublisherDTO publisherDTO) {
        log.debug(format("Mapping Publisher's DTO #%d to entity before updating...", publisherDTO.getId() ) );
        publisherDAO.update(publisherMapper.convertToEntity(publisherDTO));
    }

    public void insertForBook(long bId, long pId){
        publisherDAO.insertPublisherForBook(bId, pId);
    }
}
