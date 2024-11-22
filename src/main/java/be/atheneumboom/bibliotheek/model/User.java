package be.atheneumboom.bibliotheek.model;

import be.atheneumboom.bibliotheek.model.DTO.UserDTO;
import be.atheneumboom.bibliotheek.model.token.ConfirmationToken;
import be.atheneumboom.bibliotheek.model.token.ConfirmationTokenService;
import be.atheneumboom.bibliotheek.repository.UserRepo;
import be.atheneumboom.bibliotheek.service.EmailService;
import be.atheneumboom.bibliotheek.service.UserService;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Comparable<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column()
    @NotEmpty(message = "De voornaam mag niet leeg zijn.")
    private String voornaam;
    @Column()
    @NotEmpty(message = "De familienaam mag niet leeg zijn.")
    private String familienaam;
    @Column(unique = true)
    private String email;
    @Column()
    private int aantalItemsGeleend = 0;
    @Column()
    private boolean enabled = false;
    @Column()
    private boolean verwittigd1 = false;
    @Column()
    private boolean verwittigdR = false;

    @Column()
    private String paswoord;

    @Transient
    private String function;

    @Column
    private String auth = "ROLE_USER";

    private boolean enabledUI = false;



    public User(String voornaam, String familienaam, String email) {
        this(voornaam,familienaam, email, "paswoord");
    }

    public User(String voornaam, String familienaam, String email, String paswoord) {
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.email = email;
        this.paswoord = paswoord;
    }


    @Override
    public String toString() {
        return familienaam + " " + voornaam;
    }

    @Override
    public int compareTo(User o) {
        return this.familienaam.compareToIgnoreCase(o.getFamilienaam());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getVoornaam().equals(user.getVoornaam()) && getFamilienaam().equals(user.getFamilienaam());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVoornaam(), getFamilienaam());
    }


}
