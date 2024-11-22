package be.atheneumboom.bibliotheek.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "lkbooks")
public class LKBook implements Comparable<LKBook> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "boek_id")
    private Long boekId;
    private String type;
    private String titel;
    @Column(name = "code_lkboek")
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
    private Categorie_LK taal;
    private String graad;
    private String fictieNonFictie;

    public LKBook() {}

    public LKBook(String titel, String auteur, Categorie_LK taal, String graad, String fictieNonFictie) {
        this.titel=titel;
        System.out.println("Boek_lk aangemaakt");
        this.auteur = auteur;
        this.taal = taal;
        this.graad = graad;
        this.fictieNonFictie = fictieNonFictie;
        this.uitgeleend=false;
        setType("lkbook");
        this.code=null;
    }

    @Override
    public int compareTo(LKBook o) {
        return this.auteur.compareToIgnoreCase(o.getAuteur());
    }

    @Override
    public String toString() {
        return String.format("\t%s\n\t%s",getTitel(), getAuteur());
    }
}
