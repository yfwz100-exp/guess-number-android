package maxon.guessnumber;

public class ExpressionTest {

    public static void main(String...args) throws Exception {
        Expression expr = Expression.generate();
        for (int i = 1; i < 5; i++) {
            System.out.println(expr.getAt(i));
        }
    }

}