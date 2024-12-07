# Jotterpreter
 Group 14 Jott Interpreter PLC Project
 Phase 2: all of our node files that make up our parse tree are in the src/nodes folder.
 call the parse function in JottParser to build a parse tree.
 
 Phase 3: We created a SymbolTable.java that is located inside of the provided folder.
 Additionally, we decorated our parse tree with validateTree() to ensure semantic correctness,
 and worked out the Jott.java file to tokenize, parse, and decorate.
 
 Also, we added our own batch files located in the main project directory that can be run:
    On Windows: ./phase3tester.bat
    On Unix/Mac: ./phase3tester.sh
 These will run all test .jott files for this phase, if you would like to use them.

 Phase 4: We added some more hashmaps to SymbolTable.java to help with scoping, initializing
 variables, and dealing with values. Jott.java now executes the program node which kicks off
 the execution of the entire decorated parse tree. We also added the reallyLong.jott file
 found on myCourses to the root directory for testing.