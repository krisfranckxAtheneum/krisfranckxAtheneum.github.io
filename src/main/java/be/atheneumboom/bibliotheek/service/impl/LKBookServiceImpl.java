package be.atheneumboom.bibliotheek.service.impl;

import be.atheneumboom.bibliotheek.config.Settings;
import be.atheneumboom.bibliotheek.exceptions.BookNotFoundException;
import be.atheneumboom.bibliotheek.model.*;
import be.atheneumboom.bibliotheek.model.DTO.UserDTO;
import be.atheneumboom.bibliotheek.repository.BookRepo;
import be.atheneumboom.bibliotheek.repository.LKBookRepo;
import be.atheneumboom.bibliotheek.repository.MagazineRepo;
import be.atheneumboom.bibliotheek.service.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LKBookServiceImpl implements LKBookService {
    private final LKBookRepo lkBookRepo;
    private final BookRepo bookRepo;
    private final MagazineRepo magazineRepo;
    private final UserService userService;
    private final EmailService emailService;


    @Override
    public Page<LKBook> getBooks(Integer pageNumber, Integer pageSize, String sortField, String keywordFilter) {
        Page<LKBook> books;
        if (keywordFilter==null){
            books = lkBookRepo.findAll(
                    PageRequest.of(
                            pageNumber!=null?pageNumber:0, pageSize!=null?pageSize:Settings.PAGESIZE_BOOKS).
                            withSort(Sort.by(sortField!=null?sortField:Settings.PAGESORT_BOOKS)) );
        }else{
            System.out.println(keywordFilter.trim().toLowerCase());
            books = lkBookRepo.findAll(
                    PageRequest.of(
                            pageNumber!=null?pageNumber:0, pageSize!=null?pageSize:Settings.PAGESIZE_BOOKS).
                            withSort(Sort.by(sortField!=null?sortField:Settings.PAGESORT_BOOKS)), keywordFilter.trim().toLowerCase() );
        }
        List<LKBook> booksList = books.stream().toList();
        return new PageImpl<>(booksList);
    }



    @Override
    public Stream<LKBook> getBooksDataProvider(Integer offset, Integer pagesize, String sortField, String keywordFilter, SortDirection direction) {
        Page<LKBook> books;
        String dir = direction.toString().equals("ASCENDING")?"ASC":"DESC";
        if (keywordFilter==null){
            books = lkBookRepo.findAll(
                    PageRequest.of(
                                    offset!=null?offset:0, pagesize!=null?pagesize:Settings.PAGESIZE_BOOKS).
                            withSort(Sort.by(Sort.Direction.valueOf(dir), sortField!=null?sortField:Settings.PAGESORT_BOOKS)));
        }else{
            System.out.println(keywordFilter.trim().toLowerCase());
            books = lkBookRepo.findAll(
                    PageRequest.of(
                                    offset!=null?offset:0, pagesize!=null?pagesize:Settings.PAGESIZE_BOOKS).
                            withSort(Sort.by(Sort.Direction.valueOf(dir) ,sortField!=null?sortField:Settings.PAGESORT_BOOKS)), keywordFilter.trim().toLowerCase() );
        }
        Stream<LKBook> booksList = books.stream();
        return booksList;
    }

    @Override
    public List<LKBook> getBooksAllWithFilter(String filter) {
        return lkBookRepo.findAllWithFilter(filter);
    }

    @Override
    public List<LKBook> getBooks() {
        return lkBookRepo.findAll();
    }
    @Override
    public LKBook saveBook(LKBook book) {

        LKBook newBoek = new LKBook(book.getTitel(), book.getAuteur(), book.getTaal(), book.getGraad(), book.getFictieNonFictie());
        lkBookRepo.save(newBoek);
        newBoek.setCode(generateCode(newBoek));
        return lkBookRepo.save(newBoek);
    }

    @Override
    public LKBook updateBook(Long id, LKBook book) {
        LKBook bookUpdated = lkBookRepo.findById(id).orElseThrow(()-> new BookNotFoundException("Boek met id "+id+ " niet gevonden."));
        bookUpdated.setAuteur   (book.getAuteur());
        bookUpdated.setTaal     (book.getTaal());
        bookUpdated.setGraad    (book.getGraad());
        bookUpdated.setTitel    (book.getTitel());
        bookUpdated.setFictieNonFictie  (book.getFictieNonFictie());
        bookUpdated.setCode(generateCode(book));

        return lkBookRepo.save(
                bookUpdated
        );
    }

    @Override
    public void deleteBook(Long id) {
        lkBookRepo.deleteById(id);
    }

    @Override
    public LKBook getBook(Long id) {
        return lkBookRepo.findById(id).orElseThrow(()->new BookNotFoundException("Boek met id "+id + " niet gevonden."));
    }

    @Override
    public void borrowBook(LKBook book, UserDTO userDTO) {
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
        lkBookRepo.save(book);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String datum = book.getUitgeleendTot().format(formatters);
        emailService.sendHtmlEmail(user.getVoornaam(), user.getEmail(), book, datum);
        user.setAantalItemsGeleend(user.getAantalItemsGeleend()+1);
        checkEnabledUI(user);
    }

    @Override
    public void reserveBook(LKBook book, UserDTO userDTO) {
        User user = userService.getRealUser(userDTO.getId());
        book.setGereserveerdDoor(user);
        book.setGereserveerdTot(book.getUitgeleendTot().plusDays(Settings.AANTAL_DAGEN_RESERVATIE));
        lkBookRepo.save(book);

        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String datum = book.getGereserveerdTot().format(formatters);
        emailService.sendHtmlEmail_reservatie(user.getVoornaam(), user.getEmail(), book, datum);

    }

    @Override
    public void bringbackBook(LKBook book, User user) {
        book.setGeleendDoor(null);
        book.setUitgeleend(false);
        book.setUitgeleendTot(null);
        lkBookRepo.save(book);

        user.setAantalItemsGeleend(user.getAantalItemsGeleend()-1);
        userService.saveUser(user);
        checkEnabledUI(user);
    }

    @Override
    public void vrijgeven(LKBook book) {
        book.setGereserveerdDoor(null);
        book.setGereserveerdTot(null);
        lkBookRepo.save(book);
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
        for (LKBook lkBook:lkBookRepo.findAll()) {
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
    public void verlengen(LKBook book) {
        book.setUitgeleendTot(book.getUitgeleendTot().plusDays(Settings.AANTAL_DAGEN_LENEN_EXTRA));
        lkBookRepo.save(book);
    }

    private String generateCode(LKBook book){
        StringBuilder sb = new StringBuilder();
        for (String s : book.getAuteur().split(" ")) {
            sb.append(s.toCharArray()[0]);
        }
        sb.append(book.getBoekId());
        return sb.toString();
    }
}
