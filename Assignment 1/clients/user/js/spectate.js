var app = new Vue({
    el: '#app',
    data: {
        _socket: null,
        gameName: null,
        game: null
    },
    created: function() {
        this._socket = new WebSocket("ws://localhost:8080/assignment/game");
        this._socket.onmessage = this.onMessage;
        let self = this;
        this.gameName = window.router.getParam('id');
        this._socket.onopen = function() {
            self._socket.send(self.buildAction(window.MessageType.SPECTATE_GAME, { gameName: self.gameName }));
        }
    },
    methods: {
        onMessage: function(event) {
            let msg = JSON.parse(event.data);
                let data = JSON.parse(msg.data);
                if (data.success) {
                    switch(msg.type) {
                        case window.MessageType.PLAYER_MOVE:
                        case window.MessageType.SPECTATE_GAME:
                            this.game = new window.gameModel(data.game);
                            break;
                    }
                }
        },
        buildAction: function(type, data) {
            return JSON.stringify(new window.Message('spectator', type, data));
        }
    }
});