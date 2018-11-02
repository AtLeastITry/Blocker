window.gameModel = function(game) {
    var self = this;

    this.board = [];
    this.influenceCards = game._influenceCards;
    this.inProgress = game._inProgress;
    this.gameName = game.name;
    this.numPlayers = game._userPlayers.length;
    this.playerTurn = game._playerTurn;
    this.players = game._userPlayers;
    this.finished = game._gameFinished;

    for (let i = 0; i < game._board.length; i++) {
        const row = [];
        
        for (let j = 0; j < game._board[i].length; j++) {
            row.push(new window.block(game._board[i][j]));
        }

        this.board.push(row);
    }

    this.getInfluenceCards = function(playerId) {
        let result = self.influenceCards[playerId].map(card => {
            return new window.cardModel(card);
        });

        return result;
    }
}