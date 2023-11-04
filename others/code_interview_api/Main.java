import java.util.Arrays;

public class Main
{
    public static void main(String[] args)
    {
        var multiply = Multiply.multiply(10, 20);
        var sum = Sum.sum(10, 20, 30, 50);
        var pow = Pow.pow(2,16);

        System.out.println("10 x 20 = " + multiply);
        System.out.println("10 + 20 + 30 + 50 = " + sum);
        System.out.println("2^13 = " + pow);
        System.out.println("10 * 2 = " + Multiply.multiplyByTwo(10));
        System.out.println("10 / 2 = " + Divide.divideByTwo(10));
    }

    static class Multiply
    {
        public static int multiply(int a, int b)
        {
            return a * b;
        }

        public static int multiplyByTwo(int a)
        {
            return a << 1;
        }
    }

    static class Divide
    {
        public static int divideByTwo(int number)
        {
            return number >> 1;
        }
    }
    static class Sum
    {
        public static int sum(int... numbers)
        {
            return Arrays.stream(numbers).sum();
        }
    }

    static class Pow
    {
        public static int pow(int op1, int op2)
        {
            return (int) Math.pow(op1, op2);
        }
    }
}