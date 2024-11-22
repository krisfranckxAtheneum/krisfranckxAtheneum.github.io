package be.atheneumboom.bibliotheek.service;

import be.atheneumboom.bibliotheek.model.Book;
import be.atheneumboom.bibliotheek.model.DTO.UserDTO;
import be.atheneumboom.bibliotheek.model.Magazine;
import be.atheneumboom.bibliotheek.model.User;
import org.springframework.data.domain.Page;

public interface MagazineService {
    Page<Magazine> getMagazines(Integer offset, Integer pagesize, String sortField, String keywordFilter);

    Magazine saveMagazine(Magazine magazine);

    Magazine updateMagazine(Long id, Magazine magazine);
    void deleteMagazine(Long id);

    Magazine getMagazine(Long id);

    void borrowMagazine(Magazine magazine, UserDTO userDTO);
    void reserveMagazine(Magazine magazine, UserDTO user);

    void bringbackMagezine(Magazine magazine, User user);

    void vrijgeven(Magazine magazine);

    void verlengen(Magazine magazine);
}
