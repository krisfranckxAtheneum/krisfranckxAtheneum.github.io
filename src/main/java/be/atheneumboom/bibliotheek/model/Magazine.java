package be.atheneumboom.bibliotheek.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "tijdschrift")
public class Magazine implements Comparable<Magazine> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tijdschriftid")
    private Long tijdschriftid;
    private String naamtijdschrift;
    private Boolean uitgeleend;
    @ManyToOne
    @JoinColumn(name = "id")
    private User geleendDoor;
    private LocalDate uitgeleendTot;
    @ManyToOne
    @JoinColumn(name = "idReserv")
    private User gereserveerdDoor;
    private LocalDate gereserveerdTot;
    private int nummertijdschrift;
    private String code_tijdschrift;
    private int jaargang;

    public Magazine(String titel, int nummertijdschrift, int jaargang) {
        this.naamtijdschrift=titel;
        System.out.println("Magazine aangemaakt");
        this.nummertijdschrift = nummertijdschrift;
        this.jaargang = jaargang;
    }

    public Magazine() {

    }

    @Override
    public String toString() {
        return String.format("\t%s\n"+ " - " +"\t%s",getNaamtijdschrift(), getJaargang());

    }

    public int compareTo(Magazine magazine) {
        return this.getNaamtijdschrift().compareToIgnoreCase(magazine.getNaamtijdschrift());
    }
}
