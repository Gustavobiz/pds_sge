package br.imd.ufrn.sge.service;

import br.imd.ufrn.sge.exceptions.DeletandoDadosLigadosException;
import br.imd.ufrn.sge.exceptions.IdNaoEncontradoException;
import br.imd.ufrn.sge.exceptions.NomeNaoEncontradoException;
import br.imd.ufrn.sge.models.DadosPessoais;
import br.imd.ufrn.sge.repository.DadosPessoaisRepository;
import br.imd.ufrn.sge.repository.DiscenteRepository;
import br.imd.ufrn.sge.repository.DocenteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DadosPessoaisService {

    @Autowired
    private DadosPessoaisRepository dadosPessoaisRepository;

    @Autowired
    private DiscenteRepository discenteRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    public List<DadosPessoais> listarTodos() {
        return dadosPessoaisRepository.findAll();
    }

    public Optional<DadosPessoais> encontrarPorId(Long id) {
        if (dadosPessoaisRepository.findById(id).isEmpty()){
            throw new IdNaoEncontradoException();
        }
        return dadosPessoaisRepository.findById(id);
    }

    public List<DadosPessoais> findByName(String name) {
        if(name.isEmpty()){throw new NomeNaoEncontradoException();}
        return dadosPessoaisRepository.findByName(name);
    }

    public boolean pessoaExiste(Long id) {
        Optional<DadosPessoais> pessoaExistente = encontrarPorId(id);
        return pessoaExistente.isPresent();

    }

    @Transactional
    public DadosPessoais salvar(DadosPessoais pessoa) {
        return dadosPessoaisRepository.save(pessoa);
    }

    @Transactional
    public void deletar(Long id) {
        if (!pessoaExiste(id)){
            throw new IdNaoEncontradoException();
        }if (docenteRepository.findByDadosPessoais(id).isEmpty()&& discenteRepository.findByDadosPessoais(id).isEmpty()){
            dadosPessoaisRepository.deleteById(id);
        }else {
            throw new DeletandoDadosLigadosException();
        }
    }

    public List<DadosPessoais> buscarDadosPeloAnoDeCriacao(int ano) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, ano);

        Date data = calendar.getTime();

        return dadosPessoaisRepository.findByYearOfCreation(data);
    }
}
