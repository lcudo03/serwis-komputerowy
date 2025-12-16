package pl.serwis.komputerowy.web.dto;

public record ZamowienieResponse(
    Long id,
    Long zlecenieId,
    Long doZamowieniaId,
    Integer cena,
    Boolean odebrane
) {}
