package br.imd.ufrn.sge.framework.progressao_ano;

import br.imd.ufrn.sge.models.discente.MatriculaDiscente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ProgressaoCursos")
public class ProgressaoCursos implements IProgressaoStrategy {
    @Override
    public void aprovaAno(MatriculaDiscente matriculaDiscente, List<Boolean> materiasStatus) {
        boolean statusFinal = false;
        for (Boolean status : materiasStatus) {
            if (status)
                statusFinal = true;
        }
        matriculaDiscente.setStatus(statusFinal ? MatriculaDiscente.Status.APROVADO : MatriculaDiscente.Status.REPROVADO);
    }
}
