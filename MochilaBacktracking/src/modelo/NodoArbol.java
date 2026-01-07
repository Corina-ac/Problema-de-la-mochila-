package modelo;

public class NodoArbol {
    private int nivel;
    private String decision;
    private int pesoAcumulado;
    private int valorAcumulado;
    private NodoArbol[] hijos;
    private boolean podado;
    private boolean seleccionado;
    
    public NodoArbol(int nivel, String decision, int pesoAcumulado, int valorAcumulado) {
        this.nivel = nivel;
        this.decision = decision;
        this.pesoAcumulado = pesoAcumulado;
        this.valorAcumulado = valorAcumulado;
        this.hijos = null;
        this.podado = false;
        this.seleccionado = false;
    }
    
    // Getters y setters
    public int getNivel() { return nivel; }
    public String getDecision() { return decision; }
    public int getPesoAcumulado() { return pesoAcumulado; }
    public int getValorAcumulado() { return valorAcumulado; }
    public NodoArbol[] getHijos() { return hijos; }
    public boolean isPodado() { return podado; }
    public boolean isSeleccionado() { return seleccionado; }
    
    public void setHijos(NodoArbol[] hijos) { this.hijos = hijos; }
    public void setPodado(boolean podado) { this.podado = podado; }
    public void setSeleccionado(boolean seleccionado) { this.seleccionado = seleccionado; }
    
    public int contarNodos() {
        int count = 1;
        if (hijos != null) {
            for (NodoArbol hijo : hijos) {
                if (hijo != null) {
                    count += hijo.contarNodos();
                }
            }
        }
        return count;
    }
    
    public int contarNodosPodados() {
        int count = (podado) ? 1 : 0;
        if (hijos != null) {
            for (NodoArbol hijo : hijos) {
                if (hijo != null) {
                    count += hijo.contarNodosPodados();
                }
            }
        }
        return count;
    }
}