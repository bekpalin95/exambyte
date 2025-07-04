package exambyte.builder;

import exambyte.domain.test.Frage;
import exambyte.domain.test.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestBuilder {

  private LocalDateTime start;
  private LocalDateTime end;
  private LocalDateTime ergebnis;

  private long id = -1;
  private String name = "name";
  private List<Frage> fragen = new ArrayList<>();

  public TestBuilder withStartTime(LocalDateTime start) {
    this.start = start;
    return this;
  }

  public TestBuilder withEndTime(LocalDateTime end) {
    this.end = end;
    return this;
  }

  public TestBuilder withErgebnis(LocalDateTime ergebnis) {
    this.ergebnis = ergebnis;
    return this;
  }

  public TestBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public TestBuilder withFragen(List<Frage> fragen) {
    this.fragen = fragen;
    return this;
  }

  public TestBuilder withId(long id) {
    this.id = id;
    return this;
  }

  public Test build() {
    if(id == -1) {
      return new Test(null, name, fragen, start, end, ergebnis);
    }else {
      return new Test(id, name, fragen, start, end, ergebnis);
    }
  }

}
