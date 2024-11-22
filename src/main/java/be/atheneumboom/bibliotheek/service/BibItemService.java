package be.atheneumboom.bibliotheek.service;

import be.atheneumboom.bibliotheek.model.BibItem;
import be.atheneumboom.bibliotheek.model.Book;
import org.springframework.stereotype.Service;

import java.util.Collection;

public interface BibItemService {
    Collection<BibItem> getBibItems();

    BibItem saveBibItem(BibItem bibItem);
}
