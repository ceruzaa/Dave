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

import chat.commands.*;
import chat.commands.roleDistribution.DeleteRole;
import chat.commands.roleDistribution.Enroll;
import chat.commands.roleDistribution.RegisterRole;
import chat.commands.roleDistribution.Roles;

/**
 * Factory for all commands that Dave supports.
 */
public enum Commands {
  PING("ping", new Ping()), 
  HELLO("hello", new Hello()),
  HELP("help", new Help()),
  ENROLL("enroll", new Enroll()),
  REGISTERROLE("registerRole", new RegisterRole()),
  ROLES("roles", new Roles()),
  DELETEROLE("deleterole", new DeleteRole());
  
  private final String _command;
  private final Command _commandHandler;
  
  /**
   * 
   * @param command the string to match against the command
   * @param commandHandler the class implementing the command interface
   */
  private Commands(String command, Command commandHandler) {
    this._command = command;
    this._commandHandler = commandHandler;
  }
  
  /**
   * @param command the text represenation of the command
   * @return true or false if the command matches 
   */
  public boolean equals (String command) {
    return _command.equalsIgnoreCase(command);
  } 
  
  /**
   * @return a command object
   */
  public Command getHandler() {
    return _commandHandler;
  }
}
