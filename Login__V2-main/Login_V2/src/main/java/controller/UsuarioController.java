package controller;

import entity.Usuario;
import service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Página principal - Listar todos los usuarios
    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("nuevoUsuario", new Usuario());
        return "lista";  // Cambiado de "usuarios/lista"
    }

    // Mostrar formulario de creación
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "formulario";  // Cambiado de "usuarios/formulario"
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model,
                                          RedirectAttributes redirectAttributes) {
        return usuarioService.buscarPorId(id)
                .map(usuario -> {
                    model.addAttribute("usuario", usuario);
                    return "editar";  // Cambiado de "usuarios/editar"
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("mensaje", "Usuario no encontrado");
                    redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
                    return "redirect:/usuarios";
                });
    }

    // Ver detalles de un usuario
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model,
                             RedirectAttributes redirectAttributes) {
        return usuarioService.buscarPorId(id)
                .map(usuario -> {
                    model.addAttribute("usuario", usuario);
                    return "detalle";  // Cambiado de "usuarios/detalle"
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("mensaje", "Usuario no encontrado");
                    redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
                    return "redirect:/usuarios";
                });
    }

    // Autenticar usuario (Login)
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";  // Cambiado de "usuarios/login"
    }

    // Los métodos POST no cambian (solo redireccionan)
    // Crear usuario
    @PostMapping("/crear")
    public String crearUsuario(@ModelAttribute Usuario usuario,
                               @RequestParam String password,
                               RedirectAttributes redirectAttributes) {
        try {
            usuarioService.crearUsuario(usuario, password);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario creado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/usuarios";
    }

    // Actualizar usuario
    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Long id,
                                    @ModelAttribute Usuario usuario,
                                    RedirectAttributes redirectAttributes) {
        try {
            usuarioService.actualizarUsuario(id, usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/usuarios";
    }

    // Desactivar usuario
    @PostMapping("/desactivar/{id}")
    public String desactivarUsuario(@PathVariable Long id,
                                    RedirectAttributes redirectAttributes) {
        try {
            usuarioService.desactivarUsuario(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario desactivado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "info");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/usuarios";
    }

    // Eliminar usuario
    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/autenticar")
    public String autenticar(@RequestParam String username,
                             @RequestParam String password,
                             RedirectAttributes redirectAttributes) {
        boolean autenticado = usuarioService.autenticarUsuario(username, password);

        if (autenticado) {
            redirectAttributes.addFlashAttribute("mensaje", "Autenticación exitosa");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Credenciales incorrectas");
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/usuarios";
    }
}