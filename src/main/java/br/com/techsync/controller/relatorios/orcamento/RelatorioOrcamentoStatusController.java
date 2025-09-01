package br.com.techsync.controller.relatorios.orcamento;

import br.com.techsync.service.relatorio.orcamento.RelatorioOrcamentoStatusService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/relatorio/orcamento/status")
public class RelatorioOrcamentoStatusController {

    private final RelatorioOrcamentoStatusService relatorioService;

    public RelatorioOrcamentoStatusController(RelatorioOrcamentoStatusService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping
    public ResponseEntity<byte[]> gerarRelatorioStatus() throws Exception {
        ByteArrayInputStream bis = relatorioService.gerarRelatorioStatus();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=relatorio_orcamento.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }
}
