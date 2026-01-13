package pl.serwis.komputerowy.web.dto;

import java.time.LocalDate;

/**
 * DTO dopasowane do frontendu (static/script.js).
 * Front oczekuje pól: klient, urzadzenie, status, pracownik (ID-ki).
 */
public record ZlecenieResponse(
    Long id,
    Long klient,
    Long urzadzenie,
    String modelUrzadzenia,
    String akcesoria,
    String opisUsterki,
    LocalDate data,
    Long status,
    String postepNaprawy,
    Long pracownik
) {}
