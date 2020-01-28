import java.util.ArrayList;
import java.util.List;

public class rename {
    public static void main(String[] args) {
        final String HELP_TEXT = "help text";
        List<String> files = new ArrayList<String>();
        if (args.length == 0) {
            System.out.print(HELP_TEXT);
        }
        else {
            if (args[0] == "-help") {
                System.out.print(HELP_TEXT);
            } else if (args[0] == "-file") {
                if (args.length == 1) {
                    System.out.print(getBaseNumParamsMessage(
                            "file", 1,
                            "at least"));
                } else {
                    int i = 1;
                    while (args[i].charAt(0) != '-') {
                        files.add(args[i]);
                    }
                }
            }
        }
    }

    static String getBaseNumParamsMessage(String option, int numParams,
                                          String numDescription) {
        return "Invalid option: " + option + " requires " + numDescription
                + " " + Integer.toString(numParams) + "argument"
                + (numParams > 1 ? "s" : "") + "to be specified.";
    }
}