package vista;

import modelo.NodoArbol;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class PanelArbol extends JPanel {
    private NodoArbol raiz;
    private int nivelMaximo = 0;
    private static final int NODO_ANCHO = 140;
    private static final int NODO_ALTO = 80;
    private static final int ESPACIO_HORIZONTAL = 100;
    private static final int ESPACIO_VERTICAL = 150;
    
    // Control de zoom
    private double zoomLevel = 1.0;
    private static final double ZOOM_MIN = 0.3;
    private static final double ZOOM_MAX = 3.0;
    private static final double ZOOM_STEP = 0.1;
    
    // Paleta de colores moderna
    private static final Color COLOR_FONDO = new Color(249, 250, 251);
    private static final Color COLOR_NODO_NORMAL = Color.WHITE;
    private static final Color COLOR_NODO_SELECCIONADO = new Color(220, 252, 231); // Green-100
    private static final Color COLOR_NODO_PODADO = new Color(254, 226, 226); // Red-100
    private static final Color COLOR_BORDE_NORMAL = new Color(209, 213, 219); // Gray-300
    private static final Color COLOR_BORDE_SELECCIONADO = new Color(16, 185, 129); // Green-500
    private static final Color COLOR_BORDE_PODADO = new Color(239, 68, 68); // Red-500
    private static final Color COLOR_LINEA = new Color(156, 163, 175); // Gray-400
    private static final Color COLOR_TEXTO = new Color(17, 24, 39); // Gray-900
    private static final Color COLOR_TEXTO_SECUNDARIO = new Color(107, 114, 128); // Gray-500
    private static final Color COLOR_PRIMARIO = new Color(79, 70, 229); // Indigo
    
    // Componentes
    private JPanel panelControles;
    private JPanel panelArbolDibujo;
    private JLabel labelZoom;
    private JSlider sliderZoom;
    
    public PanelArbol() {
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);
        
        // Crear panel de controles
        crearPanelControles();
        
        // Crear panel para el árbol con scroll
        panelArbolDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Configurar antialiasing y calidad de renderizado
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Aplicar zoom
                g2d.scale(zoomLevel, zoomLevel);
                
                if (raiz != null) {
                    // Dibujar leyenda
                    dibujarLeyenda(g2d);
                    
                    int startX = (int)(getWidth() / (2 * zoomLevel));
                    int startY = 80;
                    dibujarArbol(g2d, raiz, startX, startY, 
                                calcularAnchoSubarbol(raiz) * ESPACIO_HORIZONTAL / 2);
                } else {
                    dibujarMensajeVacio(g2d);
                }
            }
        };
        panelArbolDibujo.setBackground(COLOR_FONDO);
        panelArbolDibujo.setPreferredSize(new Dimension(1500, 1000));
        
        JScrollPane scrollPane = new JScrollPane(panelArbolDibujo);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void crearPanelControles() {
        panelControles = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelControles.setBackground(Color.WHITE);
        panelControles.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(229, 231, 235)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Título de zoom
        JLabel tituloZoom = new JLabel("🔎 Control de Zoom:");
        tituloZoom.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tituloZoom.setForeground(COLOR_TEXTO);
        panelControles.add(tituloZoom);
        
        // Botón zoom out
        JButton btnZoomOut = new JButton("➖");
        btnZoomOut.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnZoomOut.setFocusPainted(false);
        btnZoomOut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnZoomOut.setPreferredSize(new Dimension(45, 35));
        btnZoomOut.setToolTipText("Alejar (Zoom Out)");
        btnZoomOut.setBackground(new Color(239, 246, 255));
        btnZoomOut.setBorder(BorderFactory.createLineBorder(new Color(147, 197, 253), 2));
        btnZoomOut.addActionListener(e -> aplicarZoom(-ZOOM_STEP));
        panelControles.add(btnZoomOut);
        
        // Label porcentaje de zoom
        labelZoom = new JLabel("100%");
        labelZoom.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelZoom.setForeground(COLOR_PRIMARIO);
        labelZoom.setPreferredSize(new Dimension(60, 35));
        labelZoom.setHorizontalAlignment(SwingConstants.CENTER);
        labelZoom.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 2));
        labelZoom.setOpaque(true);
        labelZoom.setBackground(new Color(238, 242, 255));
        panelControles.add(labelZoom);
        
        // Botón zoom in
        JButton btnZoomIn = new JButton("➕");
        btnZoomIn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnZoomIn.setFocusPainted(false);
        btnZoomIn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnZoomIn.setPreferredSize(new Dimension(45, 35));
        btnZoomIn.setToolTipText("Acercar (Zoom In)");
        btnZoomIn.setBackground(new Color(239, 246, 255));
        btnZoomIn.setBorder(BorderFactory.createLineBorder(new Color(147, 197, 253), 2));
        btnZoomIn.addActionListener(e -> aplicarZoom(ZOOM_STEP));
        panelControles.add(btnZoomIn);
        
        // Slider de zoom
        sliderZoom = new JSlider(JSlider.HORIZONTAL, 
            (int)(ZOOM_MIN * 100), (int)(ZOOM_MAX * 100), 100);
        sliderZoom.setBackground(Color.WHITE);
        sliderZoom.setPreferredSize(new Dimension(200, 35));
        sliderZoom.setFocusable(false);
        sliderZoom.setMajorTickSpacing(50);
        sliderZoom.setMinorTickSpacing(10);
        sliderZoom.setPaintTicks(true);
        sliderZoom.addChangeListener(e -> {
            if (!sliderZoom.getValueIsAdjusting()) {
                zoomLevel = sliderZoom.getValue() / 100.0;
                actualizarZoom();
            }
        });
        panelControles.add(sliderZoom);
        
        // Separador
        panelControles.add(new JSeparator(SwingConstants.VERTICAL));
        
        // Botón reset zoom
        JButton btnResetZoom = new JButton("⟲ Restablecer 100%");
        btnResetZoom.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnResetZoom.setFocusPainted(false);
        btnResetZoom.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnResetZoom.setToolTipText("Restaurar zoom al 100%");
        btnResetZoom.setBackground(new Color(220, 252, 231));
        btnResetZoom.setForeground(new Color(22, 101, 52));
        btnResetZoom.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(134, 239, 172), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        btnResetZoom.addActionListener(e -> {
            zoomLevel = 1.0;
            sliderZoom.setValue(100);
            actualizarZoom();
        });
        panelControles.add(btnResetZoom);
        
        // Agregar panel de controles al norte
        add(panelControles, BorderLayout.NORTH);
    }
    
    private void aplicarZoom(double delta) {
        zoomLevel = Math.max(ZOOM_MIN, Math.min(ZOOM_MAX, zoomLevel + delta));
        sliderZoom.setValue((int)(zoomLevel * 100));
        actualizarZoom();
    }
    
    private void actualizarZoom() {
        // Actualizar label
        labelZoom.setText(String.format("%d%%", (int)(zoomLevel * 100)));
        
        // Actualizar tamaño del panel de dibujo
        if (raiz != null) {
            int anchoBase = calcularAnchoSubarbol(raiz) * ESPACIO_HORIZONTAL * 3;
            int altoBase = (nivelMaximo + 1) * (NODO_ALTO + ESPACIO_VERTICAL) + 150;
            
            int anchoZoom = (int)(Math.max(1500, anchoBase) * zoomLevel);
            int altoZoom = (int)(Math.max(1000, altoBase) * zoomLevel);
            
            panelArbolDibujo.setPreferredSize(new Dimension(anchoZoom, altoZoom));
            panelArbolDibujo.revalidate();
        }
        
        panelArbolDibujo.repaint();
    }
    
    public void setRaiz(NodoArbol raiz) {
        this.raiz = raiz;
        nivelMaximo = 0;
        if (raiz != null) {
            calcularNivelMaximo(raiz, 0);
        }
        actualizarZoom();
    }
    
    private void calcularNivelMaximo(NodoArbol nodo, int nivel) {
        if (nivel > nivelMaximo) {
            nivelMaximo = nivel;
        }
        if (nodo.getHijos() != null) {
            for (NodoArbol hijo : nodo.getHijos()) {
                if (hijo != null) {
                    calcularNivelMaximo(hijo, nivel + 1);
                }
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    
    private void dibujarLeyenda(Graphics2D g2d) {
        int x = 20;
        int y = 20;
        int spacing = 150;
        
        // Nodo normal
        dibujarItemLeyenda(g2d, x, y, COLOR_NODO_NORMAL, COLOR_BORDE_NORMAL, "⬜ Nodo Explorado");
        
        // Nodo seleccionado
        dibujarItemLeyenda(g2d, x + spacing, y, COLOR_NODO_SELECCIONADO, COLOR_BORDE_SELECCIONADO, "✓ Solución Óptima");
        
        // Nodo podado
        dibujarItemLeyenda(g2d, x + spacing * 2, y, COLOR_NODO_PODADO, COLOR_BORDE_PODADO, "✗ Nodo Podado");
    }
    
    private void dibujarItemLeyenda(Graphics2D g2d, int x, int y, Color colorFondo, Color colorBorde, String texto) {
        // Dibujar cuadrado de muestra
        g2d.setColor(colorFondo);
        g2d.fillRoundRect(x, y, 30, 30, 8, 8);
        g2d.setColor(colorBorde);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawRoundRect(x, y, 30, 30, 8, 8);
        
        // Dibujar texto
        g2d.setColor(COLOR_TEXTO);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g2d.drawString(texto, x + 40, y + 20);
    }
    
    private void dibujarMensajeVacio(Graphics2D g2d) {
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        g2d.setColor(COLOR_TEXTO_SECUNDARIO);
        
        String[] mensajes = {
            "🌳 No hay árbol de decisión para mostrar",
            "",
            "Ejecute el algoritmo de Backtracking primero",
            "para visualizar el proceso de búsqueda."
        };
        
        FontMetrics fm = g2d.getFontMetrics();
        int y = 400; // Posición fija para el mensaje
        
        for (String mensaje : mensajes) {
            int x = (1500 - fm.stringWidth(mensaje)) / 2; // Centrado en el ancho base
            g2d.drawString(mensaje, x, y);
            y += fm.getHeight() + 5;
        }
    }
    
    private int calcularAnchoSubarbol(NodoArbol nodo) {
        if (nodo == null) return 0;
        if (nodo.getHijos() == null) return 1;
        
        int anchoTotal = 0;
        for (NodoArbol hijo : nodo.getHijos()) {
            if (hijo != null) {
                anchoTotal += calcularAnchoSubarbol(hijo);
            }
        }
        return Math.max(1, anchoTotal);
    }
    
    private void dibujarArbol(Graphics2D g2d, NodoArbol nodo, int x, int y, int espacio) {
        // Dibujar conexiones a hijos primero
        if (nodo.getHijos() != null) {
            int hijoY = y + ESPACIO_VERTICAL;
            int numHijos = contarHijosNoNulos(nodo.getHijos());
            
            // Calcular espacio total necesario para distribuir los hijos
            int espacioTotal = espacio * 2;
            int espacioPorHijo = numHijos > 1 ? espacioTotal / (numHijos - 1) : 0;
            int hijoX = x - espacio;
            
            for (NodoArbol hijo : nodo.getHijos()) {
                if (hijo != null) {
                    // Calcular posiciones para la conexión
                    int x1 = x;
                    int y1 = y + NODO_ALTO;
                    int x2 = hijoX;
                    int y2 = hijoY;
                    
                    // Dibujar línea curva con gradiente
                    dibujarConexionCurva(g2d, x1, y1, x2, y2, hijo);
                    
                    // Dibujar etiqueta de decisión
                    String decision = hijo.getDecision().startsWith("Excluir") ? "NO" : "SI";
                    dibujarEtiquetaDecision(g2d, x1, y1, x2, y2, decision, hijo.isPodado());
                    
                    // Calcular espacio recursivo para el hijo
                    int anchoSubarbol = calcularAnchoSubarbol(hijo);
                    int nuevoEspacio = Math.max(ESPACIO_HORIZONTAL, anchoSubarbol * ESPACIO_HORIZONTAL / 2);
                    
                    // Dibujar subárbol del hijo
                    dibujarArbol(g2d, hijo, hijoX, hijoY, nuevoEspacio);
                    
                    // Mover a la siguiente posición para el siguiente hijo
                    hijoX += espacioPorHijo;
                }
            }
        }
        
        // Dibujar el nodo actual encima de las conexiones
        dibujarNodo(g2d, nodo, x, y);
    }
    
    private void dibujarConexionCurva(Graphics2D g2d, int x1, int y1, int x2, int y2, NodoArbol hijo) {
        // Configurar color y grosor
        g2d.setColor(hijo.isPodado() ? COLOR_BORDE_PODADO : COLOR_LINEA);
        g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        // Crear curva Bézier para conexión suave
        int midY = (y1 + y2) / 2;
        Path2D path = new Path2D.Double();
        path.moveTo(x1, y1);
        path.curveTo(x1, midY, x2, midY, x2, y2);
        
        g2d.draw(path);
    }
    
    private void dibujarEtiquetaDecision(Graphics2D g2d, int x1, int y1, int x2, int y2, String decision, boolean podado) {
        int midX = (x1 + x2) / 2;
        int midY = (y1 + y2) / 2;
        
        // Dibujar fondo de etiqueta
        g2d.setColor(podado ? COLOR_NODO_PODADO : new Color(219, 234, 254)); // Blue-100
        int anchoEtiqueta = 40;
        int altoEtiqueta = 24;
        g2d.fillRoundRect(midX - anchoEtiqueta/2, midY - altoEtiqueta/2, 
                         anchoEtiqueta, altoEtiqueta, 12, 12);
        
        // Dibujar borde de etiqueta
        g2d.setColor(podado ? COLOR_BORDE_PODADO : new Color(59, 130, 246)); // Blue-500
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(midX - anchoEtiqueta/2, midY - altoEtiqueta/2, 
                         anchoEtiqueta, altoEtiqueta, 12, 12);
        
        // Dibujar texto
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
        FontMetrics fm = g2d.getFontMetrics();
        int anchoTexto = fm.stringWidth(decision);
        g2d.drawString(decision, midX - anchoTexto/2, midY + fm.getAscent()/2 - 2);
    }
    
    private int contarHijosNoNulos(NodoArbol[] hijos) {
        int count = 0;
        if (hijos != null) {
            for (NodoArbol hijo : hijos) {
                if (hijo != null) count++;
            }
        }
        return count;
    }
    
    private void dibujarNodo(Graphics2D g2d, NodoArbol nodo, int x, int y) {
        // Determinar colores según estado
        Color colorFondo;
        Color colorBorde;
        String icono;
        
        if (nodo.isPodado()) {
            colorFondo = COLOR_NODO_PODADO;
            colorBorde = COLOR_BORDE_PODADO;
            icono = "✗";
        } else if (nodo.isSeleccionado()) {
            colorFondo = COLOR_NODO_SELECCIONADO;
            colorBorde = COLOR_BORDE_SELECCIONADO;
            icono = "★";
        } else {
            colorFondo = COLOR_NODO_NORMAL;
            colorBorde = COLOR_BORDE_NORMAL;
            icono = "○";
        }
        
        int xNodo = x - NODO_ANCHO/2;
        int yNodo = y;
        
        // Dibujar sombra
        g2d.setColor(new Color(0, 0, 0, 15));
        g2d.fillRoundRect(xNodo + 3, yNodo + 3, NODO_ANCHO, NODO_ALTO, 20, 20);
        
        // Dibujar fondo del nodo con gradiente
        GradientPaint gradient = new GradientPaint(
            xNodo, yNodo, colorFondo,
            xNodo, yNodo + NODO_ALTO, colorFondo.brighter()
        );
        g2d.setPaint(gradient);
        g2d.fillRoundRect(xNodo, yNodo, NODO_ANCHO, NODO_ALTO, 20, 20);
        
        // Dibujar borde del nodo
        g2d.setColor(colorBorde);
        g2d.setStroke(new BasicStroke(3.0f));
        g2d.drawRoundRect(xNodo, yNodo, NODO_ANCHO, NODO_ALTO, 20, 20);
        
        // Dibujar contenido del nodo
        g2d.setColor(COLOR_TEXTO);
        
        // Ícono y nivel
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2d.drawString(icono, xNodo + 10, yNodo + 25);
        
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g2d.setColor(COLOR_TEXTO_SECUNDARIO);
        g2d.drawString("Nivel " + nodo.getNivel(), xNodo + 35, yNodo + 25);
        
        // Decisión
        g2d.setColor(COLOR_TEXTO);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        String decision = nodo.getDecision();
        if (decision.length() > 18) {
            decision = decision.substring(0, 15) + "...";
        }
        g2d.drawString(decision, xNodo + 10, yNodo + 45);
        
        // Peso y Valor
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g2d.setColor(new Color(124, 58, 237)); // Violet-600
        String stats = String.format("P:%d  V:%d", nodo.getPesoAcumulado(), nodo.getValorAcumulado());
        g2d.drawString(stats, xNodo + 10, yNodo + 65);
        
        if (nodo.isPodado()) {
            dibujarBadge(g2d, xNodo + NODO_ANCHO - 55, yNodo + NODO_ALTO - 10, 
                        "PODADO", COLOR_BORDE_PODADO);
        } else if (nodo.isSeleccionado()) {
            dibujarBadge(g2d, xNodo + NODO_ANCHO - 65, yNodo + NODO_ALTO - 10, 
                        "SOLUCIÓN", COLOR_BORDE_SELECCIONADO);
        }
    }
    
    private void dibujarBadge(Graphics2D g2d, int x, int y, String texto, Color color) {
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 9));
        FontMetrics fm = g2d.getFontMetrics();
        int ancho = fm.stringWidth(texto) + 12;
        int alto = 18;
        
        g2d.setColor(color);
        g2d.fillRoundRect(x, y, ancho, alto, 10, 10);
        
        g2d.setColor(Color.WHITE);
        g2d.drawString(texto, x + 6, y + 13);
    }
}