package carritocompras;

/**
 * Valida los datos mínimos requeridos antes de consultar el inicio de sesión.
 */
public final class ValidacionLogin {

    private ValidacionLogin() {
    }

    /**
     * @return el mensaje de error, o null cuando los campos son válidos.
     */
    public static String validarCampos(String usuario, String contrasena) {
        boolean usuarioVacio = usuario == null || usuario.trim().isEmpty();
        boolean contrasenaVacia = contrasena == null || contrasena.trim().isEmpty();

        if (usuarioVacio && contrasenaVacia) {
            return "Por favor, captura el usuario y la contraseña.";
        }
        if (usuarioVacio) {
            return "Por favor, captura el usuario.";
        }
        if (contrasenaVacia) {
            return "Por favor, captura la contraseña.";
        }
        return null;
    }
}
