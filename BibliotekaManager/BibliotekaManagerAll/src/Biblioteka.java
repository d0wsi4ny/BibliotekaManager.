import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Autor {
    private String imie;
    private String nazwisko;
    private String dataUrodzenia;

    public Autor(String imie, String nazwisko, String dataUrodzenia) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.dataUrodzenia = dataUrodzenia;
    }

    @Override
    public String toString() {
        return imie + " " + nazwisko;
    }
}

class Ksiazka implements Comparable<Ksiazka> {
    private String tytul;
    private int rokWydania;
    private List<Autor> autorstwo;
    private int iloscEgzemplarzy;
    private int iloscDostepnychEgzemplarzy;

    public Ksiazka(String tytul, int rokWydania, int iloscEgzemplarzy) {
        this.tytul = tytul;
        this.rokWydania = rokWydania;
        this.autorstwo = new ArrayList<>();
        this.iloscEgzemplarzy = iloscEgzemplarzy;
        this.iloscDostepnychEgzemplarzy = iloscEgzemplarzy;
    }

    public void dodajAutora(Autor autor) {
        autorstwo.add(autor);
    }

    public String getTytul() {
        return tytul;
    }

    public String getAutorstwoAsString() {
        if (autorstwo.isEmpty()) {
            return "Brak informacji o autorach";
        }

        StringBuilder autorString = new StringBuilder();
        autorString.append(autorstwo.get(0).toString());

        for (int i = 1; i < autorstwo.size(); i++) {
            autorString.append(", ").append(autorstwo.get(i).toString());
        }
        return autorString.toString();
    }

    public int getIloscDostepnychEgzemplarzy() {
        return iloscDostepnychEgzemplarzy;
    }

    public void wypozyczenieEgzemplarza() {
        if (iloscDostepnychEgzemplarzy > 0) {
            iloscDostepnychEgzemplarzy--;
        } else {
            System.out.println("Brak dostępnych egzemplarzy");
        }
    }

    public void zwrocenieEgzemplarza() {
        if (iloscDostepnychEgzemplarzy < iloscEgzemplarzy) {
            iloscDostepnychEgzemplarzy++;
        } else {
            System.out.println("Wszystkie egzemplarze są już zwrócone");
        }
    }

    @Override
    public String toString() {
        return tytul;
    }

    @Override
    public int compareTo(Ksiazka innaKsiazka) {
        return Integer.compare(this.rokWydania, innaKsiazka.rokWydania);
    }
}

class Uzytkownik {
    private String imie;
    private String nazwisko;
    private String nrKartyBibliotecznej;

    public Uzytkownik(String imie, String nazwisko, String nrKartyBibliotecznej) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.nrKartyBibliotecznej = nrKartyBibliotecznej;
    }

    @Override
    public String toString() {
        return imie + " " + nazwisko;
    }
}

class Wypozyczenie {
    private LocalDate dataWypozyczenia;
    private LocalDate dataZwrotu;
    private Ksiazka ksiazka;
    private Uzytkownik uzytkownik;

    public Wypozyczenie(Ksiazka ksiazka, Uzytkownik uzytkownik, LocalDate dataWypozyczenia) {
        this.dataWypozyczenia = dataWypozyczenia;
        this.ksiazka = ksiazka;
        this.uzytkownik = uzytkownik;
    }

    public LocalDate getDataWypozyczenia() {
        return dataWypozyczenia;
    }

    // Dodaj getter i setter dla dataZwrotu
    public LocalDate getDataZwrotu() {
        return dataZwrotu;
    }

    public void setDataZwrotu(LocalDate dataZwrotu) {
        this.dataZwrotu = dataZwrotu;
    }

    public Ksiazka getKsiazka() {
        return ksiazka;
    }

    public Uzytkownik getUzytkownik() {
        return uzytkownik;
    }
}



public class Biblioteka {
    private List<Ksiazka> listaKsiazek;
    private List<Uzytkownik> listaUzytkownikow;
    private List<Wypozyczenie> listaWypozyczen;

    public List<Ksiazka> getListaKsiazek() {
        return listaKsiazek;
    }

    public List<Uzytkownik> getListaUzytkownikow() {
        return listaUzytkownikow;
    }

