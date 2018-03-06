import java.util.ArrayList;
import java.util.Random;

public class main {
		
	  static HashTable table17;
	    static HashTable table37;
	    static HashTable tableDups;
	    static HashTable tableRemoved;
	
	public static void main(String[] args) throws Exception  {
		setUp(); 
		
//          Search Tuple Removed 0 
		System.out.println(tableRemoved.search(new Tuple(0, "string0")) == 0); 
        System.out.println(tableRemoved.search(new Tuple(0, "string0")));
        
        
        
//        float x = table37.loadFactor();
//        System.out.println(x);
//        
//        float a = table37.averageLoad(); 
//        System.out.println(a);
//        table37.averageLoad() > 1.77f && table37.averageLoad() < 1.78f
        
        System.out.println(tableDups.search(new Tuple(0, "string0")) == 2);
        System.out.println(tableDups.search(new Tuple(0, "string0")));
	}
	
	
	   public static void setUp() throws Exception {
	        boolean success = true;

	        try {
	            // we can't choose JUnit execution order, so if we want to test add and
	            //   removal, we have to set up a separate case for each stage of add and remove
	            table17 = new HashTable(17);
	            table37 = new HashTable(17);
	            tableDups = new HashTable(17);
	            tableRemoved = new HashTable(17);
	        } catch (Exception e) {
	            System.out.println("SETUP ERROR: couldn't call HashTable constructors");
	            success = false;
	        }

	        try {
	            // setup initial hashtables
	            ArrayList<Tuple> tuplesToAdd = new ArrayList<Tuple>();

	            for (int i = 0; i < 9; i++) {
	                tuplesToAdd.add(new Tuple(11 * i * i + 5 * i, "string" + i));	               
	            }

	            for (Tuple t : tuplesToAdd) {
//	                table17.add(t);
//	                table37.add(t);
//	                tableDups.add(t);
//	                tableRemoved.add(t);	  
	            	
	                table17.add(new Tuple(t.getKey(), t.getValue()));
	                table37.add(new Tuple(t.getKey(), t.getValue()));
	                tableDups.add(new Tuple(t.getKey(), t.getValue()));;
	                tableRemoved.add(new Tuple(t.getKey(), t.getValue()));	 
	            }

	            // force hashtables to double
	            ArrayList<Tuple> tuplesToAdd1 = new ArrayList<Tuple>();

	            for (int i = 9; i < 16; i++) {
	                tuplesToAdd1.add(new Tuple(11 * i * i + 5 * i, "string" + i));
//	   	            table37.add(new Tuple(11 * i * i + 5 * i, "string" + i));
//	   	            tableDups.add(new Tuple(11 * i * i + 5 * i, "string" + i));
//	   	            tableRemoved.add(new Tuple(11 * i * i + 5 * i, "string" + i));
	            }
	            for (Tuple t : tuplesToAdd1) {
	                table37.add(t);
	                tableDups.add(t);
	                tableRemoved.add(t);
	            }
	            
	            
	            
	            
	        } catch (Exception e) {
	            System.out.println("SETUP ERROR: couldn't add to HashTables");
	            success = false;
	        }

	        try {
	            // add duplicate elements
	            tableDups.add(new Tuple(0, "string0"));
	            tableDups.add(new Tuple(16, "string1"));
	            tableDups.add(new Tuple(54, "string2"));

	            tableRemoved.add(new Tuple(0, "string0"));
	            tableRemoved.add(new Tuple(16, "string1"));
	            tableRemoved.add(new Tuple(54, "string2"));
	        } catch (Exception e) {
	            System.out.println("SETUP ERROR: couldn't add duplicates to HashTables");
	            success = false;
	        }

	        try {
	            // remove elements
	            tableRemoved.remove(new Tuple(0, "string0"));
	            tableRemoved.remove(new Tuple(0, "string0"));
	            tableRemoved.remove(new Tuple(114, "string3"));
	            tableRemoved.remove(new Tuple(196, "string4"));
	        } catch (Exception e) {
	            System.out.println("SETUP ERROR: couldn't remove from HashTables");
	            success = false;
	        }

	        // setup efficiency test strings
	        try {
	            Random r = new Random(0l); // always initialized to the same seed for consistency
//
//	            // BFS
//	            StringBuilder sbBFS0 = new StringBuilder("");
//	            StringBuilder sbBFS1 = new StringBuilder("");
//	            for (int i = 0; i < bfsStringLength; i++) {
//	                sbBFS0.append((char) (97 + r.nextInt(26)));
//	                sbBFS1.append((char) (97 + r.nextInt(26)));
//	            }
//
//	            bfsString0 = sbBFS0.toString();
//	            bfsString1 = sbBFS1.toString();
//
//	            // HSS
//	            StringBuilder sbHSS0 = new StringBuilder("");
//	            StringBuilder sbHSS1 = new StringBuilder("");
//	            for (int i = 0; i < hssStringLength; i++) {
//	                sbHSS0.append((char) (97 + r.nextInt(26)));
//	                sbHSS1.append((char) (97 + r.nextInt(26)));
//	            }
//
//	            hssString0 = sbHSS0.toString();
//	            hssString1 = sbHSS1.toString();
//
//	            // HCS
//	            StringBuilder sbHCS0 = new StringBuilder("");
//	            StringBuilder sbHCS1 = new StringBuilder("");
//	            for (int i = 0; i < hcsStringLength; i++) {
//	                sbHCS0.append((char) (97 + r.nextInt(26)));
//	                sbHCS1.append((char) (97 + r.nextInt(26)));
//	            }
//
//	            hcsString0 = sbHCS0.toString();
//	            hcsString1 = sbHCS1.toString();


	        } catch (Exception e) {
	            System.out.println("SETUP ERROR: couldn't setup strings for similarity efficiency tests");
	            success = false;
	        }

	    }
	
	
	
}
