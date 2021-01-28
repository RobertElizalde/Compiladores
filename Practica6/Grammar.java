import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author robert
 */
public class Grammar {
    
    //List of productions tha make up a grammar
    private List<Production> productions;
    
    //constants that help to recognize if a char is terminal, no terminal or epsilon
    private static final int TERMINAL = 0;
    private static final int NO_TERMINAL = 1;
    private static final int EPSILON = 2;
    
    public Grammar(){
        this.productions = new ArrayList();
    }
    
    /**
     * Method to add a production to the grammar
     * @param head head of a production
     * @param body body of a production
     */
    public void addProduction(char head, String body){
        this.productions.add(new Production(head, body));
    }
    
    /**
     * Method to get all productions
     * @return grammar productions
     */
    public List<Production> getProductions(){
        return this.productions;
    }
    
    /**
     * Method that return size of production list, amount of productions on the grammar
     * @return amount of productions on the grammar
     */
    public int getNumOfProductions(){
        return this.productions.size();
    }
    
    /**
     * Method to get specific production saved on the i- index
     * @param i index of the productions list
     * @return a object Production 
     */
    public Production getProduction(int i){
        return this.productions.get(i);
    }
    public String getNoTerminals(){
        StringBuilder heads = new StringBuilder();
        for (int i = 0; i < productions.size(); i++) {
            if(!hasSymbol(productions.get(i).head,heads))
                heads.append(productions.get(i).head);
        }
        return heads.toString();
    }
    
    /**
     * Method to get only the symbols that represent terminals 
     * @return all terminal symbols concatenated
     */
    public String getTerminals(){
        StringBuilder terminals = new StringBuilder();
        
        for (int i = 0; i < productions.size(); i++) {
            String body = productions.get(i).body;
            for (int j = 0; j < body.length(); j++) {
                if(body.charAt(j)>=97 &&  body.charAt(j)<=122){
                    if(!hasSymbol(body.charAt(j), terminals)){
                        terminals.append(body.charAt(j));
                    }
                }
            }
        }
        return terminals.toString();
    }
    
    /**
     * Method that check if a char c is in s
     * @param c caracter to search
     * @param s string where it necessary find c
     * @return true if s contains c, false otherwise
     */
    private boolean hasSymbol(char c, StringBuilder s){
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                return true;
            }
        }
        return false;
    }
}
