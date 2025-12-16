package pl.serwis.komputerowy.web.dto;

import jakarta.validation.constraints.NotNull;

public record AssignPracownikRequest(@NotNull Long pracownikId) {}
