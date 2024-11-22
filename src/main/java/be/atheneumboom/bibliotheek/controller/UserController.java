package be.atheneumboom.bibliotheek.controller;

import be.atheneumboom.bibliotheek.model.DTO.UserDTO;
import be.atheneumboom.bibliotheek.model.Response;
import be.atheneumboom.bibliotheek.model.User;
import be.atheneumboom.bibliotheek.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("")
    public ResponseEntity<Response> getUsers(@RequestParam(value = "pageSize", required = false) Integer pageSize,
                                             @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                             @RequestParam(value = "filter", required = false) String filter,
                                             @RequestParam(value = "sort", required = false) String sort){
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .data(Map.of("users",userService.getUsers(pageNumber, pageSize, sort, filter).stream().toList()))
                        .message("Alle users opgehaald")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<Response> getUser(@PathVariable("id")Long id){
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .data(Map.of("user",userService.getUser(id)))
                        .message("User met id "+id+" opgehaald")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @PostMapping ()
    public ResponseEntity<Response> saveUser(@RequestBody User user){
        User user1 = null;
        user1 = userService.saveUserWithPassword(user);
        UserDTO userDTO = new UserDTO(user1);
        return ResponseEntity.ok(
                Response.builder()
                            //.timeStamp(LocalDateTime.now())
                            .data(Map.of("user",userDTO))
                            .message("User bewaard "+userDTO)
                            .status(CREATED)
                            .statusCode(CREATED.value())
                            .build());
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<Response> deleteUser(@PathVariable("userId") Long userId) {
            boolean deleted = userService.deleteUser(userId);
            return ResponseEntity.ok(
                    Response.builder()
                            //.timeStamp(LocalDateTime.now())
                            .data(Map.of("deleted", deleted))
                            .message("User gedeleted met id " + userId)
                            .status(OK)
                            .statusCode(OK.value())
                            .build());
    }

    @GetMapping("/sec/{email}")
    public ResponseEntity<Response> getUser(@PathVariable("email")String email){
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())   //gaf probleem in vaadin
                        .data(Map.of("securityUser",userService.getUserByEmail(email)))
                        .message("User met email "+email+" opgehaald")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}
