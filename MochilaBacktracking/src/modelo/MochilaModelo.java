package modelo;

public class MochilaModelo {
    private int[] pesos;
    private int[] valores;
    private int capacidad;
    private int n;
    private int mejorValor;
    private int[] mejorCombinacion;
    private int[] combinacionActual;
    private NodoArbol raizArbol;
    private NodoArbol[] nodosSolucion; // Para rastrear el camino de la solución
    
    public MochilaModelo(int capacidad, int[] pesos, int[] valores) {
        this.capacidad = capacidad;
        this.pesos = pesos;
        this.valores = valores;
        this.n = pesos.length;
        this.mejorValor = 0;
        this.mejorCombinacion = new int[n];
        this.combinacionActual = new int[n];
        this.nodosSolucion = new NodoArbol[n + 1];
    }
    
    public void resolverMochila() {
        mejorValor = 0;
        // Inicializar arrays
        for (int i = 0; i < n; i++) {
            mejorCombinacion[i] = 0;
            combinacionActual[i] = 0;
            nodosSolucion[i] = null;
        }
        
        // Crear raíz del árbol
        raizArbol = new NodoArbol(0, "Raíz", 0, 0);
        nodosSolucion[0] = raizArbol;
        
        backtracking(0, 0, 0, raizArbol);
    }
    
    private void backtracking(int indice, int pesoActual, int valorActual, NodoArbol nodoActual) {
        // Caso base: hemos considerado todos los elementos
        if (indice == n) {
            if (valorActual > mejorValor) {
                mejorValor = valorActual;
                // Copiar combinacionActual a mejorCombinacion
                for (int i = 0; i < n; i++) {
                    mejorCombinacion[i] = combinacionActual[i];
                }
                // Marcar camino como solución
                marcarCaminoSolucion(nodoActual);
            }
            return;
        }
        
        // Crear nodos hijos para las dos decisiones
        NodoArbol[] hijos = new NodoArbol[2];
        
        // Opción 1: NO incluir el elemento actual
        NodoArbol hijoNo = new NodoArbol(indice + 1, 
            "Excluir E" + (indice+1), 
            pesoActual, 
            valorActual);
        hijos[0] = hijoNo;
        
        // Opción 2: INCLUIR el elemento actual (si cabe)
        NodoArbol hijoSi = null;
        if (pesoActual + pesos[indice] <= capacidad) {
            hijoSi = new NodoArbol(indice + 1,
                "Incluir E" + (indice+1) + " (P:" + pesos[indice] + ", V:" + valores[indice] + ")",
                pesoActual + pesos[indice],
                valorActual + valores[indice]);
            hijos[1] = hijoSi;
        } else {
            // Crear nodo podado
            hijoSi = new NodoArbol(indice + 1,
                "PODADO: Peso excede capacidad",
                pesoActual + pesos[indice],
                valorActual + valores[indice]);
            hijoSi.setPodado(true);
            hijos[1] = hijoSi;
        }
        
        nodoActual.setHijos(hijos);
        
        // Recursión para hijo NO (excluir)
        backtracking(indice + 1, pesoActual, valorActual, hijoNo);
        
        // Recursión para hijo SI (incluir)
        if (pesoActual + pesos[indice] <= capacidad) {
            combinacionActual[indice] = 1;
            backtracking(indice + 1, 
                        pesoActual + pesos[indice], 
                        valorActual + valores[indice], 
                        hijoSi);
            combinacionActual[indice] = 0; // Backtrack
        }
    }
    
    private void marcarCaminoSolucion(NodoArbol nodoFinal) {
        // Marcar todos los nodos en el camino actual como parte de una solución
        // (En una implementación real, necesitaríamos rastrear el camino completo)
        nodoFinal.setSeleccionado(true);
    }
    
    // Métodos getter adicionales para el árbol
    public NodoArbol getRaizArbol() {
        return raizArbol;
    }
    
    public int getTotalNodos() {
        return (raizArbol != null) ? raizArbol.contarNodos() : 0;
    }
    
    public int getNodosPodados() {
        return (raizArbol != null) ? raizArbol.contarNodosPodados() : 0;
    }
    
    public String getEstructuraArbol() {
        if (raizArbol == null) {
            return "Árbol no generado";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Estructura del Árbol de Decisión:\n");
        sb.append("──────────────────────────────────\n");
        imprimirArbol(raizArbol, 0, sb);
        return sb.toString();
    }
    
    private void imprimirArbol(NodoArbol nodo, int profundidad, StringBuilder sb) {
        String indentacion = "  ".repeat(profundidad);
        String simbolo = nodo.isPodado() ? "✗ " : (nodo.isSeleccionado() ? "★ " : "○ ");
        
        sb.append(indentacion)
          .append(simbolo)
          .append("Nivel ").append(nodo.getNivel())
          .append(": ").append(nodo.getDecision())
          .append(" [Peso: ").append(nodo.getPesoAcumulado())
          .append(", Valor: ").append(nodo.getValorAcumulado()).append("]");
        
        if (nodo.isPodado()) {
            sb.append(" (PODADO)");
        }
        if (nodo.isSeleccionado()) {
            sb.append(" (SOLUCIÓN)");
        }
        sb.append("\n");
        
        if (nodo.getHijos() != null) {
            for (NodoArbol hijo : nodo.getHijos()) {
                if (hijo != null) {
                    imprimirArbol(hijo, profundidad + 1, sb);
                }
            }
        }
    }
    
    // Métodos getter originales
    public int getMejorValor() {
        return mejorValor;
    }
    
    public int[] getMejorCombinacion() {
        return mejorCombinacion;
    }
    
    public int getPesoTotal() {
        int pesoTotal = 0;
        for (int i = 0; i < n; i++) {
            if (mejorCombinacion[i] == 1) {
                pesoTotal += pesos[i];
            }
        }
        return pesoTotal;
    }
    
    public String getElementosSeleccionados() {
        StringBuilder sb = new StringBuilder();
        boolean primero = true;
        for (int i = 0; i < n; i++) {
            if (mejorCombinacion[i] == 1) {
                if (!primero) {
                    sb.append(", ");
                }
                sb.append("E").append(i + 1)
                  .append("(P:").append(pesos[i])
                  .append(",V:").append(valores[i]).append(")");
                primero = false;
            }
        }
        return sb.toString();
    }
}