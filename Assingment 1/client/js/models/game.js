window.game = function(game) {
    this.board = [];
    this.influenceCards = game._influenceCards;
    this.inProgress = game._inProgress;
    this.gameName = game.name;
    this.numPlayers = game._userPlayers.length;

    for (let i = 0; i < game._board.length; i++) {
        const row = [];
        
        for (let j = 0; j < game._board[i].length; j++) {
            row.push(new window.block(game._board[i][j]));
        }

        this.board.push(row);
    }

    this.getInfluenceCards = function(playerId) {

    }
}