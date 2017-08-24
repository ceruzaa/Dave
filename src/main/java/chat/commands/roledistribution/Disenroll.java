package chat.commands.roledistribution;

import chat.commands.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import static util.MessageUtils.sendMessage;

/**
 * Allows a user to unsubscribe from a specific role.
 */
public class Disenroll extends Command {

  /**
   * {@inheritDoc}
   */
  @Override
  public void processCommand(IDiscordClient dave, IMessage message) {
    IChannel channel = message.getChannel();
    IUser author = message.getAuthor();
    String[] messageContent = message.getContent().split(" ");

    if (messageContent.length != 3) {
      sendMessage(dave, channel, "the syntax for this command is:" + getHelpText());
    } else {
      final String roleName = messageContent[2];
      IRole role = dave.getRoles()
              .stream()
              .filter(r -> r.getName().equals(roleName))
              .findFirst()
              .get();

      if (role == null) {
        sendMessage(dave, channel, "I couldn't find the role " + roleName + ".");
      } else {
        author.removeRole(role);
        sendMessage(dave, channel, "You have been unassigned the role: " + roleName);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getHelpText() {
    return "\n\"@Dave disenroll roleName\"\nRemove a role from your own role pool. \n";
  }
}
