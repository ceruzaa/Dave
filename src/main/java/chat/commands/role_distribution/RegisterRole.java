package chat.commands.role_distribution;

import chat.commands.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

import java.util.EnumSet;
import java.util.Optional;

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

      final String finalRoleName = roleName;
      Optional<IRole> hasRrole = guild.getRoles()
              .stream()
              .filter(r -> r.getName().equalsIgnoreCase(finalRoleName))
              .findFirst();

      IRole role = null;
      if(hasRrole.isPresent()){
        role = hasRrole.get();
      }
      if (role != null) {
        new MessageBuilder(dave)
                .withChannel(channel)
                .withContent("the role "
                        + roleName
                        + " already exists!")
                .build();
        return;
      }

      IRole newRole = guild.createRole();
      newRole.changeName(finalRoleName);
      newRole.changeMentionable(true);
      dave.getOurUser().addRole(newRole);
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
