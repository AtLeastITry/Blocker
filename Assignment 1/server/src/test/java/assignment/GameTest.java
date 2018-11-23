package assignment;

import assignment.models.*;
import assignment.services.MoveValidator;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class GameTest 
    extends TestCase
{
    public GameTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( GameTest.class );
    }

    public void testFreedomCardOnOpenBlock()
    {
        Player player = new Player();
        GameState game = new GameState("test");
        game.addPlayer("1", player);

        game.updateBoard(new Coordinates(0, 5), player.getMyPlayerId());

        Move move = new Move(InfluenceCard.FREEDOM, new Coordinates(5, 5), null);
        MoveValidator validator = new MoveValidator(game);
        boolean isMoveAllowed = validator.validate(move, player.getMyPlayerId());
        Assert.assertTrue(isMoveAllowed);
    }

    public void testFreedomCardOnTakenBlock()
    {
        Player player1 = new Player();
        Player player2 = new Player();
        GameState game = new GameState("test");
        game.addPlayer("1", player1);
        game.addPlayer("2", player2);

        game.updateBoard(new Coordinates(0, 5), player1.getMyPlayerId());
        game.updateBoard(new Coordinates(3, 5), player2.getMyPlayerId());

        Move move = new Move(InfluenceCard.FREEDOM, new Coordinates(0, 5), null);

        MoveValidator validator = new MoveValidator(game);
        boolean isMoveAllowed = validator.validate(move, player2.getMyPlayerId());
        Assert.assertFalse(isMoveAllowed);
    }

    public void testReplacementCardOnTakenBlock()
    {
        Player player1 = new Player();
        Player player2 = new Player();
        GameState game = new GameState("test");
        game.addPlayer("1", player1);
        game.addPlayer("2", player2);

        game.updateBoard(new Coordinates(0, 5), player1.getMyPlayerId());
        game.updateBoard(new Coordinates(1, 5), player2.getMyPlayerId());

        Move move = new Move(InfluenceCard.REPLACEMENT, new Coordinates(0, 5), null);

        MoveValidator validator = new MoveValidator(game);
        boolean isMoveAllowed = validator.validate(move, player2.getMyPlayerId());
        Assert.assertTrue(isMoveAllowed);
    }

    public void testDoubleCardOnOpenBlocks()
    {
        Player player1 = new Player();
        GameState game = new GameState("test");
        game.addPlayer("1", player1);

        game.updateBoard(new Coordinates(0, 5), player1.getMyPlayerId());

        Move move = new Move(InfluenceCard.DOUBLE, new Coordinates(1, 5), new Coordinates(2, 5));

        MoveValidator validator = new MoveValidator(game);
        boolean isMoveAllowed = validator.validate(move, player1.getMyPlayerId());
        Assert.assertTrue(isMoveAllowed);
    }

    public void testNoCardOnOpenBlocks()
    {
        Player player1 = new Player();
        GameState game = new GameState("test");
        game.addPlayer("1", player1);

        game.updateBoard(new Coordinates(0, 5), player1.getMyPlayerId());

        Move move = new Move(null, new Coordinates(1, 5), null);

        MoveValidator validator = new MoveValidator(game);
        boolean isMoveAllowed = validator.validate(move, player1.getMyPlayerId());
        Assert.assertTrue(isMoveAllowed);
    }
    
    public void testNoCardOnTakenBlock()
    {
        Player player1 = new Player();
        Player player2 = new Player();
        GameState game = new GameState("test");
        game.addPlayer("1", player1);
        game.addPlayer("2", player2);

        game.updateBoard(new Coordinates(0, 5), player1.getMyPlayerId());
        game.updateBoard(new Coordinates(1, 5), player2.getMyPlayerId());

        Move move = new Move(null, new Coordinates(0, 5), null);

        MoveValidator validator = new MoveValidator(game);
        boolean isMoveAllowed = validator.validate(move, player2.getMyPlayerId());
        Assert.assertFalse(isMoveAllowed);
    }
}
