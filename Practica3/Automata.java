import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author robert
 */
public abstract class Automata {
    //Lista de objetos Transicion que aloja todas las transiciones leidas
    protected List<Transicion> transiciones;
    
    //int asociado al estado inicial
    protected int estadoInicial;
    
    //Lista de objetos Integar que almacena todos los estados finales
    protected List<Integer> estadosFinales;
    
    /**
     * Metodo para cargar un automata desde una archivo AF
     * @param ruta ruta en donde esta el archivo AF
     */
    public void cargar_desde(String ruta){
        AFReader afReader = new AFReader(ruta);
        this.transiciones = afReader.getTransiciones();
        this.estadoInicial = afReader.getEstadoInicial();
        this.estadosFinales = afReader.getEstadosFinales();
    }
    
    /**
     * Metetodo que añade transiciones al autamata
     * @param inicio int value asociado al estado inicio
     * @param fin int asociado al estado fin de una trasicion
     * @param simbolo simbolo para mover de un estado inicio a un estado fin
     */
    public void agregar_transicion(int inicio, int fin, char simbolo){
        this.transiciones.add(new Transicion(inicio,fin, simbolo));
    }
    
    /**
     * Metodo para eliminar transiciones del automata
     * @param inicio valor int asociado al estado inicio de una trasicion
     * @param fin int int asociado al estado fin de una trasicion
     * @param simbolo simbolo para mover de un estado inicio a un estado fin
     */
    public void eliminar_transicion(int inicio, int fin, char simbolo){
        for (int i = 0; i < transiciones.size(); i++) {
            if (transiciones.get(i).estadoInicio == inicio 
                    && transiciones.get(i).estadoFin == fin
                       && transiciones.get(i).simbolo == simbolo) {
                transiciones.remove(i);
            }
        }
    }
    
    /**
     * Obtiene el estado incial
     * @return estado inicial
     */
    public int obtener_inicial() {
        return estadoInicial;
    }

    /**
     * Obtiene los estados finales
     * @return estados finales
     */
    public List<Integer> obtener_finales() {
        return estadosFinales;
    }
    
    /**
     * Metodo que establece el estado inicial
     * @param estado estado inicial
     */
    public void establecer_inicial(int estado){        
        //Checa si el 'estado' existeene el automata
        for (int i = 0; i < transiciones.size(); i++) {
            if (transiciones.get(i).estadoInicio == estado || transiciones.get(i).estadoFin == estado) {
                this.estadoInicial = estado;
            }
        }
    }
    
    /**
     * Metodo que establece el estado final
     * @param estado estado para establecerlo como final
     * @param unico verdadero si se quiere establecer como unico estado inicial, falso de otro manera.
     */
    public void establecer_final(int estado, boolean unico){
        //Checa si el 'estado' existeene el automata
        boolean estaEnAutomata = false;
        for (int i = 0; i < transiciones.size(); i++) {
            if (transiciones.get(i).estadoInicio == estado || transiciones.get(i).estadoFin == estado) {
                estaEnAutomata = true;
            }
        }
        if(estaEnAutomata){
            if (unico) {
                this.estadosFinales = new ArrayList<>();
                estadosFinales.add(estado);
            }else{
                boolean yaEsFinal = false;
                for (int i = 0; i < estadosFinales.size(); i++) {
                    if (estado == estadosFinales.get(i)) {
                        yaEsFinal = true;
                        break;
                    }
                }
                if (!yaEsFinal) {
                    estadosFinales.add(estado);
                }
            }
        }
    }
    
    /**
     * Metodo que evalua el tipo de automata 
     * @return true si el automata es un  AFD, else si es un AFN
     */
    public boolean esAFD(){
        
        Transicion temp;
        boolean isAFD = true;
        
        for (int i = 0; i < transiciones.size(); i++) {
            
            temp = transiciones.get(i);

            for (int j = i+1; j < transiciones.size(); j++) {
                if(temp.estadoInicio == transiciones.get(j).estadoInicio
                        && temp.simbolo == transiciones.get(j).simbolo){
                    isAFD = false;
                    return isAFD;
                }
            }
        }
        return isAFD;
    }
    
    public boolean esAFN(){
        return !esAFD();
    }
    
    /**
     * Metodo abstracto que determina si la cadena puede ser aceptada por el automata
     * @param cadena string que sera evaluado
     * @return true el cadena es aceptada, false de otra manera
     */
    public abstract boolean acepta(String cadena);
    
    /**
     * Metodo abstracto que genera una cadena altoria por el automata
     * @return string generado por el automata
     */
    public abstract String generarCadena();  
    
    public List<Transicion> getTrans(){
        return this.transiciones;
    }
    
    
    
}
