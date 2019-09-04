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
    computed: {
        gameSummary: function() {
            var result = []
            if (this.game) {
                for (let i = 0; i < this.game.board.length; i++) {
                    const row = this.game.board[i];
                    for (let j = 0; j < row.length; j++) {
                        let column = row[j];
                        if (column.playerId == 0) {
                            continue;
                        }
    
                        let found = false;
    
                        for (let h = 0; h < result.length; h++) {
                            let item = result[h];
                            if (item.id == column.playerId) {
                                item.score = item.score + 1;
                                found = true;
                                break;
                            }
                        }
    
                        if (result < 1 || !found) {
                            result.push({
                                id: column.playerId,
                                name: 'player ' + column.playerId,
                                score: 1
                            });
                        }
                    }
                }
                
                result.sort((a, b) => {
                    if (a.score == b.score) {
                        return b.id - a.id;
                    }
                    else if(a.score > b.score) {
                        return -1
                    }
                    else if(b.score > a.score) {
                        return 1
                    }

                    return 0
                });
            }

            return result;
        },
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
                            window.gameTest = new window.gameModel(data.game);
                            break;
                    }
                }
        },
        buildAction: function(type, data) {
            return JSON.stringify(new window.Message('spectator', type, data));
        }
    }
});