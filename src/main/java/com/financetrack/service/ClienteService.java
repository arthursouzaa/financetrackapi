package com.financetrack.service;

import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.api.exception.SenhaInvalidaException;
import com.financetrack.model.entity.Cliente;
import com.financetrack.model.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ClienteService implements UserDetailsService {

    private final PasswordEncoder encoder;
    private final ClienteRepository repository;

    public ClienteService(PasswordEncoder encoder, ClienteRepository repository) {
        this.encoder = encoder;
        this.repository = repository;
    }

    public List<Cliente> getClientes() {
        return repository.findAll();
    }

    public Optional<Cliente> getClienteById(Long id) {
        return repository.findById(id);
    }

    public UserDetails autenticar(Cliente cliente) {
        UserDetails user = loadUserByUsername(cliente.getEmail());
        boolean senhasBatem = encoder.matches(cliente.getSenha(), user.getPassword());

        if (senhasBatem) {
            return user;
        }
        throw new SenhaInvalidaException();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Cliente usuario = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        String[] roles = usuario.isAdmin()
                ? new String[]{"ADMIN", "USER"}
                : new String[]{"USER"};

        return User
                .builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }

    @Transactional
    public Cliente salvar(Cliente cliente) {
        validar(cliente);

        String senhaCriptografada = encoder.encode(cliente.getSenha());
        cliente.setSenha(senhaCriptografada);
        cliente.setSenhaConfirmada(senhaCriptografada);

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
            throw new RegraNegocioException("Senha inválida.");
        }

        Pattern pattern = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{8,32}$");
        Matcher matcher = pattern.matcher(cliente.getSenha());
        if (!matcher.find()) {
            throw new RegraNegocioException("A senha deve conter letras maiúsculas, minúsculas, números e ter entre 8 e 32 caracteres.");
        }

        if (cliente.getSenhaConfirmada() == null || cliente.getSenhaConfirmada().trim().isEmpty() || !(cliente.getSenhaConfirmada().equals(cliente.getSenha()))) {
            throw new RegraNegocioException("As senhas não conferem.");
        }
    }
}