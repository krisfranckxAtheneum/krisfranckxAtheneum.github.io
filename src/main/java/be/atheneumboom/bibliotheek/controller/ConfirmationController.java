package be.atheneumboom.bibliotheek.controller;

import be.atheneumboom.bibliotheek.config.Settings;
import be.atheneumboom.bibliotheek.model.Response;
import be.atheneumboom.bibliotheek.model.token.ConfirmationTokenService;
import be.atheneumboom.bibliotheek.service.UserService;
import be.atheneumboom.bibliotheek.service.impl.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@Controller
@RequestMapping("api/registration")
@AllArgsConstructor
public class ConfirmationController {
    @Autowired
    private final RegistrationService registrationService;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;

    @GetMapping("/confirm")
    public ResponseEntity<Response> confirm(@RequestParam("token") String token, @RequestParam(value = "userclick", required = false) String userclick) {

        if (userclick == null || userclick.isEmpty()){
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl(Settings.BASE_URL_FRONT+"/confirm?token="+token);
            return ResponseEntity.ok(
                    Response.builder()
                            .data(Map.of("view",redirectView))
                            .status(OK)
                            .statusCode(OK.value())
                            .build());
        }
        if (userclick.equals("true")) {
            try{
                registrationService.confirmToken(token);
                /*Long userId = confirmationTokenService.getToken(token).get().getUser().getId();
                registrationService.sendNewConfirmationMail(userService.getRealUser(userId));*/
                System.out.println("Confirmed°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°");
                RedirectView redirectView = new RedirectView();
                redirectView.setUrl(Settings.BASE_URL_FRONT+"/confirmed");
                return ResponseEntity.ok(
                        Response.builder()
                                .data(Map.of("view",redirectView))
                                .status(OK)
                                .statusCode(OK.value())
                                .build());
            }catch (IllegalStateException e){

            }
        } else {
            RedirectView redirectView = new RedirectView();
        redirectView.setUrl(Settings.BASE_URL_FRONT+"/confirm?token="+token);
        return ResponseEntity.ok(
                Response.builder()
                        .data(Map.of("view",redirectView))
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
        }
        return null;
    }


    
}
