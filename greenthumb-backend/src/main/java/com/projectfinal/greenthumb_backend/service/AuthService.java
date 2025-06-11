package com.projectfinal.greenthumb_backend.service;

import com.projectfinal.greenthumb_backend.dto.ClienteRegistroDTO;
import com.projectfinal.greenthumb_backend.dto.UsuarioDTO;
import com.projectfinal.greenthumb_backend.entities.Administrador;
import com.projectfinal.greenthumb_backend.entities.Cliente;
import com.projectfinal.greenthumb_backend.entities.Usuario;
import com.projectfinal.greenthumb_backend.repositories.ClienteRepository;
import com.projectfinal.greenthumb_backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List; // Asegúrate de importar List
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private UsuarioDTO convertirAUsuarioDTO(Usuario usuario) {
        if (usuario == null) {
            // Manejar el caso de un usuario nuevo que aún no existe en la BD
            UsuarioDTO dto = new UsuarioDTO();
            dto.setRegistroCompleto(false);
            dto.setRoles(new ArrayList<>()); // Devolver una lista vacía
            return dto;
        }

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getUsuarioId());
        dto.setEmail(usuario.getEmail());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());

        List<String> roles = new ArrayList<>();
        if (usuario instanceof Administrador) {
            roles.add("ADMIN");
        }
        if (usuario instanceof Cliente) {
            roles.add("CLIENTE");
        }
        dto.setRoles(roles);

        // La lógica de registro completo se mantiene
        if (usuario instanceof Cliente) {
            Cliente cliente = (Cliente) usuario;
            dto.setRegistroCompleto(cliente.getCalle() != null && !cliente.getCalle().isBlank());
        } else {
            // Los admins u otros tipos de usuario se consideran completos por defecto
            dto.setRegistroCompleto(true);
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public UsuarioDTO sincronizarUsuario(String auth0Id, String email, String nombre, String apellido) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByAuth0Id(auth0Id);

        if (usuarioOpt.isPresent()) {
            System.out.println("LOG: Usuario existente encontrado en BD: " + email);
            Usuario usuario = usuarioOpt.get();

            // CORRECCIÓN 1: Determinar el rol y usar el constructor correcto
            // Creamos una lista de roles. Por ahora es uno, pero podría ser más.
            List<String> roles;
            if (usuario instanceof Administrador) {
                roles = List.of("ADMIN");
            } else {
                roles = List.of("CLIENTE");
            }

            // Usamos el constructor que creaste
            UsuarioDTO dto = new UsuarioDTO(
                    usuario.getUsuarioId(),
                    usuario.getEmail(),
                    usuario.getNombre(),
                    usuario.getApellido(),
                    usuario.getAuth0Id(),
                    roles
            );
            dto.setRegistroCompleto(true); // El registro está completo
            return dto;

        } else {
            System.out.println("LOG: Usuario nuevo detectado. Requiere completar registro: " + email);

            // CORRECCIÓN 2: Usar el constructor vacío y setters
            // Como el usuario no está completo, no podemos usar el constructor principal.
            UsuarioDTO dto = new UsuarioDTO();
            dto.setAuth0Id(auth0Id);
            dto.setEmail(email);
            dto.setNombre(nombre);
            dto.setApellido(apellido != null ? apellido : ""); // El apellido puede ser nulo en Auth0
            dto.setRegistroCompleto(false); // La señal clave para el frontend
            dto.setRoles(List.of("CLIENTE")); // Asignamos rol por defecto

            return dto;
        }
    }

    @Transactional
    public UsuarioDTO registrarNuevoCliente(ClienteRegistroDTO request, String auth0Id) {
        if (usuarioRepository.findByAuth0Id(auth0Id).isPresent()) {
            throw new IllegalStateException("El usuario ya está registrado.");
        }

        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setAuth0Id(auth0Id);
        nuevoCliente.setEmail(request.getEmail());
        nuevoCliente.setNombre(request.getNombre());
        nuevoCliente.setApellido(request.getApellido());
        nuevoCliente.setTelefono(request.getTelefono());
        nuevoCliente.setCalle(request.getCalle());
        nuevoCliente.setNumero(request.getNumero());
        nuevoCliente.setCiudad(request.getCiudad());
        nuevoCliente.setProvincia(request.getProvincia());
        nuevoCliente.setCodigoPostal(request.getCodigoPostal());
        nuevoCliente.setContrasena(passwordEncoder.encode("AUTH0_MANAGED_" + auth0Id));

        Cliente clienteGuardado = clienteRepository.save(nuevoCliente);
        System.out.println("LOG: Nuevo cliente registrado y guardado en BD con ID: " + clienteGuardado.getUsuarioId());

        // CORRECCIÓN 3: Envolver el rol en una lista
        UsuarioDTO dto = new UsuarioDTO(
                clienteGuardado.getUsuarioId(),
                clienteGuardado.getEmail(),
                clienteGuardado.getNombre(),
                clienteGuardado.getApellido(),
                clienteGuardado.getAuth0Id(),
                List.of("CLIENTE") // Aquí estaba el error
        );
        dto.setRegistroCompleto(true); // Ya está completo
        return dto;
    }
    public Optional<Usuario> findUsuarioByAuth0Id(String auth0Id) {
        return usuarioRepository.findByAuth0Id(auth0Id);
    }

}