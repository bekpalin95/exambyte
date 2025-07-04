package exambyte.domain.dto;

import jakarta.validation.constraints.NotNull;

public record UserAntwortDTO(
    @NotNull Integer testId,
    @NotNull Integer frageId,
    String[] antwort) {
}
