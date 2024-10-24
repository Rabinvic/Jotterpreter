import java.util.HashMap;

public class SymbolTable {
    // This just stores <Key, Value> pairs of <Function Name, Type>
    public static HashMap<String, String> funcTypes = new HashMap<String, String>();

    // This stores <Key, <Key, Value>> pairs of <Function Name, <Variable Name, Type>>
    public static HashMap<String, HashMap<String,String>> funcTables = new HashMap<String, HashMap<String,String>>();
}
