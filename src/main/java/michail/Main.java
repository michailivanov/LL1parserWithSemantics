package michail;

public class Main {
    public static void main(String[] args) {
        ParsingTable table = new ParsingTable();
        Parser parser = new Parser(table);
        String inputStr = "bcaaaa";
        System.out.println("Input: " + inputStr);
        parser.parse(inputStr);
    }
}
