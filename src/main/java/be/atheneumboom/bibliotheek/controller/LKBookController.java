
package be.atheneumboom.bibliotheek.controller;

import be.atheneumboom.bibliotheek.model.Book;
import be.atheneumboom.bibliotheek.model.DTO.UserDTO;
import be.atheneumboom.bibliotheek.model.LKBook;
import be.atheneumboom.bibliotheek.model.Response;
import be.atheneumboom.bibliotheek.model.User;
import be.atheneumboom.bibliotheek.service.BookService;
import be.atheneumboom.bibliotheek.service.LKBookService;
import be.atheneumboom.bibliotheek.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.SortDirection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/lkadmin/books")
@RequiredArgsConstructor
public class LKBookController {
    private final LKBookService bookService;
    private final UserService userService;

    @GetMapping("/provider")
    public Stream<LKBook> getBooks(@RequestParam(value = "pageSize", required = false) Integer pageSize,
                                   @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                   @RequestParam(value = "filter", required = false) String filter,
                                   @RequestParam(value = "sort", required = false) String sort,
                                   @RequestParam(value = "direction", required = false) SortDirection direction
    ){
        return bookService.getBooksDataProvider(pageNumber, pageSize, sort, filter, direction);
        /*return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        //.data(Map.of("lkbooks",bookService.getBooks(pageNumber, pageSize, sort, filter).stream().toList()))
                        .data(Map.of("lkbooksStream",bookService.getBooksDataProvider(pageNumber, pageSize, sort, filter)))
                        .message("Alle boeken opgehaald")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );*/
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
                        .data(Map.of("lkBooksAllWithFilter",bookService.getBooksAllWithFilter(filter)))
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
            @RequestBody LKBook book){
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
            @RequestBody LKBook book,
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
        LKBook book = bookService.getBook(bookId);
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
        LKBook book = bookService.getBook(bookId);
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
        LKBook book = bookService.getBook(bookId);
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
        LKBook book = bookService.getBook(bookId);
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
        LKBook book = bookService.getBook(bookId);
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

