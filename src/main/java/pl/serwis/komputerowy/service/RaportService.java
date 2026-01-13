package pl.serwis.komputerowy.service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.serwis.komputerowy.repository.RaportRepository;

@Service
public class RaportService {

  private final RaportRepository raportRepository;

  public RaportService(RaportRepository raportRepository) {
    this.raportRepository = raportRepository;
  }

  @Transactional(readOnly = true)
  public List<RaportRepository.PracownikStatusCount> raport1() {
    return raportRepository.raport1();
  }

  @Transactional(readOnly = true)
  public List<RaportRepository.MiesiacCount> raport2(Integer rok) {
    int y = (rok == null) ? Year.now().getValue() : rok;
    return raportRepository.raport2(y);
  }

  @Transactional(readOnly = true)
  public List<RaportRepository.RaportKlientaRow> raport3(String imie, String nazwisko) {
    return raportRepository.raport3(imie, nazwisko);
  }

  @Transactional(readOnly = true)
  public List<RaportRepository.PracownikZleceniaSummary> pracownikZleceniaSummary() {
    return raportRepository.pracownikZleceniaSummary();
  }

  /**
   * CSV: zestawienie per pracownik (wszystkie + rozbicie na statusy + aktywne).
   */
  public byte[] exportCsvPracownikZlecenia(List<RaportRepository.PracownikZleceniaSummary> rows) {
    StringBuilder sb = new StringBuilder();
    sb.append("PracownikId,Pracownik,Wszystkie,Przyjete,W_realizacji,Zakonczone,Wydane,Aktywne\n");
    for (var r : rows) {
      sb.append(r.pracownikId() == null ? "" : r.pracownikId()).append(',')
        .append(escape(r.pracownik())).append(',')
        .append(r.wszystkie()).append(',')
        .append(r.przyjete()).append(',')
        .append(r.wRealizacji()).append(',')
        .append(r.zakonczone()).append(',')
        .append(r.wydane()).append(',')
        .append(r.aktywne())
        .append('\n');
    }
    return sb.toString().getBytes(StandardCharsets.UTF_8);
  }

  public byte[] exportCsvRaport1(List<RaportRepository.PracownikStatusCount> rows) {
    StringBuilder sb = new StringBuilder();
    sb.append("Pracownik,Status,Liczba_zleceń\n");
    for (var r : rows) {
      sb.append(escape(str(invoke(r, "getPracownik", "pracownik")))).append(',')
        .append(escape(str(invoke(r, "getStatus", "status")))).append(',')
        .append(num(invoke(r, "getLiczbaZlecen", "liczbaZlecen", "getLiczbaZleceń", "liczbaZleceń")))
        .append('\n');
    }
    return sb.toString().getBytes(StandardCharsets.UTF_8);
  }

  public byte[] exportPdfRaport1(List<RaportRepository.PracownikStatusCount> rows) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Document doc = new Document();
    PdfWriter.getInstance(doc, out);
    doc.open();

    Paragraph title = new Paragraph("Raport 1: liczba zleceń per pracownik i status");
    title.setAlignment(Element.ALIGN_CENTER);
    doc.add(title);
    doc.add(new Paragraph(" "));

    PdfPTable table = new PdfPTable(3);
    table.addCell("Pracownik");
    table.addCell("Status");
    table.addCell("Liczba zleceń");

    for (var r : rows) {
      table.addCell(nullToEmpty(str(invoke(r, "getPracownik", "pracownik"))));
      table.addCell(nullToEmpty(str(invoke(r, "getStatus", "status"))));
      table.addCell(String.valueOf(num(invoke(r, "getLiczbaZlecen", "liczbaZlecen", "getLiczbaZleceń", "liczbaZleceń"))));
    }

    doc.add(table);
    doc.close();
    return out.toByteArray();
  }

  // --- helpers ---

  private static Object invoke(Object target, String... methodNames) {
    for (String name : methodNames) {
      try {
        Method m = target.getClass().getMethod(name);
        return m.invoke(target);
      } catch (NoSuchMethodException ignored) {
        // try next
      } catch (Exception e) {
        throw new RuntimeException("Cannot invoke " + name + " on " + target.getClass(), e);
      }
    }
    throw new RuntimeException("No matching accessor found on " + target.getClass() + " for: " + String.join(", ", methodNames));
  }

  private static String str(Object o) {
    return o == null ? "" : String.valueOf(o);
  }

  private static long num(Object o) {
    if (o == null) return 0L;
    if (o instanceof Number n) return n.longValue();
    return Long.parseLong(String.valueOf(o));
  }

  private static String escape(String v) {
    if (v == null) return "";
    String s = v.replace("\"", "\"\"");
    if (s.contains(",") || s.contains("\n") || s.contains("\"")) {
      return "\"" + s + "\"";
    }
    return s;
  }

  private static String nullToEmpty(String v) {
    return v == null ? "" : v;
  }
}
