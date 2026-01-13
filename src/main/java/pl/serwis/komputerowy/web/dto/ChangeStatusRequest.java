package pl.serwis.komputerowy.web.dto;

import jakarta.validation.constraints.NotNull;

/** Request dopasowany do frontendu (static/script.js). */
public record ChangeStatusRequest(
    @NotNull Long status,
    String postepNaprawy
) {}
