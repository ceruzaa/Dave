package chat.commands.roleDistribution;

import chat.commands.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.MessageBuilder;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by Dries on 5/07/2017.
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
                        + roleName
                        + ".")
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
                        + " has been removed from my roles to distribute.")
                .build();
      }
    } else {
      new MessageBuilder(dave)
              .withChannel(channel)
              .withContent("This command requires admin permission to run!")
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
