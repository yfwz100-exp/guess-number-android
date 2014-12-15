package maxon.guessnumber;

import java.util.Random;

/**
 * The expression class.
 *
 * @author yfwz100
 */
public class Expression {

    private IntExprNode variable;
    private ExprNode root;

    public Expression() {
        this(2);
    }

    public Expression(int number) {
        Random random = new Random();
        root = variable = new IntExprNode(1);
        for (int i = 0; i < number; i++) {
            switch (random.nextInt(5)) {
                case 0:
                    root = new PlusExprNode(root, new IntExprNode(random.nextInt(10)));
                    break;
                case 1:
                    root = new MinusExprNode(root, new IntExprNode(random.nextInt(10)));
                    break;
                case 2:
                    root = new MultiplyExprNode(root, new IntExprNode(random.nextInt(10)));
                    break;
                case 3:
                    root = new DivideExprNode(root, new IntExprNode(random.nextInt(10)+1));
                    break;
                case 4:
                    root = new PowerExprNode(root, new IntExprNode(2));
                    break;
            }
        }
    }

    public static Expression generate() {
        return new Expression();
    }

    @Override
    public String toString() {
        return root.toString();
    }

    public int getAt(int i) {
        variable.setValue(i);
        System.out.println(root);
        return root.compute();
    }

    private interface ExprNode {
        int compute();
    }

    protected class IntExprNode implements ExprNode {

        private int value;

        public IntExprNode(int i) {
            value = i;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public int compute() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    protected abstract class AbstractArrayExprNode implements ExprNode {

        protected ExprNode[] nodes;

        public AbstractArrayExprNode(ExprNode... nodes) {
            this.nodes = nodes;
        }

    }

    protected class PlusExprNode extends AbstractArrayExprNode {

        public PlusExprNode(ExprNode... nodes) {
            super(nodes);
        }

        @Override
        public int compute() {
            int result = 0;
            for (ExprNode exprNode : nodes) {
                result += exprNode.compute();
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder("(");
            builder.append(nodes[0].toString());
            for (int i = 1; i < nodes.length; i++) {
                builder.append('+').append(nodes[i].toString());
            }
            builder.append(')');
            return builder.toString();
        }
    }

    protected class MinusExprNode extends AbstractArrayExprNode {

        public MinusExprNode(ExprNode... nodes) {
            super(nodes);
        }

        @Override
        public int compute() {
            int result = nodes[0].compute();
            for (int i = 1; i < nodes.length; i++) {
                result -= nodes[i].compute();
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder("(");
            builder.append(nodes[0].toString());
            for (int i = 1; i < nodes.length; i++) {
                builder.append('-').append(nodes[i].toString());
            }
            builder.append(')');
            return builder.toString();
        }
    }

    protected class MultiplyExprNode extends AbstractArrayExprNode {

        public MultiplyExprNode(ExprNode... nodes) {
            super(nodes);
        }

        @Override
        public int compute() {
            int result = 1;
            for (ExprNode exprNode : nodes) {
                result *= exprNode.compute();
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder("(");
            builder.append(nodes[0].toString());
            for (int i = 1; i < nodes.length; i++) {
                builder.append('*').append(nodes[i].toString());
            }
            builder.append(')');
            return builder.toString();
        }
    }

    protected class DivideExprNode extends AbstractArrayExprNode {

        public DivideExprNode(ExprNode... nodes) {
            super(nodes);
        }

        @Override
        public int compute() {
            int result = nodes[0].compute();
            for (int i = 1; i < nodes.length; i++) {
                result /= nodes[i].compute();
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder("[");
            builder.append(nodes[0].toString());
            for (int i = 1; i < nodes.length; i++) {
                builder.append('/').append(nodes[i].toString());
            }
            builder.append(']');
            return builder.toString();
        }
    }

    protected class PowerExprNode implements ExprNode {

        private ExprNode a;
        private ExprNode b;

        public PowerExprNode(ExprNode a, ExprNode b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public int compute() {
            return ((int) Math.pow(a.compute(), b.compute()));
        }
    }

}
