package be.atheneumboom.bibliotheek.service;

import be.atheneumboom.bibliotheek.model.Book;
import be.atheneumboom.bibliotheek.model.DTO.UserDTO;
import be.atheneumboom.bibliotheek.model.LKBook;
import be.atheneumboom.bibliotheek.model.User;
import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Stream;

public interface LKBookService {
    Page<LKBook> getBooks(Integer offset, Integer pagesize, String sortField, String keywordFilter);
    Stream<LKBook> getBooksDataProvider(Integer offset, Integer pagesize, String sortField, String keywordFilter, SortDirection direction);

    List<LKBook> getBooksAllWithFilter(String filter);

    List<LKBook> getBooks();

    LKBook saveBook(LKBook book);

    LKBook updateBook(Long id, LKBook book);
    void deleteBook(Long id);

    LKBook getBook(Long id);

    void borrowBook(LKBook book, UserDTO userDTO);

    void reserveBook(LKBook book, UserDTO user);

    void bringbackBook(LKBook book, User user);

    void vrijgeven(LKBook book);

    void verlengen(LKBook book);
}
