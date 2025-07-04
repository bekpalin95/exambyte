package exambyte.domain.user;

public class TestResult {

  private Long testId;
  private Integer erreichtePunkte;
  private boolean bestanden;

  public TestResult(Long testId, Integer erreichtePunkte, boolean bestanden) {
    this.testId = testId;
    this.erreichtePunkte = erreichtePunkte;
    this.bestanden = bestanden;
  }

  public Long getTestId() {
    return testId;
  }

  public Integer getErreichtePunkte() {
    return erreichtePunkte;
  }

  public boolean isBestanden() {
    return bestanden;
  }

}
