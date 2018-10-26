var WebSocketMock = function() {
    this.games = [];

    this.users = [];

    this.onmessage = null;

    this.join = function(message) {
        this.users.push(message.data.player);
        let gameIndex = null;
        let tempGame = null;
        for (let i = 0; i < this.games.length; i++) {
            if(this.games[i].name == message.data.gameName) {
                this.games[i].users.push(message.data.player);
                gameIndex = [i];
                tempGame = this.games[i];
            }
        }

        this.onmessage({
            msg: JSON.stringify({
                data: {
                    success: true,
                    date: new Date(),
                    type: 'join',
                    gameIndex: gameIndex,
                    game: tempGame
                }
            }),
        });
    };

    this.playerMove = function(message) {
        this.games[message.data.gameIndex].state[message.data.rowIndex][message.data.blockIndex].select(message.data.player);
        this.onmessage({
            msg: JSON.stringify({
                data: {
                    success: true,
                    date: new Date(),
                    type: 'playerMove',
                    game: this.games[message.data.gameIndex]
                }
            }),
        });
    }

    this.host = function(message) {
        this.users.push(message.data.player);
        var game = new window.game();
        game.players.push(message.data.player);
        this.games.push(game);

        let gameIndex = null;
        for (let i = 0; i < this.games.length; i++) {
            if (this.games[i].name == game.name) {
                gameIndex = i;
            }
        }

        this.onmessage({
            msg: JSON.stringify({
                data: {
                    success: true,
                    date: new Date(),
                    type: 'host',
                    gameIndex: gameIndex,
                    game: game
                }
            }),
        });
    };

    this.leave = function(message) {
        this.users.splice(this.users.indexOf(message.data.player), 1);

        for (let i = 0; i < this.games.length; i++) {
            let index = this.games[i].players.indexOf(message.data.player);
            if (index > -1) {
                this.games[i].players.splice(index, 1)
            }
        }

        this.onmessage({
            msg: JSON.stringify({
                data: {
                    success: true,
                    date: new Date(),
                    type: 'leave'
                }
            }),
        });
    };

    this.send = function(message) {
        var data = JSON.parse(message);
        switch(data.action) {
            case 'join':
                this.join(data);
                break;
            case 'host':
                this.host(data);
                break;
            case 'leave':
                this.leave(data);
                break;
            case 'playerMove':
                this.playerMove(data);
                break;

        }
    }
}

window.WebSocketMock = WebSocketMock;