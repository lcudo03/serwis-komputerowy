package pl.serwis.komputerowy.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CzescUpsertRequest(
    @NotBlank @Size(max = 255) String nazwa,
    @NotBlank @Size(max = 120) String nrKatalogowy,
    @Min(0) int ilosc,
    @Size(max = 120) String lokalizacja
) {}
