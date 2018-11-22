var app = new Vue({
    el: '#app',
    data: {
        _socket: null,
        games: []
    },
    created: function() {
        this._socket = new WebSocket("ws://localhost:8080/assignment/game");
        this._socket.onmessage = this.onMessage;
        let self = this;
        this._socket.onopen = function() {
            self._socket.send(self.buildAction(window.MessageType.GAMES_IN_PROGRESS, null));
        }
    },
    methods: {
        onMessage: function(event) {
            let msg = JSON.parse(event.data);
                let data = JSON.parse(msg.data);
                if (data.success) {
                    switch(msg.type) {
                        case window.MessageType.NEW_GAME_IN_PROGRESS:
                            this.games.push(data.game);
                            break;
                        case window.MessageType.GAMES_IN_PROGRESS:
                            this.games = data.games;
                            break;
                    }
                }
        },
        sepctateGame: function(game) {
            window.router.goTo("spectate", { id: game.name });
        },
        buildAction: function(type, data) {
            return JSON.stringify(new window.Message('games', type, data));
        }
    }
});