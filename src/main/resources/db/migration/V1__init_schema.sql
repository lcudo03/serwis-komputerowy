-- Schema zgodny z ERD + rozszerzenie UC6 (magazyn + audyt)

CREATE TABLE IF NOT EXISTS klienci (
  id_klienta BIGSERIAL PRIMARY KEY,
  imie VARCHAR(120) NOT NULL,
  nazwisko VARCHAR(120) NOT NULL,
  nr_telefonu VARCHAR(50),
  adres VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS pracownik (
  id_pracownika BIGSERIAL PRIMARY KEY,
  imie VARCHAR(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS status (
  id_statusu BIGSERIAL PRIMARY KEY,
  status VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS urzadzenie (
  id_urzadzenia BIGSERIAL PRIMARY KEY,
  urzadzenie VARCHAR(120) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS zlecenia (
  id_zlecenia BIGSERIAL PRIMARY KEY,
  version BIGINT NOT NULL DEFAULT 0,
  klient BIGINT NOT NULL REFERENCES klienci(id_klienta),
  urzadzenie BIGINT NOT NULL REFERENCES urzadzenie(id_urzadzenia),
  model_urzadzenia VARCHAR(120),
  akcesoria VARCHAR(255),
  opis_usterki VARCHAR(500) NOT NULL,
  data DATE NOT NULL,
  status BIGINT NOT NULL REFERENCES status(id_statusu),
  postep_naprawy VARCHAR(255),
  pracownik BIGINT REFERENCES pracownik(id_pracownika)
);

CREATE TABLE IF NOT EXISTS do_zamowienia (
  id_zamowionego_przedmiotu BIGSERIAL PRIMARY KEY,
  zamowiony_przedmiot VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS zamowienia (
  id_zamowienia BIGSERIAL PRIMARY KEY,
  zlecenie BIGINT NOT NULL REFERENCES zlecenia(id_zlecenia) ON DELETE CASCADE,
  zamowiony_przedmiot BIGINT NOT NULL REFERENCES do_zamowienia(id_zamowionego_przedmiotu),
  cena INTEGER,
  odebrane BOOLEAN NOT NULL DEFAULT FALSE
);

-- Rozszerzenie 
CREATE TABLE IF NOT EXISTS czesci_zamienne (
  id_czesci BIGSERIAL PRIMARY KEY,
  version BIGINT NOT NULL DEFAULT 0,
  nazwa VARCHAR(255) NOT NULL,
  nr_katalogowy VARCHAR(120) NOT NULL,
  ilosc INTEGER NOT NULL DEFAULT 0,
  lokalizacja VARCHAR(120),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
  CONSTRAINT uk_czesc_nr_katalogowy UNIQUE (nr_katalogowy)
);

CREATE TABLE IF NOT EXISTS czesc_audit (
  id_audytu BIGSERIAL PRIMARY KEY,
  czesc_id BIGINT NOT NULL REFERENCES czesci_zamienne(id_czesci) ON DELETE CASCADE,
  zmiana INTEGER NOT NULL,
  powod VARCHAR(255),
  utworzono TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_zlecenia_status ON zlecenia(status);
CREATE INDEX IF NOT EXISTS idx_zlecenia_pracownik ON zlecenia(pracownik);
CREATE INDEX IF NOT EXISTS idx_zamowienia_odebrane ON zamowienia(odebrane);
CREATE INDEX IF NOT EXISTS idx_czesci_nr_katalogowy ON czesci_zamienne(nr_katalogowy);
