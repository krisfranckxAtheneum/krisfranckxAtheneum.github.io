package be.atheneumboom.bibliotheek.repository;

import be.atheneumboom.bibliotheek.model.Book;
import be.atheneumboom.bibliotheek.model.LKBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepo extends JpaRepository<Book,Long> {

    @Query(value = "SELECT * FROM boek b " +
            "WHERE " +
            "lower(b.auteur) LIKE CONCAT('%',:keywordFilter,'%') OR " +
            "lower(b.titel) LIKE CONCAT('%',:keywordFilter,'%') OR " +
            "lower(b.code_boek) LIKE CONCAT('%',:keywordFilter,'%') OR " +
            "lower(b.taal) LIKE CONCAT('%',:keywordFilter,'%')"
            //taal werkt niet???? nummer van enum
            , nativeQuery = true)
    Page<Book> findAll(PageRequest withSort, String keywordFilter);

    @Query(value = "SELECT COUNT(titel) as AantalBoeken FROM boek"
            , nativeQuery = true)
    Integer findNumberofBooks();

    @Query(value = "SELECT * FROM boek b WHERE " +
            "lower(b.auteur) LIKE CONCAT('%',:keywordFilter,'%') OR " +
            "lower(b.titel) LIKE CONCAT('%',:keywordFilter,'%') OR " +
            "lower(b.code_boek) LIKE CONCAT('%',:keywordFilter,'%') OR " +
            "lower(b.taal) LIKE CONCAT('%',:keywordFilter,'%')"         //taal werkt niet????
            , nativeQuery = true)
    List<Book> findAllWithFilter(String keywordFilter);

    @Query(value = "SELECT * FROM boek b WHERE " +
            "b.uitgeleend = TRUE"
            , nativeQuery = true)
    List<Book> findAllWithFilterBorrowed(String keywordFilter);

}
