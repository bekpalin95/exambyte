package exambyte.domain.test;

import exambyte.domain.dto.FrageDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

public class Test {

  @Id
  private final Long id;
  private String name;
  private List<Frage> fragen;

  private LocalDateTime startZeitPunkt;
  private LocalDateTime endZeitPunkt;
  private LocalDateTime ergebnisZeitPunkt;

  @PersistenceCreator
  public Test(Long id, String name, List<Frage> fragen, LocalDateTime startZeitPunkt,
              LocalDateTime endZeitPunkt, LocalDateTime ergebnisZeitPunkt) {
    this.id = id;
    this.name = name;
    this.fragen = fragen;
    this.startZeitPunkt = startZeitPunkt;
    this.endZeitPunkt = endZeitPunkt;
    this.ergebnisZeitPunkt = ergebnisZeitPunkt;
  }


  public Test(String name, LocalDateTime startZeitPunkt, LocalDateTime endZeitPunkt, LocalDateTime ergebnisZeitPunkt) {
    this(null, name, new ArrayList<>(), startZeitPunkt, endZeitPunkt, ergebnisZeitPunkt);
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<Frage> getFragen() {
    return fragen;
  }

  public LocalDateTime getStartZeitPunkt() {
    return startZeitPunkt;
  }

  public LocalDateTime getEndZeitPunkt() {
    return endZeitPunkt;
  }

  public LocalDateTime getErgebnisZeitPunkt() {
    return ergebnisZeitPunkt;
  }

  @Override
  public String toString() {
    return "Test{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", fragen=" + fragen +
        ", startZeitPunkt=" + startZeitPunkt +
        ", endZeitPunkt=" + endZeitPunkt +
        ", ergebnisZeitPunkt=" + ergebnisZeitPunkt +
        '}';
  }


  public void addFrage(FrageDTO frageDTO) {
    Frage frage = new Frage(frageDTO.fragestellung(),
        frageDTO.punkte(), frageDTO.erklaerung(), frageDTO.antwortMoeglichkeiten(), frageDTO.korrekteAntworten());
    fragen.add(frage);
  }
}
