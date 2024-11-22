package be.atheneumboom.bibliotheek.repository;

import be.atheneumboom.bibliotheek.model.Book;
import be.atheneumboom.bibliotheek.model.LKBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LKBookRepo extends JpaRepository<LKBook,Long> {

    @Query(value = "SELECT * FROM lkbooks b WHERE " +
            "lower(b.auteur) LIKE CONCAT('%',:keywordFilter,'%') OR " +
            "lower(b.titel) LIKE CONCAT('%',:keywordFilter,'%') OR " +
            "lower(b.code_lkboek) LIKE CONCAT('%',:keywordFilter,'%') OR " +
            "lower(b.taal) LIKE CONCAT('%',:keywordFilter,'%')"         //taal werkt niet????
            , nativeQuery = true)
    Page<LKBook> findAll(PageRequest withSort, String keywordFilter);


    @Query(value = "SELECT * FROM lkbooks b WHERE " +
            "lower(b.auteur) LIKE CONCAT('%',:keywordFilter,'%') OR " +
            "lower(b.titel) LIKE CONCAT('%',:keywordFilter,'%') OR " +
            "lower(b.code_lkboek) LIKE CONCAT('%',:keywordFilter,'%') OR " +
            "lower(b.taal) LIKE CONCAT('%',:keywordFilter,'%')"         //taal werkt niet????
            , nativeQuery = true)
    List<LKBook> findAllWithFilter(String keywordFilter);

}
