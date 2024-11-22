package be.atheneumboom.bibliotheek.service;

import be.atheneumboom.bibliotheek.model.Book;
import be.atheneumboom.bibliotheek.model.LKBook;
import be.atheneumboom.bibliotheek.model.Magazine;
import be.atheneumboom.bibliotheek.model.User;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface EmailService {

  // void sendSimpleMailMessage(String name, String to, String body);
    @Async
    void sendWelcomeEmail(String name, String to);

   /* @Async
    void sendHtmlEmail_paswoord(String name, String to, String body);*/

    @Async
    void sendHtmlEmail(String name, String to, Book boek, String date);

    @Async
    void sendHtmlEmail(String name, String to, Magazine tijdschrift, String date);

    @Async
    void sendHtmlEmail_reservatie(String name, String to, Book boek, String date);

  @Async
  void sendHtmlEmail_reservatie(String name, String to, LKBook boek, String date);

  @Async
  void sendHtmlEmail_reservatie(String name, String to, Magazine magazine, String date);


  @Async
  void sendHtmlEmail_verwittiging1(String name, String to, List<Magazine> tijdschriften, List<Book> boeken, List<LKBook> lkBooks);


    void sendHtmlEmail_reservatieTijdschrift(String voornaam, String email, Magazine tijdschrift, String datum);


  @Async
  void sendHtmlEmail(String name, String to, LKBook boek, String date);

  @Async
  void sendHtmlEmail_verwittigingR(String voornaam, String email, List<Magazine> lijstTijdschriften, List<Book> lijstBoeken, List<LKBook> lijstLKBoeken);

  void sendHtmlEmail_confirmRegistration(String to, User user, String token, String baseURL);

}
