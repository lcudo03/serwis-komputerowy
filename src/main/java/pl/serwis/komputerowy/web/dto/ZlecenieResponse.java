package pl.serwis.komputerowy.web.dto;

import java.time.LocalDate;

public record ZlecenieResponse(
    Long id,
    Long klientId,
    Long urzadzenieId,
    String modelUrzadzenia,
    String akcesoria,
    String opisUsterki,
    LocalDate data,
    Long statusId,
    String postepNaprawy,
    Long pracownikId
) {}
