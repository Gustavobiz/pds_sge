package br.imd.ufrn.sge.framework.aprovacao_materia;

import br.imd.ufrn.sge.models.DiscenteMateria;
import br.imd.ufrn.sge.models.Frequencia;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class AprovacaoTemplate {

    public void aprovacaoMethod(DiscenteMateria discenteMateria, List<Frequencia> frequencias) {
        calculaFrequencia(frequencias);
        aprovaAluno(discenteMateria, frequencias);
    }

    public float calculaFrequencia(List<Frequencia> frequencias) {
        int totalAulas = frequencias.size();
        int totalPresencas = 0;
        for (Frequencia frequencia : frequencias) {
            if (frequencia.isPresenca()) {
                totalPresencas++;
            }
        }
        if (totalAulas == 0) {
            return 0;
        }
        return (totalPresencas * 100) / totalAulas;
    }

    public abstract void aprovaAluno(DiscenteMateria discenteMateria, List<Frequencia> frequencias);
}