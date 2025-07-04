package exambyte.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record FrageDTO(@NotBlank(message = "Fragestellung darf nicht leer sein") String fragestellung,
                       @Min(value = 1, message = "Punkte können nicht kleiner als 1 sein") Integer punkte,
                       @NotBlank(message = "Erklärung darf nicht leer sein") String erklaerung,
                       @NotBlank(message = "Antwortmöglichkeiten dürfen nicht leer sein") String antwortMoeglichkeiten,
                       @NotBlank(message = "korrekte Antworten dürfen nicht leer sein") String korrekteAntworten) {
}
