# Serwis komputerowy – backend (Spring Boot + PostgreSQL + Docker + Swagger)

Projekt realizuje wymagania z dokumentacji: CRUD klientów, zleceń, przypisanie pracownika, zmiana statusu,
magazyn części z audytem, zamówienia oraz raporty (zapytania 1–3) + eksport PDF/CSV.

## Uruchomienie (Docker)
```bash
docker compose up --build
```

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

## Najważniejsze endpointy

### Klienci (UC1/UC2, WF.01/WF.02, WF.11)
- `POST /api/klienci`
- `PUT /api/klienci/{id}`
- `GET /api/klienci?imie=&nazwisko=&telefon=`
- `GET /api/klienci/{id}`

### Zlecenia (UC3/UC4/UC5, WF.03–WF.06, WF.12, WF.11)
- `POST /api/zlecenia`
- `PATCH /api/zlecenia/{id}/pracownik`
- `PATCH /api/zlecenia/{id}/status`
- `GET /api/zlecenia?statusId=&pracownikId=&from=YYYY-MM-DD&to=YYYY-MM-DD`

### Magazyn części (UC6, WF.07, WNF.03 audyt, WF.11)
- `POST /api/czesci` (upsert po nr katalogowym)
- `GET /api/czesci?q=`
- `PATCH /api/czesci/{id}/stan` (zmiana stanu + wpis do audytu)
- `GET /api/czesci/{id}/audit`

### Zamówienia (UC7, WF.08)
- `POST /api/zamowienia`
- `GET /api/zamowienia?odebrane=true|false`
- `PATCH /api/zamowienia/{id}/odebrane?value=true`

### Raporty (UC8, WF.09/WF.10) – odpowiedniki zapytań z PDF
- `GET /api/raporty/1`
- `GET /api/raporty/2?rok=2025`
- `GET /api/raporty/3?imie=Jan&nazwisko=Nowak`
- `GET /api/raporty/1/export?format=csv|pdf`

## Przykładowe requesty

### Dodanie klienta
```bash
curl -X POST http://localhost:8080/api/klienci \
  -H 'Content-Type: application/json' \
  -d '{"imie":"Jan","nazwisko":"Nowak","nrTelefonu":"123456789","adres":"Warszawa"}'
```

### Dodanie zlecenia
```bash
curl -X POST http://localhost:8080/api/zlecenia \
  -H 'Content-Type: application/json' \
  -d '{"klientId":1,"urzadzenieId":1,"modelUrzadzenia":"Dell XPS","akcesoria":"ładowarka","opisUsterki":"nie włącza się","data":"2025-12-16","statusId":1}'
```

### Raport 1 (JSON) i eksport CSV
```bash
curl http://localhost:8080/api/raporty/1
curl -OJ http://localhost:8080/api/raporty/1/export?format=csv
```

## Notatka o rozszerzeniu ERD
Dokumentacja UC6 wymaga pól: nazwa, nr katalogowy, ilość, lokalizacja. Ponieważ ERD ze screenu nie zawiera tabeli magazynu,
dodano tabele: `czesci_zamienne` oraz `czesc_audit` (audyt zmian stanów) – zgodnie z WNF.03.
