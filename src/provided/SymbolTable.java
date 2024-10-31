package provided;
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

    public static void addFunction(String functionName, String returnType, ArrayList<String> params){
        funcTypes.put(functionName, returnType);
        funcParamTypes.put(functionName, params);
        HashMap<String,String> functionTable = new HashMap<String,String>();
        funcTables.put(functionName, functionTable);

        currentFunc = functionName;
    }

    public static HashMap<String, String> getLocalSymTable(){
        return funcTables.get(currentFunc);
    }

    public static String getFunctionReturn(String functionName){
        return funcTypes.get(functionName);
    }

    public static ArrayList<String> getFunctionParameters(String functionName){
        return funcParamTypes.get(functionName);
    }
}
