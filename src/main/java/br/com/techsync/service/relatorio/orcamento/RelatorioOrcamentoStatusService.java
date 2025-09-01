package br.com.techsync.service.relatorio.orcamento;

import br.com.techsync.dto.OrcamentoStatusResumo;
import br.com.techsync.models.Empresa;
import br.com.techsync.repository.OrcamentoRepository;
import br.com.techsync.repository.EmpresaRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class RelatorioOrcamentoStatusService {

    private final OrcamentoRepository orcamentoRepository;
    private final EmpresaRepository empresaRepository; // Para pegar dados da empresa

    public RelatorioOrcamentoStatusService(OrcamentoRepository orcamentoRepository,
                                           EmpresaRepository empresaRepository) {
        this.orcamentoRepository = orcamentoRepository;
        this.empresaRepository = empresaRepository;
    }

    public ByteArrayInputStream gerarRelatorioStatus() throws Exception {
        List<OrcamentoStatusResumo> dados = orcamentoRepository.resumoPorMesEStatus();
        Document document = new Document(PageSize.A4, 36, 36, 72, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // --- LOGO DA EMPRESA ---
        Empresa empresa = empresaRepository.findAll().stream().findFirst().orElse(null);
        if (empresa != null && empresa.getLogo() != null) {
            Image logo = Image.getInstance(empresa.getLogo());
            logo.scaleToFit(100, 50);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
        }

        // --- TITULO ---
        Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph titulo = new Paragraph("Relatório de Status de Orçamentos", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(10f);
        document.add(titulo);

        Font subTituloFont = new Font(Font.HELVETICA, 12, Font.NORMAL);
        Paragraph subTitulo = new Paragraph("Gerado em: " + java.time.LocalDate.now(), subTituloFont);
        subTitulo.setAlignment(Element.ALIGN_CENTER);
        subTitulo.setSpacingAfter(20f);
        document.add(subTitulo);

        // --- TABELA ---
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2f, 3f, 2f}); // Ajusta proporção das colunas

        // Cabeçalhos com fundo colorido
        Font headFont = new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE);

        PdfPCell h1 = new PdfPCell(new Phrase("Mês", headFont));
        h1.setBackgroundColor(Color.GRAY);
        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        h1.setPadding(5f);
        table.addCell(h1);

        PdfPCell h2 = new PdfPCell(new Phrase("Status", headFont));
        h2.setBackgroundColor(Color.GRAY);
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        h2.setPadding(5f);
        table.addCell(h2);

        PdfPCell h3 = new PdfPCell(new Phrase("Quantidade", headFont));
        h3.setBackgroundColor(Color.GRAY);
        h3.setHorizontalAlignment(Element.ALIGN_CENTER);
        h3.setPadding(5f);
        table.addCell(h3);

        // Linhas alternadas
        boolean odd = true;
        for (OrcamentoStatusResumo r : dados) {
            Color bgColor = odd ? new Color(230, 230, 230) : Color.WHITE;
            odd = !odd;

            PdfPCell cell1 = new PdfPCell(new Phrase(r.getMes()));
            cell1.setBackgroundColor(bgColor);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(new Phrase(r.getStatus()));
            cell2.setBackgroundColor(bgColor);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell2);

            PdfPCell cell3 = new PdfPCell(new Phrase(String.valueOf(r.getQuantidade())));
            cell3.setBackgroundColor(bgColor);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell3);
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
