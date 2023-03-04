package baloot;

import baloot.command.CommandHandler;
import baloot.command.CommandParser;
import baloot.domain.CommandDTO;
import org.json.JSONObject;

import java.util.Scanner;

public class Application {
    private final CommandHandler commandHandler;

    private final CommandParser commandParser;

    private final Scanner scanner;

    public Application(
        Scanner scanner,
        CommandParser commandParser,
        CommandHandler commandHandler
    ) {
        this.commandHandler = commandHandler;
        this.scanner = scanner;
        this.commandParser = commandParser;
    }

    public void run() {
        while (true) {
            String commandText = this.scanner.nextLine();
            CommandDTO command = this.commandParser.parseCommand(commandText);
            JSONObject response = this.commandHandler.doCommand(command);
            System.out.println(response);
        }
    }
}
