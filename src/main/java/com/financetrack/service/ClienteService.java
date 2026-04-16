package com.financetrack.service;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.model.entity.Cliente;
import com.financetrack.model.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ClienteService {
    private ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> getClientes() {
        return repository.findAll();
    }

    public Optional<Cliente> getClienteById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Cliente salvar(Cliente cliente) {
        validar(cliente);
        return repository.save(cliente);
    }

    @Transactional
    public void excluir(Cliente cliente) {
        Objects.requireNonNull(cliente.getId());
        repository.delete(cliente);
    }

    public void validar(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Nome inválido.");
        }
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new RegraNegocioException("Email inválido.");
        }
        if (cliente.getTelefone() == null || cliente.getTelefone().trim().isEmpty()) {
            throw new RegraNegocioException("Telefone inválido.");
        }
        if (cliente.getSenha() == null || cliente.getSenha().trim().isEmpty()) {
            Pattern pattern = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{8,32}$");
            Matcher matcher = pattern.matcher(cliente.getSenha());
            if (!matcher.find()) {
                throw new RegraNegocioException("Senha inválida.");
            }
        }
        if (cliente.getSenhaConfirmada() == null || cliente.getSenhaConfirmada().trim().isEmpty() || !(cliente.getSenhaConfirmada().equals(cliente.getSenha()))) {
            throw new RegraNegocioException("Senha inválida.");
        }
    }
}