    public List<Wypozyczenie> getListaWypozyczen() {
        return listaWypozyczen;
    }

    public Biblioteka() {
        this.listaKsiazek = new ArrayList<>();
        this.listaUzytkownikow = new ArrayList<>();
        this.listaWypozyczen = new ArrayList<>();
    }

    public void dodajKsiazke(Ksiazka ksiazka) {
        listaKsiazek.add(ksiazka);
    }

    public void usunKsiazke(Ksiazka ksiazka) {
        listaKsiazek.remove(ksiazka);
    }

    public void dodajUzytkownika(Uzytkownik uzytkownik) {
        listaUzytkownikow.add(uzytkownik);
    }

    public void usunUzytkownika(Uzytkownik uzytkownik) {
        listaUzytkownikow.remove(uzytkownik);
    }

    public void wypozyczanieKsiazki(Ksiazka ksiazka, Uzytkownik uzytkownik) {
        listaWypozyczen.add(new Wypozyczenie(ksiazka, uzytkownik, LocalDate.now()));
        ksiazka.wypozyczenieEgzemplarza();

    }
    public void wypozyczanieKsiazki(Wypozyczenie wypozyczenie) {
        listaWypozyczen.add(wypozyczenie);
        wypozyczenie.getKsiazka().wypozyczenieEgzemplarza();
    }
    public void zwracanieKsiazki(Wypozyczenie wypozyczenie) {
        wypozyczenie.setDataZwrotu(LocalDate.now());
        listaWypozyczen.remove(wypozyczenie);
        wypozyczenie.getKsiazka().zwrocenieEgzemplarza();
    }

    public void sortowanieRokWydania() {
        Collections.sort(listaKsiazek);
    }

    public void generowanieRaportuDostepnosci() {
        System.out.println("Raport dostępności książek:");
        for (Ksiazka ksiazka : listaKsiazek) {
            System.out.println("Tytuł książki: " + ksiazka.toString());
            System.out.println("Autorstwa: " + ksiazka.getAutorstwoAsString());
            System.out.println("Dostępnych egzemplarzy: " + ksiazka.getIloscDostepnychEgzemplarzy());
            System.out.println("<.>.<.>.<.>.<.>.<.>.<.>.<.>.<.>");
        }
    }

    public void generowanieRaportuHistorii() {
        System.out.println("Raport historii wypożyczeń książki:");
        for (Wypozyczenie wypozyczenie : listaWypozyczen) {
            System.out.println("Data wypożyczenia: " + wypozyczenie.getDataWypozyczenia());
            System.out.println("Data zwrotu: " + wypozyczenie.getDataZwrotu());
            System.out.println("Tytuł: " + wypozyczenie.getKsiazka().toString());
            System.out.println("Wypożyczył: " + wypozyczenie.getUzytkownik().toString());
            System.out.println("<.>.<.>.<.>.<.>.<.>.<.>.<.>.<.>");
        }
    }

    public void zapiszRaport(String nazwaPliku, String raport, FormatPliku format) {
        switch (format) {
            case TXT:
                zapiszDoPlikuTXT(nazwaPliku, raport);
                break;
            case CSV:
                zapiszDoPlikuCSV(nazwaPliku, raport);
                break;
            case XLSX:
                zapiszDoPlikuXLSX(nazwaPliku, raport);
                break;
            default:
                System.out.println("Nieobsługiwany format pliku.");
        }
    }

    private void zapiszDoPlikuTXT(String nazwaPliku, String raport) {
        zapiszDoPliku(nazwaPliku + ".txt", raport);
    }

    private void zapiszDoPlikuCSV(String nazwaPliku, String raport) {
        zapiszDoPliku(nazwaPliku + ".csv", raport);
    }

    private void zapiszDoPlikuXLSX(String nazwaPliku, String raport) {
        zapiszDoPliku(nazwaPliku + ".xlsx", raport);
    }

    private void zapiszDoPliku(String nazwaPliku, String raport) {
        try (FileWriter writer = new FileWriter(nazwaPliku)) {
            writer.write(raport);
            System.out.println("Raport został zapisany w pliku - " + nazwaPliku);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.now().format(formatter);
    }


    public enum FormatPliku {
        TXT,
        CSV,
        XLSX
    }
}

