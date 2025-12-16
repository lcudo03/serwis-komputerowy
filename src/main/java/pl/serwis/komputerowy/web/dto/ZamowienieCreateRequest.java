package pl.serwis.komputerowy.web.dto;

import jakarta.validation.constraints.NotNull;

public record ZamowienieCreateRequest(
    @NotNull Long zlecenieId,
    @NotNull Long doZamowieniaId,
    Integer cena
) {}
