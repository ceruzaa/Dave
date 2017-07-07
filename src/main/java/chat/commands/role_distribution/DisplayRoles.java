package chat.commands.role_distribution;

import chat.commands.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.List;
import java.util.stream.Collectors;

/*
 * Dave replies with a list of his roles that he can distribute with the enroll command
 */
public class DisplayRoles implements Command {
  /**
   * {@inheritDoc}
   */
  @Override
  public void processCommand(IDiscordClient dave, IMessage message) {
    String[] messageContent = message.getContent().split(" ");
    List<IUser> mentionedUsers = message.getMentions();
    if (mentionedUsers.size() > 1) {
      IUser user = mentionedUsers.get(0);
      String rolesString = user.getRolesForGuild(message.getGuild()).stream()
              .map(role -> role.getName().replace('@', ' '))
              .collect(Collectors.joining(", "));

      new MessageBuilder(dave)
              .withChannel(message.getChannel())
              .withContent(user
                      + " has the roles: \n"
                      + rolesString)
              .build();
    } else {
      String rolesString = dave.getRoles().stream()
              .filter(specificRole -> !specificRole.getPermissions().contains(Permissions.ADMINISTRATOR))
              .skip(1)
              .map(o -> o.getName().replace('@', ' '))
              .collect(Collectors.joining(", "));
      new MessageBuilder(dave)
              .withChannel(message.getChannel())
              .withContent("The roles I can distribute are:\n "
                      + rolesString)
              .build();
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