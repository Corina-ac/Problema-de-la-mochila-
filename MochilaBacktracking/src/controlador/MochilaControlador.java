package controlador;

import modelo.MochilaModelo;
import vista.MochilaVista;
import modelo.NodoArbol;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MochilaControlador {
    private MochilaVista vista;
    private MochilaModelo modelo;
    
    public MochilaControlador(MochilaVista vista) {
        this.vista = vista;
        this.modelo = null;
        agregarListeners();
    }
    
    private void agregarListeners() {
        vista.getResolverButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resolverProblema();
            }
        });
        
        vista.getMostrarArbolButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarArbol();
            }
        });
    }
    
    private void resolverProblema() {
        try {
            // Leer capacidad
            int capacidad = Integer.parseInt(vista.getCapacidadText().trim());
            if (capacidad <= 0) {
                vista.mostrarError("La capacidad debe ser mayor que 0");
                return;
            }
            
            // Leer elementos
            String[] lineas = vista.getElementosText().split("\n");
            int n = lineas.length;
            
            if (n == 0) {
                vista.mostrarError("Ingrese al menos un elemento");
                return;
            }
            
            if (n > 8) {
                int respuesta = JOptionPane.showConfirmDialog(vista,
                    "Con " + n + " elementos, el árbol tendrá aproximadamente " + 
                    (int)Math.pow(2, n+1) + " nodos.\n" +
                    "¿Desea continuar? (La visualización puede ser compleja)",
                    "Advertencia de Complejidad",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                
                if (respuesta != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
            int[] pesos = new int[n];
            int[] valores = new int[n];
            
            for (int i = 0; i < n; i++) {
                String[] partes = lineas[i].trim().split(",");
                if (partes.length != 2) {
                    vista.mostrarError("Formato incorrecto en línea " + (i+1) + 
                                     "\nUse: peso,valor (ej: 2,3)");
                    return;
                }
                pesos[i] = Integer.parseInt(partes[0].trim());
                valores[i] = Integer.parseInt(partes[1].trim());
                
                if (pesos[i] <= 0 || valores[i] <= 0) {
                    vista.mostrarError("Pesos y valores deben ser positivos (línea " + (i+1) + ")");
                    return;
                }
            }
            
            // Crear modelo y resolver
            modelo = new MochilaModelo(capacidad, pesos, valores);
            
            // Mostrar información inicial
            StringBuilder resultado = new StringBuilder();
            resultado.append("╔══════════════════════════════════════════════════════╗\n");
            resultado.append("║     PROBLEMA DE LA MOCHILA - BACKTRACKING           ║\n");
            resultado.append("╚══════════════════════════════════════════════════════╝\n\n");
            
            resultado.append("═ DATOS DE ENTRADA ═══════════════════════════════════\n");
            resultado.append("Capacidad máxima: ").append(capacidad).append("\n");
            resultado.append("Número de elementos: ").append(n).append("\n\n");
            resultado.append("Elementos disponibles:\n");
            resultado.append("┌────┬───────┬────────┐\n");
            resultado.append("│ ID │ Peso  │ Valor  │\n");
            resultado.append("├────┼───────┼────────┤\n");
            
            for (int i = 0; i < n; i++) {
                resultado.append(String.format("│ %2d │ %5d │ %6d │\n", 
                    i+1, pesos[i], valores[i]));
            }
            resultado.append("└────┴───────┴────────┘\n\n");
            
            resultado.append("═ EJECUTANDO BACKTRACKING ════════════════════════════\n");
            resultado.append("Espacio de búsqueda teórico: 2^").append(n)
                    .append(" = ").append((int)Math.pow(2, n)).append(" combinaciones\n");
            resultado.append("Generando árbol de decisiones...\n");
            
            // Ejecutar backtracking
            long tiempoInicio = System.currentTimeMillis();
            modelo.resolverMochila();
            long tiempoFin = System.currentTimeMillis();
            
            // Obtener estadísticas del árbol
            int totalNodos = modelo.getTotalNodos();
            int nodosPodados = modelo.getNodosPodados();
            int nodosExplorados = totalNodos - nodosPodados;
            
            resultado.append("Árbol generado: ").append(totalNodos).append(" nodos totales\n");
            resultado.append("• Nodos explorados: ").append(nodosExplorados).append("\n");
            resultado.append("• Nodos podados: ").append(nodosPodados).append(" (")
                    .append(String.format("%.1f", (nodosPodados*100.0/totalNodos))).append("%)\n");
            resultado.append("Tiempo de ejecución: ").append(tiempoFin - tiempoInicio)
                    .append(" ms\n\n");
            
            // Mostrar resultados
            resultado.append("═ SOLUCIÓN ÓPTIMA ENCONTRADA ════════════════════════\n");
            resultado.append("Valor máximo: ").append(modelo.getMejorValor()).append("\n");
            resultado.append("Peso utilizado: ").append(modelo.getPesoTotal())
                    .append(" / ").append(capacidad).append("\n");
            
            String elementosSeleccionados = modelo.getElementosSeleccionados();
            resultado.append("Elementos seleccionados: ");
            if (elementosSeleccionados.isEmpty()) {
                resultado.append("(ninguno)\n");
            } else {
                resultado.append("\n  ").append(elementosSeleccionados).append("\n");
            }
            
            resultado.append("\nRepresentación binaria de la solución:\n");
            resultado.append("  [");
            int[] combinacion = modelo.getMejorCombinacion();
            for (int i = 0; i < n; i++) {
                resultado.append(combinacion[i]);
                if (i < n - 1) {
                    resultado.append(" ");
                }
            }
            resultado.append("] (1=incluido, 0=excluido)\n\n");
            
            resultado.append("═ ANÁLISIS DEL ALGORITMO ════════════════════════════\n");
            resultado.append("• Backtracking explora el árbol de decisiones en profundidad\n");
            resultado.append("• Evalúa todas las combinaciones posibles de elementos\n");
            resultado.append("• Poda automática cuando se supera la capacidad (peso > ").append(capacidad).append(")\n");
            resultado.append("• Complejidad teórica: O(2^n) en peor caso\n");
            resultado.append("• Complejidad práctica con poda: O(2^").append(nodosExplorados).append(")\n");
            resultado.append("• Reducción por poda: ").append(nodosPodados).append(" nodos evitados\n\n");
            
            resultado.append("═ ESTRUCTURA DEL ÁRBOL ════════════════════════════\n");
            resultado.append(modelo.getEstructuraArbol());
            
            vista.setResultadoText(resultado.toString());
            vista.habilitarBotonArbol(true);
            
            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(vista,
                "¡Algoritmo ejecutado exitosamente!\n\n" +
                "Se generó un árbol con " + totalNodos + " nodos.\n" +
                "Haga clic en 'Mostrar Árbol' para ver la visualización gráfica.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException ex) {
            vista.mostrarError("Error en formato numérico: " + ex.getMessage());
        } catch (Exception ex) {
            vista.mostrarError("Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void mostrarArbol() {
        if (modelo != null) {
            NodoArbol raiz = modelo.getRaizArbol();
            if (raiz != null) {
                vista.setRaizArbol(raiz);
                JOptionPane.showMessageDialog(vista,
                    "Árbol de decisiones mostrado.\n\n" +
                    "Leyenda:\n" +
                    "★ Nodos verdes: Parte de la solución óptima\n" +
                    "✗ Nodos rojos: Podados (superan capacidad)\n" +
                    "○ Nodos grises: Explorados pero no seleccionados\n\n" +
                    "Las líneas SI/NO representan decisiones de incluir/excluir elementos.",
                    "Leyenda del Árbol",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}