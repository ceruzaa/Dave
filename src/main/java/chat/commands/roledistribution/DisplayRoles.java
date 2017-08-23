package chat.commands.roledistribution;

import chat.commands.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.List;
import java.util.stream.Collectors;
import sx.blah.discord.handle.obj.IChannel;
import static util.MessageUtils.sendMessage;

/*
 * Dave replies with a list of his roles that he can distribute with the enroll command
 */
public class DisplayRoles extends Command {
  /**
   * {@inheritDoc}
   */
  @Override
  public void processCommand(IDiscordClient dave, IMessage message) {
    String[] messageContent = message.getContent().split(" ");
    IChannel channel = message.getChannel();
    List<IUser> mentionedUsers = message.getMentions();
    if (mentionedUsers.size() > 1) {
      IUser user = mentionedUsers.get(0);
      String rolesString = user.getRolesForGuild(message.getGuild()).stream()
              .map(role -> role.getName().replace('@', ' '))
              .collect(Collectors.joining(", "));

      sendMessage(dave, channel, user + " has the roles: \n" + rolesString);
    } else {
      String rolesString = dave.getRoles().stream()
              .filter(specificRole -> !specificRole.getPermissions().contains(Permissions.ADMINISTRATOR))
              .skip(1)
              .map(o -> o.getName().replace('@', ' '))
              .collect(Collectors.joining(", "));
      sendMessage(dave, channel, "The roles I can distribute are:\n " + rolesString);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getHelpText() {
    return "\n\"@Dave roles\"\nDisplay a list of roles that Dave can distribute \n";
  }
}
