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

/**
 * Created by Dries on 7/07/2017.
 */
public class Disenroll implements Command{

  @Override
  public void processCommand(IDiscordClient dave, IMessage message) {
    IChannel channel = message.getChannel();
    IUser author = message.getAuthor();
    String[] messageContent = message.getContent().split(" ");

    if (messageContent.length != 3) {
      new MessageBuilder(dave)
              .withChannel(channel)
              .withContent("the syntax for this command is:"
                      + getHelpText())
              .build();
      return;
    }
    else
    {
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
      } else{
        author.removeRole(role);
        new MessageBuilder(dave)
                .withChannel(channel)
                .withContent("The role "
                        + roleName
                        + " has been removed.")
                .build();
      }
    }
  }

  @Override
  public String getHelpText() {
    return "\n\"@Dave disenroll roleName\"\nRemove a role from your own role pool. \n";
  }
}
