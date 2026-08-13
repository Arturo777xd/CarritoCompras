package carritocompras;

/** Pruebas unitarias de los mensajes de validación del inicio de sesión. */
public class ValidacionLoginTest {

    public static void main(String[] args) {
        verificar("Por favor, captura el usuario y la contraseña.",
                ValidacionLogin.validarCampos("", ""), "Ambos campos vacíos");
        verificar("Por favor, captura el usuario.",
                ValidacionLogin.validarCampos("  ", "secreta"), "Usuario vacío");
        verificar("Por favor, captura la contraseña.",
                ValidacionLogin.validarCampos("Arturo", " "), "Contraseña vacía");
        verificar(null, ValidacionLogin.validarCampos("Arturo", "Arturo777"), "Datos completos");

        System.out.println("ValidacionLoginTest: OK");
    }

    private static void verificar(String esperado, String actual, String caso) {
        if (esperado == null ? actual != null : !esperado.equals(actual)) {
            throw new AssertionError(caso + ": se esperaba '" + esperado + "' y se obtuvo '" + actual + "'.");
        }
    }
}
