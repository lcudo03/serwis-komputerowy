package pl.serwis.komputerowy.web.dto;

import java.time.Instant;

public record CzescResponse(
    Long id,
    String nazwa,
    String nrKatalogowy,
    int ilosc,
    String lokalizacja,
    Instant createdAt,
    Instant updatedAt
) {}
