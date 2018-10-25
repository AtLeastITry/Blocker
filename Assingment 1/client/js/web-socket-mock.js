var WebSocketMock = function() {
    this.games = [
        { 
            name: "game1",
            users: []
        }
    ];

    this.users = [];

    this.onmessage = new function(event) {
        
    }

    this.join = function(message) {
        this.users.push(message.data.username);
        for (let i = 0; i < this.games.length; i++) {
            if(this.games[i].name == message.data.game_name) {
                this.games[i].users.push(message.data.username);
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
        this.users.push(message.data.username);
        this.games.push({
            name: "game2",
            users: [
                message.data.username
            ]
        });

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
        this.users.splice(this.users.indexof(message.data.username), 1);

        for (let i = 0; i < this.games.length; i++) {
            let index = this.games[i].users.indexof(message.data.username);
            if (index > -1) {
                this.games[i].users.splice(index, 1)
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