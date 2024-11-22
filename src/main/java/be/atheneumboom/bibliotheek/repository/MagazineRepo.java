package be.atheneumboom.bibliotheek.repository;

import be.atheneumboom.bibliotheek.model.Book;
import be.atheneumboom.bibliotheek.model.Magazine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MagazineRepo extends JpaRepository<Magazine,Long> {
    @Query(value = "SELECT * FROM tijdschrift m WHERE " +
            "lower(m.naamtijdschrift) LIKE CONCAT('%',:keywordFilter,'%') OR " +
            "lower(m.code_tijdschrift) LIKE CONCAT('%',:keywordFilter,'%') OR " +
            "lower(m.nummertijdschrift) LIKE CONCAT('%',:keywordFilter,'%') OR " +
            "lower(m.jaargang) LIKE CONCAT('%',:keywordFilter,'%')"         //taal werkt niet????
            , nativeQuery = true)
    Page<Magazine> findAll(PageRequest withSort, String keywordFilter);
}
