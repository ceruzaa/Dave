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
package chat;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import static util.MessageUtils.sendMessage;

/**
 * This class is used to setup handlers for various events that are broadcast
 * from the Discord API. 
 */
public class DiscordEventManager {

  private final IDiscordClient _dave;

  public DiscordEventManager(IDiscordClient dave) {
    this._dave = dave;
  }

  @EventSubscriber
  /**
   * Do anything you want to have done on startup.
   */
  public void onReadyEvent(ReadyEvent event) {
    System.out.println("Client ready.");
  }

  @EventSubscriber
  /**
   * Handle chat commands
   */
  public void onMessageReceivedEvent(MessageReceivedEvent event) {
    IMessage message = event.getMessage();

    if (message.getMentions().indexOf(_dave.getOurUser()) != -1) {
      String commandMessage = message.getContent().split(" ")[1];
      String commandMessageAddition = "";

      if (message.getContent().split(" ").length >= 3) {
        commandMessageAddition = message.getContent().split(" ")[2];
      }

      for (Commands command : Commands.values()) {
        if (command.equals(commandMessage)) {
          if (commandMessageAddition.equals("help")) {
            sendMessage(_dave, message.getChannel(),
                command.getHandler().getHelpText());
          } else {
             command.getHandler().processCommand(_dave, message);
          }
        }
      }
    }
  }
}
