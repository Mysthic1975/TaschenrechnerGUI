import java.util.Stack;

public class ShuntingYard {
    public static Stack<String> parse(String eingabe) {
        Stack<String> ausgabe = new Stack<>();
        Stack<String> operatorStack = new Stack<>();
        String[] tokens = eingabe.split(" ");

        for (String token : tokens) {
            if (isNumber(token)) {
                ausgabe.push(token);
            } else if (isOperator(token)) {
                while (!operatorStack.isEmpty() && isOperator(operatorStack.peek())) {
                    if ((isLeftAssociative(token) && getPrecedence(token) <= getPrecedence(operatorStack.peek()))
                            || (isRightAssociative() && getPrecedence(token) < getPrecedence(operatorStack.peek()))) {
                        ausgabe.push(operatorStack.pop());
                        continue;
                    }
                    break;
                }
                operatorStack.push(token);
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    ausgabe.push(operatorStack.pop());
                }
                if (!operatorStack.isEmpty()) {
                    operatorStack.pop();
                }
            }
        }

        while (!operatorStack.isEmpty()) {
            ausgabe.push(operatorStack.pop());
        }

        // Drehe die Reihenfolge der Tokens
        Stack<String> result = new Stack<>();
        while (!ausgabe.isEmpty()) {
            result.push(ausgabe.pop());
        }

        return result;
    }

    public static boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isOperator(String token) {
        return isLeftAssociative(token);
    }

    private static boolean isLeftAssociative(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    private static boolean isRightAssociative() {
        return false;
    }

    private static int getPrecedence(String token) {
        return switch (token) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            default -> 0;
        };
    }
}
