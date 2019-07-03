package calculator.ast;

import calculator.interpreter.Environment;
import calculator.errors.EvaluationError;
import calculator.gui.ImageDrawer;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import misc.exceptions.NotYetImplementedException;

/**
 * All of the public static methods in this class are given the exact same parameters for
 * consistency. You can often ignore some of these parameters when implementing your
 * methods.
 *
 * Some of these methods should be recursive. You may want to consider using public-private
 * pairs in some cases.
 */
public class ExpressionManipulators {
    /**
     * Checks to make sure that the given node is an operation AstNode with the expected
     * name and number of children. Throws an EvaluationError otherwise.
     */
    private static void assertNodeMatches(AstNode node, String expectedName, int expectedNumChildren) {
        if (!node.isOperation()
                && !node.getName().equals(expectedName)
                && node.getChildren().size() != expectedNumChildren) {
            throw new EvaluationError("Node is not valid " + expectedName + " node.");
        }
    }

    /**
     * Accepts an 'toDouble(inner)' AstNode and returns a new node containing the simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'toDouble'.
     * - The 'node' parameter has exactly one child: the AstNode to convert into a double.
     *
     * Postconditions:
     *
     * - Returns a number AstNode containing the computed double.
     *
     * For example, if this method receives the AstNode corresponding to
     * 'toDouble(3 + 4)', this method should return the AstNode corresponding
     * to '7'.
     * 
     * This method is required to handle the following binary operations
     *      +, -, *, /, ^
     *  (addition, subtraction, multiplication, division, and exponentiation, respectively) 
     * and the following unary operations
     *      negate, sin, cos
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if any of the expressions uses an unknown operation.
     */
    public static AstNode handleToDouble(Environment env, AstNode node) {
        // To help you get started, we've implemented this method for you.
        // You should fill in the locations specified by "your code here"
        // in the 'toDoubleHelper' method.
        //
        // If you're not sure why we have a public method calling a private
        // recursive helper method, review your notes from CSE 143 (or the
        // equivalent class you took) about the 'public-private pair' pattern.

        assertNodeMatches(node, "toDouble", 1);
        AstNode exprToConvert = node.getChildren().get(0);
        return new AstNode(toDoubleHelper(env.getVariables(), exprToConvert));
    }

    private static double toDoubleHelper(IDictionary<String, AstNode> variables, AstNode node) {
        if (node.isNumber()) {
            return node.getNumericValue();
        } else if (node.isVariable()) {
            if (variables.containsKey(node.getName())) {
                if (variables.get(node.getName()).isNumber()) {
                    return variables.get(node.getName()).getNumericValue();
                }
                return toDoubleHelper(variables, variables.get(node.getName()));
            }
            throw new EvaluationError("undefined variable1");
        } else {
            return helpOperation(variables, node);
        }
    }

    private static double helpOperation(IDictionary<String, AstNode> variables, AstNode root) {
        int sizes = root.getChildren().size();
        AstNode[] array= new AstNode[2];
        double numberLeft;
        double numberRight;
        int j = 0;
        for (AstNode node: root.getChildren()) {
            array[j] = node;
            j++;
        }
        if (sizes == 0) {
            //should other exception;
            throw new EvaluationError("undefined variable2");
        }
        String now = root.getName();
        if (sizes == 1) {
            numberLeft =toDoubleHelper(variables, array[0]);
            return singleCalculate(now, numberLeft);
        } else if (sizes == 2) {
            numberLeft = toDoubleHelper(variables, array[0]);
            numberRight = toDoubleHelper(variables, array[1]);
            return doubleCalculate(now, numberLeft, numberRight);
            
        } else {
            throw new NotYetImplementedException();
        }
    }
    
    private static double singleCalculate(String operator, double num) {
        if (operator.equals("negate")) {
            return -num;
        } else if (operator.equals("sin")) {
            return Math.sin(num);
        } else if (operator.equals("cos")) {
            return Math.cos(num);
        }
        throw new EvaluationError("Not Defined operator");
    }
    
