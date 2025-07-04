package exambyte.services;

import exambyte.domain.dto.UserAntwortDTO;
import exambyte.domain.test.Frage;
import exambyte.domain.user.Studi;
import exambyte.domain.user.TestResult;
import exambyte.domain.user.UserAntwort;
import exambyte.services.repositorys.StudiRepository;
import exambyte.services.repositorys.TestRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TestBewertungService {

  private StudiRepository studiRepository;


  public TestBewertungService(TestRepository testRepository, StudiRepository studiRepository) {
    this.studiRepository = studiRepository;
  }

  public List<UserAntwort> getUserAntwortenForTest(String username, Integer testId) {
    return studiRepository.findByUsername(username).getUserAntworten().stream()
        .filter(e -> e.testId() == testId).toList();
  }

  public void addAntwort(String username, UserAntwortDTO dto) {
    Studi studi = studiRepository.findByUsername(username);
    List<UserAntwort> userAntworten = studi.getUserAntworten();
    Optional<UserAntwort> userAntwort = userAntworten.stream()
        .filter(e -> e.testId() == dto.testId() && e.frageId() == dto.frageId()).findAny();
    if(userAntwort.isPresent()) {
      studi.getUserAntworten().remove(userAntwort.get());
    }
    studi.addUserAntwort(dto);
    studiRepository.save(studi);
  }

  public void addNewStudi(String login) {
    if(studiRepository.findByUsername(login) == null) {
      Studi newStudi = new Studi(login);
      studiRepository.save(newStudi);
    }
  }

  //Spezialfall
  public String getAntworten(String username, Integer testId, Integer frageId) {
    Studi studi = studiRepository.findByUsername(username);
    Optional<UserAntwort> any = studi.getUserAntworten().stream()
        .filter(e -> e.testId() == testId && e.frageId() == frageId)
        .findAny();
    return any.map(UserAntwort::antwort).orElse(null);
  }


  public void testBewerten(String username, Long testId, List<Frage> fragen) {
    Studi studi = studiRepository.findByUsername(username);
    List<UserAntwort> userAntworten = studi.getUserAntworten();
    int erreichtePunkte = 0;
    int gesamtPunkte = 0;
    for(Frage frage : fragen) {
      Optional<UserAntwort> antwort = userAntworten.stream()
          .filter(e -> e.testId() == Math.toIntExact(testId) && e.frageId() == frage.getId())
          .findAny();
      if(antwort.isPresent()) {
        erreichtePunkte += frage.bewerten(antwort.get().antwort());
      }
      gesamtPunkte += frage.getPunktzahl();
    }
    TestResult testResult = new TestResult(testId, erreichtePunkte, erreichtePunkte >= gesamtPunkte/2);

    List<TestResult> testResults = studi.getTestResults();
    Optional<TestResult> any =
        testResults.stream().filter(e -> e.getTestId() == testId).findAny();

    any.ifPresent(testResults::remove);
    testResults.add(testResult);

    studiRepository.save(studi);
  }

  public Optional<TestResult> getTestResult(String username, Long testId) {
    Studi studi = studiRepository.findByUsername(username);
    return studi.getTestResults().stream().filter(e -> e.getTestId() == testId).findAny();
  }


  public List<TestResult> getTestResultsForUser(String username) {
    return studiRepository.findByUsername(username).getTestResults();
  }
}
