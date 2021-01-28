import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para convertir un AFN a un AFD
 * @author robert
 */
public class AFNConvertidor {
    
    //AFN del que se desea obtener su AFD equivalente
    private AFN automaParaConvertir;
    
    //AFD resultante de convertir el AFN
    private AFD automataEquivalente;
    
    private List<List<Integer>> destados;
    private List<Integer> estadosSinMarcar;
    private int idEstadosMarcados;
    
    private List<Character> simbolosDeEntrada;
    
    /**
     * Construtor de la clase AFNConvertidor
     * @param afn AFN al que se desae obtener su AFD equivalente
     * 
     */
    public AFNConvertidor(AFN afn){
        this.automaParaConvertir = afn;
        destados = new ArrayList<List<Integer>>();
        estadosSinMarcar = new ArrayList<>();
        idEstadosMarcados = 0;
        simbolosDeEntrada = obtenerSimbolosEntrada();
        automataEquivalente = new AFD();
    }
    
    /**
     * Metodo para obtener el AFD equivalente
     * @return AFD equivalente 
     */
    public AFD obtenerAFD(){
        construccionSubconjunto();
        return this.automataEquivalente;
    }
    
    
    private void construccionSubconjunto() {
        //Paso 1 e-cerradura(estadoInicial)
        
        destados.add(eCerraduraS(automaParaConvertir.estadoInicial, new ArrayList<>()));
        estadosSinMarcar.add(idEstadosMarcados);
        idEstadosMarcados++;
        
        List<Integer> T, U; 
        while(hayEstadoSinMarcar()){
            T = destados.get(estadosSinMarcar.get(estadosSinMarcar.size()-1));
            System.out.println("T: " + T);
            estadosSinMarcar.remove(estadosSinMarcar.size()-1);
            for (int i = 0; i < simbolosDeEntrada.size(); i++) {
                System.out.println("mover(T"+simbolosDeEntrada.get(i) +"):" + moverTa(T, simbolosDeEntrada.get(i)));
                U = eCerraduraT(moverTa(T, simbolosDeEntrada.get(i)));
                System.out.println("U: " + U);
                if (!estaEnDestados(U)) {
                    destados.add(U);
                    estadosSinMarcar.add(idEstadosMarcados);
                    idEstadosMarcados++;
                }
                dtrans(T,simbolosDeEntrada.get(i),U);
            }
        }
        
        automataEquivalente.establecer_inicial(0);
        int idenEstadoFinal = -1;
        for (int i = 0; i < destados.size(); i++) {
            for (int j = 0; j < destados.get(i).size(); j++) {
                if(destados.get(i).get(j) == automaParaConvertir.estadosFinales.get(0)){
                    automataEquivalente.establecer_final(i, false);
                }
            }
        }
        

    }
    
    
    
    /**
     * Operacion e-cerradura(s)
     * @param s estado del que se le quiere obtener e-cerradura(s)
     * @param estadosCerraduraS estados obtenidos en esa iteración
     * @return lista de estados del AFN a los que se llega desde s  con transiciones E
     */
    private List<Integer> eCerraduraS(int s, List<Integer> estadosCerraduraS){
        estadosCerraduraS.add(s);
        for (int i = 0; i < automaParaConvertir.transiciones.size(); i++) { 
            if(automaParaConvertir.transiciones.get(i).estadoInicio == s
                    && automaParaConvertir.transiciones.get(i).simbolo == 'E'){
                eCerraduraS(automaParaConvertir.transiciones.get(i).estadoFin, estadosCerraduraS);
            }
        }
        return estadosCerraduraS;
    }
    