    private static double doubleCalculate(String operator, double number1, double number2) {
        if (operator.equals("+")) {
            return number1 + number2;
        } else if (operator.equals("-")) {
            return number1 - number2;
        } else if (operator.equals("*")) {
            return number1 * number2;
        } else if (operator.equals("/")) {
            return number1 / number2;
        } else if (operator.equals("^")) {
            return Math.pow(number1, number2);
        }
        throw new EvaluationError("Not Defined operator");
        
    }
    
    /**
     * Accepts a 'simplify(inner)' AstNode and returns a new node containing the simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'simplify'.
     * - The 'node' parameter has exactly one child: the AstNode to simplify
     *
     * Postconditions:
     *
     * - Returns an AstNode containing the simplified inner parameter.
     *
     * For example, if we received the AstNode corresponding to the expression
     * "simplify(3 + 4)", you would return the AstNode corresponding to the
     * number "7".
     *
     * Note: there are many possible simplifications we could implement here,
     * but you are only required to implement a single one: constant folding.
     *
     * That is, whenever you see expressions of the form "NUM + NUM", or
     * "NUM - NUM", or "NUM * NUM", simplify them.
     */
    public static AstNode handleSimplify(Environment env, AstNode node) {
        // Try writing this one on your own!
        // Hint 1: Your code will likely be structured roughly similarly
        //         to your "handleToDouble" method
        // Hint 2: When you're implementing constant folding, you may want
        //         to call your "handleToDouble" method in some way
        // Hint 3: When implementing your private pair, think carefully about
        //         when you should recurse. Do you recurse after simplifying
        //         the current level? Or before?

        //should I condider num + ??? + num
        assertNodeMatches(node, "simplify", 1);
        return findAndDelete(node.getChildren().get(0), env);
    }
    
