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

@Service
public class PublisherService{
    private Logger log = LoggerFactory.getLogger(PublisherService.class);
    private final PublisherMapper publisherMapper;
    private final PublisherDAO publisherDAO;
    @Autowired
    public PublisherService(PublisherMapper publisherMapper, PublisherDAO publisherDAO) {
        this.publisherMapper = publisherMapper;
        this.publisherDAO = publisherDAO;
    }

    public List<PublisherDTO> getAllDTOs() {
        log.info("Mapping all Publisher entities to DTOs...");
        return publisherDAO.getAll()
                .stream()
                .map(publisherMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public PublisherDTO getDTO(long id) {
        log.info("Mapping the Publisher entity to DTO...");
        return publisherMapper.convertToDTO(publisherDAO.get(id));
    }

    public PublisherDTO getDTOByBook(long id) {
        log.info("Mapping the Publisher entity of certain book to DTO...");
        return publisherMapper.convertToDTO(publisherDAO.getByBook(id));
    }

    public void save(PublisherDTO publisherDTO) {
        log.info("Mapping Publisher's DTO to entity before saving...");
        publisherDAO.save(publisherMapper.convertToEntity(publisherDTO));
    }

    public void remove(long id) {
        publisherDAO.remove(id);
    }

    public void update(PublisherDTO publisherDTO) {
        log.info("Mapping Publisher's DTO to entity before updating...");
        publisherDAO.update(publisherMapper.convertToEntity(publisherDTO));
    }

    public void insertForBook(long bId, long pId){
        publisherDAO.insertPublisherForBook(bId, pId);
    }
}
