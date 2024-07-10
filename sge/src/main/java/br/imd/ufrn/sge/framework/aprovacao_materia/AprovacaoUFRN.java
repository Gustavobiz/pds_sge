package br.imd.ufrn.sge.framework.aprovacao_materia;

import br.imd.ufrn.sge.models.DiscenteMateria;
import br.imd.ufrn.sge.models.Frequencia;
import br.imd.ufrn.sge.models.discente.MatriculaDiscente;
import br.imd.ufrn.sge.service.DiscenteMateriaService;

import java.util.List;

public class AprovacaoUFRN extends AprovacaoTemplate {

    private DiscenteMateriaService discenteMateriaService;
    @Override
    public void aprovaAluno(DiscenteMateria discenteMateria, List<Frequencia> frequencias) {
        float u1 = discenteMateria.getUnidade1();
        float u2 = discenteMateria.getUnidade2();
        float u3 = discenteMateria.getUnidade3();
        float pf = discenteMateria.getProvaFinal();
        float media = discenteMateriaService.calcularNota(u1, u2, u3);
        float menorNota = Math.min(u1, Math.min(u2, u3));

        float frequencia = calculaFrequencia(frequencias);

        if((menorNota < 4 || media < 6) && pf > menorNota) {
            if(menorNota == u1) {
                u1 = pf;
            } else if(menorNota == u2) {
                u2 = pf;
            } else {
                u3 = pf;
            }
            media = discenteMateriaService.calcularNota(u1, u2, u3);
            if(media >= 50 && frequencia >= 75) {
                discenteMateria.setStatus(MatriculaDiscente.Status.APROVADO);
            } else {
                discenteMateria.setStatus(MatriculaDiscente.Status.REPROVADO);
            }
        } else  if(media >= 60 && frequencia >= 75) {
            discenteMateria.setStatus(MatriculaDiscente.Status.APROVADO);
        } else {
            discenteMateria.setStatus(MatriculaDiscente.Status.REPROVADO);
        }
    }
}
