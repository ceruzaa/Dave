package chat.commands.roleDistribution;

import chat.commands.Command;
import com.sun.org.apache.bcel.internal.generic.IUSHR;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.MessageBuilder;

import java.security.Permission;
import java.util.EnumSet;

/**
 * A command that creates a new role with no permissions and assigns it to dave.
 */
public class RegisterRole implements Command {

  /**
   * {@inheritDoc}
   */
  @Override
  public void processCommand(IDiscordClient dave, IMessage message) {
    IChannel channel = message.getChannel();
    IUser author = message.getAuthor();
    IGuild guild = message.getGuild();
    if (isAdministrator(author, guild)) {
      String roleName = "";
      String[] messageContent = message.getContent().split(" ");

      if (messageContent.length < 3) {
        new MessageBuilder(dave)
                .withChannel(channel)
                .withContent("the syntax for this command is:"
                        + getHelpText()
                        + "you appear to be missing the rolename")
                .build();
        return;
      }

      for (int i = 2; i < messageContent.length; i++) {
        roleName += messageContent[i];
      }

      for (IRole role : guild.getRoles()) {
        if (role.getName().equals(roleName)) {
          new MessageBuilder(dave)
                  .withChannel(channel)
                  .withContent("the role "
                          + roleName
                          + " already exists!")
                  .build();
          return;
        }
      }

      IRole role = guild.createRole();
      role.changeName(roleName);
      role.changeMentionable(true);
      dave.getOurUser().addRole(role);
      new MessageBuilder(dave)
              .withChannel(channel)
              .withContent("the role "
                      + roleName
                      + " has been added!")
              .build();
    } else {
      new MessageBuilder(dave)
              .withChannel(channel)
              .withContent("you are not an admin!")
              .build();
    }
  }

  private boolean isAdministrator(IUser user, IGuild guild) {
    EnumSet<Permissions> permissions = user.getPermissionsForGuild(guild);
    return permissions.contains(Permissions.ADMINISTRATOR);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getHelpText() {
    return "\n\"@Dave registerRole name\"\nAdd a role with basic permissions and assign it to Dave \n";
  }
}
