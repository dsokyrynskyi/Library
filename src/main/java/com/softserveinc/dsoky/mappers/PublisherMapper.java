package com.softserveinc.dsoky.mappers;

import com.softserveinc.dsoky.api.Publisher;
import com.softserveinc.dsoky.dto.PublisherDTO;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper {
    public PublisherDTO convertToDTO(Publisher publisher){
        PublisherDTO publisherDTO = new PublisherDTO();
        publisherDTO.setId(publisher.getId());
        publisherDTO.setName(publisher.getName());
        publisherDTO.setCountry(publisher.getCountry());
        return publisherDTO;
    }
    public Publisher convertToEntity(PublisherDTO publisherDTO){
        Publisher publisher = new Publisher();
        publisher.setId(publisherDTO.getId());
        publisher.setName(publisherDTO.getName());
        publisher.setCountry(publisherDTO.getCountry());
        return publisher;
    }
}
