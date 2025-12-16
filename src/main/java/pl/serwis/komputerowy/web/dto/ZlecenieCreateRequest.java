package pl.serwis.komputerowy.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ZlecenieCreateRequest(
    @NotNull Long klientId,
    @NotNull Long urzadzenieId,
    @Size(max = 120) String modelUrzadzenia,
    @Size(max = 255) String akcesoria,
    @NotBlank @Size(max = 500) String opisUsterki,
    @NotNull LocalDate data,
    @NotNull Long statusId
) {}
