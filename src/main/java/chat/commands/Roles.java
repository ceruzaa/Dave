package chat.commands;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.stream.Collectors;

/**
 * Created by Dries on 3/07/2017.
 */
public class Roles implements Command {
  /**
   * {@inheritDoc}
   */
  @Override
  public void processCommand(IDiscordClient dave, IMessage message) {
    String rolesString = dave.getRoles().stream()
            .filter(specificRole -> !specificRole.getPermissions().contains(Permissions.ADMINISTRATOR))
            .map(o -> o.getName().replace('@', ' '))
            .collect(Collectors.joining(", "));
    new MessageBuilder(dave)
            .withChannel(message.getChannel())
            .withContent("Hello friend, The roles I can distribute are:\n "
                    + rolesString)
            .build();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getHelpText() {
    return "\n\"@Dave roles\"\nDisplay a list of roles that Dave can distribute \n";
  }
}
