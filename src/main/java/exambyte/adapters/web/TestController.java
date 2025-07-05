package exambyte.adapters.web;

import exambyte.domain.dto.FrageDTO;
import exambyte.domain.dto.UserAntwortDTO;
import exambyte.domain.test.Frage;
import exambyte.domain.user.TestResult;
import exambyte.services.TestBewertungService;
import exambyte.services.TestService;
import exambyte.services.exceptions.BearbeitungsZeitNochNichtBegonnen;
import exambyte.services.exceptions.BearbeitungszeitBeendet;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TestController {
//jsjdjaj
  private final TestService testService;
  private final TestBewertungService testBewertungService;

  public TestController(TestService testService, TestBewertungService testBewertungService) {
    this.testService = testService;
    this.testBewertungService = testBewertungService;
  }

  @GetMapping("/")
  public String main(Model model, @AuthenticationPrincipal OAuth2User principal) {
    model.addAttribute("user", principal != null ? principal.getAttribute("login") : null);
    return "index";
  }

  @GetMapping("/main")
  public String index(Model model, @AuthenticationPrincipal OAuth2User user) {
    if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
      model.addAttribute("admin", true);
    }
    model.addAttribute("username", user.getAttribute("login"));
    model.addAttribute("tests", testService.getAllTests());
    return "main";
  }

  @GetMapping("/test/bearbeiten/{id}")
  @Secured("ROLE_ADMIN")
  public String testBearbeiten(@PathVariable("id") long id, Model model) {
    model.addAttribute("test", testService.getTest(id));
    model.addAttribute("fragen", testService.getFragenZumTest(id));
    return "testbearbeiten";
  }

  @PostMapping("/addtest")
  @Secured("ROLE_ADMIN")
  public String addTest(RedirectAttributes redirectAttributes,
                        @RequestParam("name") String name,
                        @RequestParam("start") LocalDateTime start,
                        @RequestParam("end") LocalDateTime end,
                        @RequestParam("ergebnis") LocalDateTime ergebnis) {
    try {
      testService.testHinzufuegen(name, start, end, ergebnis);
    } catch (IllegalArgumentException e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/";
  }

  @PostMapping("/deletetest")
  @Secured("ROLE_ADMIN")
  public String deleteTest(Long testId) {
    testService.deleteTest(testId);
    return "redirect:/";
  }

  //----------------FRAGEN------------------


  @PostMapping("/test/addfrage")
  @Secured("ROLE_ADMIN")
  public String addFrage(Long testId,
                         @Valid FrageDTO frageDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      redirectAttributes.addFlashAttribute("error",
          bindingResult.getFieldError().getDefaultMessage());
      return "redirect:/test/bearbeiten/" + testId;
    }
    testService.addFrageZumTest(testId, frageDTO);
    return "redirect:/test/bearbeiten/" + testId;
  }

  @PostMapping("/test/deletefrage")
  @Secured("ROLE_ADMIN")
  public String deleteFrage(Long testId, Integer frageId) {
    testService.deleteFrageFromTest(testId, frageId);
    return "redirect:/test/bearbeiten/" + testId;
  }

  @GetMapping("/test/vorschau/{id}")
  public String testVorschau(@PathVariable("id") long id, Model model,
                             @AuthenticationPrincipal OAuth2User user) {
    if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
      model.addAttribute("admin", true);
    }
    model.addAttribute("testId", id);
    model.addAttribute("fragen", testService.getFragenZumTest(id));
    return "testvorschau";
  }

  @GetMapping("/test/vorschau/{testId}/frage/{frageId}")
  @Secured("ROLE_ADMIN")
  public String frageVorschau(@PathVariable("testId") Long testId,
                              @PathVariable("frageId") Integer frageId,
                              Model model) {
    Frage frage = testService.getFrageFromTest(frageId, testId);
    model.addAttribute("testId", testId);
    model.addAttribute("antwortMoeglichkeiten",
        Frage.semikolonFetchen(frage.getAntwortMoeglichkeiten()));
    model.addAttribute("korrekteAntworten", Frage.semikolonFetchen(frage.getKorrekteAntworten()));
    model.addAttribute("frage", frage);
    return "fragevorschau";
  }

  @PostMapping("/test/starten")
  public String startTest(Model model,
                          @AuthenticationPrincipal OAuth2User user,
                          Long testId,
                          RedirectAttributes redirectAttributes) {
    try {
      testService.testStarten(testId);
    }catch (BearbeitungsZeitNochNichtBegonnen e) {
      redirectAttributes.addFlashAttribute("error", "Die Bearbeitungszeit des Tests hat noch nicht begonnen");
      return "redirect:/test/vorschau/" + testId;
    }catch (BearbeitungszeitBeendet e ) {
      redirectAttributes.addFlashAttribute("error", "Die Bearbeitungszeit des Tests ist beendet");
      return "redirect:/test/vorschau/" + testId;
    }

    testBewertungService.addNewStudi(user.getAttribute("login"));

    List<Frage> fragen = testService.getFragenZumTest(testId);
    if (!fragen.isEmpty()) {
      return "redirect:/test/durchfuehren/" + testId + "/0";
    } else {
      redirectAttributes.addFlashAttribute("error", "Test enthält keine Fragen");
      return "redirect:/test/vorschau/" + testId;
    }
  }

  @GetMapping("/test/durchfuehren/{testId}/{index}")
  public String testDurchfuehren(@PathVariable("index") Integer index,
                                 @PathVariable("testId") Long testId,
                                 Model model,
                                 @AuthenticationPrincipal OAuth2User user) {
    List<Frage> fragen = testService.getFragenZumTest(testId);
    Frage frage = fragen.get(index);
    model.addAttribute("antwort", testBewertungService.getAntworten(user.getAttribute("login"), Math.toIntExact(testId),
        frage.getId()));
    model.addAttribute("fragenAnzahl", fragen.size());
    model.addAttribute("frage", frage);
    model.addAttribute("antwortMoeglichkeiten", Frage.semikolonFetchen(frage.getAntwortMoeglichkeiten()));
    model.addAttribute("testId", testId);
    model.addAttribute("index", index);
    return "fragebeantworten";
  }

  @PostMapping("/test/antwortspeichern")
  public String testAntwortspeichern(@Valid UserAntwortDTO userAntwortDTO,
                                     Integer index,
                                     @AuthenticationPrincipal OAuth2User user) {
    testBewertungService.addAntwort(user.getAttribute("login"), userAntwortDTO);
    return "redirect:/test/durchfuehren/"+ userAntwortDTO.testId() + "/" + index;
  }


  //------------------BEWERTUNG-----------------------

  @PostMapping("/test/beenden")
  public String testBeenden(@AuthenticationPrincipal OAuth2User user,
                            Long testId,
                            RedirectAttributes redirectAttributes) {
    List<Frage> fragenZumTest = testService.getFragenZumTest(testId);
    testBewertungService.testBewerten(user.getAttribute("login"),testId, fragenZumTest);
    return "redirect:/test/bewertung/" + testId;
  }


  @GetMapping("/test/bewertung/{testId}")
  public String bewertung(@AuthenticationPrincipal OAuth2User user,
                          @PathVariable("testId") Long testId,
                          Model model) {
    Optional<TestResult> testResult =
        testBewertungService.getTestResult(user.getAttribute("login"), testId);
    if(!testResult.isPresent()) {
      model.addAttribute("error", "Sie haben den Test noch nicht bearbeitet");
    }else {
      model.addAttribute("testResult", testResult.get());
    }
    model.addAttribute("gesamtPunktzahl", testService.getGesamtPunkteFromTest(testId));
    model.addAttribute("userAntworten", testBewertungService.getUserAntwortenForTest(user.getAttribute("login"), Math.toIntExact(testId)));
    model.addAttribute("fragen", testService.getFragenZumTest(testId));
    return "testbewertung";
  }

}
