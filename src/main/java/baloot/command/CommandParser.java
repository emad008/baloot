package baloot.command;

import baloot.domain.CommandDTO;

public class CommandParser {
    public CommandDTO parseCommand(String commandText) {
        int spaceIndex = commandText.indexOf(" ");

        if (spaceIndex == -1)
            return new CommandDTO(
                commandText,
                null
            );

        return new CommandDTO(
            commandText.substring(0, spaceIndex),
            commandText.substring(spaceIndex + 1)
        );
    }
}
