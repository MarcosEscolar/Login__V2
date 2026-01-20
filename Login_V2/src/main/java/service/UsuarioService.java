package service;

import entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario crearUsuario(Usuario usuario, String password);
    List<Usuario> listarTodos();
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorUsername(String username);
    Usuario actualizarUsuario(Long id, Usuario usuarioActualizado);
    void desactivarUsuario(Long id);
    void eliminarUsuario(Long id);
    boolean autenticarUsuario(String username, String password);
}