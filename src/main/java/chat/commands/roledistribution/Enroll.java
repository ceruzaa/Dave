/*
 * The MIT License
 *
 * Copyright 2017 Magnus Aronsson <magnusgoranaronsson@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package chat.commands.roledistribution;

import chat.commands.Command;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.Permissions;

import java.util.List;
import java.util.stream.Collectors;
import static util.MessageUtils.sendMessage;

/**
 * Enroll is a command that allows a user to add one of his roles
 * to the role pool for distribution.
 */
public class Enroll extends Command {
  /**
   * {@inheritDoc}
   */
  @Override
  public void processCommand(IDiscordClient dave, IMessage message) {
    IChannel channel = message.getChannel();
    List<IRole> roles = dave.getRoles();
    String[] messageContent = message.getContent().split(" ");
    String roleName = "";

    if (messageContent[2] != null) {
      roleName = messageContent[2];
    } else {
      sendMessage(dave, channel, "It seems you haven't given me a role \n the syntax for this " 
              + "command is: " + getHelpText());
    }

    for (IRole role : roles) {
      if (role.getName().equalsIgnoreCase(roleName)) {
        if (role.getPermissions().contains(Permissions.ADMINISTRATOR)) {
          String rolesString = roles.stream()
                  .filter(specificRole -> !specificRole.getPermissions()
                          .contains(Permissions.ADMINISTRATOR) )
                  .skip(1)
                  .map(o -> o.getName().replace('@',' '))
                  .collect(Collectors.joining(", "));
          sendMessage(dave, channel, "I don't hand out administrator roles, please pick a different"
                  + " role. \n The roles I can distribute are: \n" + rolesString);
        } else {
          message.getAuthor().addRole(role);
          sendMessage(dave, channel, "I have assigned you the role " + roleName);
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getHelpText() {
    return "\n\"@Dave enroll role\"\nAdd a role to be distributed \n";
  }
}
