-- V3__seed_test_data.sql
-- Testowe dane operacyjne: klienci, zlecenia, części, audyt, zamówienia

BEGIN;

-- =========================
-- 1) KLIENCI
-- =========================
INSERT INTO klienci (id_klienta, imie, nazwisko, nr_telefonu, adres)
VALUES
  (1, 'Jan',   'Nowak',    '600100200', 'Kraków, ul. Długa 10'),
  (2, 'Anna',  'Kowalska', '500222333', 'Kraków, ul. Krótka 5'),
  (3, 'Piotr', 'Zieliński','510777888', 'Wieliczka, ul. Słoneczna 12'),
  (4, 'Maria', 'Wiśniewska','690123456','Skawina, ul. Leśna 3')
ON CONFLICT (id_klienta) DO NOTHING;

-- =========================
-- 2) CZĘŚCI ZAMIENNE (MAGAZYN)
-- =========================
-- Uwaga: created_at/updated_at są NOT NULL w Auditable -> w seedzie ustawiamy NOW()
INSERT INTO czesci_zamienne (id_czesci, version, nazwa, nr_katalogowy, ilosc, lokalizacja, created_at, updated_at)
VALUES
  (1, 0, 'Dysk SSD 1TB',           'SSD-1TB-SATA',  6,  'Magazyn A1', NOW(), NOW()),
  (2, 0, 'Pasta termiczna 4g',     'PASTA-4G',      15, 'Magazyn A2', NOW(), NOW()),
  (3, 0, 'Wentylator CPU 120mm',   'FAN-CPU-120',   4,  'Magazyn B1', NOW(), NOW()),
  (4, 0, 'RAM DDR4 16GB',          'RAM-DDR4-16',   8,  'Magazyn B2', NOW(), NOW()),
  (5, 0, 'Klawiatura laptopowa',   'KBD-LAP-001',   2,  'Magazyn C1', NOW(), NOW())
ON CONFLICT (id_czesci) DO NOTHING;

-- =========================
-- 3) ZLECENIA
-- =========================
-- Wymaga istnienia: klient (FK), urzadzenie (FK), status (FK), opcjonalnie pracownik (FK)
-- version: kolumna @Version, ustawiamy 0
--
-- WAŻNE: tu musisz mieć takie ID w słownikach:
--  urzadzenie: (1..), status: (1..), pracownik: (1..)
-- Jeśli masz inne ID w V2, podmień wartości w kolumnach: klient/urzadzenie/status/pracownik.
INSERT INTO zlecenia
  (id_zlecenia, version, klient, urzadzenie, model_urzadzenia, akcesoria, opis_usterki, data, status, postep_naprawy, pracownik)
VALUES
  (1, 0, 1, 1, 'Dell Inspiron 15',     'Zasilacz',  'Laptop nie włącza się, miga dioda zasilania.', '2025-12-01', 1, 'Przyjęto do diagnozy', 1),
  (2, 0, 2, 2, 'iPhone 12',            'Brak',      'Telefon szybko się rozładowuje, bateria puchnie.', '2025-12-02', 2, 'Zamówiono baterię',  2),
  (3, 0, 3, 1, 'Lenovo ThinkPad T480', 'Torba',     'Wysokie temperatury, głośny wentylator.', '2025-12-03', 2, 'Czyszczenie układu chłodzenia', 1),
  (4, 0, 4, 3, 'HP Pavilion',          'Mysz',      'Brak obrazu na ekranie, działa podświetlenie.', '2025-12-05', 1, NULL, NULL)
ON CONFLICT (id_zlecenia) DO NOTHING;

-- =========================
-- 4) AUDYT MAGAZYNOWY CZĘŚCI
-- =========================
-- Twój byt: czesc_audit(czesc_id, zmiana, powod, utworzono)
INSERT INTO czesc_audit (id_audytu, czesc_id, zmiana, powod, utworzono)
VALUES
  (1, 1, -1, 'Wydanie na zlecenie #1 (SSD)',        NOW()),
  (2, 2, -2, 'Zużycie serwisowe (pasta termiczna)', NOW()),
  (3, 3, -1, 'Wymiana wentylatora na zlecenie #3',  NOW()),
  (4, 5, -1, 'Wydanie klawiatury na zlecenie #1',   NOW())
ON CONFLICT (id_audytu) DO NOTHING;

-- (opcjonalnie) zasymuluj aktualny stan magazynu “po audycie”
UPDATE czesci_zamienne SET ilosc = ilosc - 1, updated_at = NOW() WHERE id_czesci = 1;
UPDATE czesci_zamienne SET ilosc = ilosc - 2, updated_at = NOW() WHERE id_czesci = 2;
UPDATE czesci_zamienne SET ilosc = ilosc - 1, updated_at = NOW() WHERE id_czesci = 3;
UPDATE czesci_zamienne SET ilosc = ilosc - 1, updated_at = NOW() WHERE id_czesci = 5;

-- =========================
-- 5) ZAMÓWIENIA
-- =========================
-- Tabela: zamowienia (zlecenie, zamowiony_przedmiot, cena, odebrane)
-- Wymaga istnienia: do_zamowienia (FK)
INSERT INTO zamowienia (id_zamowienia, zlecenie, zamowiony_przedmiot, cena, odebrane)
VALUES
  (1, 2, 1, 199, FALSE),
  (2, 2, 2,  49, TRUE),
  (3, 1, 3,  89, FALSE)
ON CONFLICT (id_zamowienia) DO NOTHING;

-- =========================
-- 6) PODBICIE SEKWENCJI (gdy wstawiasz ręcznie ID)
-- =========================
-- Dzięki temu kolejne inserty bez ID nie walną konfliktem.
SELECT setval(pg_get_serial_sequence('klienci','id_klienta'),        GREATEST((SELECT COALESCE(MAX(id_klienta),0) FROM klienci), 1));
SELECT setval(pg_get_serial_sequence('czesci_zamienne','id_czesci'), GREATEST((SELECT COALESCE(MAX(id_czesci),0) FROM czesci_zamienne), 1));
SELECT setval(pg_get_serial_sequence('zlecenia','id_zlecenia'),      GREATEST((SELECT COALESCE(MAX(id_zlecenia),0) FROM zlecenia), 1));
SELECT setval(pg_get_serial_sequence('czesc_audit','id_audytu'),     GREATEST((SELECT COALESCE(MAX(id_audytu),0) FROM czesc_audit), 1));
SELECT setval(pg_get_serial_sequence('zamowienia','id_zamowienia'),  GREATEST((SELECT COALESCE(MAX(id_zamowienia),0) FROM zamowienia), 1));

COMMIT;
