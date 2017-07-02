package chat.commands;

import com.sun.org.apache.bcel.internal.generic.IUSHR;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.MessageBuilder;

import java.security.Permission;
import java.util.EnumSet;

/**
 * A command that creates a new role with no permissions and assigns dave with it.
 */
public class RegisterServer implements Command{

    /**
     * {@inheritDoc}
     */
    @Override
    public void processCommand(IDiscordClient dave, IMessage message) {
        IChannel channel = message.getChannel();
        IUser author = message.getAuthor();
        IGuild guild = message.getGuild();
        if(isAdministrator(author, guild))
        {
            String roleName = "";
            String[] messageContent = message.getContent().split(" ");

            if(messageContent.length < 3)
            {
                new MessageBuilder(dave)
                        .withChannel(channel)
                        .withContent("Hello friend, the syntax for this command is:\n "
                            + "@Dave registerServer rolename\n"
                            + "you appear to be missing the rolename")
                        .build();
                return;
            }

            for (int i = 2; i < messageContent.length; i++) {
                roleName += messageContent[i];
            }

            System.out.println("registering server: " + roleName);
            for(IRole role : guild.getRoles())
            {
                System.out.println(role.getName());
                if(role.getName().equals(roleName))
                {
                    new MessageBuilder(dave)
                            .withChannel(channel)
                            .withContent("Hello friend, the role "
                                    + roleName
                                    + " already exists!")
                            .build();
                    return;
                }
            }
            IRole role = guild.createRole();
            role.changeName(roleName);
            role.changeMentionable(true);
            new MessageBuilder(dave)
                    .withChannel(channel)
                    .withContent("Hello friend, the role "
                            + roleName
                            + " has been added!")
                    .build();
        }
        else
        {
            new MessageBuilder(dave)
                    .withChannel(channel)
                    .withContent("Hello friend, you are not an admin!")
                    .build();
        }
    }

    private boolean isAdministrator(IUser user, IGuild guild)
    {
        EnumSet<Permissions> permissions = user.getPermissionsForGuild(guild);
        return permissions.contains(Permissions.ADMINISTRATOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHelpText() { return "\n\"@Dave register name\"\nAdd a role with basic permissions and assign it to Dave \n"; }
}
