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
package chat.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

/**
 * Ping is a command command allowing users in discord chat to mention all users
 * in a voice channel and send them a link or a message without having to type
 * out all of their individual names. The new message replaces the old one so
 * the old one is removed to reduce spam.
 */
public class Ping implements Command {

  /**
   * {@inheritDoc}
   */
  @Override
  public void processCommand(IDiscordClient dave, IMessage message) {
    IChannel channel = message.getChannel();
    List<IVoiceChannel> voiceChannels = dave.getVoiceChannels();

    try {
      List<IUser> mentionedUsers = message.getMentions();
      List<IUser> allUsersInVoice = new ArrayList<>();
      String content = "";

      for (IVoiceChannel voiceChannel : voiceChannels) {
        allUsersInVoice.addAll(voiceChannel.getConnectedUsers());
      }

      if (allUsersInVoice.contains(mentionedUsers.get(0))) {
        IVoiceChannel channelToPing = mentionedUsers.get(0)
                .getVoiceStateForGuild(dave.getGuilds().get(0))
                .getChannel();

        List<IUser> usersInChannel = channelToPing.getConnectedUsers();

        if (usersInChannel.contains(message.getAuthor())) {
          usersInChannel.remove(message.getAuthor());
        }

        content += message.getAuthor() + " has sent you something ";

        for (IUser user : usersInChannel) {
          content += user + " ";
        }
        content += "\n";

        String[] messageContent = message.getContent().split(" ");
        for (int i = 3; i < messageContent.length; i++) {
          content += messageContent[i] + " ";
        }
        
        message.delete();
      }
      else {
        content = "Pinging me with no work to do is just plain rude, you know. "
                + "That person is not in voice chat.";
      }

      new MessageBuilder(dave)
              .withChannel(channel)
              .withContent(content)
              .build();
    }
    catch (RateLimitException e) {
      System.err.print("Sending messages too quickly!");
    }
    catch (DiscordException e) {
      System.err.print(e.getErrorMessage());
    }
    catch (MissingPermissionsException e) {
      System.err.print("Missing permissions for channel!");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getHelpText() {
    return "\n\"@Dave ping @personinroom contentyouwishtosend\"\n" +
            "Ping is a command you can use to mention everyone that are in " +
            "a voice room. All you have to do is mention dave with the " +
            "command ping, mention one of the people who are in the room " +
            "and add on the link or message you would like them to see. Dave " +
            "will remove your initial message to avoid spam.\n";
  }

}
