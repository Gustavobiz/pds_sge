package br.imd.ufrn.sge.service;

import br.imd.ufrn.sge.framework.config.LLMProviderConfiguration;
import br.imd.ufrn.sge.framework.llm.models.LLAMA2;
import br.imd.ufrn.sge.framework.llm.models.LLAMA3;
import br.imd.ufrn.sge.framework.llm.strategy.LLMContext;
import br.imd.ufrn.sge.models.discente.MatriculaDiscente;
import br.imd.ufrn.sge.relatorio.relatorio.Relatorio;
import br.imd.ufrn.sge.relatorio.data.DadosAcademicoFetcher;
import br.imd.ufrn.sge.relatorio.data.DadosObservacaoFetcher;
import br.imd.ufrn.sge.relatorio.interfaces.ILLMProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

@Service
public class RelatorioService {

    @Autowired
    DadosAcademicoFetcher dadosAcademicoFetcher;

    @Autowired
    DadosObservacaoFetcher dadosObservacaoFetcher;

    @Autowired
    MatriculaDiscenteService matriculaDiscenteService;

    @Autowired
    DiscenteMateriaService notaService;

    @Autowired
    LLMProviderConfiguration llmProviderConfiguration;

    public Relatorio obterRelatorioAcademico(Long idMatriculaDiscente) throws IOException, InterruptedException, IllegalArgumentException {
        Optional<MatriculaDiscente> matriculaDiscenteDB = matriculaDiscenteService.findById(idMatriculaDiscente);
        if (matriculaDiscenteDB.isPresent()) {
            if (notaService.todasUnidadesPreenchidas(idMatriculaDiscente)) {
                String data = dadosAcademicoFetcher.fetchData(matriculaDiscenteDB.get());
                //return relatorioProvider.gerarRelatorioBaseAcademico(data, matriculaDiscenteDB.get());

                LLMContext context = new LLMContext();

                Random random = new Random();

                int loadBalancer = random.nextInt(2);

                if(loadBalancer == 0) {
                    context.setModelo(new LLAMA2(llmProviderConfiguration));
                } else {
                    context.setModelo(new LLAMA3(llmProviderConfiguration));
                }
                return context.gerarRelatorioBaseAcademico(data, matriculaDiscenteDB.get());
            } else {
                throw new IllegalArgumentException("Impossível gerar relatório acadêmico, nem todas as unidades estão preenchidas.");
            }

        }else{
            throw new IllegalArgumentException("Matricula não encontrada");
        }
    }

    public Relatorio obterRelatorioPessoal(ILLMProvider relatorioProvider, Long idMatriculaEstudante) throws IOException, InterruptedException {
       /* String data = dadosObservacaoFetcher.fetchData(idMatriculaEstudante);
        return relatorioProvider.gerarRelatorioBasePessoal(data);*/
        return null;
    }


}
