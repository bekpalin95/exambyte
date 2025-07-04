package exambyte.services;

import exambyte.domain.dto.FrageDTO;
import exambyte.domain.test.Frage;
import exambyte.domain.test.Test;
import exambyte.services.exceptions.BearbeitungsZeitNochNichtBegonnen;
import exambyte.services.exceptions.BearbeitungszeitBeendet;
import exambyte.services.repositorys.TestRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TestService {

  private TestRepository testRepository;

  public TestService(TestRepository testRepository) {
    this.testRepository = testRepository;
  }

  public void testHinzufuegen(String name, LocalDateTime start, LocalDateTime end, LocalDateTime ergebnis) {
    if (start.isAfter(end)) {
      throw new IllegalArgumentException("Der Startzeitpunkt muss vor dem Endzeitpunkt liegen.");
    }

    if (end.isAfter(ergebnis)) {
      throw new IllegalArgumentException("Der Endzeitpunkt muss vor dem Ergebnis-Veröffentlichungszeitpunkt liegen.");
    }
    Test test = new Test(name, start, end, ergebnis);
    testRepository.save(test);
  }

  public Test getTest(long id) {
    return testRepository.findById(id);
  }

  public List<Test> getAllTests() {
    return testRepository.findAll();
  }

  public void deleteTest(long id) {
    Test test = testRepository.findById(id);
    testRepository.delete(test);
  }

  public void addFrageZumTest(Long id, FrageDTO frageDTO) {
    Test test = testRepository.findById(id);
    test.addFrage(frageDTO);
    testRepository.save(test);
  }

  public List<Frage> getFragenZumTest(long testId) {
    Test test = testRepository.findById(testId);
    return test.getFragen();
  }

  public void deleteFrageFromTest(long testId, Integer frageId) {
    Test test = testRepository.findById(testId);
    Frage frage = test.getFragen().stream().filter(e -> e.getId() == frageId).findFirst().get();
    test.getFragen().remove(frage);
    testRepository.save(test);
  }

  public Frage getFrageFromTest(Integer frageId, Long testId) {
    Test test = testRepository.findById(testId);
    return test.getFragen().stream().filter(e -> e.getId() == frageId).findFirst().get();
  }

  public int getGesamtPunkteFromTest(Long testId) {
    Test test = testRepository.findById(testId);
    List<Frage> fragen = test.getFragen();
    return fragen.stream().mapToInt(Frage::getPunktzahl).sum();
  }

  public void testStarten(Long testId) {
    Test test = testRepository.findById(testId);
    LocalDateTime zeit = LocalDateTime.now();
    if(zeit.isBefore(test.getStartZeitPunkt())) {
      throw new BearbeitungsZeitNochNichtBegonnen();
    }else if(zeit.isAfter(test.getEndZeitPunkt())) {
      throw new BearbeitungszeitBeendet();
    }
  }
}
