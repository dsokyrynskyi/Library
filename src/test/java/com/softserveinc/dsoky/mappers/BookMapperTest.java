package com.softserveinc.dsoky.mappers;

import com.softserveinc.dsoky.api.Author;
import com.softserveinc.dsoky.api.Book;
import com.softserveinc.dsoky.api.Publisher;
import com.softserveinc.dsoky.dao.AuthorDAO;
import com.softserveinc.dsoky.dto.BookDTO;
import com.softserveinc.dsoky.dto.RichBookDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookMapperTest {

    @Mock
    private AuthorDAO authorDAO;

    @Before
    public void beforeTest(){
        when(authorDAO.get(anyLong())).thenReturn(new Author(100L));
    }

    @Test
    public void dateFormatIsCorrectAfterConvertingToDTO(){
        BookMapper bookMapper = new BookMapper(authorDAO);
        Book book = new Book();
        book.setPublishDate(LocalDate.of(2017, 1, 1));
        BookDTO bookDTO = bookMapper.convertToDTO(book);
        assertThat(bookDTO.getPublishDate(), equalTo("2017-01-01"));
    }

    @Test
    public void authorsListIsCorrectAfterConvertingToEntity(){
        BookMapper bookMapper = new BookMapper(authorDAO);
        RichBookDTO bookDTO = new RichBookDTO();
        bookDTO.getAuthors().add(100L);
        Book book = bookMapper.convertToEntity(bookDTO);
        assertThat(book.getAuthors(), equalTo(Collections.singletonList(new Author(100L))));
    }

    @Test
    public void publisherWithZeroIdIfEmptyPublisher(){
        BookMapper bookMapper = new BookMapper(authorDAO);
        RichBookDTO bookDTO = new RichBookDTO();
        Book book = bookMapper.convertToEntity(bookDTO);
        assertThat(book.getPublisher(), equalTo(new Publisher(0)));
    }
}
