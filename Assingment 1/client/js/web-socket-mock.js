var WebSocketMock = function() {
    this.games = [];

    this.users = [];

    this.onmessage = null;

    this.join = function(message) {
        this.users.push(message.data.player);
        for (let i = 0; i < this.games.length; i++) {
            if(this.games[i].name == message.data.gameName) {
                this.games[i].users.push(message.data.player);
            }
        }

        this.onmessage({
            msg: JSON.stringify({
                data: {
                    success: true,
                    date: new Date(),
                    type: 'join'
                }
            }),
        });
    };

    this.host = function(message) {
        this.users.push(message.data.player);
        var game = new window.game();
        game.players.push(message.data.player);
        this.games.push(game);

        this.onmessage({
            msg: JSON.stringify({
                data: {
                    success: true,
                    date: new Date(),
                    type: 'host'
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

        }
    }
}

window.WebSocketMock = WebSocketMock;