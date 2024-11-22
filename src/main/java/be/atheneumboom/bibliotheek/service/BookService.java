package be.atheneumboom.bibliotheek.service;

import be.atheneumboom.bibliotheek.model.BibItem;
import be.atheneumboom.bibliotheek.model.Book;
import be.atheneumboom.bibliotheek.model.DTO.UserDTO;
import be.atheneumboom.bibliotheek.model.LKBook;
import be.atheneumboom.bibliotheek.model.User;
import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface BookService {
    Page<Book> getBooks(Integer offset, Integer pagesize, String sortField, String keywordFilter);

    Stream<Book> getBooksDataProvider(Integer offset, Integer pagesize, String sortField, String keywordFilter, SortDirection direction);

    List<Book> getBooksAllWithFilter(String filter);

    List<Book> getBooksAllWithFilterBorrowed(String filter);

    List<Book> getBooksNoPages(String keywordFilter);

    List<Book> getBooks();

    Book saveBook(Book book);

    Book updateBook(Long id, Book book);

    void deleteBook(Long id);

    Book getBook(Long id);

    void borrowBook(Book book, UserDTO userDTO);

    void reserveBook(Book book, UserDTO user);

    void bringbackBook(Book book, User user);

    void vrijgeven(Book book);

    void verlengen(Book book);
}
