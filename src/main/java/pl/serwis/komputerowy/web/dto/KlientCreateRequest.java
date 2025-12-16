package pl.serwis.komputerowy.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record KlientCreateRequest(
    @NotBlank @Size(max = 120) String imie,
    @NotBlank @Size(max = 120) String nazwisko,
    @Size(max = 50) String nrTelefonu,
    @Size(max = 255) String adres
) {}
