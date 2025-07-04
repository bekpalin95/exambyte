package exambyte.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import exambyte.adapters.web.helper.WithMockOAuth2User;
import exambyte.builder.TestBuilder;
import exambyte.configuration.MethodSecurityConfiguration;
import exambyte.domain.test.Frage;
import exambyte.services.TestBewertungService;
import exambyte.services.TestService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TestController.class)
@Import(MethodSecurityConfiguration.class)
class TestControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  TestService testService;

  @MockBean
  TestBewertungService testBewertungService;

  @Test
  @WithMockOAuth2User
  @DisplayName("Startseite wird angezeigt und alle Tests werden angezeigt")
  void test_index() throws Exception {
    exambyte.domain.test.Test test = new TestBuilder().build();
    when(testService.getAllTests()).thenReturn(List.of(test));
    mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(view().name("index"))
        .andExpect(model().attribute("tests", List.of(test)));
  }

  @Test
  @WithMockOAuth2User(roles = {"ADMIN"})
  @DisplayName("Tests können von admins hinzugefügt werden")
  void test_addtest() throws Exception {
    mockMvc.perform(post("/addtest")
        .param("name", "name")
        .param("start", LocalDateTime.now().toString())
        .param("end", LocalDateTime.now().toString())
        .param("ergebnis", LocalDateTime.now().toString())
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(flash().attributeCount(0));

    verify(testService).testHinzufuegen(any(), any(), any(), any());
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("Tests können von nicht admins nicht inzugefügt werden")
  void test_addtest_ohne_admin() throws Exception {
    mockMvc.perform(post("/addtest")
            .param("name", "name")
            .param("start", LocalDateTime.now().toString())
            .param("end", LocalDateTime.now().toString())
            .param("ergebnis", LocalDateTime.now().toString())
            .with(csrf()))
        .andExpect(status().is(403));
  }

  @Test
  @WithMockOAuth2User(roles = {"ADMIN"})
  @DisplayName("Falsche Zeitangabe wird erkannt")
  void test_addtest_with_false_params() throws Exception {
    doThrow(new IllegalArgumentException("hallo")).when(testService).testHinzufuegen(any(), any(), any(), any());
    mockMvc.perform(post("/addtest")
            .param("name", "name")
            .param("start", LocalDateTime.now().toString())
            .param("end", LocalDateTime.now().toString())
            .param("ergebnis", LocalDateTime.now().toString())
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(flash().attributeExists("error"));

  }

  @Test
  @WithMockOAuth2User(roles = {"ADMIN"})
  @DisplayName("Tests können von admins gelöscht werden")
  void test_deleteTest() throws Exception {
    mockMvc.perform(post("/deletetest")
        .param("testId", "1")
        .with(csrf()));

    verify(testService).deleteTest(1L);
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("Tests können von nicht admins nicht gelöscht werden")
  void test_deleteTest_ohne_admin() throws Exception {
    mockMvc.perform(post("/deletetest")
        .param("testId", "1")
        .with(csrf()))
        .andExpect(status().is(403));

  }

  @Test
  @WithMockOAuth2User(roles = {"ADMIN"})
  @DisplayName("Testbearbeiten Seite wird für admins korrekt angezeigt")
  void test_bearbeiten() throws Exception {
    exambyte.domain.test.Test test = new TestBuilder().build();
    when(testService.getTest(1L)).thenReturn(test);
    mockMvc.perform(get("/test/bearbeiten/{id}", 1))
        .andExpect(status().isOk())
        .andExpect(view().name("testbearbeiten"))
        .andExpect(model().attributeExists("fragen"))
        .andExpect(model().attribute("test", test));
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("Testbearbeiten Seite wird für admins korrekt angezeigt")
  void test_bearbeiten_ohne_berechtigung() throws Exception {
    mockMvc.perform(get("/test/bearbeiten/{id}", 1))
        .andExpect(status().is(403));
  }

  @Test
  @WithMockOAuth2User(roles = {"ADMIN"})
  @DisplayName("Fragen können von admins hinzugefügt werden")
  void test_add_fragen() throws Exception {
    exambyte.domain.test.Test test = new TestBuilder().build();
    mockMvc.perform(post("/test/addfrage")
        .param("testId","1")
        .param("fragestellung", "hey")
        .param("punkte", "2")
        .param("erklaerung", "erkl")
        .param("antwortMoeglichkeiten", "sadi")
        .param("korrekteAntworten","sajs")
            .with(csrf()))
        .andExpect(redirectedUrl("/test/bearbeiten/1"))
        .andExpect(flash().attributeCount(0));

    verify(testService).addFrageZumTest(eq(1L), any());
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("Fragen können von nicht admins nicht hinzugefügt werden")
  void test_add_fragen_ohne_admin() throws Exception {
    mockMvc.perform(post("/test/addfrage")
            .param("testId","1")
            .param("fragestellung", "hey")
            .param("punkte", "2")
            .param("erklaerung", "erkl")
            .param("antwortMoeglichkeiten", "sadi")
            .param("korrekteAntworten","sajs")
            .with(csrf()))
        .andExpect(status().is(403));

  }

  @Test
  @WithMockOAuth2User(roles = {"ADMIN"})
  @DisplayName("Validierung beim hinzufügen von Fragen funktioniert richtig")
  void test_add_fragen_with_false_params() throws Exception {
    mockMvc.perform(post("/test/addfrage")
            .param("testId","1")
            .param("fragestellung", "")
            .param("punkte", "")
            .param("erklaerung", "")
            .param("antwortMoeglichkeiten", "sadi")
            .param("korrekteAntworten","sajs")
            .with(csrf()))
        .andExpect(redirectedUrl("/test/bearbeiten/1"))
        .andExpect(flash().attributeExists("error"));

    verify(testService, never()).addFrageZumTest(eq(1L), any());
  }

  @Test
  @WithMockOAuth2User(roles = {"ADMIN"})
  @DisplayName("Tests können von admins gelöscht werden")
  void test_delete_fragen() throws Exception {
    mockMvc.perform(post("/test/deletefrage")
        .param("testId","1")
        .param("frageId", "5")
            .with(csrf()))
        .andExpect(redirectedUrl("/test/bearbeiten/1"));

    verify(testService).deleteFrageFromTest(1,5);
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("Tests können von nicht admins nicht gelöscht werden")
  void test_delete_fragen_ohne_admin() throws Exception {
    mockMvc.perform(post("/test/deletefrage")
            .param("testId","1")
            .param("frageId", "5")
            .with(csrf()))
        .andExpect(status().is(403));
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("Testvorschau wird korrekt angezeigt")
  void testVorschau() throws Exception {
    mockMvc.perform(get("/test/vorschau/1"))
        .andExpect(status().isOk())
        .andExpect(view().name("testvorschau"))
        .andExpect(model().attribute("testId", 1L))
        .andExpect(model().attributeExists("fragen"));
  }

  @Test
  @WithMockOAuth2User(roles = {"ADMIN"})
  @DisplayName("Fragevorschau wird für admins korrekt angezeigt")
  void frageVorschau() throws Exception {
    Frage frage = new Frage(2, "Fragestellung", 3, "erklärung", "hallo;ja", "eins;zwei");
    when(testService.getFrageFromTest(1,1L)).thenReturn(frage);
    mockMvc.perform(get("/test/vorschau/1/frage/1"))
        .andExpect(status().isOk())
        .andExpect(view().name("fragevorschau"))
        .andExpect(model().attribute("antwortMoeglichkeiten", List.of("hallo", "ja")))
        .andExpect(model().attribute("korrekteAntworten", List.of("eins", "zwei")));
  }

  @Test
  @WithMockOAuth2User
  @DisplayName("Fragevorschau wird für nicht admins nicht angezeigt")
  void frageVorschau_ohne_admin() throws Exception {
    mockMvc.perform(get("/test/vorschau/1/frage/1"))
        .andExpect(status().is(403));
  }

}