package metro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static MetroGraph graph;

    public static void main(String[] args) {

        try {
            graph = JsonParser.readJson(args[0]);
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Error! Such file doesn't exist!.");
            System.exit(1);
        }
        Scanner scanner = new Scanner(System.in);
        String command;
        do {
             command = scanner.nextLine();
        } while (executeCommand(command));
    }
    private static boolean executeCommand(String command) {
        if (command.matches("/append.*")) {
            Station[] arguments;
            try {
                arguments = parseCommand(command);
            } catch (IOException e) {
                System.out.println("Invalid command");
                return true;
            }
            append(arguments);
        } else if (command.matches("/add.*")) {
            Station[] arguments;
            try {
                arguments = parseCommand(command);
            } catch (IOException e) {
                System.out.println("Invalid command");
                return true;
            }
            addHead(arguments);
        } else if (command.matches("/remove.*")) {
            Station[] arguments;
            try {
                arguments = parseCommand(command);
            } catch (IOException e) {
                System.out.println("Invalid command");
                return true;
            }
            remove(arguments);
        } else if (command.matches("/connect.*")) {
            Station[] arguments;
            try {
                arguments = parseCommand(command);
            } catch (IOException e) {
                System.out.println("Invalid command");
                return true;
            }
            connect(arguments);
        } else if (command.matches("/output.*")) {
            //output();
        } else if (command.matches("/route.*")) {
            Station[] arguments;
            try {
                arguments = parseCommand(command);
            } catch (IOException e) {
                System.out.println("Invalid command");
                return true;
            }
            shortestRoute(arguments);
        } else if (command.matches("/fastest-route.*")) {
            Station[] arguments = new Station[0];
            try {
                arguments = parseCommand(command);
            } catch (IOException e) {
                System.out.println("Invalid command");
                return true;
            }
            fastestRoute(arguments);
        } else if (command.matches("/exit")) {
            return false;
        } else {
            System.out.println("Invalid command");
        }
        return true;
    }

    private static Station[] parseCommand(String command) throws IOException {
        ArrayList<Station> output = new ArrayList<>();
        ArrayList<String> input = new ArrayList<>();

        Matcher matcher = Pattern.compile("\\s+" + RegexUtils.nameInCommandRegex).matcher(command); // \\s+ is in order to not capture command name

        while (matcher.find()) {
            input.add(matcher.group().replaceAll("\"", "").replaceAll("^\\s+", ""));
        }

        if (input.size() % 2 == 1) {
            throw new IOException();
        }

        for (int i = 0; i < input.size() - 1; i += 2) {
            Station s = new Station(input.get(i + 1), input.get(i));
            output.add(s);
        }

        return output.toArray(new Station[0]);
    }

    private static void append(Station[] operands) {
        if (operands.length != 1) {
            System.out.println("Invalid command");
            return;
        }

        // Do stuff
    }

    private static void addHead(Station[] operands) {
        if (operands.length != 1) {
            System.out.println("Invalid command");
            return;
        }

        // Do stuff
    }

    private static void remove(Station[] operands)  {
        if (operands.length != 1) {
            System.out.println("Invalid command");
            return;
        }

        // Do stuff
    }

    private static void connect(Station[] operands) {
        if (operands.length != 2) {
            System.out.println("Invalid command");
            return;
        }

        // Do stuff
    }

    private static void output(String command) {

    }

    private static void fastestRoute(Station[] operands) {
        if (operands.length != 2) {
            System.out.println("Invalid command");
            return;
        }

        String route = graph.route(operands[0], operands[1], true);
        System.out.println(route);
    }

    private static void shortestRoute(Station[] operands) {
        if (operands.length != 2) {
            System.out.println("Invalid command");
            return;
        }

        String route = graph.route(operands[0], operands[1], false);
        System.out.println(route);
    }
}