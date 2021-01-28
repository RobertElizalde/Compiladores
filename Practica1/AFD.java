import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author robert
 */
public class AFD extends Automata{

    @Override
    public boolean acepta(String cadena) {
        int estadoActual = this.estadoInicial;
        for (int i = 0; i < cadena.length(); i++) {
            estadoActual = evaluaSimbolo(cadena.charAt(i),estadoActual);
            
            /*if estadoActual = -1, then, the state can be pass move to the
              next state by consuming the current symbol, the string is not
              accepted*/
            if (estadoActual==-1) {
                return false;
            }
        }
        //Check if after the end of the for, the estadoActual is part of the final states
        for (int i = 0; i < this.estadosFinales.size(); i++) {
            if(this.estadosFinales.get(i)==estadoActual)return true;
        }
        return false;
    }

    @Override
    public String generarCadena() {
        StringBuilder cadena = new StringBuilder("");
        int estadoActual = this.estadoInicial;
        int randomInt = (int)(Math.random() * (100 - 10 + 1) + 10);
        int randTransicion = 0;
        List<Transicion> temp = new ArrayList<>();
        for (int i = 0; i < randomInt; i++) {
            for (int j = 0; j < transiciones.size(); j++) {
                if(estadoActual == transiciones.get(j).estadoInicio){
                    temp.add(transiciones.get(j));
                }
            }
            if (temp.size()<1) {
                break;
            }
            randTransicion = (int)(Math.random() * (temp.size()));
            estadoActual = temp.get(randTransicion).estadoFin;            
            cadena.append(temp.get(randTransicion).simbolo);
            temp.clear();
        }
        return cadena.toString();
    }
    
    
    private int evaluaSimbolo(char simbolo, int estadoActual){
        int siguienteEstado = -1;
        Transicion temp;
        for (int i = 0; i < this.transiciones.size(); i++) {
            if (transiciones.get(i).estadoInicio == estadoActual
                    && transiciones.get(i).simbolo == simbolo) {
                siguienteEstado = transiciones.get(i).estadoFin;
            }
        }
        return siguienteEstado;
    }
    
}
