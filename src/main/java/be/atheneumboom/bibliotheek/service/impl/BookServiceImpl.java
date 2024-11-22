package be.atheneumboom.bibliotheek.service.impl;

import be.atheneumboom.bibliotheek.config.Settings;
import be.atheneumboom.bibliotheek.exceptions.BookNotFoundException;
import be.atheneumboom.bibliotheek.model.*;
import be.atheneumboom.bibliotheek.model.DTO.UserDTO;
import be.atheneumboom.bibliotheek.repository.BookRepo;
import be.atheneumboom.bibliotheek.repository.LKBookRepo;
import be.atheneumboom.bibliotheek.repository.MagazineRepo;
import be.atheneumboom.bibliotheek.service.BookService;
import be.atheneumboom.bibliotheek.service.EmailService;
import be.atheneumboom.bibliotheek.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepo bookRepo;
    private final MagazineRepo magazineRepo;
    private final LKBookRepo lkbookRepo;
    private final UserService userService;
    private final EmailService emailService;

    @Override
    public Page<Book> getBooks(Integer pageNumber, Integer pageSize, String sortField, String keywordFilter) {
        Page<Book> books;
        if (keywordFilter==""){
            books = bookRepo.findAll(
                    PageRequest.of(
                            pageNumber!=null?pageNumber:0, pageSize!=null?pageSize:Settings.PAGESIZE_BOOKS).
                            withSort(Sort.by(sortField!=null?sortField:Settings.PAGESORT_BOOKS)) );

        }else{
            books = bookRepo.findAll(
                    PageRequest.of(
                            pageNumber!=null?pageNumber:0, pageSize!=null?pageSize:Settings.PAGESIZE_BOOKS).
                            withSort(Sort.by(sortField!=null?sortField:Settings.PAGESORT_BOOKS)), keywordFilter.trim().toLowerCase() );
        }

        List<Book> booksList = books.stream().toList();
        return new PageImpl<>(booksList);
    }

    @Override
    public Stream<Book> getBooksDataProvider(Integer offset, Integer pagesize, String sortField, String keywordFilter, SortDirection direction) {
        Page<Book> books;
        String dir = direction.toString().equals("ASCENDING")?"ASC":"DESC";
        if (keywordFilter==null){
            books = bookRepo.findAll(
                    PageRequest.of(
                                    offset!=null?offset:0, pagesize!=null?pagesize:Settings.PAGESIZE_BOOKS).
                            withSort(Sort.by(Sort.Direction.valueOf(dir), sortField!=null?sortField:Settings.PAGESORT_BOOKS)) );
        }else{
            books = bookRepo.findAll(
                    PageRequest.of(
                                    offset!=null?offset:0, pagesize!=null?pagesize:Settings.PAGESIZE_BOOKS).
                            withSort(Sort.by(Sort.Direction.valueOf(dir), sortField!=null?sortField:Settings.PAGESORT_BOOKS)), keywordFilter.trim().toLowerCase() );
        }
        Stream<Book> booksList = books.stream();
        return booksList;
    }

    @Override
    public List<Book> getBooksAllWithFilter(String filter) {
        return bookRepo.findAllWithFilter(filter);
    }

    @Override
    public List<Book> getBooksAllWithFilterBorrowed(String filter) {
        return bookRepo.findAllWithFilterBorrowed(filter);
    }

    @Override
    public List<Book> getBooksNoPages(String keywordFilter) {
        return bookRepo.findAll();
    }
    @Override
    public List<Book> getBooks() {
        return bookRepo.findAll();
    }


    @Override
    public Book saveBook(Book book) {
        Book newBoek = new Book(book.getTitel(), book.getAuteur(), book.getTaal(), book.getGraad(), book.getFictieNonFictie());
        bookRepo.save(newBoek);
        newBoek.setCode(generateCode(newBoek));
        return bookRepo.save(newBoek);
    }

    @Override
    public Book updateBook(Long id, Book book) {
        Book bookUpdated = bookRepo.findById(id).orElseThrow(()-> new BookNotFoundException("Boek met id "+id+ " niet gevonden."));
        bookUpdated.setAuteur   (book.getAuteur());
        bookUpdated.setTaal     (book.getTaal());
        bookUpdated.setGraad    (book.getGraad());
        bookUpdated.setTitel    (book.getTitel());
        bookUpdated.setFictieNonFictie  (book.getFictieNonFictie());
        bookUpdated.setCode(generateCode(book));
        return bookRepo.save(
                bookUpdated
        );
    }

    @Override
    public void deleteBook(Long id) {
        bookRepo.deleteById(id);
    }

    @Override
    public Book getBook(Long id) {
        return bookRepo.findById(id).orElseThrow(()->new BookNotFoundException("Boek met id "+id + " niet gevonden."));
    }

    @Override
    public void borrowBook(Book book, UserDTO userDTO) {
        User user = userService.getRealUser(userDTO.getId());
        book.setGeleendDoor(user);
        book.setUitgeleend(true);
        if (user.getVoornaam().toLowerCase().contains("leestas")||user.getFamilienaam().toLowerCase().contains("leestas")){
            book.setUitgeleendTot(LocalDate.now().plusDays(Settings.AANTAL_DAGEN_LENEN_LEESTAS));
        } else {
            book.setUitgeleendTot(LocalDate.now().plusDays(Settings.AANTAL_DAGEN_LENEN));
        }
        book.setGereserveerdTot(null);
        book.setGereserveerdDoor(null);
        bookRepo.save(book);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String datum = book.getUitgeleendTot().format(formatters);
        emailService.sendHtmlEmail(user.getVoornaam(), user.getEmail(), book, datum);
        user.setAantalItemsGeleend(user.getAantalItemsGeleend()+1);
        checkEnabledUI(user);
    }

    @Override
    public void reserveBook(Book book, UserDTO userDTO) {
        User user = userService.getRealUser(userDTO.getId());
        book.setGereserveerdDoor(user);
        book.setGereserveerdTot(book.getUitgeleendTot().plusDays(Settings.AANTAL_DAGEN_RESERVATIE));
        bookRepo.save(book);

        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String datum = book.getGereserveerdTot().format(formatters);
        emailService.sendHtmlEmail_reservatie(user.getVoornaam(), user.getEmail(), book, datum);

    }

    @Override
    public void bringbackBook(Book book, User user) {
        book.setGeleendDoor(null);
        book.setUitgeleend(false);
        book.setUitgeleendTot(null);
        bookRepo.save(book);

        user.setAantalItemsGeleend(user.getAantalItemsGeleend()-1);
        userService.saveUser(user);
        checkEnabledUI(user);
    }

    @Override
    public void vrijgeven(Book book) {
        book.setGereserveerdDoor(null);
        book.setGereserveerdTot(null);
        bookRepo.save(book);
    }

    private void checkEnabledUI(User user) {
        if (user.getAantalItemsGeleend()>Settings.MAX_AANTAL_ITEMS_USER-1
            &&!(user.getVoornaam().toLowerCase().contains("leestas")||user.getFamilienaam().toLowerCase().contains("leestas"))
            ){
            user.setEnabledUI(false);
            userService.saveUser(user);
        } else{
            user.setEnabledUI(true);
            userService.saveUser(user);
        }
        for (Book book: bookRepo.findAll()) {
            if (book.getGeleendDoor()!= null && Objects.equals(book.getGeleendDoor().getId(), user.getId())
                    && book.getUitgeleendTot().isBefore(LocalDate.now())){
                user.setEnabledUI(false);
                userService.saveUser(user);
            }
        }
        for (LKBook lkBook:lkbookRepo.findAll()) {
            if (lkBook.getGeleendDoor()!= null && Objects.equals(lkBook.getGeleendDoor().getId(), user.getId())
                    && lkBook.getUitgeleendTot().isBefore(LocalDate.now())){
                user.setEnabledUI(false);
                userService.saveUser(user);
            }
        }
        for (Magazine magazine:magazineRepo.findAll()) {
            if (magazine.getGeleendDoor()!= null && Objects.equals(magazine.getGeleendDoor().getId(), user.getId())
                    && magazine.getUitgeleendTot().isBefore(LocalDate.now())){
                user.setEnabledUI(false);
                userService.saveUser(user);
            }
        }
    }
    @Override
    public void verlengen(Book book) {
        book.setUitgeleendTot(book.getUitgeleendTot().plusDays(Settings.AANTAL_DAGEN_LENEN_EXTRA));
        bookRepo.save(book);
    }

    private String generateCode(Book book){
        StringBuilder sb = new StringBuilder();
        for (String s : book.getAuteur().split(" ")) {
            sb.append(s.toCharArray()[0]);
        }
        sb.append(book.getBoekId());
        return sb.toString();
    }
}
