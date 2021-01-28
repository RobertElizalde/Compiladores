/**
 *
 * @author robert
 */
public class Transicion {
    public int estadoInicio;
    public int estadoFin;
    public char simbolo;
    
    /**
     * Constructor de la clase Transicion
     * @param estadoInicio int asociado al estado inicio
     * @param estadoFin int asociado al estado fin
     * @param simbolo simbolo usado para transitar de un estado a otro
     */
    public Transicion(int estadoInicio, int estadoFin, char  simbolo){
        this.estadoInicio = estadoInicio;
        this.estadoFin = estadoFin;
        this.simbolo = simbolo;
    }
}
