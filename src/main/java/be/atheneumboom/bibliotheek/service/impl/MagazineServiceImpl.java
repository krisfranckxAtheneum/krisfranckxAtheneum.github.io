package be.atheneumboom.bibliotheek.service.impl;

import be.atheneumboom.bibliotheek.config.Settings;
import be.atheneumboom.bibliotheek.exceptions.BookNotFoundException;
import be.atheneumboom.bibliotheek.model.*;
import be.atheneumboom.bibliotheek.model.DTO.UserDTO;
import be.atheneumboom.bibliotheek.repository.BookRepo;
import be.atheneumboom.bibliotheek.repository.LKBookRepo;
import be.atheneumboom.bibliotheek.repository.MagazineRepo;
import be.atheneumboom.bibliotheek.service.EmailService;
import be.atheneumboom.bibliotheek.service.MagazineService;
import be.atheneumboom.bibliotheek.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MagazineServiceImpl implements MagazineService {
    private final MagazineRepo magazineRepo;
    private final LKBookRepo lkBookRepo;
    private final BookRepo bookRepo;
    private final UserService userService;
    private final EmailService emailService;

    @Override
    public Page<Magazine> getMagazines(Integer pageNumber, Integer pageSize, String sortField, String keywordFilter) {
        Page<Magazine> books;
        if (keywordFilter==null){
            books = magazineRepo.findAll(
                    PageRequest.of(
                            pageNumber!=null?pageNumber:0, pageSize!=null?pageSize:Settings.PAGESIZE_MAGAZINES).
                            withSort(Sort.by(sortField!=null?sortField:Settings.PAGESORT_MAGAZINES)) );
            books.forEach(System.out::println);

        }else{
            System.out.println(keywordFilter.trim().toLowerCase());
            books = magazineRepo.findAll(
                    PageRequest.of(
                            pageNumber!=null?pageNumber:0, pageSize!=null?pageSize:Settings.PAGESIZE_MAGAZINES).
                            withSort(Sort.by(sortField!=null?sortField:Settings.PAGESORT_MAGAZINES)), keywordFilter.trim().toLowerCase() );
        }

        List<Magazine> magazineList = books.stream().toList();
        return new PageImpl<>(magazineList);
    }
    public List<Magazine> getMagazines() {
        return magazineRepo.findAll();
    }

    @Override
    public Magazine saveMagazine(Magazine magazine) {

        Magazine newMagazine = new Magazine(magazine.getNaamtijdschrift(), magazine.getNummertijdschrift(), magazine.getJaargang());
        magazineRepo.save(newMagazine);
        newMagazine.setCode_tijdschrift(generateCode(newMagazine));
        return magazineRepo.save(newMagazine);
    }

    @Override
    public Magazine updateMagazine(Long id, Magazine magazine) {
        Magazine magazineUpdated = magazineRepo.findById(id).orElseThrow(()-> new BookNotFoundException("Magazine met id "+id+ " niet gevonden."));
        magazineUpdated.setJaargang                 (magazine.getJaargang());
        magazineUpdated.setNummertijdschrift        (magazine.getNummertijdschrift());
        magazineUpdated.setNaamtijdschrift(magazine.getNaamtijdschrift());
        magazineUpdated.setCode_tijdschrift(generateCode(magazineUpdated));
        return magazineRepo.save(
                magazineUpdated
        );
    }

    @Override
    public void deleteMagazine(Long id) {
        magazineRepo.deleteById(id);
    }

    @Override
    public Magazine getMagazine(Long id) {
        return magazineRepo.findById(id).orElseThrow(()->new BookNotFoundException("Boek met id "+id + " niet gevonden."));
    }

    @Override
    public void borrowMagazine(Magazine magazine, UserDTO userDTO) {
        User user = userService.getRealUser(userDTO.getId());
        magazine.setGeleendDoor(user);
        magazine.setUitgeleend(true);
        if (user.getVoornaam().toLowerCase().contains("leestas")||user.getFamilienaam().toLowerCase().contains("leestas")) {
            magazine.setUitgeleendTot(LocalDate.now().plusDays(Settings.AANTAL_DAGEN_LENEN_LEESTAS));
        } else{
            magazine.setUitgeleendTot(LocalDate.now().plusDays(Settings.AANTAL_DAGEN_LENEN));
        }
        magazine.setGereserveerdDoor(null);
        magazine.setGereserveerdTot(null);
        magazineRepo.save(magazine);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String datum = magazine.getUitgeleendTot().format(formatters);
        emailService.sendHtmlEmail(user.getVoornaam(), user.getEmail(), magazine, datum);
        user.setAantalItemsGeleend(user.getAantalItemsGeleend()+1);
        checkEnabledUI(user);
    }

    @Override
    public void reserveMagazine(Magazine magazine, UserDTO userDTO) {
        User user = userService.getRealUser(userDTO.getId());
        magazine.setGereserveerdDoor(user);
        magazine.setGereserveerdTot(magazine.getUitgeleendTot().plusDays(Settings.AANTAL_DAGEN_RESERVATIE));
        magazineRepo.save(magazine);

        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String datum = magazine.getGereserveerdTot().format(formatters);
        emailService.sendHtmlEmail_reservatie(user.getVoornaam(), user.getEmail(), magazine, datum);

    }

    @Override
    public void bringbackMagezine(Magazine magazine, User user) {
        magazine.setGeleendDoor(null);
        magazine.setUitgeleend(false);
        magazine.setUitgeleendTot(null);
        magazineRepo.save(magazine);

        user.setAantalItemsGeleend(user.getAantalItemsGeleend()-1);
        userService.saveUser(user);
        checkEnabledUI(user);
    }

    private void checkEnabledUI(User user) {
        if (user.getAantalItemsGeleend()>Settings.MAX_AANTAL_ITEMS_USER-1
        &&!(user.getVoornaam().toLowerCase().contains("leestas")||user.getFamilienaam().toLowerCase().contains("leestas"))
            ){
            user.setEnabledUI(false);
            userService.saveUser(user);
        }else{
            user.setEnabledUI(true);
            userService.saveUser(user);
        }
        for (Book book: bookRepo.findAll()) {
            if (book.getGeleendDoor()!= null && Objects.equals(book.getGeleendDoor().getId(), user.getId())
                    && book.getUitgeleendTot().isBefore(LocalDate.now())){
                user.setEnabledUI(false);
                userService.saveUser(user);
            }
        }
        for (LKBook lkBook:lkBookRepo.findAll()) {
            if (lkBook.getGeleendDoor()!= null && Objects.equals(lkBook.getGeleendDoor().getId(), user.getId())
                    && lkBook.getUitgeleendTot().isBefore(LocalDate.now())){
                user.setEnabledUI(false);
                userService.saveUser(user);
            }
        }
        for (Magazine magazine:magazineRepo.findAll()) {
            if (magazine.getGeleendDoor()!= null && Objects.equals(magazine.getGeleendDoor().getId(), user.getId())
                    && magazine.getUitgeleendTot().isBefore(LocalDate.now())){
                user.setEnabledUI(false);
                userService.saveUser(user);
            }
        }
    }
    @Override
    public void vrijgeven(Magazine magazine) {
        magazine.setGereserveerdDoor(null);
        magazine.setGereserveerdTot(null);
        magazineRepo.save(magazine);
    }

    @Override
    public void verlengen(Magazine magazine) {
        magazine.setUitgeleendTot(magazine.getUitgeleendTot().plusDays(Settings.AANTAL_DAGEN_LENEN_EXTRA));
        magazineRepo.save(magazine);
    }

    private String generateCode(Magazine book){
        StringBuilder sb = new StringBuilder();
        for (String s : book.getNaamtijdschrift().split(" ")) {
            sb.append(s.toCharArray()[0]);
        }
        sb.append(book.getTijdschriftid());
        return sb.toString();
    }
}
