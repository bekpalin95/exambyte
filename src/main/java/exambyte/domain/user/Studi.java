package exambyte.domain.user;

import exambyte.domain.dto.UserAntwortDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

public class Studi {

  @Id
  private Integer id;
  private String username;
  private List<UserAntwort> userAntworten;

  private List<TestResult> testResults;

  @PersistenceCreator
  public Studi(Integer id, String username, List<UserAntwort> userAntworten,
               List<TestResult> testResults) {
    this.id = id;
    this.username = username;
    this.userAntworten = userAntworten;
    this.testResults = testResults;
  }

  public Studi(String username) {
    this(null, username, new ArrayList<>(), new ArrayList<>());
  }

  public Integer getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public List<UserAntwort> getUserAntworten() {
    return userAntworten;
  }

  public List<TestResult> getTestResults() {
    return testResults;
  }

  public void addUserAntwort(UserAntwortDTO dto) {
    String antwort = "";
    if(dto.antwort() != null) {
      for(String a : dto.antwort()) {
        antwort += a + ";";
      }
    }
    UserAntwort userAntwort = new UserAntwort(dto.testId(), dto.frageId(), antwort);
    userAntworten.add(userAntwort);
  }
}
