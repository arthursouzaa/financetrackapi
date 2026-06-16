package com.financetrack.api.controller;

import com.financetrack.api.dto.ClienteDTO;
import com.financetrack.api.dto.TokenDTO;
import com.financetrack.api.dto.CredenciaisDTO;
import com.financetrack.api.exception.RegraNegocioException;
import com.financetrack.api.exception.SenhaInvalidaException;
import com.financetrack.model.entity.Cliente;
import com.financetrack.security.JwtService;
import com.financetrack.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Clientes", description = "API de Gerenciamento de Clientes e Autenticação")
public class ClienteController {

    private final ClienteService service;
    private final JwtService jwtService;

    @GetMapping()
    @Operation(summary = "Listar todos os Clientes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de Clientes retornada com sucesso!")
    })
    public ResponseEntity<List<ClienteDTO>> get() {
        List<Cliente> clientes = service.getClientes();
        List<ClienteDTO> dtos = clientes.stream()
                .map(ClienteDTO::create)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes do Cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado!"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    public ResponseEntity<?> get(@PathVariable("id") Long id) {
        Optional<Cliente> cliente = service.getClienteById(id);
        if (cliente.isEmpty()) {
            return new ResponseEntity<>("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ClienteDTO.create(cliente.get()));
    }

    @PostMapping()
    @Operation(summary = "Cadastrar novo Cliente (Sign Up)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao cadastrar cliente.")
    })
    public ResponseEntity<?> post(@RequestBody ClienteDTO dto) {
        try {
            if (dto.getSenha() == null || dto.getSenha().trim().equals("") ||
                    dto.getSenhaConfirmada() == null || dto.getSenhaConfirmada().trim().equals("")) {
                return ResponseEntity.badRequest().body("Senha inválida");
            }
            if (!dto.getSenha().equals(dto.getSenhaConfirmada())) {
                return ResponseEntity.badRequest().body("Senhas não conferem");
            }
            Cliente cliente = converter(dto);
            cliente = service.salvar(cliente);
            return new ResponseEntity<>(ClienteDTO.create(cliente), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/auth")
    @Operation(summary = "Autenticar Cliente (Sign In)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso!"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas.")
    })
    public ResponseEntity<?> autenticar(@RequestBody CredenciaisDTO credenciais) {
        try {
            Cliente usuarioDadosLogin = new Cliente();
            usuarioDadosLogin.setEmail(credenciais.getEmail());
            usuarioDadosLogin.setSenha(credenciais.getSenha());

            UserDetails userDetails = service.autenticar(usuarioDadosLogin);

            Cliente usuarioAutenticado = service.getClienteByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado após autenticação."));

            String token = jwtService.gerarToken(usuarioAutenticado);

            return ResponseEntity.ok(new TokenDTO(
                    usuarioAutenticado.getId(),
                    usuarioAutenticado.getEmail(),
                    token
            ));

        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválidos.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno no servidor: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar Cliente."),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    public ResponseEntity<?> atualizar(@PathVariable("id") Long id, @RequestBody ClienteDTO dto) {
        try {
            Cliente dadosNovos = converter(dto);
            Cliente clienteAtualizado = service.atualizar(id, dadosNovos);
            String novoToken = jwtService.gerarToken(clienteAtualizado);

            return ResponseEntity.ok(new TokenDTO(
                    clienteAtualizado.getId(),
                    clienteAtualizado.getEmail(),
                    novoToken
            ));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir Cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado."),
            @ApiResponse(responseCode = "400", description = "Erro ao excluir Cliente.")
    })
    public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
        Optional<Cliente> cliente = service.getClienteById(id);
        if (cliente.isEmpty()) {
            return new ResponseEntity<>("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(cliente.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Cliente converter(ClienteDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Cliente.class);
    }
}