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
package main;

import chat.DiscordEventManager;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONObject;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;

/**
 * Runs the main application, and is responsible for parsing and handling any
 * configuration.
 */
public class ApplicationStarter {
  /**
   * Runs the application.
   * @param args 
   */
  public static void main(String[] args) {
    try {
      String configContent = readFile("config.json", StandardCharsets.UTF_8);
      JSONObject config = new JSONObject(configContent);
      IDiscordClient dave = createClient(config.getString("token"));
      EventDispatcher dispatcher = dave.getDispatcher();
      dispatcher.registerListener(new DiscordEventManager(dave));
    }
    catch (IOException ex) {
      System.out.println("Configfile not present, aborting.");
      System.exit(2);
    }
  }
   
  /**
   * Reads a file and returns a string constructed based on the charset specified. 
   * 
   * @param path the path to the file
   * @param encoding the charsec encoding
   * @return the constructed string
   * @throws IOException 
   */
  public static String readFile(String path, Charset encoding)
          throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return new String(encoded, encoding);
  }
  
  /**
   * Returns the main Dave object if login is successful. If not, the application
   * 
   * is shut down.
   * @param token
   * @return the discord client
   */
  public static IDiscordClient createClient(String token) {
    ClientBuilder clientBuilder = new ClientBuilder();
    clientBuilder.withToken(token);
    try {
        return clientBuilder.login();
    }
    catch (DiscordException e) {
      System.out.println(e);
      System.exit(2);
      return null;
    }
  }
}