    /**
     * Metodo para calcular e-cerradura(T)
     * @param T Conjunto de estados T 
     * @return conjunto resultante luego de aplicaar e-cerradura(T)
     */
    private List<Integer> eCerraduraT(List<Integer> T){
        List<Integer> estadosCerraduraT = new ArrayList<>();
        for (int i = 0; i < T.size(); i++) {
            estadosCerraduraT.addAll(eCerraduraS(T.get(i), new ArrayList<>()));
        }
        
        return eliminarRepeticiones(estadosCerraduraT);
    }
    /**
     * Metodo para realizar la operacion mover(T,a)
     * @param T Conjunto de estado a los que se le desea realizar la operacion
     * @param a simbolo por el cual se hace una transicion
     * @return 
     */
    private List<Integer> moverTa(List<Integer> T, char a){
        
        List<Integer> resultado = new ArrayList<>();
        
        for (int i = 0; i < automaParaConvertir.transiciones.size(); i++) {
            for (int j = 0; j < T.size(); j++) {
                if(automaParaConvertir.transiciones.get(i).simbolo == a
                        && automaParaConvertir.transiciones.get(i).estadoInicio==T.get(j)){
                    resultado.add(automaParaConvertir.transiciones.get(i).estadoFin);
                }
            }
        }
        
        return eliminarRepeticiones(resultado);
    }
    
    /**
     * Metedo para eliminar repeticiones de estados en los conjuntos
     * @param estados
     * @return lista con estados sin repeticiones
     */
    private List<Integer> eliminarRepeticiones(List<Integer> estados) {
        for (int i = 0; i < estados.size(); i++) {
            for (int j = i+1; j < estados.size(); j++) {
                if(estados.get(i)==estados.get(j)){
                    estados.remove(j);
                }
            }
        }
        return estados;
    }
    
    /**
     * Metodo que analiza si hay estados sin marcar
     * @return true si hay estados sin marcar, false de lo contrario
     */
    private boolean hayEstadoSinMarcar() {
        if(estadosSinMarcar.size()>0){
            return true;
        }
        return false;
    }
    
    private List<Character> obtenerSimbolosEntrada(){
        List<Character> simbolos = new ArrayList<>();
        
        for (int i = 0; i < automaParaConvertir.transiciones.size(); i++) {
            if(automaParaConvertir.transiciones.get(i).simbolo != 'E')
                simbolos.add(automaParaConvertir.transiciones.get(i).simbolo);
        }
       
        return eliminarRepeticionesSimbolos(simbolos);
    }
    
     /**
     * Metedo para eliminar repeticiones de simbolos
     * @param simbolos
     * @return lista con simbolos sin repeticiones
     */
    private List<Character> eliminarRepeticionesSimbolos(List<Character> simbolos) {
        List<Character> result = new ArrayList<>();
        
        for (int i = 0; i < simbolos.size(); i++) {
            if(!estaEnLista(result, simbolos.get(i))){
                result.add(simbolos.get(i));
            }
        }
        
        return result;
    }
    
    private boolean estaEnLista(List<Character> ls, char c){
        for (int i = 0; i < ls.size(); i++) {
            if(ls.get(i)==c){
                return true;
            }
        }
        return false;
    }

    private boolean estaEnDestados(List<Integer> U) {
        int coincidencias = -1;
        for (int i = 0; i < destados.size(); i++) {
            if (destados.get(i).size() == U.size()) {
                coincidencias = U.size();
                for (int j = 0; j < U.size(); j++) {
                    for (int k = 0; k < destados.get(i).size(); k++) {
                        if (U.get(j) ==destados.get(i).get(k) ) {
                            coincidencias--;
                        }
                    }
                }
                if (coincidencias==0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void dtrans(List<Integer> T, char a, List<Integer> U) {
        automataEquivalente.agregar_transicion(destados.indexOf(T), destados.indexOf(U), a);
    }
    
    public void printAFD(){
        Transicion temp;
        for (int i = 0; i < automataEquivalente.transiciones.size(); i++) {
            temp = automataEquivalente.transiciones.get(i);
            System.out.println(temp.estadoInicio+"->"+ temp.estadoFin+','+temp.simbolo);
        }
        for (int i = 0; i < destados.size(); i++) {
            System.out.println(i + ": "+ destados.get(i));
        }
    }
}
