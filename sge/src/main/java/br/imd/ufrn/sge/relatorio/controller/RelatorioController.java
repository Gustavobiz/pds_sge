package br.imd.ufrn.sge.relatorio.controller;

import br.imd.ufrn.sge.dto.RelatorioDTO;
import br.imd.ufrn.sge.framework.config.LLMProviderConfiguration;
import br.imd.ufrn.sge.framework.llm.models.LLAMA2;
import br.imd.ufrn.sge.framework.llm.models.LLAMA3;
import br.imd.ufrn.sge.framework.llm.strategy.LLMContext;
import br.imd.ufrn.sge.models.discente.MatriculaDiscente;
import br.imd.ufrn.sge.relatorio.providers.LLMAProvider;
import br.imd.ufrn.sge.relatorio.relatorio.Relatorio;
import br.imd.ufrn.sge.relatorio.relatorio.tipos.RelatorioAcademico;
import br.imd.ufrn.sge.relatorio.relatorio.tipos.RelatorioPessoal;
import br.imd.ufrn.sge.relatorio.repository.relatorios.RelatorioAcademicoRepository;
import br.imd.ufrn.sge.relatorio.repository.relatorios.RelatorioPessoalRepository;
import br.imd.ufrn.sge.relatorio.repository.relatorios.RelatorioRepository;
import br.imd.ufrn.sge.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Random;

@RestController
@RequestMapping(path="/api/relatorio", produces="application/json")
public class RelatorioController {

    @Autowired
    RelatorioService relatorioService;

    @Autowired
    LLMAProvider llmaProvider;

    @Autowired
    RelatorioAcademicoRepository relatorioAcademicoRepository;

    @Autowired
    RelatorioPessoalRepository relatorioPessoalRepository;

    @Autowired
    LLMProviderConfiguration llmProviderConfiguration;

    @GetMapping("/academico/{idMatriculaDiscente}")
    ResponseEntity<RelatorioDTO> getRelatorioAcademico(@PathVariable Long idMatriculaDiscente) throws IOException, InterruptedException {
        RelatorioAcademico rel = null;
        RelatorioDTO relDTO = new RelatorioDTO();

      /*  LLMContext context = new LLMContext();

        Random random = new Random();

        int loadBalancer = random.nextInt(2);

        if(loadBalancer == 0) {
            // Load config
            context.setModelo(new LLAMA2(llmProviderConfiguration));
        } else {
            context.setModelo(new LLAMA3(llmProviderConfiguration));
        }

        context.gerarRelatorioBaseAcademico("...", null);*/

        try {
            rel = (RelatorioAcademico) relatorioService.obterRelatorioAcademico(idMatriculaDiscente);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.internalServerError().build();
        }

        relDTO.setTexto(rel.getTexto());
        relDTO.setNomeEstudante(rel.getMatriculaDiscente().getDiscente().getDadosPessoais().getNome());
        relDTO.setMatricula(rel.getMatriculaDiscente().getMatricula());
        return ResponseEntity.ok().body(relDTO);
    }

    @GetMapping("/pessoal/{idMatriculaDiscente}")
    ResponseEntity<RelatorioPessoal> getRelatorioPessoal(@PathVariable Long idMatriculaDiscente) {
        RelatorioPessoal rel = null;
        try {
            rel = (RelatorioPessoal) relatorioService.obterRelatorioPessoal(llmaProvider, idMatriculaDiscente);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.internalServerError().build();
        }
        //relatorioPessoalRepository.save(rel);
        return ResponseEntity.ok().body(rel);
    }

}
