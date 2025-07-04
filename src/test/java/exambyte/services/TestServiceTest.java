package exambyte.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import exambyte.builder.TestBuilder;
import exambyte.domain.dto.FrageDTO;
import exambyte.domain.test.Frage;
import exambyte.services.repositorys.TestRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class TestServiceTest {

  private final TestRepository repo = mock(TestRepository.class);

  @Test
  @DisplayName("Test wird hinzugefügt wenn Start, End und Ergebniszeitpunkt richtig gesetzt werden")
  void testHinzufuegen_1() {
    TestService testService = new TestService(repo);
    testService.testHinzufuegen("test",
        LocalDateTime.of(2002, 04, 23, 23, 04),
        LocalDateTime.of(2003, 04, 23, 23, 04),
        LocalDateTime.of(2004, 04, 23, 23, 04));
    verify(repo).save(any());
  }

  @Test
  @DisplayName("Exception, wenn Start, End und Ergebniszeitpunkt nicht richtig gesetzt werden")
  void testHinzufuegen_2() {
    TestService testService = new TestService(repo);
    assertThrows(IllegalArgumentException.class, () -> {
      testService.testHinzufuegen("test",
          LocalDateTime.of(2024, 04, 23, 23, 04),
          LocalDateTime.of(2004, 04, 23, 23, 04),
          LocalDateTime.of(2003, 04, 23, 23, 04));
    });
  }

  @Test
  void getTest() {
    exambyte.domain.test.Test test = new TestBuilder().build();
    when(repo.findById(2)).thenReturn(test);
    TestService testService = new TestService(repo);
    assertThat(testService.getTest(2)).isEqualTo(test);
  }

  @Test
  void getAllTests() {
    exambyte.domain.test.Test test = new TestBuilder().build();
    when(repo.findAll()).thenReturn(List.of(test));
    TestService testService = new TestService(repo);
    assertThat(testService.getAllTests()).contains(test);

  }

  @Test
  void deleteTest() {
    TestService testService = new TestService(repo);
    testService.deleteTest(1L);
    verify(repo).delete(any());
  }

  @Test
  @DisplayName("Fragen können hinzugefügt werden")
  void add_frage() {
    when(repo.findById(2)).thenReturn(new TestBuilder().build());
    TestService testService = new TestService(repo);
    FrageDTO dto = new FrageDTO("ha", 2, "d", "d", "d");
    testService.addFrageZumTest(2L, dto);
    verify(repo).save(any());
  }

  @Test
  @DisplayName("Alle Tests werden angezeigt")
  void test_alleTests() {
    Frage frage = new Frage(1,"s",2,"ss","f","dsad");
    when(repo.findById(1L)).thenReturn(new TestBuilder()
        .withFragen(List.of(frage))
        .build());
    TestService testService = new TestService(repo);
    List<Frage> fragen = testService.getFragenZumTest(1L);

    assertThat(fragen).contains(frage);
  }

  @Test
  @DisplayName("Eine bestimmte Frage aus einem bestimmten Test wird zurückgegeben")
  void test_get_frage_from_test() {
    Frage frage = new Frage(1,"s",2,"ss","f","dsad");
    Frage frage1 = new Frage(2,"s",2,"ss","f","dsad");
    exambyte.domain.test.Test test = new TestBuilder().withFragen(List.of(frage,frage1)).build();
    when(repo.findById(1L)).thenReturn(test);
    TestService testService = new TestService(repo);
    Frage findFrage = testService.getFrageFromTest(2, 1L);

    assertThat(findFrage).isEqualTo(frage1);
  }


}