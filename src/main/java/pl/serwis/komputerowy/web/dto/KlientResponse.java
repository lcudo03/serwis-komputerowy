package pl.serwis.komputerowy.web.dto;

public record KlientResponse(
    Long id,
    String imie,
    String nazwisko,
    String nrTelefonu,
    String adres
) {}
