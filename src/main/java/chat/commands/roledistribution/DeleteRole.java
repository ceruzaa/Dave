package chat.commands.roledistribution;

import chat.commands.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import static util.MessageUtils.sendMessage;

/**
 * DeleteRole is a command that allows an admin to remove a role from Dave's role pool.
 * Doing this will remove this role from his pool of roles to distribute.
 */
public class DeleteRole extends Command {

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
        sendMessage(dave, channel, "the syntax for this command is:" + getHelpText());
        return;
      }

      final String roleName = messageContent[2];

      IRole role = dave.getRoles()
              .stream()
              .filter(r -> r.getName().equals(roleName))
              .findFirst()
              .get();

      if (role == null) {
        sendMessage(dave, channel, "I couldn't find the role " + roleName + ".");
      } else if (role.getPermissions().contains(Permissions.ADMINISTRATOR)) {
        sendMessage(dave, channel, "The role " + roleName + " is an admin role, "
                + "which I can not delete.");
      } else{
        dave.getOurUser().removeRole(role);
        sendMessage(dave, channel, "The role " + roleName + " has been removed.");
      }
    } else {
      sendMessage(dave, channel, "Only an admin can run this command!");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getHelpText() {
    return "\n\"@Dave deleterole rolename\"\nRemove a certain role from Dave.\n";
  }
}
