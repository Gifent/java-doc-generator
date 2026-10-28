package student.javadoc; 

/**
 * Выполняет математические операции.
 */
public class Calculator {

    /** Последний результат. */
    private int result;

    /**
     * Складывает два числа.
     * @param a первое число
     * @param b второе число
     * @return сумма чисел
     */
    public int add(int a, int b) {
        result = a + b;
        return result;
    }
}
