package vista;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class MochilaVista extends JPanel {
    private JTextArea resultadoArea;
    private JTextField capacidadField;
    private JTextArea elementosArea;
    private JButton resolverButton;
    private JButton mostrarArbolButton;
    private JButton limpiarButton;
    private JLabel tituloLabel;
    private PanelArbol panelArbol;
    private JTabbedPane tabbedPane;
    
    // Paleta de colores moderna
    private static final Color COLOR_PRIMARIO = new Color(79, 70, 229); // Indigo
    private static final Color COLOR_SECUNDARIO = new Color(59, 130, 246); // Blue
    private static final Color COLOR_EXITO = new Color(16, 185, 129); // Green
    private static final Color COLOR_FONDO = new Color(249, 250, 251); // Gray-50
    private static final Color COLOR_FONDO_PANEL = Color.WHITE;
    private static final Color COLOR_BORDE = new Color(229, 231, 235); // Gray-200
    private static final Color COLOR_TEXTO = new Color(17, 24, 39); // Gray-900
    private static final Color COLOR_TEXTO_SECUNDARIO = new Color(107, 114, 128); // Gray-500
    
    public MochilaVista() {
        setLayout(new BorderLayout(0, 0));
        setBackground(COLOR_FONDO);
        
        // Panel superior con título y gradiente
        JPanel panelSuperior = crearPanelSuperior();
        
        // Crear panel con pestañas mejorado
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabbedPane.setBackground(COLOR_FONDO);
        
        // Pestaña 1: Entrada de datos
        JPanel panelEntrada = crearPanelEntrada();
        tabbedPane.addTab("📝 Entrada de Datos", panelEntrada);
        
        // Pestaña 2: Resultados
        JPanel panelResultados = crearPanelResultados();
        tabbedPane.addTab("📊 Resultados", panelResultados);
        
        // Pestaña 3: Visualización del Árbol
        panelArbol = new PanelArbol();
        JScrollPane scrollArbol = new JScrollPane(panelArbol);
        scrollArbol.setBorder(null);
        scrollArbol.getVerticalScrollBar().setUnitIncrement(16);
        tabbedPane.addTab("🌳 Visualización del Árbol", scrollArbol);
        
        // Ensamblar componentes
        add(panelSuperior, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(0, 100));
        panel.setBackground(COLOR_PRIMARIO);
        
        // Panel con gradiente simulado usando layers
        JPanel contenido = new JPanel(new GridBagLayout());
        contenido.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        
        // Ícono
        JLabel iconLabel = new JLabel("🎒");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        contenido.add(iconLabel, gbc);
        
        // Título
        tituloLabel = new JLabel("Problema de la Mochila");
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        tituloLabel.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        contenido.add(tituloLabel, gbc);
        
        // Subtítulo
        JLabel subtituloLabel = new JLabel("Algoritmo de Backtracking con Visualización de Árbol de Decisión");
        subtituloLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtituloLabel.setForeground(new Color(224, 231, 255)); // Indigo-100
        gbc.gridy = 1;
        contenido.add(subtituloLabel, gbc);
        
        panel.add(contenido, BorderLayout.CENTER);
        
        // Sombra inferior
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(0, 0, 0, 20));
        panel.add(separator, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelEntrada() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel central con formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(COLOR_FONDO);
        
        // Sección: Capacidad
        panelFormulario.add(crearSeccionCapacidad());
        panelFormulario.add(Box.createVerticalStrut(20));
        
        // Sección: Elementos
        panelFormulario.add(crearSeccionElementos());
        panelFormulario.add(Box.createVerticalStrut(20));
        
        // Panel de botones
        panelFormulario.add(crearPanelBotones());
        panelFormulario.add(Box.createVerticalStrut(20));
        
        // Panel de ejemplo
        panelFormulario.add(crearPanelEjemplo());
        
        panelPrincipal.add(panelFormulario, BorderLayout.NORTH);
        
        return panelPrincipal;
    }
    
    private JPanel crearSeccionCapacidad() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_FONDO_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDE, 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titulo = new JLabel("⚖️ Capacidad de la Mochila");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setForeground(COLOR_TEXTO);
        
        capacidadField = new JTextField("10");
        capacidadField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        capacidadField.setPreferredSize(new Dimension(150, 40));
        capacidadField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDE, 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JLabel descripcion = new JLabel("Peso máximo que puede soportar la mochila");
        descripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descripcion.setForeground(COLOR_TEXTO_SECUNDARIO);
        
        JPanel contenido = new JPanel(new BorderLayout(10, 5));
        contenido.setBackground(COLOR_FONDO_PANEL);
        contenido.add(capacidadField, BorderLayout.WEST);
        contenido.add(descripcion, BorderLayout.CENTER);
        
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(contenido, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearSeccionElementos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_FONDO_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDE, 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titulo = new JLabel("📦 Elementos Disponibles");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setForeground(COLOR_TEXTO);
        
        JLabel descripcion = new JLabel("Ingrese los elementos en formato: peso,valor (uno por línea)");
        descripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descripcion.setForeground(COLOR_TEXTO_SECUNDARIO);
        
        elementosArea = new JTextArea("2,3\n4,5\n3,2\n5,6\n1,4", 8, 40);
        elementosArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        elementosArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        elementosArea.setLineWrap(true);
        
        JScrollPane scrollPane = new JScrollPane(elementosArea);
        scrollPane.setBorder(new LineBorder(COLOR_BORDE, 1, true));
        
        JPanel encabezado = new JPanel(new BorderLayout(5, 5));
        encabezado.setBackground(COLOR_FONDO_PANEL);
        encabezado.add(titulo, BorderLayout.NORTH);
        encabezado.add(descripcion, BorderLayout.CENTER);
        
        panel.add(encabezado, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setBackground(COLOR_FONDO);
        
        resolverButton = crearBotonEstilizado("▶️ Resolver con Backtracking", COLOR_PRIMARIO);
        mostrarArbolButton = crearBotonEstilizado("🌳 Mostrar Árbol", COLOR_SECUNDARIO);
        limpiarButton = crearBotonEstilizado("🗑️ Limpiar", new Color(239, 68, 68));
        
        mostrarArbolButton.setEnabled(false);
        
        panel.add(resolverButton);
        panel.add(mostrarArbolButton);
        panel.add(limpiarButton);
        
        return panel;
    }
    
    private JButton crearBotonEstilizado(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (boton.isEnabled()) {
                    boton.setBackground(color.darker());
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
            }
        });
        
        return boton;
    }
    
    private JPanel crearPanelEjemplo() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(254, 249, 195)); // Yellow-100
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(252, 211, 77), 1, true), // Yellow-300
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel icono = new JLabel("💡");
        icono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JTextArea ejemploArea = new JTextArea(
            "Ejemplo de formato correcto:\n\n" +
            "2,3  ← Elemento 1: peso=2, valor=3\n" +
            "4,5  ← Elemento 2: peso=4, valor=5\n" +
            "3,2  ← Elemento 3: peso=3, valor=2\n\n" +
            "Cada línea representa un elemento con su peso y valor separados por coma."
        );
        ejemploArea.setEditable(false);
        ejemploArea.setBackground(new Color(254, 249, 195));
        ejemploArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ejemploArea.setForeground(new Color(120, 53, 15)); // Yellow-900
        ejemploArea.setBorder(null);
        
        panel.add(icono, BorderLayout.WEST);
        panel.add(ejemploArea, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelResultados() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        resultadoArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        resultadoArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        resultadoArea.setLineWrap(true);
        resultadoArea.setWrapStyleWord(true);
        resultadoArea.setText(
            "🎯 Resultados del Algoritmo\n" +
            "═══════════════════════════════════════════════════\n\n" +
            "Ejecute el algoritmo para visualizar:\n\n" +
            "  ✓ Solución óptima encontrada\n" +
            "  ✓ Elementos seleccionados\n" +
            "  ✓ Valor total maximizado\n" +
            "  ✓ Peso utilizado\n" +
            "  ✓ Estadísticas del árbol de búsqueda\n" +
            "  ✓ Nodos explorados vs podados\n" +
            "  ✓ Estructura completa del árbol de decisiones\n\n" +
            "Presione 'Resolver con Backtracking' para comenzar..."
        );
        
        JScrollPane scrollResultados = new JScrollPane(resultadoArea);
        scrollResultados.setBorder(new LineBorder(COLOR_BORDE, 1, true));
        scrollResultados.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scrollResultados, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Getters y setters
    public JButton getResolverButton() {
        return resolverButton;
    }
    
    public JButton getMostrarArbolButton() {
        return mostrarArbolButton;
    }
    
    public JButton getLimpiarButton() {
        return limpiarButton;
    }
    
    public String getCapacidadText() {
        return capacidadField.getText();
    }
    
    public String getElementosText() {
        return elementosArea.getText();
    }
    
    public void setResultadoText(String texto) {
        resultadoArea.setText(texto);
    }
    
    public void setRaizArbol(modelo.NodoArbol raiz) {
        panelArbol.setRaiz(raiz);
        tabbedPane.setSelectedIndex(2);
    }
    
    public void habilitarBotonArbol(boolean habilitar) {
        mostrarArbolButton.setEnabled(habilitar);
    }
    
    public void limpiarCampos() {
        capacidadField.setText("10");
        elementosArea.setText("2,3\n4,5\n3,2\n5,6\n1,4");
        resultadoArea.setText(
            "🎯 Resultados del Algoritmo\n" +
            "═══════════════════════════════════════════════════\n\n" +
            "Ejecute el algoritmo para visualizar:\n\n" +
            "  ✓ Solución óptima encontrada\n" +
            "  ✓ Elementos seleccionados\n" +
            "  ✓ Valor total maximizado\n" +
            "  ✓ Peso utilizado\n" +
            "  ✓ Estadísticas del árbol de búsqueda\n" +
            "  ✓ Nodos explorados vs podados\n" +
            "  ✓ Estructura completa del árbol de decisiones\n\n" +
            "Presione 'Resolver con Backtracking' para comenzar..."
        );
        panelArbol.setRaiz(null);
        habilitarBotonArbol(false);
        tabbedPane.setSelectedIndex(0);
    }
    
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
            this, 
            mensaje, 
            "⚠️ Error", 
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    public void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(
            this, 
            mensaje, 
            "✓ Éxito", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}