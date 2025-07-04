package exambyte.domain.test;

import java.util.Arrays;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

public class Frage {

  @Id
  private Integer id;
  private String fragestellung;
  private int punktzahl;
  private String erklaerung;
  private String antwortMoeglichkeiten;
  private String korrekteAntworten;

  @PersistenceCreator
  public Frage(Integer id,String fragestellung, int punktzahl, String erklaerung, String antwortMoeglichkeiten,
               String korrekteAntworten) {
    this.id = id;
    this.fragestellung = fragestellung;
    this.punktzahl = punktzahl;
    this.erklaerung = erklaerung;
    this.antwortMoeglichkeiten = antwortMoeglichkeiten;
    this.korrekteAntworten = korrekteAntworten;
  }

  public Frage (String fragestellung, int punktzahl, String erklaerung, String antwortMoeglichkeiten,
                String korrekteAntworten) {
    this(null, fragestellung, punktzahl, erklaerung, antwortMoeglichkeiten, korrekteAntworten);
  }

  public int bewerten(String antwort) {
    List<String> korrekteAntwort = semikolonFetchen(korrekteAntworten);
    List<String> userAntwort = semikolonFetchen(antwort);
    int anzahlFehler = 0;
    for(String option : userAntwort) {
      if(!korrekteAntwort.contains(option)) {
        anzahlFehler++;
      }
    }
    for(String option : korrekteAntwort) {
      if(!userAntwort.contains(option)) {
        anzahlFehler++;
      }
    }
    return switch (anzahlFehler) {
      case 0 -> punktzahl;
      case 1 -> (int) punktzahl / 2;
      default -> 0;
    };
  }

  public int getId() {
    return id;
  }

  public static List<String> semikolonFetchen(String string) {
    return Arrays.stream(string.split(";")).toList();
  }

  public String getFragestellung() {
    return fragestellung;
  }

  public int getPunktzahl() {
    return punktzahl;
  }

  public String getErklaerung() {
    return erklaerung;
  }

  public String getAntwortMoeglichkeiten() {
    return antwortMoeglichkeiten;
  }

  public String getKorrekteAntworten() {
    return korrekteAntworten;
  }

  @Override
  public String toString() {
    return "Frage{" +
        "id=" + id +
        ", fragestellung='" + fragestellung + '\'' +
        ", punktzahl=" + punktzahl +
        ", erklaerung='" + erklaerung + '\'' +
        ", antwortMoeglichkeiten='" + antwortMoeglichkeiten + '\'' +
        ", korrekteAntworten='" + korrekteAntworten + '\'' +
        '}';
  }
}
