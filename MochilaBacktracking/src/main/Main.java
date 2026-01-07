package main;

import vista.MochilaVista;
import controlador.MochilaControlador;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Ejecutar en el Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                crearYMostrarGUI();
            }
        });
    }
    
    private static void crearYMostrarGUI() {
        // Crear el marco principal
        JFrame frame = new JFrame("Problema de la Mochila - Backtracking con Visualización de Árbol");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 750);
        
        // Crear la vista (JPanel)
        MochilaVista vista = new MochilaVista();
        
        // Crear el controlador
        new MochilaControlador(vista);
        
        // Añadir la vista al frame
        frame.add(vista);
        
        // Centrar en pantalla
        frame.setLocationRelativeTo(null);
        
        // Hacer visible
        frame.setVisible(true);
        
        // Mostrar mensaje de inicio
        JOptionPane.showMessageDialog(frame,
            "Bienvenido al Solucionador del Problema de la Mochila\n\n" +
            "Este programa implementa backtracking con visualización del árbol de decisiones.\n\n" +
            "Características:\n" +
            "1. Resolución del problema de la mochila\n" +
            "2. Generación y visualización del árbol de búsqueda\n" +
            "3. Poda automática de ramas inviables\n" +
            "4. Estadísticas detalladas del algoritmo\n\n" +
            "Recomendación: Use hasta 6-7 elementos para una visualización clara.",
            "Instrucciones",
            JOptionPane.INFORMATION_MESSAGE);
    }
}