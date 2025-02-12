public class Main {

    public static void main(String[] args) {
        SimpleJdbcExample test = new SimpleJdbcExample();

        test.runSimpleStatement();
        test.runSimpleInsert();
        test.runSimplePreparedStatement();
    }
}
