package be.atheneumboom.bibliotheek.service;

import be.atheneumboom.bibliotheek.model.DTO.UserDTO;
import be.atheneumboom.bibliotheek.model.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {
    Page<UserDTO> getUsers(Integer offset, Integer pagesize, String sortField, String keywordFilter);

    UserDTO getUser(Long userId);

    User getRealUser(Long userId);

    User saveUserWithPassword(User user);
    boolean deleteUser(Long userId);

    User saveUser(User user);

    UserDTO updateUser(Long id, User user);

    Optional<User> getUserByEmail(String email);
}
