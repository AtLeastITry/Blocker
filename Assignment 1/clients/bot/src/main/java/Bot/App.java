package Bot;

import java.net.URISyntaxException;

import Bot.ai.IAgent;
import Bot.ai.RandomAgent;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws URISyntaxException
    {
        for (int i = 0; i < 10; i++) {
            IAgent agent = new RandomAgent();

            agent.run();
        }
    }
}
