package com.softserveinc.dsoky.mappers;

import com.softserveinc.dsoky.api.Author;
import com.softserveinc.dsoky.dto.AuthorDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class AuthorMapper {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public AuthorDTO convertToDTO(Author author){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setName(author.getName());
        authorDTO.setCountry(author.getCountry());
        if(author.getDate()!=null)
            authorDTO.setDate(author.getDate().format(formatter));
        return authorDTO;
    }
    public Author convertToEntity(AuthorDTO authorDTO){
        Author author = new Author();
        author.setId(authorDTO.getId());
        author.setName(authorDTO.getName());
        author.setCountry(authorDTO.getCountry());
        if(authorDTO.getDate() != null)
            author.setDate(LocalDate.parse(authorDTO.getDate()));
        return author;
    }
}
