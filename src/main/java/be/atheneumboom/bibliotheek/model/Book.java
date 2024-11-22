package be.atheneumboom.bibliotheek.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.context.properties.bind.Name;

import java.time.LocalDate;
import java.util.Random;

@Data
@Entity
@Table(name = "boek")
public class Book implements Comparable<Book> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "boekid")
    private Long boekId;
    private String type;
    private String titel;
    @Column(name = "code_boek")
    private String code = "testcode";
    private Boolean uitgeleend;
    @ManyToOne
    @JoinColumn(name = "id_lener")
    private User geleendDoor;
    private LocalDate uitgeleendTot;
    @ManyToOne
    @JoinColumn(name = "id_reserv")
    private User gereserveerdDoor;
    private LocalDate gereserveerdTot;
    private String auteur;
    @Enumerated
    private Taal taal;
    private String graad;
    private String fictieNonFictie;

    public Book() {
        super();
    }

    public Book(String titel, String auteur, Taal taal, String graad, String fictieNonFictie) {
        this.titel=titel;
        System.out.println("Boek aangemaakt");
        this.auteur = auteur;
        this.taal = taal;
        this.graad = graad;
        this.fictieNonFictie = fictieNonFictie;
        setType("book");
        this.code=generateCode();
    }

    private String generateCode(){
        StringBuilder sb = new StringBuilder();
        for (String s : auteur.split(" ")) {
            sb.append(s.toCharArray()[0]);
        }
        sb.append(boekId);
        return sb.toString();
    }


    @Override
    public int compareTo(Book o) {
        return this.auteur.compareToIgnoreCase(o.getAuteur());
    }

    @Override
    public String toString() {
        return String.format("\t%s\n\t%s",getTitel(), getAuteur());
    }
}
