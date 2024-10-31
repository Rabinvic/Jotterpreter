import java.util.HashMap;
import java.util.ArrayList;

public class SymbolTable {
    // This stores the current function that we are in!
    public static String currentFunc;

    // This just stores <Key, Value> pairs of <Function Name, Type>
    public static HashMap<String, String> funcTypes = new HashMap<String, String>();

    // This just stores <Key, Value> pairs of <Function Name, [Param1's Type, Param2's Type, ..]>
    public static HashMap<String, ArrayList<String>> funcParamTypes = new HashMap<String, ArrayList<String>>();

    // This stores <Key, <Key, Value>> pairs of <Function Name, <Variable Name, Type>>
    public static HashMap<String, HashMap<String,String>> funcTables = new HashMap<String, HashMap<String,String>>();
}
