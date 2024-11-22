package be.atheneumboom.bibliotheek.service.impl;

import be.atheneumboom.bibliotheek.config.Settings;
import be.atheneumboom.bibliotheek.exceptions.UserNotFountException;
import be.atheneumboom.bibliotheek.exceptions.UserNotValidException;
import be.atheneumboom.bibliotheek.model.DTO.UserDTO;
import be.atheneumboom.bibliotheek.model.User;
import be.atheneumboom.bibliotheek.repository.UserRepo;
import be.atheneumboom.bibliotheek.service.EmailService;
import be.atheneumboom.bibliotheek.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    @Override
    public Page<UserDTO> getUsers(Integer pageNumber, Integer pageSize, String sortField, String keywordFilter) {
        Page<User> users;
        if (keywordFilter==null){
            users = userRepo.findAll(
                    PageRequest.of(
                            pageNumber!=null?pageNumber:0, pageSize!=null? pageSize:Settings.PAGESIZE_USERS).
                            withSort(Sort.by(sortField!=null?sortField:Settings.PAGESORT_USERS)) );
        }else{
            users = userRepo.findAll(
                    PageRequest.of(
                            pageNumber!=null?pageNumber:0, pageSize!=null? pageSize:Settings.PAGESIZE_USERS).
                            withSort(Sort.by(sortField!=null?sortField:Settings.PAGESORT_USERS)), keywordFilter.toLowerCase() );
        }

         List<UserDTO> usersDTO = users.stream().map(user -> new UserDTO(
                user.getId(),user.getVoornaam(), user.getFamilienaam(), user.getEmail(),
                user.getAantalItemsGeleend(), user.isEnabled(), user.getAuth(), user.isEnabledUI())).toList();
        return new PageImpl<>(usersDTO);
    }

    public List<User> getUsers() {
        return userRepo.findAll();
    }


    @Override
    public UserDTO getUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(()-> new UserNotFountException("User met id "+userId+" niet gevonden."));
        return new UserDTO(user.getId(),user.getVoornaam(), user.getFamilienaam(), user.getEmail(), user.getAantalItemsGeleend(), user.isEnabled(), user.getAuth(), user.isEnabledUI());
    }
    @Override
    public User getRealUser(Long userId) {
        return userRepo.findById(userId).orElseThrow(()-> new UserNotFountException("User met id "+userId+" niet gevonden."));
    }

    @Override
    public User saveUserWithPassword(User user) {
        try{
            if (user.getPaswoord()==null){
                user.setPaswoord("paswoord");
            }
            user.setPaswoord(encoder.encode(user.getPaswoord()));
            userRepo.save(user);
        }catch (Exception e){
            throw new UserNotValidException("Email moet uniek zijn. Familienaam en voornaam mogen niet leeg zijn.");
        }
        return user;

    }

    @Override
    public User saveUser(User user) {
        try{
            userRepo.save(user);
        }catch (Exception e){
            throw new UserNotValidException("Email moet uniek zijn. Familienaam en voornaam mogen niet leeg zijn.");
        }
        return user;

    }

    @Override
    public UserDTO updateUser(Long id, User user) {
        User userUpdated = userRepo.findById(id).orElseThrow(() -> new UserNotFountException("User met id " + id + " niet gevonden."));
        userUpdated.setVoornaam(user.getVoornaam());
        userUpdated.setFamilienaam(user.getFamilienaam());
        userUpdated.setEmail(user.getEmail());
        userUpdated.setPaswoord(encoder.encode(user.getPaswoord()));
        return new UserDTO(userRepo.save(
                userUpdated
        ));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        System.out.println("getUserByEmail aangeroepen met email "+email);
        Optional<User> optionalUser = userRepo.findByEmail(email);
        System.out.println(optionalUser);

        return optionalUser;
        //throw new UserNotFountException("Gebruiker niet gevonden "+email);
    }

    @Override
    public boolean deleteUser(Long userId) {
        try{
            userRepo.deleteById(userId);
            return true;
        }catch (Exception exception){
            throw new UserNotFountException("Delete user mislukt met id"+userId);
            // deleteById geeft geen exception als ID niet (meer) bestaat - dus deze wordt nooit ge-throwed
        }
    }

    public User createUser_registrated(User user) {
        if (userRepo.findByEmail(user.getEmail()).isPresent()){
            throw new SecurityException("email al in gebruik");
        }else {
            System.out.println("Saving new user: " + user);
            //emailService.sendHtmlEmail(user.getVoornaam(), user.getEmail(), "test");
            user.setEnabledUI(false);
            if (user.getFunction().equalsIgnoreCase("leraar")){
                user.setAuth("ROLE_LK");
            }else{
                user.setAuth("ROLE_USER");
            }

            return saveUserWithPassword(user);
        }
    }
}
