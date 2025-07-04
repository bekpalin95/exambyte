package exambyte.services.repositorys;



import static org.assertj.core.api.Assertions.assertThat;


import exambyte.adapters.db.TestRepoDAO;
import exambyte.builder.TestBuilder;
import exambyte.domain.dto.FrageDTO;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;


@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class TestRepositoryTest {

  @Autowired
  TestRepoDAO testRepository;

  @Test
  @DisplayName("ein Test kann gespeichert werden")
  void test_save() throws Exception {
    exambyte.domain.test.Test test = new TestBuilder().build();
    testRepository.save(test);
    assertThat(testRepository.findAll()).hasSize(1);
  }

  @Test
  @DisplayName("ein Test kann gelöscht werden")
  @Sql("testdata.sql")
  void test_delete() throws Exception {
    testRepository.deleteById(testRepository.findAll().getFirst().getId());
    assertThat(testRepository.findAll()).isEmpty();
  }

  @Test
  @DisplayName("Tests werden korrekt ausgegeben")
  void test_findAll() throws Exception {
    exambyte.domain.test.Test test1 = new TestBuilder().build();
    exambyte.domain.test.Test test2 = new TestBuilder().build();
    testRepository.save(test1);
    testRepository.save(test2);
    assertThat(testRepository.findById(2L)).isNotNull();
  }

  @Test
  @DisplayName("Fragen für eine Test Id werden korrekt ausgegeben")
  @Sql("testmitfragen.sql")
  void frage_findByTestId() throws Exception {
    exambyte.domain.test.Test test = testRepository.findByName("test");
    FrageDTO frageDTO = new FrageDTO("ha", 2, "d", "d", "d");
    test.addFrage(frageDTO);
    testRepository.save(test);
    exambyte.domain.test.Test test1 = testRepository.findByName("test");
    assertThat(test1.getFragen()).hasSize(3);
  }

}