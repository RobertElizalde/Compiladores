import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author robert
 */
public class AuxiliaryFunctions {
    
    //Grammar to calculate first() and follow(funtions)
    private Grammar grammar;
    
    private static final int TERMINAL = 0;
    private static final int NO_TERMINAL = 1;
    private static final int EPSILON = 2;
    
    public AuxiliaryFunctions(Grammar grammar){
        this.grammar = grammar;
    }
    
    /**
     * Method that return the result of calculate first(symbol)
     * @param symbol symbol to calculate first
     * @return result of first calculation
     */
    public String first(String symbol){
        return first(symbol, new ArrayList<>());
    }
    
    /**
     * Method that return the result of calculate first(symbol)
     * @param symbol symbol to calculate first
     * @param alreadyProcessed list of already processed no terminals symbols
     * @return result of first calculation
     */
    public String first(String symbol, List<Character> alreadyProcessed){
       
        StringBuilder temp = new StringBuilder();
        char c;
        for (int i = 0; i < symbol.length(); i++) {
            c = symbol.charAt(i);
            switch(getType(c)){
                case TERMINAL:
                    temp = new StringBuilder().append(c);
                    
                    i = symbol.length();//Termina for
                    break;
                case NO_TERMINAL:
                    for (int j = 0; i < grammar.getNumOfProductions(); i++) {
                        if (grammar.getProduction(i).head == c) {
                            if(!isAlreadyProcessed(c, alreadyProcessed)){
                                alreadyProcessed.add(c);
                                temp.append(first(grammar.getProduction(i).body,alreadyProcessed));
                                alreadyProcessed.remove(alreadyProcessed.size()-1);
                            }
                        }                     
                    }
                    break;
                case EPSILON:
                    temp.append(c);
                    break;
                default:
                    //Error code, invalid symbol
                    break;
                    
            }
            
        }
        
        //Remove repetitions
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < temp.length(); i++) {
            if(!isCharFound(temp.charAt(i), result)){
                result.append(temp.charAt(i));
            }
        }
        return result.toString();
    }
    
    /**
     * Method that check if there is a c char on b
     * @param c char to find
     * @param b string where it will be find the c char
     * @return true if c is in s, false otherwise
     */
    public boolean isCharFound(char c, StringBuilder b){
        for (int i = 0; i < b.length(); i++) {
            if (b.charAt(i)==c) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Method that determine if a symbol is terminal, no terminal or epsilon
     * @param symbol symbol to determinate
     * @return type of symbol according to the constants NO_TERMINAL, TERMINAL, EPSILON 
     */
    private int getType(char symbol){
        int charValue = symbol, type = -1;
        if (charValue>=65 && charValue<=90){
            if(symbol != 'E'){
                type = NO_TERMINAL;
            }
            else{
                type = EPSILON;
            }
        }else{
            if(charValue>=97 && charValue<=122){
                type = TERMINAL;
            }
        }
        return type;
    }

    /**
     * Method that check if a symbols no terminal was already process by a firs() or follow function
     * @param c Not terminal symbol to check
     * @param alreadyProcessed list of already processed symbols
     * @return true if was already processed, false otherwise
     */
    private boolean isAlreadyProcessed(char c, List<Character> alreadyProcessed) {
        for (int i = 0; i < alreadyProcessed.size(); i++) {
            if(alreadyProcessed.get(i)== c)
                return true;
        }
        return false;
    }
    
    /**
     * Method to calculate follow
     * @param symbol symbol for calculate follow
     * @return result of calculating follow
     */
    public String follow(String symbol){
        return follow(symbol, new ArrayList<>());
    }
    public String follow(String symbol , List<Character> alP){        
        
        char c = symbol.charAt(0);
        
        
        
        StringBuilder result = new StringBuilder();
        
        if (isInitial(c)) {
            result.append('$');
        }
        
        //Search in al productions
        // (1) A -> alfaX     -> follow(A)
        // (2) A -> alfaXbeta -> first(beta)
        // (3) A -> alfaXbeta -> if first(beta) has E then, add siguienteA
        
        
        Production pTemp;
        for (int i = 0; i < grammar.getNumOfProductions(); i++) {
            pTemp = grammar.getProduction(i);
            
            //Case 1  A -> alfaX     -> siguiente(A)
            //X is at the end of the production's body
            if(pTemp.body.charAt(pTemp.body.length()-1) == c){
                if(!isAlreadyProcessed(pTemp.head, alP)){
                    alP.add(pTemp.head);
                    result.append(follow(pTemp.head + "",alP));
                    alP.remove(alP.size()-1);
                }
            }
            
            //Case 2 A -> alfaXbeta -> primero(beta)
            //X is in the middle of the production's body
            //if the length of the array that is got from String.split(X) is 2
            //then, X is in the middle of to strings(alpha, beta)
            
            //Case 3 (Special) A -> alfaXbeta -> si primero(beta) contiene a E entonces add siguienteA
            //In the case A -> Xbeta, E is considered as alpha
            //and it has to be at the begening of the string
            String alpha0Beta1[] = pTemp.body.split(c+"");
            
            
            if(alpha0Beta1.length == 2 || (pTemp.body.charAt(0)==c && pTemp.body.length()>1)){
                String sTemp =  "";
                if(alpha0Beta1.length == 2){
                    sTemp = first(alpha0Beta1[1], new ArrayList());;
                    
                }else{
                    if (pTemp.body.charAt(0)==c && pTemp.body.length()>1) {
                        StringBuilder temp1 = new StringBuilder();
                        for (int j = 1; j < pTemp.body.length(); j++) {
                             temp1.append(pTemp.body.charAt(j));
                        }
                        sTemp = first(temp1.toString(), new ArrayList());;
                    }
                }
                
                result.append(sTemp);
                if (hasEpsilon(sTemp)) {
                    if(!isAlreadyProcessed(pTemp.head, alP)){
                        alP.add(pTemp.head);
                        result.append(follow(pTemp.head + "" , alP));
                        alP.remove(alP.size()-1);
                    }
                }
            }
            
        }
        
        StringBuilder resultfinal = new StringBuilder();
        
        for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i)!= 'E' && !isCharFound(result.charAt(i), resultfinal)) {
                resultfinal.append(result.charAt(i));
            }
        }
        return resultfinal.toString();
    }
    
    /**
     * Method to check if a String contains 'E' char
     * @param s string where 'E' will be searched
     * @return 
     */
    public boolean hasEpsilon(String s){
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'E') {
                return true;
            }
        }
        return false;
    } 

    /**
     * Method that determine if c is initial
     * @param c char to check
     * @return true if c is initial, false otherwise
     */
    private boolean isInitial(char c) {
        if (grammar.getProduction(0).head == c) {
            return true;
        }
        return false;
    }
}
