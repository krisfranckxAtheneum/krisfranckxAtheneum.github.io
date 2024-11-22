package be.atheneumboom.bibliotheek.repository;

import be.atheneumboom.bibliotheek.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM users b WHERE " +
            "lower(b.voornaam) LIKE CONCAT('%',:keywordFilter,'%') OR " +
            "lower(b.familienaam) LIKE CONCAT('%',:keywordFilter,'%') OR " +
            "lower(b.email) LIKE CONCAT('%',:keywordFilter,'%')"
            , nativeQuery = true)
    Page<User> findAll(PageRequest withSort, String keywordFilter);

    Optional<User> findByEmail(String email);


}
