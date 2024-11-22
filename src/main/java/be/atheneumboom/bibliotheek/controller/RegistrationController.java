package be.atheneumboom.bibliotheek.controller;

import be.atheneumboom.bibliotheek.config.Settings;
import be.atheneumboom.bibliotheek.model.Response;
import be.atheneumboom.bibliotheek.model.User;
import be.atheneumboom.bibliotheek.model.token.ConfirmationTokenService;
import be.atheneumboom.bibliotheek.service.UserService;
import be.atheneumboom.bibliotheek.service.impl.RegistrationService;
import be.atheneumboom.bibliotheek.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

@RestController
@RequestMapping("api/registration")
@AllArgsConstructor
public class RegistrationController {
    @Autowired
    private final RegistrationService registrationService;
    private final UserService userService;
    private final UserServiceImpl userDetailsService;

    private final ConfirmationTokenService confirmationTokenService;
    @PostMapping
    public Response register(@RequestBody() User user){
        try {
            System.out.println("Registration Test "+ user.getEmail());
            registrationService.register(user);
        }catch (IllegalStateException e){
            return Response.builder().build();
        } catch (Exception e){
            return Response.builder().build();
        }
        return Response.builder().build();
    }



}
