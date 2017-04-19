package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Publisher;

import java.util.List;

public interface PublisherDAO extends LibraryResourceDAO<Publisher>{
    Publisher getByBook(long id);
}
