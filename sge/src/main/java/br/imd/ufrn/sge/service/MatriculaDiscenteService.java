package br.imd.ufrn.sge.service;

import br.imd.ufrn.sge.config.GlobalStrategy;
import br.imd.ufrn.sge.framework.aprovacao_materia.AprovacaoTemplate;
import br.imd.ufrn.sge.framework.notas.INotaStrategy;
import br.imd.ufrn.sge.framework.notas.NotaAmericanaStrategy;
import br.imd.ufrn.sge.framework.notas.NotaNormalStrategy;
import br.imd.ufrn.sge.framework.notas.NotaPonderadaStrategy;
import br.imd.ufrn.sge.framework.progressao_ano.IProgressaoStrategy;
import br.imd.ufrn.sge.framework.progressao_ano.ProgressaoCursos;
import br.imd.ufrn.sge.framework.progressao_ano.ProgressaoParcial;
import br.imd.ufrn.sge.framework.progressao_ano.ProgressaoTotal;
import br.imd.ufrn.sge.models.discente.MatriculaDiscente;
import br.imd.ufrn.sge.repository.MatriculaDiscenteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class MatriculaDiscenteService {

    @Autowired
    private GlobalStrategy globalStrategy;

    @Autowired
    MatriculaDiscenteRepository matriculaDiscenteRepository;

    private final Map<String, IProgressaoStrategy> mapProgressaoStrategy = Map.of(
            "1", new ProgressaoParcial(),
            "2", new ProgressaoCursos(),
            "3", new ProgressaoTotal()
    );

    public Optional<MatriculaDiscente> encontrarMatriculaPorIdPessoaEAno(Long idPessoa, int ano) {
        return matriculaDiscenteRepository.findByIdPessoaAndAno(idPessoa, ano);
    }

    public Optional<MatriculaDiscente> encontrarMatriculaPorIdDiscente(Long idDiscente) {
        return matriculaDiscenteRepository.findByIdDiscente(idDiscente);
    }

    public Optional<MatriculaDiscente> findById(Long id) {
        return matriculaDiscenteRepository.findById(id);
    }

    public Optional<MatriculaDiscente> encontrarMatriculaPorIdMatriculaEAno(Long idMatricula, int ano) {
        return matriculaDiscenteRepository.findByIdMatriculaAndAno(idMatricula, ano);
    }

    public MatriculaDiscente salvarMatricula(MatriculaDiscente matriculaDiscente) {
        return matriculaDiscenteRepository.save(matriculaDiscente);
    }

    public Optional<MatriculaDiscente> encontrarMatriculaDiscentePorNumeroMatricula(String numeroMatricula) {
        return matriculaDiscenteRepository.findByNumeroMatricula(numeroMatricula);
    }

    @Transactional
    public MatriculaDiscente atualizarStatusAprovacao(MatriculaDiscente matriculaDiscente, List<MatriculaDiscente.Status> materiasStatus) {
        String strategyChoice = globalStrategy.getEscolhaStrategy().toLowerCase();
        IProgressaoStrategy strategy = mapProgressaoStrategy.get(strategyChoice);
        strategy.aprovaAno(matriculaDiscente, materiasStatus);
        return matriculaDiscenteRepository.save(matriculaDiscente);
    }

    public void salvar(MatriculaDiscente matDis) {
        matriculaDiscenteRepository.save(matDis);
    }
}
