package chat.commands.roledistribution;

import chat.commands.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import java.util.Optional;
import static util.MessageUtils.sendMessage;

/**
 * A command that creates a new role with no permissions and assigns it to dave.
 */
public class RegisterRole extends Command {

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
        sendMessage(dave, channel, "the syntax for this command is:" + getHelpText()
                + "you appear to be missing the rolename");
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
        dave.getOurUser().addRole(role);
        sendMessage(dave, channel, "the role " + roleName + " has been added to my pool of roles " 
                + "to distribute.");
        return;
      }

      IRole newRole = guild.createRole();
      newRole.changeName(finalRoleName);
      newRole.changeMentionable(true);
      dave.getOurUser().addRole(newRole);
      sendMessage(dave, channel, "the role " + roleName + " has been added!");
    } else {
      sendMessage(dave, channel, "you are not an admin!");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getHelpText() {
    return "\n\"@Dave registerRole name\"\nAdd a role with basic permissions and assign it to Dave \n";
  }
}
