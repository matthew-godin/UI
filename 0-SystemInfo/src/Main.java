public class Main {
    public static void main(String[] args) {
        new Main();
    }

    Main() {
        SystemInfo info = new SystemInfo();
        System.out.print(info.toString());
    }
}