package com.financetrack.service;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.Aporte;
import com.financetrack.model.repository.AporteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AporteService {
    private AporteRepository repository;

    public AporteService(AporteRepository repository) {
        this.repository = repository;
    }

    public List<Aporte> getAportes() {
        return repository.findAll();
    }

    public Optional<Aporte> getAporteById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Aporte salvar(Aporte aporte) {
        validar(aporte);
        return repository.save(aporte);
    }

    @Transactional
    public void excluir(Aporte aporte) {
        Objects.requireNonNull(aporte.getId());
        repository.delete(aporte);
    }

    public void validar(Aporte aporte) {
        if(aporte.getValor() <= 0) {
            throw new RegraNegocioException("Valor inválido!");
        }

        if (aporte.getMetaFinanceira() == null || aporte.getMetaFinanceira().getId() == null || aporte.getMetaFinanceira().getId() == 0) {
            throw new RegraNegocioException("Meta inválida!");
        }

        // crítica de data: somente datas de hj pra trás
    }
}
