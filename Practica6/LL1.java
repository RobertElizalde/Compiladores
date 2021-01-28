import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author robert
 */
public class LL1 {
    
    // Grammar that is used to generate LL1 table
    private Grammar grammar;
    
    //AuxiliaryFuntions object to allow the calculations of firs() and follow()
    private AuxiliaryFunctions auxFuntions;
    
    //Table LL1 represented with a list of lists
    private List<List<CellLL1>> tableLL1;
    
    /**
     * Constructor of LL1 class
     * @param grammar grammar to process in order to generate a LL1 table
     */
    public LL1(Grammar grammar){
        this.grammar = grammar;
        auxFuntions = new AuxiliaryFunctions(this.grammar);
        makeLL1Table();
    }

    /**
     * Method that make the LL1 table
     */
    private void makeLL1Table() {
        
        tableLL1 = new ArrayList<List<CellLL1>>();
        
        String noTerminals = grammar.getNoTerminals();
        String terminals = grammar.getTerminals() + '$';
        
        for (int i = 0; i < noTerminals.length(); i++) {
            tableLL1.add(new ArrayList<>());
            for (int j = 0; j < terminals.length(); j++) {
                tableLL1.get(i).add(new CellLL1(terminals.charAt(j), noTerminals.charAt(i)));
            }
        }
        
        //Fill LL1 table
        
        for (int i = 0; i < grammar.getNumOfProductions(); i++) {
            Production tempProduction = grammar.getProduction(i);
            String sTemp;
            //Case 1
                        sTemp = auxFuntions.first(tempProduction.body);
            for (int j = 0; j < sTemp.length(); j++) {
                if(sTemp.charAt(j) != 'E'){
                    fillCell(tempProduction, tempProduction.head, sTemp.charAt(j));
                }
            }
            if (auxFuntions.hasEpsilon(sTemp)) {
                sTemp = auxFuntions.follow(tempProduction.head + "");
                for (int j = 0; j < sTemp.length(); j++) {
                    fillCell(tempProduction, tempProduction.head, sTemp.charAt(j));
                }
            }
        }
    }
    
    /**
     * Method to fill a LL1 cell with a production
     * @param production the production to add in a LL1Cell
     * @param noTermi No terminal symbol of the cell that we want to add production
     * @param termi terminal symbol of the cell that we want to add production
     */
    private void fillCell(Production production, char noTermi, char termi){
        for (int i = 0; i < tableLL1.size(); i++) {
            if(tableLL1.get(i).get(0).noTerminal  == noTermi){
                for (int j = 0; j < tableLL1.get(i).size(); j++) {
                    if(tableLL1.get(i).get(j).terminal == termi){
                       tableLL1.get(i).get(j).addProduction(production);
                    }
                }
            }
        }
    }
    
    /**
     * Method to show the final LL1 table
     */
    public void showLL1Table(){
        for (int i = 0; i < tableLL1.size(); i++) {
            for (int j = 0; j < tableLL1.get(i).size(); j++) {
                
                CellLL1 temp = tableLL1.get(i).get(j);
                
                System.out.print("|"+ temp.noTerminal + ", " + temp.terminal + ", ");
                
                if (temp.productions.size()==0)System.out.print("~"); 
                
                for (int k = 0; k < temp.productions.size(); k++) {
                    System.out.print(" " + temp.productions.get(k).head + "->" + temp.productions.get(k).body);
                }
                
                System.out.print("|  ");
            }
            System.out.println("\n");
        }
    }
    
    /**
     * Inner class CellLL1
     */
    private class CellLL1{
        
        // Terminal that represent colomns
        public char terminal;
        
        // No terminals that represent rows
        public char noTerminal;
        
        //Productions on each cell
        public List<Production> productions;
        
        /**
         * Constructor of CellLL1 class
         * @param terminal terminal of the specific cell
         * @param noTerminal  no terminal of the specific cell
         */
        public CellLL1(char terminal , char noTerminal){
            this.terminal = terminal;
            this.noTerminal = noTerminal;
            productions = new ArrayList<>();
        }
       
        /**
         * Method to add a production on a cell of the LL1 table
         * @param production production to add
         */
        public void addProduction(Production production){
            this.productions.add(production);
        }

        
    }
     
    
    
}

