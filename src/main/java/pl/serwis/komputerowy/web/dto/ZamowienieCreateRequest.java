package pl.serwis.komputerowy.web.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Request dopasowany do frontendu (static/script.js).
 */
public record ZamowienieCreateRequest(
    @NotNull Long zlecenie,
    @NotNull Long zamowionyPrzedmiot,
    Integer cena
) {}
