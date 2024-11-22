package be.atheneumboom.bibliotheek.config;

import be.atheneumboom.bibliotheek.model.User;

public class Settings {
    public static final String BASE_URL_BACK="http://bib.atheneumboom.be:8080/api";
    public static final String BASE_URL_FRONT="http://bib.atheneumboom.be:8080/web";
    //public static final String BASE_URL_FRONT="http://localhost:5000";
    //public static final String BASE_URL_BACK="http://localhost:3000";

    public static final int PAGESIZE_USERS = 500;
    public static final String PAGESORT_USERS = "voornaam";
    public static final int PAGESIZE_BOOKS = 940;
    public static final String PAGESORT_BOOKS = "auteur";
    public static final int PAGESIZE_MAGAZINES= 800;
    public static final String PAGESORT_MAGAZINES = "naamtijdschrift";
    public static final int MAX_AANTAL_ITEMS_USER = 3;
    public static final long AANTAL_DAGEN_RESERVATIE = 7;
    public static final long AANTAL_DAGEN_LENEN = 21;
    public static final long AANTAL_DAGEN_LENEN_LEESTAS = 365;
    public static final long AANTAL_DAGEN_LENEN_EXTRA = 7; //na verlengen
}
