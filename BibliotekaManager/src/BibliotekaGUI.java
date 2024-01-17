import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BibliotekaGUI extends Application {

    private Biblioteka biblioteka;

    public static void main(String[] args) {
        launch(args);
    }

    @Override

    public void start(Stage primaryStage) {
        biblioteka = new Biblioteka();

        primaryStage.setTitle("Biblioteka");

        Button dodajKsiazkeButton = new Button("Dodaj Książkę");
        Button usunKsiazkeButton = new Button("Usuń Książkę");
        Button dodajUzytkownikaButton = new Button("Dodaj Użytkownika");
        Button usunUzytkownikaButton = new Button("Usuń Użytkownika");
        Button wypozyczButton = new Button("Wypożycz Książkę");
        Button zwrocButton = new Button("Zwróć Książkę");
        Button raportDostepnosciButton = new Button("Generuj Raport Dostępności");
        Button raportHistoriiButton = new Button("Generuj Raport Historii");
        Button zapiszRaportButton = new Button("Zapisz Raport");

        TextField nazwaPlikuField = new TextField();
        Label nazwaPlikuLabel = new Label("Nazwa Pliku:");

        ComboBox<Biblioteka.FormatPliku> formatPlikuComboBox = new ComboBox<>();
        formatPlikuComboBox.getItems().addAll(Biblioteka.FormatPliku.values());
        formatPlikuComboBox.setValue(Biblioteka.FormatPliku.TXT);
        Label formatPlikuLabel = new Label("Format Pliku:");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20, 20, 20, 20));
        vbox.getChildren().addAll(
                dodajKsiazkeButton, usunKsiazkeButton,
                dodajUzytkownikaButton, usunUzytkownikaButton,
                wypozyczButton, zwrocButton,
                raportDostepnosciButton, raportHistoriiButton,
                nazwaPlikuLabel, nazwaPlikuField,
                formatPlikuLabel, formatPlikuComboBox,
                zapiszRaportButton
        );

        dodajKsiazkeButton.setOnAction(e -> dodajKsiazke());
        usunKsiazkeButton.setOnAction(e -> usunKsiazke());
        dodajUzytkownikaButton.setOnAction(e -> dodajUzytkownika());
        usunUzytkownikaButton.setOnAction(e -> usunUzytkownika());
        wypozyczButton.setOnAction(e -> wypozyczKsiazke());
        zwrocButton.setOnAction(e -> zwrocKsiazke());
        raportDostepnosciButton.setOnAction(e -> generujRaportDostepnosci());
        raportHistoriiButton.setOnAction(e -> generujRaportHistorii());
        zapiszRaportButton.setOnAction(e -> zapiszRaport(nazwaPlikuField.getText(), formatPlikuComboBox.getValue()));

        Scene scene = new Scene(vbox, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void dodajKsiazke() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Dodaj Książkę");
        dialog.setHeaderText(null);
        dialog.setContentText("Podaj tytuł książki:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::accept);
    }

    private void usunKsiazke() {
        ChoiceDialog<Ksiazka> dialog = new ChoiceDialog<>(null, biblioteka.getListaKsiazek());
        dialog.setTitle("Usuń Książkę");
        dialog.setHeaderText(null);
        dialog.setContentText("Wybierz książkę do usunięcia:");

        Optional<Ksiazka> result = dialog.showAndWait();
        result.ifPresent(k -> {
            biblioteka.usunKsiazke(k);
            System.out.println("Usunięto książkę: " + k);
        });
    }

    private void dodajUzytkownika() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Dodaj Użytkownika");
        dialog.setHeaderText(null);
        dialog.setContentText("Podaj imię użytkownika:");

        Optional<String> imieResult = dialog.showAndWait();
        imieResult.ifPresent(imie -> {
            TextInputDialog nazwiskoDialog = new TextInputDialog();
            nazwiskoDialog.setTitle("Dodaj Użytkownika");
            nazwiskoDialog.setHeaderText(null);
            nazwiskoDialog.setContentText("Podaj nazwisko użytkownika:");

            Optional<String> nazwiskoResult = nazwiskoDialog.showAndWait();
            nazwiskoResult.ifPresent(nazwisko -> {
                Uzytkownik nowyUzytkownik = new Uzytkownik(imie, nazwisko, "nrKartyBibliotecznej");
                biblioteka.dodajUzytkownika(nowyUzytkownik);
                System.out.println("Dodano użytkownika: " + nowyUzytkownik);
            });
        });
    }

    private void usunUzytkownika() {
        ChoiceDialog<Uzytkownik> dialog = new ChoiceDialog<>(null, biblioteka.getListaUzytkownikow());
        dialog.setTitle("Usuń Użytkownika");
        dialog.setHeaderText(null);
        dialog.setContentText("Wybierz użytkownika do usunięcia:");

        Optional<Uzytkownik> result = dialog.showAndWait();
        result.ifPresent(u -> {
            biblioteka.usunUzytkownika(u);
            System.out.println("Usunięto użytkownika: " + u);
        });
    }

    private void wypozyczKsiazke() {
        ChoiceDialog<Ksiazka> ksiazkaDialog = new ChoiceDialog<>(null, biblioteka.getListaKsiazek());
        ksiazkaDialog.setTitle("Wypożycz Książkę");
        ksiazkaDialog.setHeaderText(null);
        ksiazkaDialog.setContentText("Wybierz książkę do wypożyczenia:");

        Optional<Ksiazka> ksiazkaResult = ksiazkaDialog.showAndWait();
        ksiazkaResult.ifPresent(ks -> {
            ChoiceDialog<Uzytkownik> uzytkownikDialog = new ChoiceDialog<>(null, biblioteka.getListaUzytkownikow());
            uzytkownikDialog.setTitle("Wypożycz Książkę");
            uzytkownikDialog.setHeaderText(null);
            uzytkownikDialog.setContentText("Wybierz użytkownika:");

            Optional<Uzytkownik> uzytkownikResult = uzytkownikDialog.showAndWait();
            uzytkownikResult.ifPresent(u -> {
                biblioteka.wypozyczanieKsiazki(ks, u);
                System.out.println("Wypożyczono książkę: " + ks + " dla użytkownika: " + u);
            });
        });
    }


    private void zwrocKsiazke() {
        ChoiceDialog<Wypozyczenie> dialog = new ChoiceDialog<>(null, biblioteka.getListaWypozyczen());
        dialog.setTitle("Zwróć Książkę");
        dialog.setHeaderText(null);
        dialog.setContentText("Wybierz wypożyczenie do zakończenia:");

        Optional<Wypozyczenie> result = dialog.showAndWait();
        result.ifPresent(w -> {
            biblioteka.zwracanieKsiazki(w);
            System.out.println("Zakończono wypożyczenie: " + w);
        });
    }

    private void generujRaportDostepnosci() {
        biblioteka.generowanieRaportuDostepnosci();
    }

    private void generujRaportHistorii() {
        biblioteka.generowanieRaportuHistorii();
    }

    private void zapiszRaport(String nazwaPliku, Biblioteka.FormatPliku format) {
        String raport;
        switch (format) {
            case TXT:
                raport = generujRaportDostepnosciTxt();
                break;
            case CSV:
                raport = generujRaportDostepnosciCsv();
                break;
            case XLSX:
                raport = generujRaportDostepnosciXlsx();
                break;
            default:
                System.out.println("Nieobsługiwany format pliku.");
                return;
        }
        biblioteka.zapiszRaport(nazwaPliku, raport, format);
    }

    private String generujRaportDostepnosciTxt() {
        StringBuilder raport = new StringBuilder();
        raport.append("Raport dostępności książek:\n");
        for (Ksiazka ksiazka : biblioteka.getListaKsiazek()) {
            raport.append("Tytuł książki: ").append(ksiazka.getTytul()).append("\n");
            raport.append("Autorstwa: ").append(ksiazka.getAutorstwoAsString()).append("\n");
            raport.append("Dostępnych egzemplarzy: ").append(ksiazka.getIloscDostepnychEgzemplarzy()).append("\n");
            raport.append("<.>.<.>.<.>.<.>.<.>.<.>.<.>.<.>\n");
        }
        return raport.toString();
    }
    private String generujRaportDostepnosciCsv() {
        StringBuilder raport = new StringBuilder();
        raport.append("Tytuł książki,Autorstwa,Dostępnych egzemplarzy\n");
        for (Ksiazka ksiazka : biblioteka.getListaKsiazek()) {
            raport.append(ksiazka.getTytul()).append(",");
            raport.append(ksiazka.getAutorstwoAsString()).append(",");
            raport.append(ksiazka.getIloscDostepnychEgzemplarzy()).append("\n");
        }
        return raport.toString();
    }

    private String generujRaportDostepnosciXlsx() {
        StringBuilder raport = new StringBuilder();
        raport.append("Tytuł książki,Autorstwa,Dostępnych egzemplarzy\n");
        for (Ksiazka ksiazka : biblioteka.getListaKsiazek()) {
            raport.append(ksiazka.getTytul()).append(",");
            raport.append(ksiazka.getAutorstwoAsString()).append(",");
            raport.append(ksiazka.getIloscDostepnychEgzemplarzy()).append("\n");
        }
        return raport.toString();
    }

    private void accept(String tytul) {
        TextInputDialog autorDialog = new TextInputDialog();
        autorDialog.setTitle("Dodaj Książkę");
        autorDialog.setHeaderText(null);
        autorDialog.setContentText("Podaj autorów książki (oddzielając ich przecinkiem):");

        Optional<String> autorResult = autorDialog.showAndWait();
        autorResult.ifPresent(autorString -> {
            String[] autorzy = autorString.split(",");
            List<Autor> autorList = Arrays.stream(autorzy)
                    .map(String::trim)
                    .map(autor -> new Autor(autor, "", ""))
                    .toList();

            TextInputDialog rokDialog = new TextInputDialog();
            rokDialog.setTitle("Dodaj Książkę");
            rokDialog.setHeaderText(null);
            rokDialog.setContentText("Podaj rok wydania:");

            Optional<String> rokResult = rokDialog.showAndWait();
            rokResult.ifPresent(rok -> {
                try {
                    int rokWydania = Integer.parseInt(rok);


                    DatePicker dataWypozyczeniaPicker = new DatePicker();
                    DatePicker dataZwrotuPicker = new DatePicker();
                    dataWypozyczeniaPicker.setPromptText("Data wypożyczenia");
                    dataZwrotuPicker.setPromptText("Data zwrotu");

                    VBox vBox = new VBox(10);
                    vBox.getChildren().addAll(
                            new Label("Data Wypożyczenia:"), dataWypozyczeniaPicker,
                            new Label("Data Zwrotu:"), dataZwrotuPicker
                    );

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Wypożycz Książkę");
                    alert.setHeaderText(null);
                    alert.getDialogPane().setContent(vBox);

                    Optional<ButtonType> alertResult = alert.showAndWait();
                    alertResult.ifPresent(type -> {
                        if (type == ButtonType.OK) {
                            LocalDate dataWypozyczenia = dataWypozyczeniaPicker.getValue();
                            LocalDate dataZwrotu = dataZwrotuPicker.getValue();

                            Ksiazka nowaKsiazka = new Ksiazka(tytul, rokWydania, 1);
                            autorList.forEach(nowaKsiazka::dodajAutora);

                            Uzytkownik dummyUzytkownik = new Uzytkownik("dummy", "dummy", "dummy");
                            Wypozyczenie wypozyczenie = new Wypozyczenie(nowaKsiazka, dummyUzytkownik, dataWypozyczenia);
                            wypozyczenie.setDataZwrotu(dataZwrotu);
                            biblioteka.wypozyczanieKsiazki(wypozyczenie);

                            System.out.println("Dodano książkę: " + nowaKsiazka);
                        }
                    });
                } catch (NumberFormatException e) {
                    System.out.println("Podano nieprawidłowy rok wydania.");
                }
            });
        });
    }
}


