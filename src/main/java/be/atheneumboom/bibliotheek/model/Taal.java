package be.atheneumboom.bibliotheek.model;

public enum Taal {
    algemeen("algemeen"),
    Engels("Engels"),
    Nederlands("Nederlands"),
    Frans("Frans"),
    Spaans("Spaans"),
    Duits("Duits");

    private final String vak;

    Taal(String vak) {
        this.vak = vak;
    }

    public String getVak() {
        return vak;
    }
}
