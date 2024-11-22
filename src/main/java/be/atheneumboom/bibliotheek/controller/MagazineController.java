
package be.atheneumboom.bibliotheek.controller;

import be.atheneumboom.bibliotheek.model.Book;
import be.atheneumboom.bibliotheek.model.DTO.UserDTO;
import be.atheneumboom.bibliotheek.model.Magazine;
import be.atheneumboom.bibliotheek.model.Response;
import be.atheneumboom.bibliotheek.model.User;
import be.atheneumboom.bibliotheek.service.MagazineService;
import be.atheneumboom.bibliotheek.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/admin/magazines")
@RequiredArgsConstructor
public class MagazineController {
    private final MagazineService magazineService;
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<Response> getMagazines(@RequestParam(value = "pageSize", required = false) Integer pageSize,
                                             @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                             @RequestParam(value = "filter", required = false) String filter,
                                             @RequestParam(value = "sort", required = false) String sort){
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .data(Map.of("magazines",magazineService.getMagazines(pageNumber, pageSize, sort, filter).stream().toList()))
                        .message("Alle magazines opgehaald")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<Response> getMagazine(
            @PathVariable("id")Long id){
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .data(Map.of("magazine",magazineService.getMagazine(id)))
                        .message("Magazine met id "+id+" opgehaald")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @PostMapping ()
    public ResponseEntity<Response> saveMagazine(
            @RequestBody Magazine magazine){
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .data(Map.of("magazine",magazineService.saveMagazine(magazine)))
                        .message("Magazine bewaard: "+magazine)
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build());
    }

    @PutMapping ("{magazineId}")
    public ResponseEntity<Response> updateMagazine(
            @RequestBody Magazine magazine,
            @PathVariable("magazineId") Long magazineId){
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .data(Map.of("magazine",magazineService.updateMagazine(magazineId, magazine)))
                        .message("Magazine aangepast: "+magazine)
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @DeleteMapping("{magazineId}")
    public ResponseEntity<Response> deleteMagazine(
            @PathVariable("magazineId") Long magazineId) {
        magazineService.deleteMagazine(magazineId);
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .message("Magazine gedeleted met id " + magazineId)
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/{magazineId}/lenen/{userId}")
    public ResponseEntity<Response> borrowMagazine(
            @PathVariable("magazineId")Long magazineId,
            @PathVariable("userId")Long userId){
        UserDTO user = userService.getUser(userId);
        Magazine magazine = magazineService.getMagazine(magazineId);
        magazineService.borrowMagazine(magazine, user);
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .message("Magazine "+magazine.getNaamtijdschrift()+" uitgeleend aan "
                                +user.getVoornaam())
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/{magazineId}/reserveren/{userId}")
    public ResponseEntity<Response> reserveMagazine(
            @PathVariable("magazineId")Long magazineId,
            @PathVariable("userId")Long userId){
        UserDTO user = userService.getUser(userId);
        Magazine magazine = magazineService.getMagazine(magazineId);
        magazineService.reserveMagazine(magazine, user);
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .message("Magazine "+magazine.getNaamtijdschrift()+" gereserveerd voor "
                                +user.getVoornaam()+ " tot "+magazine.getGereserveerdTot().format(DateTimeFormatter.ofPattern("d/MM/uuuu")))
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/{magazineId}/binnenleveren/{userId}")
    public ResponseEntity<Response> bringBackMagazine(
            @PathVariable("magazineId")Long magazineId,
            @PathVariable("userId")Long userId){
        User user = userService.getRealUser(userId);
        Magazine magazine = magazineService.getMagazine(magazineId);
        magazineService.bringbackMagezine(magazine, user);
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .message("Magazine "+magazine.getNaamtijdschrift()+" teruggebracht door "
                                +user.getVoornaam())
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @GetMapping("/{magazineId}/vrijgeven")
    public ResponseEntity<Response> magazineVrijgeven(
            @PathVariable("magazineId")Long magazineId){
        Magazine magazine = magazineService.getMagazine(magazineId);
        User user = magazine.getGereserveerdDoor();
        magazineService.vrijgeven(magazine);
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .message("Magazine "+magazine.getNaamtijdschrift()+": niet langer gereserveerd door "
                                +user.getVoornaam())
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @GetMapping("/{magazineId}/verlengen")
    public ResponseEntity<Response> magazineVerlengen(
            @PathVariable("magazineId")Long magazineId){
        Magazine magazine = magazineService.getMagazine(magazineId);
        magazineService.verlengen(magazine);
        return ResponseEntity.ok(
                Response.builder()
                        //.timeStamp(LocalDateTime.now())
                        .message("Magazine "+magazine.getNaamtijdschrift()+": Uitleendatum verlengd tot "+magazine.getUitgeleendTot())
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}

