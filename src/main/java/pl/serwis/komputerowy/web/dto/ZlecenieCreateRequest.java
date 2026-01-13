package pl.serwis.komputerowy.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/** Request dopasowany do frontendu (static/script.js). */
public record ZlecenieCreateRequest(
    @NotNull Long klient,
    @NotNull Long urzadzenie,
    @Size(max = 120) String modelUrzadzenia,
    @Size(max = 255) String akcesoria,
    @NotBlank @Size(max = 500) String opisUsterki,
    LocalDate data,
    Long status,
    Long pracownik
) {}
