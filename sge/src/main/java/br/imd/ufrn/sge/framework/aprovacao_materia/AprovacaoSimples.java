package br.imd.ufrn.sge.framework.aprovacao_materia;

import br.imd.ufrn.sge.models.DiscenteMateria;
import br.imd.ufrn.sge.models.Frequencia;
import br.imd.ufrn.sge.models.discente.MatriculaDiscente;
import br.imd.ufrn.sge.service.DiscenteMateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AprovacaoSimples extends AprovacaoTemplate {

    @Autowired
    private DiscenteMateriaService discenteMateriaService;

    @Override
    public void aprovaAluno(DiscenteMateria discenteMateria, List<Frequencia> frequencias) {
        float u1 = discenteMateria.getUnidade1();
        float u2 = discenteMateria.getUnidade2();
        float u3 = discenteMateria.getUnidade3();
        float pf = discenteMateria.getProvaFinal();
        float media = discenteMateriaService.calcularNota(u1,u2,u3);

        float frequencia = calculaFrequencia(frequencias);

        if(media < 60) {
            media = (media + pf) / 2;
        }

        if(media >= 60 && frequencia >= 75) {
            discenteMateria.setStatus(MatriculaDiscente.Status.APROVADO);
        } else {
            discenteMateria.setStatus(MatriculaDiscente.Status.REPROVADO);
        }
    }
}
