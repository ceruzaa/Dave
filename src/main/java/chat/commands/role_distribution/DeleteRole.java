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

/**
 * DeleteRole is a command that allows an admin to remove a role from Dave's role pool.
 * Doing this will remove this role from his pool of roles to distribute.
 */
public class DeleteRole implements Command {

  /**
   * {@inheritDoc}
   */
  @Override
  public void processCommand(IDiscordClient dave, IMessage message) {
    IChannel channel = message.getChannel();
    IUser author = message.getAuthor();
    IGuild guild = message.getGuild();

    if (isAdministrator(author, guild)) {

      String[] messageContent = message.getContent().split(" ");

      if (messageContent.length != 3) {
        new MessageBuilder(dave)
                .withChannel(channel)
                .withContent("the syntax for this command is:"
                        + getHelpText())
                .build();
        return;
      }

      final String roleName = messageContent[2];

      IRole role = dave.getRoles()
              .stream()
              .filter(r -> r.getName().equals(roleName))
              .findFirst()
              .get();

      if (role == null) {
        new MessageBuilder(dave)
                .withChannel(channel)
                .withContent("I couldn't find the role "
                        + roleName + ".")
                .build();
      } else if (role.getPermissions().contains(Permissions.ADMINISTRATOR)) {
        new MessageBuilder(dave)
                .withChannel(channel)
                .withContent("The role "
                        + roleName
                        + " is an admin role, which I can not delete.")
                .build();
      } else{
        dave.getOurUser().removeRole(role);
        new MessageBuilder(dave)
                .withChannel(channel)
                .withContent("The role "
                        + roleName
                        + " has been removed.")
                .build();
      }
    } else {
      new MessageBuilder(dave)
              .withChannel(channel)
              .withContent("Only an admin can run this command!")
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
    return "\n\"@Dave deleterole rolename\"\nRemove a certain role from Dave.\n";
  }
}
