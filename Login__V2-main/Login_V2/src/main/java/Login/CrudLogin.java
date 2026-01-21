package Login;

import entity.Usuario;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("entity")                      // ← ESCANEA ENTIDADES
@EnableJpaRepositories("repository")
@ComponentScan(basePackages = {"Login", "entity", "service", "repository", "security"})
public class CrudLogin implements CommandLineRunner {

    @Autowired
    private UsuarioService usuarioService;

    public static void main(String[] args) {
        SpringApplication.run(CrudLogin.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🚀 CRUD COMPLETO - SPRING BOOT + JPA + MYSQL");
        System.out.println("=".repeat(60));

        try {
            // 1. CREAR (CREATE)
            System.out.println("\n1. 📝 CREANDO USUARIO...");
            Usuario usuario1 = new Usuario();
            usuario1.setUsername("testuser");
            usuario1.setEmail("test@email.com");
            Usuario creado = usuarioService.crearUsuario(usuario1, "password123");
            System.out.println("   ✅ Usuario creado: " + creado.getUsername() +
                    " (ID: " + creado.getId() + ")");

            Thread.sleep(1000);

            // 2. LEER TODOS (READ ALL)
            System.out.println("\n2. 📋 LISTANDO USUARIOS...");
            var usuarios = usuarioService.listarTodos();
            if (usuarios.isEmpty()) {
                System.out.println("   No hay usuarios registrados.");
            } else {
                usuarios.forEach(u -> {
                    System.out.println("   - ID: " + u.getId() +
                            " | Usuario: " + u.getUsername() +
                            " | Email: " + u.getEmail() +
                            " | Activo: " + u.getActivo());
                });
            }

            Thread.sleep(1000);

            // 3. LEER POR ID (READ BY ID)
            System.out.println("\n3. 🔍 BUSCANDO POR ID...");
            usuarioService.buscarPorId(creado.getId()).ifPresentOrElse(
                    u -> System.out.println("   ✅ Encontrado: " + u.getUsername()),
                    () -> System.out.println("   ❌ No encontrado")
            );

            Thread.sleep(1000);

            // 4. ACTUALIZAR (UPDATE)
            System.out.println("\n4. ✏️ ACTUALIZANDO EMAIL...");
            Usuario actualizado = new Usuario();
            actualizado.setEmail("nuevo.email@test.com");

            try {
                Usuario resultado = usuarioService.actualizarUsuario(creado.getId(), actualizado);
                System.out.println("   ✅ Email actualizado a: " + resultado.getEmail());
            } catch (Exception e) {
                System.out.println("   ❌ Error al actualizar: " + e.getMessage());
            }

            Thread.sleep(1000);

            // 5. VERIFICAR ACTUALIZACIÓN
            System.out.println("\n5. 🔍 VERIFICANDO CAMBIOS...");
            usuarioService.listarTodos().forEach(u -> {
                System.out.println("   " + u.getUsername() + " → " + u.getEmail());
            });

            Thread.sleep(1000);

            // 6. DESACTIVAR (SOFT DELETE)
            System.out.println("\n6. 🚫 DESACTIVANDO USUARIO...");
            usuarioService.desactivarUsuario(creado.getId());
            System.out.println("   ✅ Usuario desactivado");

            Thread.sleep(1000);

            // 7. AUTENTICACIÓN
            System.out.println("\n7. 🔐 PROBANDO AUTENTICACIÓN...");
            boolean authCorrecta = usuarioService.autenticarUsuario("testuser", "password123");
            boolean authIncorrecta = usuarioService.autenticarUsuario("testuser", "wrongpass");
            System.out.println("   Contraseña correcta: " + (authCorrecta ? "✅" : "❌"));
            System.out.println("   Contraseña incorrecta: " + (authIncorrecta ? "✅" : "❌"));

            Thread.sleep(1000);

            // 8. ELIMINAR (DELETE)
            System.out.println("\n8. 🗑️ ELIMINANDO USUARIO...");
            usuarioService.eliminarUsuario(creado.getId());
            System.out.println("   ✅ Usuario eliminado");

            Thread.sleep(1000);

            // 9. VERIFICAR ELIMINACIÓN
            System.out.println("\n9. 📊 VERIFICANDO BASE DE DATOS...");
            var usuariosFinal = usuarioService.listarTodos();
            if (usuariosFinal.isEmpty()) {
                System.out.println("   ✅ Base de datos vacía (CRUD completo exitoso)");
            } else {
                System.out.println("   ⚠️  Quedaron " + usuariosFinal.size() + " usuarios");
                usuariosFinal.forEach(u -> {
                    System.out.println("   - " + u.getUsername());
                });
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.out.println("\n❌ ERROR CRÍTICO: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("🏁 CRUD COMPLETADO");
        System.out.println("=".repeat(60));
    }
}