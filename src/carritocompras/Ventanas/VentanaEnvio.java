package carritocompras.Ventanas;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** Formulario sencillo para guardar y reutilizar la información de envío. */
public class VentanaEnvio extends JDialog {

    private final Connection conexionActiva;
    private final int idUsuario;
    private boolean datosGuardados;
    private final JTextField nombreCompleto = new JTextField();
    private final JTextField telefono = new JTextField();
    private final JTextField direccion = new JTextField();
    private final JTextField colonia = new JTextField();
    private final JTextField ciudad = new JTextField();
    private final JTextField estado = new JTextField();
    private final JTextField codigoPostal = new JTextField();
    private final JTextField referencias = new JTextField();

    public VentanaEnvio(java.awt.Frame padre, Connection conexion, int idUsuario) {
        super(padre, "Datos de envío", true);
        this.conexionActiva = conexion;
        this.idUsuario = idUsuario;
        crearFormulario();
        cargarDatos();
        pack();
        setResizable(false);
        setLocationRelativeTo(padre);
    }

    private void crearFormulario() {
        JPanel campos = new JPanel(new GridLayout(8, 2, 10, 8));
        campos.setBorder(BorderFactory.createEmptyBorder(16, 16, 12, 16));
        agregarCampo(campos, "Nombre completo", nombreCompleto);
        agregarCampo(campos, "Teléfono", telefono);
        agregarCampo(campos, "Dirección", direccion);
        agregarCampo(campos, "Colonia", colonia);
        agregarCampo(campos, "Ciudad", ciudad);
        agregarCampo(campos, "Estado", estado);
        agregarCampo(campos, "Código postal", codigoPostal);
        agregarCampo(campos, "Referencias", referencias);

        JButton guardar = new JButton("Guardar y continuar");
        guardar.addActionListener(e -> guardarDatos());
        JButton cancelar = new JButton("Cancelar");
        cancelar.addActionListener(e -> dispose());
        JPanel acciones = new JPanel();
        acciones.add(cancelar);
        acciones.add(guardar);

        setLayout(new BorderLayout());
        add(campos, BorderLayout.CENTER);
        add(acciones, BorderLayout.SOUTH);
        TemaAplicacion.aplicarComponente(campos);
        TemaAplicacion.aplicarComponente(acciones);
    }

    private void agregarCampo(JPanel panel, String etiqueta, JTextField campo) {
        panel.add(new JLabel(etiqueta + ":"));
        panel.add(campo);
    }

    private void cargarDatos() {
        String sql = "SELECT nombre_completo, telefono, direccion, colonia, ciudad, estado, codigo_postal, referencias "
                + "FROM usuarios WHERE id_usuario = ?";
        try {
            java.sql.PreparedStatement ps = conexionActiva.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nombreCompleto.setText(valor(rs.getString("nombre_completo")));
                telefono.setText(valor(rs.getString("telefono")));
                direccion.setText(valor(rs.getString("direccion")));
                colonia.setText(valor(rs.getString("colonia")));
                ciudad.setText(valor(rs.getString("ciudad")));
                estado.setText(valor(rs.getString("estado")));
                codigoPostal.setText(valor(rs.getString("codigo_postal")));
                referencias.setText(valor(rs.getString("referencias")));
            }
        } catch (java.sql.SQLException e) {
            mostrarError("No se pudieron cargar los datos de envío", e);
        }
    }

    private void guardarDatos() {
        if (nombreCompleto.getText().trim().isEmpty() || telefono.getText().trim().isEmpty()
                || direccion.getText().trim().isEmpty() || colonia.getText().trim().isEmpty()
                || ciudad.getText().trim().isEmpty() || estado.getText().trim().isEmpty()
                || codigoPostal.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Completa todos los datos de envío, excepto referencias.",
                    "Datos incompletos", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        String sql = "UPDATE usuarios SET nombre_completo = ?, telefono = ?, direccion = ?, colonia = ?, "
                + "ciudad = ?, estado = ?, codigo_postal = ?, referencias = ? WHERE id_usuario = ?";
        try {
            java.sql.PreparedStatement ps = conexionActiva.prepareStatement(sql);
            ps.setString(1, nombreCompleto.getText().trim());
            ps.setString(2, telefono.getText().trim());
            ps.setString(3, direccion.getText().trim());
            ps.setString(4, colonia.getText().trim());
            ps.setString(5, ciudad.getText().trim());
            ps.setString(6, estado.getText().trim());
            ps.setString(7, codigoPostal.getText().trim());
            ps.setString(8, referencias.getText().trim());
            ps.setInt(9, idUsuario);
            ps.executeUpdate();
            datosGuardados = true;
            dispose();
        } catch (java.sql.SQLException e) {
            mostrarError("No se pudieron guardar los datos de envío", e);
        }
    }

    public boolean isDatosGuardados() {
        return datosGuardados;
    }

    public String getDireccionFactura() {
        String texto = nombreCompleto.getText().trim() + " - " + direccion.getText().trim()
                + ", " + colonia.getText().trim() + ", " + ciudad.getText().trim()
                + ", " + estado.getText().trim() + " CP " + codigoPostal.getText().trim();
        if (!referencias.getText().trim().isEmpty()) {
            texto += " - Ref: " + referencias.getText().trim();
        }
        return texto;
    }

    private String valor(String texto) {
        return texto == null ? "" : texto;
    }

    private void mostrarError(String mensaje, java.sql.SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(this, mensaje + ": " + e.getMessage(),
                "Datos de envío", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}
