package be.atheneumboom.bibliotheek.model.DTO;

import be.atheneumboom.bibliotheek.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Comparable<UserDTO> {

    private Long id;
    private String voornaam;
    private String familienaam;
    private String email;
    private int aantalItemsGeleend = 0;
    private boolean enabled = true;
    private String auth = "ROLE_USER";
    private boolean enabledUI = true;

    public UserDTO(User user) {
        this(user.getId(), user.getVoornaam(), user.getFamilienaam(), user.getEmail());
    }

    public UserDTO(Long id, String voornaam, String familienaam, String email) {
        this.id=id;
        this.voornaam=voornaam;
        this.familienaam=familienaam;
        this.email=email;
    }

    @Override
    public String toString() {
        return familienaam + " " + voornaam;
    }

    @Override
    public int compareTo(UserDTO o) {
        return this.familienaam.compareToIgnoreCase(o.getFamilienaam());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO user = (UserDTO) o;
        return getVoornaam().equals(user.getVoornaam()) && getFamilienaam().equals(user.getFamilienaam());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVoornaam(), getFamilienaam());
    }
}
