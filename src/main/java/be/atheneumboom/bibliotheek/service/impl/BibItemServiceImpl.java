package be.atheneumboom.bibliotheek.service.impl;

import be.atheneumboom.bibliotheek.model.BibItem;
import be.atheneumboom.bibliotheek.model.Book;
import be.atheneumboom.bibliotheek.model.LKBook;
import be.atheneumboom.bibliotheek.model.Magazine;
import be.atheneumboom.bibliotheek.repository.BookRepo;
import be.atheneumboom.bibliotheek.repository.LKBookRepo;
import be.atheneumboom.bibliotheek.repository.MagazineRepo;
import be.atheneumboom.bibliotheek.service.BibItemService;
import be.atheneumboom.bibliotheek.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/*@Service
@RequiredArgsConstructor
public class BibItemServiceImpl implements BibItemService {
    private final BookRepo bookRepo;
    private final LKBookRepo lkBookRepo;
    private final MagazineRepo magazineRepo;
    @Override
    public Collection<BibItem> getBibItems() {
        Collection<BibItem> collection = new ArrayList<>();
        collection.addAll(bookRepo.findAll());
        collection.addAll(magazineRepo.findAll());
        return collection;
    }
    public Collection<BibItem> getLKBibItems() {
        Collection<BibItem> collection = new ArrayList<>();
        collection.addAll(lkBookRepo.findAll());
        return collection;
    }

    @Override
    public BibItem saveBibItem(BibItem bibItem) {
        if (bibItem.getClass().getName().toLowerCase().contains("lk")){
            lkBookRepo.save((LKBook) bibItem);
        } else if (bibItem.getClass().getName().toLowerCase().contains("book")) {
            bookRepo.save((Book) bibItem);
        } else {
            magazineRepo.save((Magazine) bibItem);
        }
        return bibItem;
    }
}*/
