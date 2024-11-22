package be.atheneumboom.bibliotheek.service.impl;

import be.atheneumboom.bibliotheek.config.Settings;
import be.atheneumboom.bibliotheek.model.Response;
import be.atheneumboom.bibliotheek.model.User;
import be.atheneumboom.bibliotheek.model.token.ConfirmationToken;
import be.atheneumboom.bibliotheek.model.token.ConfirmationTokenService;
import be.atheneumboom.bibliotheek.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RegistrationService {

    //private final EmailValidator emailValidator;
    private final UserServiceImpl userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final PasswordEncoder encoder;
    private final EmailService emailService;


    public Response register(User user) {
        User newUser = user;
        newUser.setEnabledUI(false);
        signUpUser(newUser);
        return Response.builder().build();
    }

    @Transactional
    public void confirmToken(String token){

        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(()->new IllegalStateException("token not found"));
        if (confirmationToken.getConfirmedAt()!=null){
            throw new IllegalStateException("email already comfirmed");
        }

        LocalDateTime expiresAt = confirmationToken.getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("token expired");
        }
        emailService.sendWelcomeEmail(confirmationToken.getUser().getVoornaam(),confirmationToken.getUser().getEmail());
        confirmationTokenService.setConfirmedAt(token);
        enableUser(confirmationToken.getUser().getEmail());
    }

    public String signUpUser(User user){
        boolean userExists = userService.getUserByEmail(user.getEmail()).isPresent() ;
        if(userExists){
            throw new IllegalStateException("Email al in gebruik");
        }
        userService.createUser_registrated(user);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(3L),
                user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        emailService.sendHtmlEmail_confirmRegistration(user.getEmail(), user, confirmationToken.getToken(), Settings.BASE_URL_FRONT);
        return token;
    }

    public void enableUser(String email) {
        User user = userService.getUserByEmail(email).get();
        user.setEnabled(true);
        user.setEnabledUI(true);
        userService.saveUser(user);
    }

    public void sendNewConfirmationMail(User user){
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(3L),
                user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        emailService.sendHtmlEmail_confirmRegistration(user.getEmail(), user, confirmationToken.getToken(), Settings.BASE_URL_BACK);
    }

}
