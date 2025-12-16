package pl.serwis.komputerowy.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AdjustStockRequest(
    @NotNull Integer zmiana,
    @Size(max = 255) String powod
) {}
