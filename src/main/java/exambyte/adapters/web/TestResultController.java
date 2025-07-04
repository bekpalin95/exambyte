package exambyte.adapters.web;

import exambyte.domain.user.TestResult;
import exambyte.services.TestBewertungService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TestResultController {

  private TestBewertungService testBewertungService;

  public TestResultController(TestBewertungService testBewertungService) {
    this.testBewertungService = testBewertungService;
  }

  @GetMapping("/download/test-results")
  public ResponseEntity<byte[]> downloadTestResults(@RequestParam String username) {
    List<TestResult> testResults = testBewertungService.getTestResultsForUser(username);

    String csvContent = generateCSV(testResults);

    byte[] csvBytes = csvContent.getBytes(StandardCharsets.UTF_8);

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test_results.csv");
    headers.add(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");

    return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
  }

  private String generateCSV(List<TestResult> testResults) {
    String header = "Test-ID,Erreichte Punkte,Bestanden\n";
    String rows = testResults.stream()
        .map(tr -> tr.getTestId() + "," + tr.getErreichtePunkte() + "," + tr.isBestanden())
        .collect(Collectors.joining("\n"));

    return header + rows;
  }
}
