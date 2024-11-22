
package be.atheneumboom.bibliotheek.controller;

import be.atheneumboom.bibliotheek.model.*;
import be.atheneumboom.bibliotheek.model.DTO.UserDTO;
import be.atheneumboom.bibliotheek.service.BookService;
import be.atheneumboom.bibliotheek.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.SortDirection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/admin/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final UserService userService;

    @GetMapping("/provider")
    public Stream<Book> getBooks(@RequestParam(value = "pageSize", required = false) Integer pageSize,
                                 @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                 @RequestParam(value = "filter", required = false) String filter,
                                 @RequestParam(value = "sort", required = false) String sort,
                                 @RequestParam(value = "direction", required = false) SortDirection direction
                                 ){


      /*  User user = userService.getRealUser(1903L);
        user.setPaswoord("pas");
        userService.saveUserWithPassword(user);
        User user2 = userService.getRealUser(1902L);
        user2.setPaswoord("pas");
        userService.saveUserWithPassword(user2);
        System.out.println("Users paswoord aangepast____________________");*/
        /*return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("books",bookService.getBooks(pageNumber, pageSize, sort, filter).stream().toList()))
                        .message("Alle boeken opgehaald")
                 sort       .status(OK)
                        .statusCode(OK.value())
                        .build()
        );*/
        return bookService.getBooksDataProvider(pageNumber, pageSize, sort, filter, direction);
    }

    @GetMapping("/count")
    public Integer getNumberOfBooks(){
        return bookService.getBooks().size();
    }

    @GetMapping("")
    public ResponseEntity<Response> getBooksAllWithFilter(@RequestParam(value = "filter") String filter){
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        //.data(Map.of("lkbooks",bookService.getBooks(pageNumber, pageSize, sort, filter).stream().toList()))
                        .data(Map.of("booksAllWithFilter",bookService.getBooksAllWithFilter(filter),
                                "numberofBooksWithFilter", bookService.getBooksAllWithFilter(filter).size()))
                        .message("Alle boeken opgehaald")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/borrowed")
    public ResponseEntity<Response> getBooksAllWithFilterBorrowed(
            @RequestParam(value = "filter") String filter,
            @RequestParam(value = "borrowed") String borrowed
            ){
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        //.data(Map.of("lkbooks",bookService.getBooks(pageNumber, pageSize, sort, filter).stream().toList()))
                        .data(Map.of("booksAllWithFilter",bookService.getBooksAllWithFilterBorrowed(filter)))
                        .message("Alle boeken opgehaald")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getBook(
            @PathVariable("id")Long id){
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .data(Map.of("book",bookService.getBook(id)))
                        .message("Boek met id "+id+" opgehaald")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @PostMapping ()
    public ResponseEntity<Response> saveBook(
            @RequestBody Book book){
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .data(Map.of("book",bookService.saveBook(book)))
                        .message("Boek bewaard: "+book)
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build());
    }

    @PutMapping ("{bookId}")
    public ResponseEntity<Response> updateBook(
            @RequestBody Book book,
            @PathVariable("bookId") Long bookId){
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .data(Map.of("book",bookService.updateBook(bookId, book)))
                        .message("Boek aangepast: "+book)
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @DeleteMapping("{bookId}")
    public ResponseEntity<Response> deleteBoek(
            @PathVariable("bookId") Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .message("Boek gedeleted met id " + bookId)
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/{bookId}/lenen/{userId}")
    public ResponseEntity<Response> borrowBook(
            @PathVariable("bookId")Long bookId,
            @PathVariable("userId")Long userId){
        UserDTO user = userService.getUser(userId);
        Book book = bookService.getBook(bookId);
        bookService.borrowBook(book, user);
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .message("Boek "+book.getTitel()+" uitgeleend aan "
                                +user.getVoornaam())
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @GetMapping("/{bookId}/reserveren/{userId}")
    public ResponseEntity<Response> reserveBook(
            @PathVariable("bookId")Long bookId,
            @PathVariable("userId")Long userId){
        UserDTO user = userService.getUser(userId);
        Book book = bookService.getBook(bookId);
        bookService.reserveBook(book, user);
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .message("Boek "+book.getTitel()+" gereserveerd voor "
                                +user.getVoornaam()+ " tot "+book.getGereserveerdTot().format(DateTimeFormatter.ofPattern("d/MM/uuuu")))
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/{bookId}/binnenleveren/{userId}")
    public ResponseEntity<Response> bringBackBook(
            @PathVariable("bookId")Long bookId,
            @PathVariable("userId")Long userId){
        User user = userService.getRealUser(userId);
        Book book = bookService.getBook(bookId);
        bookService.bringbackBook(book, user);
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .message("Boek "+book.getTitel()+" teruggebracht door "
                                +user.getVoornaam())
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @GetMapping("/{bookId}/vrijgeven")
    public ResponseEntity<Response> bookVrijgeven(
            @PathVariable("bookId")Long bookId){
        Book book = bookService.getBook(bookId);
        User user = book.getGereserveerdDoor();
        bookService.vrijgeven(book);
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .message("Boek "+book.getTitel()+": niet langer gereserveerd door "
                                +user.getVoornaam())
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/{bookId}/verlengen")
    public ResponseEntity<Response> bookVerlengen(
            @PathVariable("bookId")Long bookId){
        Book book = bookService.getBook(bookId);
        bookService.verlengen(book);
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .message("Boek "+book.getTitel()+": Uitleendatum verlengd tot "+book.getUitgeleendTot())
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}

