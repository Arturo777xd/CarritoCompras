package carritocompras.Ventanas;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * Estilo visual compartido por las ventanas de la aplicación.
 * Se basa únicamente en componentes estándar de Java Swing.
 */
public final class TemaAplicacion {

    public static final Color AZUL_PRINCIPAL = new Color(45, 91, 140);
    public static final Color AZUL_SELECCION = new Color(220, 232, 245);
    public static final Color FONDO = new Color(245, 247, 250);
    public static final Color BLANCO = Color.WHITE;
    public static final Color TEXTO = new Color(48, 55, 65);
    public static final Color BORDE = new Color(205, 212, 220);

    private static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FUENTE_ETIQUETA = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 20);
    private static final Border BORDE_CAMPO = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE),
            BorderFactory.createEmptyBorder(5, 8, 5, 8));

    private TemaAplicacion() {
    }

    /** Configura valores base antes de crear las ventanas. */
    public static void instalar() {
        UIManager.put("Panel.background", FONDO);
        UIManager.put("Label.foreground", TEXTO);
        UIManager.put("Button.font", FUENTE_NORMAL);
        UIManager.put("TextField.font", FUENTE_NORMAL);
        UIManager.put("ComboBox.font", FUENTE_NORMAL);
        UIManager.put("Table.font", FUENTE_NORMAL);
    }

    /** Aplica el estilo a los controles ya creados por el formulario de NetBeans. */
    public static void aplicarVentana(JFrame ventana) {
        ventana.getContentPane().setBackground(FONDO);
        ventana.setBackground(FONDO);
        for (Component componente : ventana.getContentPane().getComponents()) {
            aplicarComponente(componente);
        }
        ventana.pack();
        ventana.setResizable(false);
    }

    /** Sirve para volver a dar formato a controles creados dinámicamente. */
    public static void aplicarComponente(Component componente) {
        if (componente instanceof JPanel) {
            JPanel panel = (JPanel) componente;
            panel.setBackground(BLANCO);
            if (panel.getBorder() == null) {
                panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            }
        } else if (componente instanceof JLabel) {
            estilizarEtiqueta((JLabel) componente);
        } else if (componente instanceof JTextField) {
            JTextField campo = (JTextField) componente;
            campo.setFont(FUENTE_NORMAL);
            campo.setForeground(TEXTO);
            campo.setBackground(BLANCO);
            campo.setBorder(BORDE_CAMPO);
            campo.setPreferredSize(new Dimension(campo.getPreferredSize().width, 32));
        } else if (componente instanceof JButton) {
            estilizarBoton((JButton) componente);
        } else if (componente instanceof JComboBox) {
            JComboBox<?> lista = (JComboBox<?>) componente;
            lista.setFont(FUENTE_NORMAL);
            lista.setForeground(TEXTO);
            lista.setBackground(BLANCO);
            lista.setBorder(BorderFactory.createLineBorder(BORDE));
            lista.setPreferredSize(new Dimension(lista.getPreferredSize().width, 32));
        } else if (componente instanceof JTable) {
            estilizarTabla((JTable) componente);
        } else if (componente instanceof JScrollPane) {
            JScrollPane scroll = (JScrollPane) componente;
            scroll.setBorder(BorderFactory.createLineBorder(BORDE));
            scroll.getViewport().setBackground(BLANCO);
        }

        if (componente instanceof Container) {
            for (Component hijo : ((Container) componente).getComponents()) {
                aplicarComponente(hijo);
            }
        }
    }

    private static void estilizarEtiqueta(JLabel etiqueta) {
        etiqueta.setForeground(TEXTO);
        etiqueta.setFont(esTitulo(etiqueta.getText()) ? FUENTE_TITULO : FUENTE_ETIQUETA);
    }

    private static boolean esTitulo(String texto) {
        if (texto == null) {
            return false;
        }
        String valor = texto.toLowerCase();
        return valor.startsWith("bienvenido") || valor.equals("productos")
                || valor.equals("mis compras") || valor.equals("producto");
    }

    private static void estilizarBoton(JButton boton) {
        boolean principal = esAccionPrincipal(boton.getText());
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setMargin(new java.awt.Insets(7, 14, 7, 14));
        boton.setOpaque(true);
        boton.setBackground(principal ? AZUL_PRINCIPAL : BLANCO);
        boton.setForeground(principal ? BLANCO : AZUL_PRINCIPAL);
        boton.setBorder(BorderFactory.createLineBorder(principal ? AZUL_PRINCIPAL : BORDE));
    }

    private static boolean esAccionPrincipal(String texto) {
        if (texto == null) {
            return false;
        }
        String valor = texto.toLowerCase();
        return valor.contains("iniciar") || valor.contains("publicar")
                || valor.contains("finalizar") || valor.contains("agregar")
                || valor.contains("añadir");
    }

    private static void estilizarTabla(JTable tabla) {
        tabla.setFont(FUENTE_NORMAL);
        tabla.setForeground(TEXTO);
        tabla.setBackground(BLANCO);
        tabla.setGridColor(new Color(228, 232, 237));
        tabla.setRowHeight(28);
        tabla.setSelectionBackground(AZUL_SELECCION);
        tabla.setSelectionForeground(TEXTO);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(AZUL_PRINCIPAL);
        tabla.getTableHeader().setForeground(BLANCO);
        tabla.getTableHeader().setReorderingAllowed(false);
    }
}
