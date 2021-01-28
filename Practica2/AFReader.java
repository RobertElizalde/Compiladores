import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author robert
 */
public class AFReader {
    
    //Lista de objetos Transicion que aloja todas las transiciones leidas
    private List<Transicion> transiciones;
    
    //int asociado al estado inicial
    private int estadoInicial;
    
    //Lista de objetos Integar que almacena todos los estados finales
    private List<Integer> estadosFinales;
    
    //Constantes usadas en diferentes metodos
    private static String INICIAL = "inicial";
    private static String FINALES = "finales";
    
    //Constantes para mensajes de errores
    private static String ERROR_LINEA_INICIAL =  "SYNTAX_ERROR: Linea 'inicial'";
    private static String ERROR_LINEA_FINALES =  "SYNTAX_ERROR: Linea 'finales'";
    
    //Constantes que represetan operadores de separación
    private static String SEPARADOR_ESTADOS = ","; 
    private static String SEPARADOR_PARAMETROS = ":";
    private static String SEPARADOR_TRANSICION = "->";
    
    
    /**
     * Constructor del clase AFReader
     * @param ruta ruta donde se encuentra el archivo AF
     */
    public AFReader (String ruta){
        
        /**Procesamiento de archivo**/
        
        //String para guardar una linea
        String linea;
        
        //Lista de todas las lineas del archivo AF
        List<String> lineas = new ArrayList<>();
        
        //Objeto FileReader para leer el archivo AF
        FileReader fileReader = null;
        try{
            fileReader = new FileReader(ruta);
        }catch(FileNotFoundException e){
            e.printStackTrace();
            System.exit(0);
        }
        
        //Objeto BuferredReader para leer las lineas que se encuentran el archivo Af
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        
        try{
            while((linea = bufferedReader.readLine())!= null){
                lineas.add(linea);
            }
        }catch(IOException e){
            e.printStackTrace();
            System.exit(0);
        }
        
        //Procesa primera linea (estado inicial)
        this.estadoInicial = obtenerEstadoInicialDelArchivo(lineas.get(0));
        
        //Procesa segunda linea (estados finales)
        this.estadosFinales = obtenerEstosFinalesDelArchivo(lineas.get(1));
        
        /*Procesa el resto de las lineas del archivo AF, las cuales contienen las transiciones
         *3 representa la linea donde da comienzo las transiciones
         */
        this.transiciones = obtenerTransicionesDelArchivo(lineas.subList(2, lineas.size()));
    }
    
    /**
     * Metodo para obtener el estado inicial
     * @param linea primera linea leida del archivo AF
     * @return int que representa el entero asociado al estado inicial
     */
    private int obtenerEstadoInicialDelArchivo(String linea){
        //Divide la linea uno en dos elemtentos de la siguiente estrucura -> inicial:estado
        String parametros[] = linea.split(SEPARADOR_PARAMETROS);
        
        //Evalua si el parametro de la derecha corresponde a la palabra 'inicial'
        if (!parametros[0].equals(INICIAL) || parametros.length<2){
            System.out.println(ERROR_LINEA_INICIAL);
            System.exit(0);
        }
        
        // Estado inicial
        int estadoInicial = -1;
        try{
            estadoInicial = Integer.parseInt(parametros[1]);
        }catch(NumberFormatException e){
            e.printStackTrace();
            System.exit(0);
        }
        return estadoInicial;
    }
     
    
    /**
     * Metodo que obtiene los estados finales de la linea 2
     * @param linea
     * @return 
     */
    private List<Integer> obtenerEstosFinalesDelArchivo(String linea) {
        //Divide la linea 2 en dos partes de la siguiente estructura -> finales:estado,estado,...
        String parametros[] = linea.split(SEPARADOR_PARAMETROS);
        
        //Evalua si la palabra "finales" se cuentra y ':' es encontrado
        if (!parametros[0].equals(FINALES) || parametros.length<2){
            System.out.println(ERROR_LINEA_FINALES);
            System.exit(0);
        }
        
        // Divide la segunda parte del primera linea en estados separados por ','
        String parametros2[] = parametros[1].split(SEPARADOR_ESTADOS);
        
        //Lista temporal para guardar estados finales
        List<Integer> temp = new ArrayList<>();
        
        try{
            for (int i = 0; i < parametros2.length; i++) {
                temp.add(Integer.parseInt(parametros2[i]));
            }
        }catch(NumberFormatException e){
            e.printStackTrace();
            System.exit(0);
        }
        
        return temp;
    }
    
    /**
     * Metodo para obtener las transiciones del archivo AF 
     * @param subList list obtenidas del archivo AF que representan las transiciones
     * @return lista de objetos Transicion
     */
    private List<Transicion> obtenerTransicionesDelArchivo(List<String> lineas) {
       
        List<Transicion> temp = new ArrayList<>();
        
        
        String[] parametros, parametros2;
        
        
        int tempEstIni, tempEstFin;
        char tempSymbol;
        
        for (int i = 0; i < lineas.size(); i++) {
            parametros = lineas.get(i).split(SEPARADOR_TRANSICION);
            parametros2 = parametros[1].split(SEPARADOR_ESTADOS);
            
            
            if (!(parametros2.length==2 && parametros.length>1)){
                System.out.println(ERROR_LINEA_FINALES);
                System.exit(0);
            }
            try{
                tempEstIni = Integer.parseInt(parametros[0]);
                tempEstFin = Integer.parseInt(parametros2[0]);
                tempSymbol = parametros2[1].charAt(0); 
                temp.add(new Transicion(tempEstIni, tempEstFin, tempSymbol));
            }catch(NumberFormatException e){
                e.printStackTrace();
                System.exit(0);
            }
            
            
        }
        
        return temp;
    }
    
    /**
     * Metodo para obtener todsa las trasiciones leidas
     * @return 
     */
    public List<Transicion> getTransiciones() {
        return transiciones;
    }

    /**
     * Metodo para obtener el estado inicial
     * @return estado inicial
     */
    public int getEstadoInicial() {
        return estadoInicial;
    }

    /**
     * Metodo para obtener lista de los estado finales
     * @return estados finales
     */
    public List<Integer> getEstadosFinales() {
        return estadosFinales;
    }
    
    
}