    private static AstNode findAndDelete(AstNode root, Environment env) {
        int sizes = root.getChildren().size();
        double numberLeft;
        double numberRight;
//        if (sizes > 2 || sizes < 0) {
//            throw new NotYetImplementedException();
//        }
        if (sizes == 0) {
            if (checkDoubleNumber(root, root, env)) {
                if (root.isNumber()) {
                    return new AstNode(toDoubleHelper(env.getVariables(), root)); 
                } else if (root.isVariable()) {
                    return findAndDelete(env.getVariables().get(root.getName()), env);
                }
                throw new NotYetImplementedException();
            }
            
            return root;
        }
        if (sizes == 1) {
            String now = root.getName();
            if (now.equals("sin") || now.equals("cos") || now.equals("negate")) {
                IList<AstNode> childrenNow = new DoubleLinkedList<>();
                childrenNow.add(findAndDelete(root.getChildren().get(0), env));
                return new AstNode(now, childrenNow); 
            }
            return root;
        }
        String now = root.getName();
        if (now.equals("+") || now.equals("-") ||  now.equals("*")) {
            if (checkDoubleNumber(root.getChildren().get(1), root.getChildren().get(0),  env)) {
                numberLeft = toDoubleHelper(env.getVariables(), root.getChildren().get(0));
                numberRight = toDoubleHelper(env.getVariables(), root.getChildren().get(1));
                double result = doubleCalculate(now, numberLeft, numberRight);
                return new AstNode(result);
            }
        }
        IList<AstNode> childrenNow = new DoubleLinkedList<>();
        childrenNow.add( findAndDelete(root.getChildren().get(0), env));
        childrenNow.add( findAndDelete(root.getChildren().get(1), env));
        return new AstNode(root.getName(), childrenNow);
    }
    
    
    private static boolean checkDoubleNumber(AstNode node1, AstNode node2, Environment env) {
        IDictionary<String, AstNode> variable = env.getVariables();
        if (!(node1.isNumber() || node1.isVariable()) || !(node2.isNumber() || node2.isVariable())) {
            return false;
        } else if (node1.isVariable()) {
            if (!variable.containsKey(node1.getName())) {
                return false;
            }
        } else if (node2.isVariable()) {
            if (!variable.containsKey(node2.getName())) {
                return false;
            }
        }
        return true;
    }
    /**
     * Accepts an Environment variable and a 'plot(exprToPlot, var, varMin, varMax, step)'
     * AstNode and generates the corresponding plot on the ImageDrawer attached to the
     * environment. Returns some arbitrary AstNode.
     *
     * Example 1:
     *
     * >>> plot(3 * x, x, 2, 5, 0.5)
     *
     * This method will receive the AstNode corresponding to 'plot(3 * x, x, 2, 5, 0.5)'.
     * Your 'handlePlot' method is then responsible for plotting the equation
     * "3 * x", varying "x" from 2 to 5 in increments of 0.5.
     *
     * In this case, this means you'll be plotting the following points:
     *
     * [(2, 6), (2.5, 7.5), (3, 9), (3.5, 10.5), (4, 12), (4.5, 13.5), (5, 15)]
     *
     * ---
     *
     * Another example: now, we're plotting the quadratic equation "a^2 + 4a + 4"
     * from -10 to 10 in 0.01 increments. In this case, "a" is our "x" variable.
     *
     * >>> c := 4
     * 4
     * >>> step := 0.01
     * 0.01
     * >>> plot(a^2 + c*a + a, a, -10, 10, step)
     *
     * ---
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if varMin > varMax
     * @throws EvaluationError  if 'var' was already defined
     * @throws EvaluationError  if 'step' is zero or negative
     */
    public static AstNode plot(Environment env, AstNode node) {
        assertNodeMatches(node, "plot", 5);
        IDictionary<String, AstNode> variables = env.getVariables();
//        AstNode root;
//        for (int i = 0; i < 4; i++) {
//            root = node.getChildren().get(i);
//            if (root.isVariable()) {
//                if (!variables.containsKey(root.getName())) {
//                    throw new NotYetImplementedException();
//                }
//            }
//        }
        //double var = toDoubleHelper( variables, node.getChildren().get(1));
        double varMin = toDoubleHelper( variables, node.getChildren().get(2));
        double varMax = toDoubleHelper( variables, node.getChildren().get(3));
        double step = toDoubleHelper( variables, node.getChildren().get(4));
        if (varMin > varMax || variables.containsKey(node.getChildren().get(1).getName()) || step <= 0) {
            throw new EvaluationError("404NotFound");
        }
        
        ImageDrawer imageDrawer = env.getImageDrawer();
        IList<Double> xvalues = new DoubleLinkedList<Double>();
        IList<Double> yvalues = new DoubleLinkedList<Double>();
        for (double i = varMin; i <= varMax; i += step) {
            AstNode copy = replaceVar(node.getChildren().get(0), i, node.getChildren().get(1).getName(),env);
            xvalues.add(toDoubleHelper(variables, new AstNode(i)));
            yvalues.add(toDoubleHelper(variables, copy));
        }
        imageDrawer.drawScatterPlot("", "x", "y",xvalues,  yvalues);
        
        return node;
    }
    
    private static AstNode replaceVar(AstNode root, double num, String var, Environment env) {
        int sizes = root.getChildren().size();
        if (sizes == 0) {
            if (root.isNumber()) {
                return new AstNode(toDoubleHelper(env.getVariables(), root)); 
            } else if (root.getName().equals(var)) {
                return new AstNode(num);
            } else if (checkDoubleNumber(root, root, env)) {
                return new AstNode(toDoubleHelper(env.getVariables(),env.getVariables().get(root.getName())));
            }
            throw new EvaluationError("here");
        } else if (sizes == 1) {
            IList<AstNode> childrenNow = new DoubleLinkedList<>();
            childrenNow.add(replaceVar(root.getChildren().get(0), num, var, env));
            return  new AstNode(root.getName(), childrenNow);
        } else if (sizes == 2) {
            IList<AstNode> childrenNow = new DoubleLinkedList<>();
            childrenNow.add(replaceVar(root.getChildren().get(0), num, var, env));
            childrenNow.add(replaceVar(root.getChildren().get(1), num, var, env));
            return new AstNode(root.getName(), childrenNow);
        }
        throw new NotYetImplementedException("here1");
    }
}
