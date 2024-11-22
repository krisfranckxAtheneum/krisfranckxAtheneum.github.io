package be.atheneumboom.bibliotheek.service.impl;


import be.atheneumboom.bibliotheek.model.Book;
import be.atheneumboom.bibliotheek.model.LKBook;
import be.atheneumboom.bibliotheek.model.Magazine;
import be.atheneumboom.bibliotheek.model.User;
import be.atheneumboom.bibliotheek.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("smtp.gmail.com")
    private String host;
    @Value("atheneumboom.bibliotheek@gmail.com")
    private String fromEmail;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    /*@Override
    @Async
    public void sendSimpleMailMessage(String name, String to, String body) {
        try {
            System.out.println("Simple mail verzonden °°°°°°°°°°°°°°°°°");

            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("Test subject");
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText("hallo! Dit werkt...");
            mailSender.send(message);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }
*/
    @Override
    @Async
    public void sendWelcomeEmail(String name, String to) {
        System.out.println("HTTP mail 'Nieuwe gebruiker' verzonden naar "+ to);
        try {
            Context context = new Context();
            context.setVariable("name", name);
            String text = templateEngine.process("emailTemplate", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setSubject("Nieuwe gebruiker voor de bibliotheek!");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);
            mailSender.send(message);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }
/*
    @Override
    @Async
    public void sendHtmlEmail_paswoord(String name, String to, String body) {
        System.out.println("HTTP mail 'Nieuwe leerling' (niet via registratie) verzonden naar "+ to);
        try {
            Context context = new Context();
            context.setVariable("name", name);
            String text = templateEngine.process("emailTemplate_paswoord", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setSubject("Nieuwe leerling voor de bibliotheek!");
            helper.setFrom(fromEmail);
            helper.setTo(to);

            helper.setText(text, true);
            mailSender.send(message);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

*/
    @Override
    @Async
    public void sendHtmlEmail(String name, String to, Book boek, String date) {
        System.out.println("HTTP mail 'Nieuw boek geleend' verzonden naar "+to);
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("boek", boek);
            context.setVariable("date", date);

            String text = templateEngine.process("emailTemplate_geleend", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setSubject("Nieuw boek geleend");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);
            mailSender.send(message);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }
    @Override
    @Async
    public void sendHtmlEmail(String name, String to, Magazine tijdschrift, String date) {
        System.out.println("HTTP mail 'Nieuw tijdschrift geleend' verzonden naar "+to);
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("tijdschrift", tijdschrift);
            context.setVariable("date", date);

            String text = templateEngine.process("emailTemplate_geleendTijdschrift", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setSubject("Nieuw tijdschrift geleend");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);
            mailSender.send(message);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }
    @Override
    @Async
    public void sendHtmlEmail(String name, String to, LKBook boek, String date) {
        System.out.println("HTTP mail 'Nieuw boek geleend' verzonden naar "+to);
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("boek", boek);
            context.setVariable("date", date);

            String text = templateEngine.process("emailTemplate_geleend", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setSubject("Nieuw boek geleend");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);
            mailSender.send(message);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }


    @Override
    @Async
    public void sendHtmlEmail_reservatie(String name, String to, Book boek, String date) {
        System.out.println("HTTP mail 'reservatie ok' verzonden naar " + to);
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("boek", boek);
            context.setVariable("date", date);

            String text = templateEngine.process("emailTemplate_reservatie", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setSubject("Boek gereserveerd");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);
            mailSender.send(message);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlEmail_reservatie(String name, String to, LKBook boek, String date) {
        System.out.println("HTTP mail 'reservatie ok' verzonden naar " + to);
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("boek", boek);
            context.setVariable("date", date);

            String text = templateEngine.process("emailTemplate_reservatie", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setSubject("Boek gereserveerd");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);
            mailSender.send(message);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlEmail_reservatie(String name, String to, Magazine magazine, String date) {
        System.out.println("HTTP mail 'reservatie ok' verzonden naar " + to);
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("magazine", magazine);
            context.setVariable("date", date);

            String text = templateEngine.process("emailTemplate_reservatie", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setSubject("Boek gereserveerd");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);
            mailSender.send(message);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlEmail_verwittiging1(String name, String to, List<Magazine> tijdschriften, List<Book> boeken, List<LKBook> lkBooks) {
        System.out.println("HTTP mail 'Verwittiging boek te laat ingeleverd' verzonden naar "+to);
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("boeken", boeken);
            context.setVariable("lkBooks", lkBooks);
            context.setVariable("tijdschriften", tijdschriften);

            String text = templateEngine.process("emailTemplate_verwittiging1", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(1);
            helper.setSubject("Bibliotheek: boek te laat ingeleverd.");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);
            mailSender.send(message);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }


    @Override
    public void sendHtmlEmail_verwittigingR(String voornaam, String email, List<Magazine> lijstTijdschriften, List<Book> lijstBoeken, List<LKBook> lijstLKBoeken) {
        System.out.println("HTTP mail 'verwittiging Reservatie verlopen' verzonden naar "+email);
        try {
            Context context = new Context();
            context.setVariable("name", voornaam);
            context.setVariable("boeken", lijstBoeken);
            context.setVariable("lkboeken", lijstLKBoeken);
            context.setVariable("tijdschriften", lijstTijdschriften);

            String text = templateEngine.process("emailTemplate_verwittigingR", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(1);
            helper.setSubject("Bibliotheek: reservatie verlopen.");
            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setText(text, true);
            mailSender.send(message);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

        @Override
        public void sendHtmlEmail_reservatieTijdschrift(String voornaam, String email, Magazine tijdschrift, String datum) {
            System.out.println("HTTP mail 'reservatie tijdschrift ok' verzonden naar "+email);
            try {
                Context context = new Context();
                context.setVariable("name", voornaam);
                context.setVariable("tijdschrift", tijdschrift);
                context.setVariable("date", datum);

                String text = templateEngine.process("emailTemplate_reservatieTijdschrift", context);
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setSubject("Tijdschrift gereserveerd");
                helper.setFrom(fromEmail);
                helper.setTo(email);
                helper.setText(text, true);
                mailSender.send(message);

            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                throw new RuntimeException(exception.getMessage());
            }
        }

    @Override
    @Async
    public void sendHtmlEmail_confirmRegistration(String to, User user, String token, String baseURL) {
        try {
            Context context = new Context();
            context.setVariable("name", user.getVoornaam());
            context.setVariable("totalUrl",baseURL+"/confirm?token="+token);

            String text = templateEngine.process("emailTemplate_confirmatieEmail.html", context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setSubject("Bevestig je email");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);
            mailSender.send(message);
            System.out.println("HTML mail 'confirmation Registration' verzonden naar "+to);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

}
