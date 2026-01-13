package pl.serwis.komputerowy.web.dto;

/**
 * DTO dopasowane do frontendu (static/script.js).
 * Front oczekuje pól: zlecenie oraz zamowionyPrzedmiot (ID-ki).
 */
public record ZamowienieResponse(
    Long id,
    Long zlecenie,
    Long zamowionyPrzedmiot,
    Integer cena,
    Boolean odebrane
) {}
