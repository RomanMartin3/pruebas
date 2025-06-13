package com.projectfinal.greenthumb_backend.service;

import com.projectfinal.greenthumb_backend.dto.AuthRequestDTO;
import com.projectfinal.greenthumb_backend.dto.ClienteRegistroDTO;
import com.projectfinal.greenthumb_backend.dto.UsuarioDTO;
import com.projectfinal.greenthumb_backend.entities.Administrador;
import com.projectfinal.greenthumb_backend.entities.Cliente;
import com.projectfinal.greenthumb_backend.entities.Usuario;
import com.projectfinal.greenthumb_backend.repositories.AdministradorRepository;
import com.projectfinal.greenthumb_backend.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final ClienteRepository clienteRepository;
    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(ClienteRepository clienteRepository,
                       AdministradorRepository administradorRepository,
                       PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioDTO authenticateUser(AuthRequestDTO authRequest) {
        Optional<Cliente> clienteOpt = clienteRepository.findByEmail(authRequest.getEmail());
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            if (passwordEncoder.matches(authRequest.getContrasena(), cliente.getContrasena())) {
                return mapUserToDTO(cliente, "CLIENTE");
            }
        }

        Optional<Administrador> adminOpt = administradorRepository.findByEmail(authRequest.getEmail());
        if (adminOpt.isPresent()) {
            Administrador admin = adminOpt.get();
            if (passwordEncoder.matches(authRequest.getContrasena(), admin.getContrasena())) {
                return mapUserToDTO(admin, "ADMIN");
            }
        }
        throw new RuntimeException("Credenciales inválidas.");
    }

    @Transactional
    public UsuarioDTO registerClient(ClienteRegistroDTO registroDTO) {
        if (clienteRepository.findByEmail(registroDTO.getEmail()).isPresent() ||
                administradorRepository.findByEmail(registroDTO.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado.");
        }

        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre(registroDTO.getNombre());
        nuevoCliente.setApellido(registroDTO.getApellido());
        nuevoCliente.setEmail(registroDTO.getEmail());
        nuevoCliente.setContrasena(passwordEncoder.encode(registroDTO.getContrasena())); // La contraseña debe ser enviada en el DTO de registro
        nuevoCliente.setTelefono(registroDTO.getTelefono());
        nuevoCliente.setCalle(registroDTO.getCalle());
        nuevoCliente.setNumero(registroDTO.getNumero());
        nuevoCliente.setCiudad(registroDTO.getCiudad());
        nuevoCliente.setProvincia(registroDTO.getProvincia());
        nuevoCliente.setCodigoPostal(registroDTO.getCodigoPostal());
        // nuevoCliente.setAuth0Id(registroDTO.getAuth0Id()); // Puedes setearlo si el DTO lo incluye y lo necesitas

        Cliente savedClient = clienteRepository.save(nuevoCliente);
        return mapUserToDTO(savedClient, "CLIENTE");
    }

    // Método auxiliar para mapear Usuario a UsuarioDTO
    private UsuarioDTO mapUserToDTO(Usuario usuario, String role) {
        List<String> roles = new ArrayList<>();
        roles.add(role);

        // Puedes añadir lógica para determinar si el registro está completo
        // Por ejemplo, si los campos de dirección están llenos para un cliente.
        boolean registroCompleto = true; // Por defecto true para este ejemplo.
        if (usuario instanceof Cliente) {
            Cliente cliente = (Cliente) usuario;
            if (cliente.getCalle() == null || cliente.getCalle().isEmpty() ||
                    cliente.getNumero() == null || cliente.getNumero().isEmpty() ||
                    cliente.getCiudad() == null || cliente.getCiudad().isEmpty() ||
                    cliente.getProvincia() == null || cliente.getProvincia().isEmpty() ||
                    cliente.getCodigoPostal() == null || cliente.getCodigoPostal().isEmpty()) {
                registroCompleto = false;
            }
        }

        // Aquí Auth0Id sería el ID interno de tu usuario, ya que no estamos usando un ID de Auth0 real
        return new UsuarioDTO(
                usuario.getUsuarioId(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getUsuarioId().toString(), // Usamos el ID interno como Auth0Id simulado
                roles,
                registroCompleto
        );
    }
}