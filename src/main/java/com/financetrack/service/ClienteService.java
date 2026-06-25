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
import java.util.regex.Pattern;

@Service
public class ClienteService implements UserDetailsService {

    private static final Pattern SENHA_PATTERN =
            Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{8,32}$");

    private final PasswordEncoder encoder;
    private final ClienteRepository repository;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

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

    public Optional<Cliente> getClienteByEmail(String email) {
        return repository.findByEmail(email);
    }

    public UserDetails autenticar(Cliente cliente) {
        UserDetails user = loadUserByUsername(cliente.getEmail());

        if (encoder.matches(cliente.getSenha(), user.getPassword())) {
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

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }

    @Transactional
    public Cliente salvar(Cliente cliente) {
        validar(cliente, true);
        validarEmail(cliente.getEmail(), null);
        criptografarSenha(cliente);
        return repository.save(cliente);
    }

    @Transactional
    public Cliente atualizar(Long id, Cliente dadosNovos) {
        Cliente clienteExistente = repository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado."));

        validarCampo(dadosNovos.getNome(), "Nome inválido.");
        validarCampo(dadosNovos.getTelefone(), "Telefone inválido.");
        validarTelefone(dadosNovos.getTelefone());
        validarEmail(dadosNovos.getEmail(), id);

        clienteExistente.setNome(dadosNovos.getNome());
        clienteExistente.setEmail(dadosNovos.getEmail());
        clienteExistente.setTelefone(dadosNovos.getTelefone());
        clienteExistente.setAdmin(dadosNovos.isAdmin());

        if (dadosNovos.getSenha() != null && !dadosNovos.getSenha().isBlank()) {
            if (!dadosNovos.getSenha().startsWith("$2a$")) {
                // Roda apenas a validação de força da senha (Regex)
                validarSenha(dadosNovos.getSenha());

                String senhaCriptografada = encoder.encode(dadosNovos.getSenha());
                clienteExistente.setSenha(senhaCriptografada);
            }
        }

        return repository.save(clienteExistente);
    }

    @Transactional
    public void excluir(Cliente cliente) {
        Objects.requireNonNull(cliente.getId());
        repository.delete(cliente);
    }

    private void validar(Cliente cliente, boolean validarSenha) {
        validarCampo(cliente.getNome(), "Nome inválido.");
        validarCampo(cliente.getTelefone(), "Telefone inválido.");
        validarTelefone(cliente.getTelefone());

        if (validarSenha || possuiSenha(cliente.getSenha())) {
            validarSenha(cliente.getSenha());
        }
    }

    private void validarEmail(String email, Long idCliente) {
        validarCampo(email, "Email inválido.");

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new RegraNegocioException("Formato de email inválido.");
        }

        repository.findByEmail(email)
                .ifPresent(cliente -> {
                    if (idCliente == null || !cliente.getId().equals(idCliente)) {
                        throw new RegraNegocioException(
                                "Já existe um usuário cadastrado com este email."
                        );
                    }
                });
    }

    private void validarCampo(String valor, String mensagem) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new RegraNegocioException(mensagem);
        }
    }

    private void validarTelefone(String telefone) {
        String telefoneNumeros = telefone.replaceAll("\\D", "");

        if (telefoneNumeros.length() < 10 || telefoneNumeros.length() > 11) {
            throw new RegraNegocioException(
                    "O telefone deve conter o DDD e ter 10 ou 11 dígitos (Ex: 11999998888)."
            );
        }
    }

    private void validarSenha(String senha) {
        validarCampo(senha, "Senha inválida.");

        if (!SENHA_PATTERN.matcher(senha).matches()) {
            throw new RegraNegocioException(
                    "A senha deve conter letras maiúsculas, minúsculas, números e ter entre 8 e 32 caracteres."
            );
        }
    }

    private boolean possuiSenha(String senha) {
        return senha != null && !senha.trim().isEmpty();
    }

    private void criptografarSenha(Cliente cliente) {
        String senhaCriptografada = encoder.encode(cliente.getSenha());
        cliente.setSenha(senhaCriptografada);
    }
}