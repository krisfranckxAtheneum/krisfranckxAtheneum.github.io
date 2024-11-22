package be.atheneumboom.bibliotheek.config;

import be.atheneumboom.bibliotheek.model.Book;
import be.atheneumboom.bibliotheek.model.LKBook;
import be.atheneumboom.bibliotheek.model.Magazine;
import be.atheneumboom.bibliotheek.model.User;
import be.atheneumboom.bibliotheek.model.token.ConfirmationToken;
import be.atheneumboom.bibliotheek.model.token.ConfirmationTokenService;
import be.atheneumboom.bibliotheek.service.EmailService;;
import be.atheneumboom.bibliotheek.service.impl.BookServiceImpl;
import be.atheneumboom.bibliotheek.service.impl.LKBookServiceImpl;
import be.atheneumboom.bibliotheek.service.impl.MagazineServiceImpl;
import be.atheneumboom.bibliotheek.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
@EnableAsync
public class Scheduler {
    private BookServiceImpl boekService;
    private UserServiceImpl userService;
    private MagazineServiceImpl magazineService;
    private LKBookServiceImpl lkBookService;
    private final EmailService emailService;

    private final ConfirmationTokenService confirmationTokenService;



    @Async
    @Scheduled(cron = "0 0 6 * * *")
    public void scheduler(){
        checkUsers();
    }

    @Async
    @Scheduled(cron = "0 0 5 * * *")
    public void scheduler_garbageCollectionTokens(){
        for (ConfirmationToken CFtoken:confirmationTokenService.list()) {
                if (CFtoken.getConfirmedAt()!=null && CFtoken.getConfirmedAt().isBefore(LocalDateTime.now())){
                    confirmationTokenService.deleteToken(CFtoken.getToken());
                }
                if (CFtoken.getExpiresAt()!=null && CFtoken.getExpiresAt().isBefore(LocalDateTime.now())){
                    confirmationTokenService.deleteToken(CFtoken.getToken());
                }
        }
    }


    void checkUsers() {
        List<User> users = userService.getUsers().stream().toList();
        List<Book> books = boekService.getBooks().stream().toList();
        List<LKBook> lkBooks = lkBookService.getBooksAllWithFilter("").stream().toList();
        List<Magazine> magazines = magazineService.getMagazines().stream().toList();

        for (User user : users) {
            List<Book> lijstBoeken              = new ArrayList<>();
            List<Magazine> lijstTijdschriften   = new ArrayList<>();
            List<LKBook> lijstLKBooks           = new ArrayList<>();
            for (Book boek : books) {
                if ((boek.getGeleendDoor() != null && boek.getGeleendDoor().equals(user)) && (boek.getUitgeleendTot() != null && boek.getUitgeleendTot().isBefore(LocalDate.now()))) {
                    lijstBoeken.add(boek); //om lijst books te kunnen meesturen met de mail
                    user.setEnabledUI(false);
                    userService.updateUser(user.getId(),user);
                }
            }
            for (Magazine tijdschrift : magazines) {
                if ((tijdschrift.getGeleendDoor() != null && tijdschrift.getGeleendDoor().equals(user)) && tijdschrift.getUitgeleendTot().isBefore(LocalDate.now())) {
                    lijstTijdschriften.add(tijdschrift);
                    user.setEnabledUI(false);
                    userService.updateUser(user.getId(),user);
                }
            }

            for (LKBook lkBook : lkBooks) {
                if ((lkBook.getGeleendDoor() != null && lkBook.getGeleendDoor().equals(user)) && lkBook.getUitgeleendTot().isBefore(LocalDate.now())) {
                    lijstLKBooks.add(lkBook);
                    user.setEnabledUI(false);
                    userService.updateUser(user.getId(),user);
                }
            }

            if (!user.isVerwittigd1() && (lijstTijdschriften.size() > 0 || lijstBoeken.size() > 0 || lijstLKBooks.size()>0)) {
                user.setVerwittigd1(true);
                userService.updateUser(user.getId(),user);
                emailService.sendHtmlEmail_verwittiging1(user.getVoornaam(), user.getEmail(), lijstTijdschriften, lijstBoeken, lijstLKBooks);
            }

            //controle reservaties verlopen?
            List<Book> lijstBoekenR = new ArrayList<>();
            List<LKBook> lijstLKBoekenR = new ArrayList<>();
            List<Magazine> lijstTijdschriftenR = new ArrayList<>();
            for (Book boek : books) {
                if ((boek.getGereserveerdDoor()!=null && boek.getGereserveerdDoor().equals(user)) && (boek.getGereserveerdTot() != null && boek.getGereserveerdTot().isBefore(LocalDate.now()))) {
                    lijstBoekenR.add(boek); //om lijst books te kunnen meesturen met de mail
                    boek.setGereserveerdDoor(null);
                    boek.setGereserveerdTot(null);
                    boekService.updateBook(boek.getBoekId(),boek);
                }
            }
            for (LKBook boek : lkBooks) {
                if ((boek.getGereserveerdDoor()!=null && boek.getGereserveerdDoor().equals(user)) && (boek.getGereserveerdTot() != null && boek.getGereserveerdTot().isBefore(LocalDate.now()))) {
                    lijstLKBoekenR.add(boek); //om lijst books te kunnen meesturen met de mail
                    boek.setGereserveerdDoor(null);
                    boek.setGereserveerdTot(null);
                    lkBookService.updateBook(boek.getBoekId(),boek);
                }
            }
            for (Magazine tijdschrift : magazines) {
                if ((tijdschrift.getGereserveerdDoor()!=null && tijdschrift.getGereserveerdDoor() == user) && (tijdschrift.getGereserveerdTot() != null && tijdschrift.getGereserveerdTot().isBefore(LocalDate.now()))) {
                    lijstTijdschriftenR.add(tijdschrift);
                    tijdschrift.setGereserveerdDoor(null);
                    tijdschrift.setGereserveerdTot(null);
                    magazineService.updateMagazine(tijdschrift.getTijdschriftid(),tijdschrift);
                }
            }
            if (!user.isVerwittigdR() && (lijstTijdschriftenR.size() > 0 || lijstBoekenR.size() > 0 || lijstLKBoekenR.size() > 0 )) {
                userService.updateUser(user.getId(),user);
                emailService.sendHtmlEmail_verwittigingR(user.getVoornaam(), user.getEmail(), lijstTijdschriftenR, lijstBoekenR, lijstLKBoekenR);
            }
        }
    }
}
