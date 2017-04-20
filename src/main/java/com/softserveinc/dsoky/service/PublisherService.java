package com.softserveinc.dsoky.service;

import com.softserveinc.dsoky.dao.PublisherDAO;
import com.softserveinc.dsoky.dto.PublisherDTO;
import com.softserveinc.dsoky.mappers.PublisherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherService{

    private final PublisherMapper publisherMapper;
    private final PublisherDAO publisherDAO;
    @Autowired
    public PublisherService(PublisherMapper publisherMapper, PublisherDAO publisherDAO) {
        this.publisherMapper = publisherMapper;
        this.publisherDAO = publisherDAO;
    }

    public List<PublisherDTO> getAllDTOs() {
        return publisherDAO.getAll()
                .stream()
                .map(publisherMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public PublisherDTO getDTO(long id) {
        return publisherMapper.convertToDTO(publisherDAO.get(id));
    }

    public PublisherDTO getDTOByBook(long id) {
        return publisherMapper.convertToDTO(publisherDAO.getByBook(id));
    }

    public void save(PublisherDTO publisherDTO) {
        publisherDAO.save(publisherMapper.convertToEntity(publisherDTO));
    }

    public void remove(long id) {
        publisherDAO.remove(id);
    }

    public void update(PublisherDTO publisherDTO) {
        publisherDAO.update(publisherMapper.convertToEntity(publisherDTO));
    }

    public void insertForBook(long bId, long pId){
        publisherDAO.insertPublisherForBook(bId, pId);
    }
}
