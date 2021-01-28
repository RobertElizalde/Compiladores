import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author robert
 */
public class Thompson {
    
    private static final int OPERADOR = 0;
    private static final int LETRA = 1;
    
    private int identificadorEstado;
    
    public Thompson(){
        identificadorEstado = 1;
    }
   
    public AFN convertir(String cadena){
        return procesar(cadena);
    }
    private AFN procesar(String cadena){
        List<AFN> bloques = new ArrayList<>();
        int bloqueActual = 0;
        if (cadena.length()>1){
            
            int indiceProcesado, i, parentesis, j;
            char c;
            StringBuilder cadenaAProcesar = new StringBuilder();
            StringBuilder temp;
            for (i = 0; i < cadena.length(); i++) {
                c = cadena.charAt(i);
                switch(detectarCaracter(c)){
                    case LETRA:
                        //cadenaAProcesar.append(c);
                        bloques.add(procesar(new String("" + c)));
                        
                        break;
                    case OPERADOR:
                        switch(c){
                            case '(':
                                parentesis = 0;
                                temp = new StringBuilder();
                                for (j = i+1; j < cadena.length(); j++) {
                                    if (cadena.charAt(j)=='(') {
                                        parentesis++;
                                    }
                                    else {
                                        if (cadena.charAt(j)==')'){
                                            if(parentesis==0) {
                                                bloques.add(procesar(temp.toString())); //AFN procesado lo que esta dentro de parenstesis
                                                bloqueActual++;
                                                break;
                                            }else{
                                                temp.append(cadena.charAt(j));
                                                parentesis--;
                                            }
                                        }
                                        else{
                                            temp.append(cadena.charAt(j));
                                        }
                                    }
                                }
                                i = j;
                                break;
                            case '*':
                                //Tiene dos casos para que el operador funcione 
                                // 'letra'*
                                // ')'*   
                                // Los demas casos son ignorados
                                // En ambos casos la estrucura seria 
                                // Son bloques
                                AFN tempAFN = cerradura(bloques.get(bloques.size()-1));
                                bloques.set(bloques.size()-1, tempAFN);
                                System.out.println("Bloque size: " + bloques.size());
                                break;
                            case '|':
                                if(cadena.charAt(i+1)=='('){
                                    parentesis = 0;
                                    temp = new StringBuilder();
                                    for (j = i+1; j < cadena.length(); j++) {
                                        if (cadena.charAt(j)=='(') {
                                            parentesis++;
                                        }
                                        else {
                                            if (cadena.charAt(j)==')'){
                                                if(parentesis==0) {
                                                    //bloques.add(procesar(temp.toString())); //AFN procesado lo que esta dentro de parenstesis
                                                    break;
                                                }else{
                                                    temp.append(cadena.charAt(j));
                                                    parentesis--;
                                                }
                                            }
                                            else{
                                                temp.append(cadena.charAt(j));
                                            }
                                        }
                                    }
                                    bloques.set(bloques.size()-1, union(bloques.get(bloques.size()-1), procesar(temp.toString())));
                                    i = j;
                                }else{
                                    if (detectarCaracter(cadena.charAt(i+1))==LETRA) {
                                        AFN temp2;
                                        if((i+2)<cadena.length()){
                                            if(cadena.charAt(i+2)=='*'){
                                                temp2 = cerradura(procesar("" + cadena.charAt(i+1)));
                                                i+=2;
                                            }else{
                                                temp2 = procesar("" + cadena.charAt(i+1));
                                                i++;
                                            }
                                        }else{
                                            temp2 = procesar("" + cadena.charAt(i+1));
                                            i++;
                                        }
                                        
                                        bloques.set(bloques.size()-1, union(bloques.get(bloques.size()-1),temp2));
                                    }
                                    
                                }
                                break;    
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        else{
            //Generar automata
            bloques.add(generarAFN(cadena.charAt(0)));
        }
        
        //Concatenar todo
        AFN finalAFN = bloques.get(0);
        if(bloques.size()>1){
            for (int i = 1; i < bloques.size(); i++) {
                finalAFN = concatenacion(finalAFN, bloques.get(i));
            }
        }
        return finalAFN;
    }
    
    private AFN union(AFN bloqueA, AFN bloqueB){
        AFN tempAFN = new AFN();
        
        //Agregar transiciones de bloqueA
        List<Transicion> temp = bloqueA.getTrans();
        for (int i = 0; i < temp.size(); i++) {
            tempAFN.agregar_transicion(temp.get(i)); 
        }
        
        //Agregar transiciones de bloqueB
        temp = bloqueB.getTrans();
        for (int i = 0; i < temp.size(); i++) {
            tempAFN.agregar_transicion(temp.get(i)); 
        }
        
        //Agrega la transicion del nuevo estado inicial al estado incial de bloqueA
        tempAFN.agregar_transicion(identificadorEstado,bloqueA.estadoInicial, 'E');
        //Agrega la transicion del nuevo estado inicial al estado incial de bloqueB
        tempAFN.agregar_transicion(identificadorEstado,bloqueB.estadoInicial, 'E');
        
        tempAFN.establecer_inicial(identificadorEstado);
        
        //Agrega la transicion del estado final de bloqueA a el nuevo estado final
        tempAFN.agregar_transicion(bloqueA.estadosFinales.get(0), identificadorEstado+1, 'E');
        //Agrega la transicion del estado final de bloque a el nuevo estado final
        tempAFN.agregar_transicion(bloqueB.estadosFinales.get(0), identificadorEstado+1, 'E');
        
        tempAFN.establecer_final(identificadorEstado+1, true);
        
        //Incrementamos en 2 el identificadorEstado por los nuevos estados agregados
        identificadorEstado += 2; 
        return tempAFN;
    }
    private AFN concatenacion(AFN bloqueA, AFN bloqueB){
        AFN tempAFN = new AFN();
        
        //Agregar transiciones de bloqueA
        List<Transicion> temp = bloqueA.getTrans();
        for (int i = 0; i < temp.size(); i++) {
            tempAFN.agregar_transicion(temp.get(i)); 
        }
        
        //Agregar transiciones de bloqueB
        temp = bloqueB.getTrans();
        for (int i = 0; i < temp.size(); i++) {
            tempAFN.agregar_transicion(temp.get(i)); 
        }
        
        //Unir estado final de bloqueA y el estado inicial de bloqueB como unico 
        //estado, usando el identificador del estad final del bloqueA como el
        //identificador de estado resultante
        tempAFN.modificarEstado(bloqueA.estadosFinales.get(0), bloqueB.estadoInicial);
        //Establece el estado inicial de bloqueA como incial general
        tempAFN.establecer_inicial(bloqueA.estadoInicial);
        //Establece el estado final de bloqueB como final general
        tempAFN.establecer_final(bloqueB.estadosFinales.get(0), true);
        
        return tempAFN;
        
    }
    
    private AFN cerradura(AFN bloque){
        AFN tempAFN = new AFN();
        
        //Agregar transiciones de bloqueA
        List<Transicion> temp = bloque.getTrans();
        for (int i = 0; i < temp.size(); i++) {
            tempAFN.agregar_transicion(temp.get(i)); 
        }
        
        //Crea la transicion del estado final al inicial atraves de epsilon
        tempAFN.agregar_transicion(bloque.estadosFinales.get(0), bloque.estadoInicial, 'E');
        
        //Agrega la transicion del nuevo estado inicial al estado incial de bloque
        tempAFN.agregar_transicion(identificadorEstado,bloque.estadoInicial, 'E');
        tempAFN.establecer_inicial(identificadorEstado);
        
        //Agrega la transicion del estado final de bloque a el nuevo estado final
        tempAFN.agregar_transicion(bloque.estadosFinales.get(0), identificadorEstado+1, 'E');
        tempAFN.establecer_final(identificadorEstado+1, true);
        
        //Agrega la transicion del estado final al estado inicial
        tempAFN.agregar_transicion(tempAFN.estadoInicial, tempAFN.estadosFinales.get(0), 'E');
        
        //Incrementamos en 2 el identificadorEstado por los nuevos estados agregados
        identificadorEstado += 2; 
        return tempAFN;
    }
    private AFN union(String a, String b){
        return null;
    }
    private AFN cerradura(String a, String b){
        return null;
    }
    private AFN parentesis(String cadena){
        return null;
    }
    private AFN generarAFN(char simbolo){
        AFN tempAFN = new AFN();
        //Agregamos las transiciones y establecemos estado inicio y final
        tempAFN.agregar_transicion(identificadorEstado, identificadorEstado+1, simbolo);
        tempAFN.establecer_inicial(identificadorEstado);
        tempAFN.establecer_final(identificadorEstado+1, true);
        System.out.println("inicio -> " + tempAFN.estadoInicial);
        System.out.println("final -> " + tempAFN.estadosFinales.get(0));
        //Incrementamos en 2 pues esta usada ya identificadorEstado y identificadorEstado+1
        identificadorEstado = identificadorEstado + 2;
        System.out.println("identificadorEstado: " + identificadorEstado);
        return tempAFN;
        
    }
    private int detectarCaracter(char c){
        int charValue = c;
        if (charValue>= 97 &&charValue<= 122) {
            return LETRA;
        }
        else{
            return OPERADOR;
        }
    }
}
