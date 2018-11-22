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
    public static void main( String[] args ) throws URISyntaxException, InterruptedException
    {
        for (int i = 0; i < 5; i++) {
            Thread.sleep(1000);
            IAgent agent = new RandomAgent();
            agent.run();   
        }   
    }
}
