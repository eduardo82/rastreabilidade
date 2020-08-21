package br.com.araujo.rastreabilidade.service.cosmos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.araujo.rastreabilidade.model.cosmos.Filial;
import br.com.araujo.rastreabilidade.repository.cosmos.FilialRepository;

@Service
public class FilialService {

	@Autowired
	private FilialRepository filialRepository;
	
	public List<Filial> listaFiliais() {
		return filialRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}
	
	public Filial buscaDescricao(Integer id) {
		return filialRepository.findById(id).orElse(new Filial());
	}
	
}
