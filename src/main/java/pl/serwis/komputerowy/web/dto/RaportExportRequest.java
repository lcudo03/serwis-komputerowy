package pl.serwis.komputerowy.web.dto;

import jakarta.validation.constraints.NotBlank;

public record RaportExportRequest(
    @NotBlank String format  // csv|pdf
) {}
