// Author: Matthew Godin
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class RenamedFile {
    private String name;
    private boolean error;
    private String errorMessage;

    public RenamedFile(String name) {
        this.name = name;
        this.error = false;
        this.errorMessage = "";
    }

    public RenamedFile(String name, boolean error, String errorMessage) {
        this.name = name;
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public String getName() {
        return name;
    }

    public boolean hasError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

interface RenameOperation {
    RenamedFile getNewFileName(String file);
}

class PrefixRenameOperation implements RenameOperation {
    private String prefix;

    @Override
    public RenamedFile getNewFileName(String file) {
        return new RenamedFile(prefix + file);
    }

    public PrefixRenameOperation(String prefix) {
        this.prefix = prefix;
    }
}

class SuffixRenameOperation implements RenameOperation {
    private String suffix;

    @Override
    public RenamedFile getNewFileName(String file) {
        return new RenamedFile(file + suffix);
    }

    public SuffixRenameOperation(String suffix) {
        this.suffix = suffix;
    }
}

class ReplaceRenameOperation implements RenameOperation {
    private String replaced, replacement;

    @Override
    public RenamedFile getNewFileName(String file) {
        if (file.indexOf(replaced) < 0) {
            return new RenamedFile(file, true,
                    "No instance of \'" + replaced + "\'" +
                            " was found in " +
                            "\'" + file + "\'\n\n");
        } else {
            return new RenamedFile(file.replace(replaced, replacement));
        }
    }

    public ReplaceRenameOperation(String replaced, String replacement) {
        this.replaced = replaced;
        this.replacement = replacement;
    }
}

public class rename {
    static final int FIRST_ARGUMENT = 0;
    static final int NEXT_ARGUMENT = 1;
    static final int SECOND_NEXT_ARGUMENT = 2;
    static final int ARGUMENT_FIRST_INDEX = 0;
    static final String EMPTY_STRING = "";
    static final String OPTION = "option";
    static final char OPTION_PREFIX_CHAR = '-';
    static final String OPTION_PREFIX = String.valueOf(OPTION_PREFIX_CHAR);
    static final String AT_LEAST = "at least";
    static final String EXACTLY = "exactly";
    static final String VALID_SEPARATOR = ", ";
    static final boolean NOT_DONE_PROCESSING_ARGUMENTS = false;
    static final boolean DONE_PROCESSING_ARGUMENTS = true;
    static final boolean DO_RENAME_FILES = true;
    static final boolean DONT_RENAME_FILES = false;
    static final int NO_FILE = 0;
    static final int NO_OPERATION = 0;

    static final String HELP = "help";
    static final String HELP_OPTION = OPTION_PREFIX + HELP;
    static final String HELP_MESSAGE = "(c) 2020 Matthew Godin. " +
            "Last revised: Jan 29, 2020.\n" +
            "  Usage: java rename [-option argument1 argument2 ...]\n" +
            "\n" +
            "  Options:\n" +
            "  -help                   :: display this help and exit.\n" +
            "  -prefix [string]        :: rename the file so that it " +
            "starts with [string].\n  -suffix [string]        :: " +
            "rename the file so that it ends with [string]. \n" +
            "  -replace [str1] [str2]  :: rename [filename] by " +
            "replacing all instances of [str1] with [str2]. \n" +
            "  -file [filename]        :: indicate the [filename] to " +
            "be modified. \n\n";

    static final String PREFIX = "prefix";
    static final String PREFIX_OPTION = OPTION_PREFIX + PREFIX;
    static final String PREFIX_QUANTIFIER_PARAMS = AT_LEAST;
    static final int PREFIX_NUM_PARAMS = 1;

    static final String SUFFIX = "suffix";
    static final String SUFFIX_OPTION = OPTION_PREFIX + SUFFIX;
    static final String SUFFIX_QUANTIFIER_PARAMS = AT_LEAST;
    static final int SUFFIX_NUM_PARAMS = 1;

    static final String REPLACE = "replace";
    static final String REPLACE_OPTION = OPTION_PREFIX + REPLACE;
    static final String REPLACE_QUANTIFIER_PARAMS = EXACTLY;
    static final int REPLACE_NUM_PARAMS = 2;

    static final String FILE = "file";
    static final String FILE_OPTION = OPTION_PREFIX + FILE;
    static final String FILE_QUANTIFIER_PARAMS = AT_LEAST;
    static final int FILE_NUM_PARAMS = 1;

    static final String VALID_OPTIONS = HELP_OPTION + VALID_SEPARATOR
            + PREFIX_OPTION + VALID_SEPARATOR + SUFFIX_OPTION
            + VALID_SEPARATOR + REPLACE_OPTION + VALID_SEPARATOR
            + FILE_OPTION;

    static final char DATE_TIME_SPECIFIER_PREFIX_CHAR = '@';
    static final String DATE_TIME_SPECIFIER_PREFIX = String.valueOf(
            DATE_TIME_SPECIFIER_PREFIX_CHAR);
    static final String DATE = "date";
    static final String DATE_SPECIFIER = DATE_TIME_SPECIFIER_PREFIX
            + DATE;
    static final String TIME = "time";
    static final String TIME_SPECIFIER = DATE_TIME_SPECIFIER_PREFIX
            + TIME;

    public static void main(String[] args) {
        int argumentListIndex = FIRST_ARGUMENT;
        boolean doneProcessingArguments = NOT_DONE_PROCESSING_ARGUMENTS;
        boolean doRenameFiles = DO_RENAME_FILES;
        List<String> files = new ArrayList<String>();
        List<RenameOperation> operations =
                new ArrayList<RenameOperation>();
        if (argumentListIndex == args.length) {
            System.out.print(HELP_MESSAGE);
        }
        else {
            if (!args[argumentListIndex].startsWith(OPTION_PREFIX)) {
                System.out.print(getBaseArgumentBeforeOptionMessage(
                        args[argumentListIndex]
                ));
                doRenameFiles = DONT_RENAME_FILES;
                doneProcessingArguments = DONE_PROCESSING_ARGUMENTS;
            }
            while (!doneProcessingArguments) {
                if (args[argumentListIndex].equals(HELP_OPTION)) {
                    System.out.print(HELP_MESSAGE);
                    doRenameFiles = DONT_RENAME_FILES;
                    break;
                } else if (args[argumentListIndex].equals(FILE_OPTION)) {
                    ++argumentListIndex;
                    if (argumentListIndex == args.length
                            || args[argumentListIndex]
                            .charAt(ARGUMENT_FIRST_INDEX)
                            == OPTION_PREFIX_CHAR) {
                        System.out.print(getBaseNumParamsMessage(
                                FILE, FILE_NUM_PARAMS,
                                FILE_QUANTIFIER_PARAMS));
                        doRenameFiles = DONT_RENAME_FILES;
                        break;
                    } else {
                        do {
                            if (args[argumentListIndex]
                                    .equals(DATE_SPECIFIER)) {
                                files.add(getCurrentDate());
                            } else if (args[argumentListIndex]
                                    .equals(TIME_SPECIFIER)) {
                                files.add(getCurrentTime());
                            }
                            else {
                                files.add(args[argumentListIndex]);
                            }
                            ++argumentListIndex;
                            if (argumentListIndex == args.length) {
                                doneProcessingArguments
                                        = DONE_PROCESSING_ARGUMENTS;
                                break;
                            }
                        } while (args[argumentListIndex]
                                .charAt(ARGUMENT_FIRST_INDEX)
                                != OPTION_PREFIX_CHAR);
                    }
                } else if (args[argumentListIndex].equals(PREFIX_OPTION)) {
                    ++argumentListIndex;
                    if (argumentListIndex == args.length
                            || args[argumentListIndex]
                            .charAt(ARGUMENT_FIRST_INDEX)
                            == OPTION_PREFIX_CHAR) {
                        System.out.print(getBaseNumParamsMessage(
                                PREFIX, PREFIX_NUM_PARAMS,
                                PREFIX_QUANTIFIER_PARAMS));
                        doRenameFiles = DONT_RENAME_FILES;
                        break;
                    } else {
                        String prefix = EMPTY_STRING;
                        do {
                            if (args[argumentListIndex]
                                    .equals(DATE_SPECIFIER)) {
                                prefix += getCurrentDate();
                            } else if (args[argumentListIndex]
                                    .equals(TIME_SPECIFIER)) {
                                prefix += getCurrentTime();
                            } else {
                                prefix += args[argumentListIndex];
                            }
                            ++argumentListIndex;
                            if (argumentListIndex == args.length) {
                                doneProcessingArguments
                                        = DONE_PROCESSING_ARGUMENTS;
                                break;
                            }
                        } while (args[argumentListIndex]
                                .charAt(ARGUMENT_FIRST_INDEX)
                                != OPTION_PREFIX_CHAR);
                        operations.add(new PrefixRenameOperation(prefix));
                    }
                } else if (args[argumentListIndex].equals(SUFFIX_OPTION)) {
                    ++argumentListIndex;
                    if (argumentListIndex == args.length
                            || args[argumentListIndex]
                            .charAt(ARGUMENT_FIRST_INDEX)
                            == OPTION_PREFIX_CHAR) {
                        System.out.print(getBaseNumParamsMessage(
                                SUFFIX, SUFFIX_NUM_PARAMS,
                                SUFFIX_QUANTIFIER_PARAMS));
                        doRenameFiles = DONT_RENAME_FILES;
                        break;
                    } else {
                        String suffix = EMPTY_STRING;
                        do {
                            if (args[argumentListIndex]
                                    .equals(DATE_SPECIFIER)) {
                                suffix += getCurrentDate();
                            } else if (args[argumentListIndex]
                                    .equals(TIME_SPECIFIER)) {
                                suffix += getCurrentTime();
                            } else {
                                suffix += args[argumentListIndex];
                            }
                            ++argumentListIndex;
                            if (argumentListIndex == args.length) {
                                doneProcessingArguments
                                        = DONE_PROCESSING_ARGUMENTS;
                                break;
                            }
                        } while (args[argumentListIndex]
                                .charAt(ARGUMENT_FIRST_INDEX)
                                != OPTION_PREFIX_CHAR);
                        operations.add(new SuffixRenameOperation(suffix));
                    }
                } else if (args[argumentListIndex].equals(REPLACE_OPTION)) {
                    ++argumentListIndex;
                    if (argumentListIndex >= args.length - NEXT_ARGUMENT
                            || args[argumentListIndex]
                            .charAt(ARGUMENT_FIRST_INDEX)
                            == OPTION_PREFIX_CHAR
                            || args[argumentListIndex + NEXT_ARGUMENT]
                            .charAt(ARGUMENT_FIRST_INDEX)
                            == OPTION_PREFIX_CHAR
                            || argumentListIndex + SECOND_NEXT_ARGUMENT
                            < args.length
                            && args[argumentListIndex
                            + SECOND_NEXT_ARGUMENT].charAt(
                                    ARGUMENT_FIRST_INDEX)
                            != OPTION_PREFIX_CHAR) {
                        System.out.print(getBaseNumParamsMessage(
                                REPLACE, REPLACE_NUM_PARAMS,
                                REPLACE_QUANTIFIER_PARAMS));
                        doRenameFiles = DONT_RENAME_FILES;
                        break;
                    } else {
                        String replaced = args[argumentListIndex],
                                replacement = args[argumentListIndex
                                        + NEXT_ARGUMENT];
                        if (args[argumentListIndex].equals(
                                DATE_SPECIFIER)) {
                            replaced = getCurrentDate();
                        } else if (args[argumentListIndex].equals(
                                TIME_SPECIFIER)) {
                            replaced = getCurrentTime();
                        }
                        if (args[argumentListIndex + NEXT_ARGUMENT]
                                .equals(
                                DATE_SPECIFIER)) {
                            replacement = getCurrentDate();
                        } else if (args[argumentListIndex + NEXT_ARGUMENT]
                                .equals(
                                TIME_SPECIFIER)) {
                            replacement = getCurrentTime();
                        }
                        operations.add(
                                new ReplaceRenameOperation(
                                        replaced,
                                        replacement));
                        argumentListIndex += SECOND_NEXT_ARGUMENT;
                        if (argumentListIndex == args.length) {
                            doneProcessingArguments
                                    = DONE_PROCESSING_ARGUMENTS;
                            break;
                        }
                    }
                } else {
                    System.out.print(
                            getBaseInvalidMessage(args[argumentListIndex],
                            OPTION,
                            VALID_OPTIONS));
                    doRenameFiles = DONT_RENAME_FILES;
                    break;
                }
            }
            if (doRenameFiles) {
                if (files.size() == NO_FILE
                        || operations.size() == NO_OPERATION) {
                    System.out.print(HELP_MESSAGE);
                } else {
                    for (String file : files) {
                        try {
                            String newFileName = file;
                            for (RenameOperation operation : operations) {
                                RenamedFile renamedFile
                                        = operation.getNewFileName(
                                        newFileName);
                                if (renamedFile.hasError()) {
                                    System.out.print(
                                            renamedFile.getErrorMessage()
                                    );
                                    doRenameFiles = DONT_RENAME_FILES;
                                    break;
                                } else {
                                    newFileName = renamedFile.getName();
                                }
                            }
                            if (!doRenameFiles) {
                                break;
                            }
                            File oldFileObject = new File(file),
                                    newFileObject = new File(newFileName);
                            if (!oldFileObject.exists()) {
                                System.out.print(
                                        getBaseFileNotFoundMessage(
                                                file
                                        )
                                );
                            } else if (!oldFileObject.canWrite()
                                    || !oldFileObject.canRead()
                                    || System.getProperty("os.name")
                                    .toLowerCase().indexOf("win") >= 0
                                    && !hasRenamePermissionsWindows(file)) {
                                System.out.print(
                                        getBaseNoPermissionForFileMessage(
                                                file
                                        )
                                );
                            } else if (!newFileName.equals(file)
                                    && newFileObject.exists()) {
                                System.out.print(
                                        getBaseFileAlreadyExistsMessage(
                                                newFileName
                                        )
                                );
                            } else {
                                System.out.print(getBaseRenamingMessage(file,
                                        newFileName));
                                if (!oldFileObject.renameTo(newFileObject)) {
                                    System.out.print(
                                            getBaseUnsuccessfulFileRenameMessage(
                                                    file, newFileName
                                            ));
                                } else {
                                    System.out.print(
                                            getBaseSuccessfulFileRenameMessage(
                                                    file, newFileName
                                            ));
                                }
                            }
                        } catch (Exception exception) {
                            System.out.print(
                                    getBaseFileRenameExceptionMessage(
                                            file, exception.toString()
                                    ));
                        }
                    }
                }
            }
        }
    }

    static boolean hasRenamePermissionsWindows(String file) {
        try {
            BufferedReader r =
                    new BufferedReader(
                            new InputStreamReader(
                                    Runtime
                                            .getRuntime()
                                            .exec(
                                                    "icacls " + file)
                                            .getInputStream()));
            String s = null, totalString = "";
            while ((s = r.readLine()) != null) {
                totalString += s + "\n";
            }
            return !(totalString.indexOf("(N)") >= 0);
        } catch (Exception e) {
            System.out.print("An error occurred while checking" +
                    "Windows permissions of \'" + file + "\'");
        }
        return false;
    }

    static String getBaseArgumentBeforeOptionMessage(String argument) {
        return "You can't pass argument \'" + argument + "\' before" +
                " passing an option\n";
    }

    static String getCurrentTime() {
        return (new SimpleDateFormat("hh-mm-ss"))
                .format(new Date());
    }

    static String getCurrentDate() {
        return (new SimpleDateFormat("MM-dd-yyyy"))
                .format(new Date());
    }

    static String getBaseFileAlreadyExistsMessage(String file) {
        return "The file \'" + file + "\' already exists\n\n";
    }

    static String getBaseNoPermissionForFileMessage(String file) {
        return "You do not have appropriate " +
                "file permissions to rename \'" + file + "\'\n\n";
    }

    static String getBaseFileNotFoundMessage(String file) {
        return "The file \'" + file + "\' was not found\n\n";
    }

    static String getBaseRenamingMessage(String oldFile, String newFile) {
        return "Renaming " + oldFile + " to " + newFile + "\n";
    }

    static String getBaseSuccessfulFileRenameMessage(String oldFile,
                                                       String newFile) {
        return "The file " + oldFile + " was renamed to " +
                newFile + " successfully\n\n";
    }

    static String getBaseUnsuccessfulFileRenameMessage(String oldFile,
                                                       String newFile) {
        return "The file \'" + oldFile + "\' couldn't be renamed to \'"
                + newFile + "\'\n\n";
    }

    static String getBaseFileRenameExceptionMessage(String file,
                                                    String exception) {
        return "An error occurred while renaming the file" +
                " \'" + file + "\': " + exception + "\n\n";
    }

    static String getBaseInvalidMessage(String invalid, String type,
                                        String validList) {
        return "\'" + invalid + "\' is an invalid " + type
                + ". Please supply valid " + type
                + "s from the following list: " + validList + "\n\n";
    }

    static String getBaseNumParamsMessage(String option, int numParams,
                                          String numDescription) {
        return "Invalid option: " + option + " requires " + numDescription
                + " " + Integer.toString(numParams) + " argument"
                + (numParams > 1 ? "s" : "") + " to be specified.\n\n";
    }
}