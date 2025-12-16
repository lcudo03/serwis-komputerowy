INSERT INTO status(status) VALUES
  ('Przyjęte'),
  ('W realizacji'),
  ('Zakończone'),
  ('Wydane klientowi')
ON CONFLICT DO NOTHING;

INSERT INTO urzadzenie(urzadzenie) VALUES
  ('Laptop'),
  ('Komputer stacjonarny'),
  ('Monitor'),
  ('Drukarka'),
  ('Inne')
ON CONFLICT DO NOTHING;

INSERT INTO pracownik(imie) VALUES
  ('Adam'),
  ('Ewa'),
  ('Kamil')
ON CONFLICT DO NOTHING;

INSERT INTO do_zamowienia(zamowiony_przedmiot) VALUES
  ('Dysk SSD 1TB'),
  ('Pasta termoprzewodząca'),
  ('RAM DDR4 16GB'),
  ('Zasilacz ATX 600W')
ON CONFLICT DO NOTHING;
