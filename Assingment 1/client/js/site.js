var app = new Vue({
    el: '#app',
    data: {
        playing: false,
        _socket: null,
        username: null,
        showJoinOptions: false,
        game: null,
        player: null,
        avaliableGameNames: [],
        chosenGame: null,
        chosenCoordinates: []
    },
    created: function() {
        this._socket = new WebSocket("ws://localhost:8080/assignment/game");
        this._socket.onmessage = this.onMessage;
        let self = this;
        this._socket.onopen = function() {
            self._socket.send(self.buildAction(window.MessageType.ALL_GAMES, null));
        }
    },
    methods: {
        selectBlock: function(rowIndex, blockIndex) {
            if (this.chosenCoordinates.length > 0 && this.chosenCoordinates[0].x == rowIndex && this.chosenCoordinates[0].y == blockIndex) {
                this.chosenCoordinates = [];
                this.game.board[rowIndex][blockIndex].newPlayerId = null;
                return;
            }

            if (this.chosenCoordinates.length > 0) {
                return;
            }

            let move = new window.move(null, new window.coordinates(rowIndex, blockIndex));
            let checkMoveRequest = new window.checkMoveRequest(this.game.gameName, move, this.player.id);
            this._socket.send(this.buildAction(window.MessageType.CHECK_MOVE, checkMoveRequest));
            
        },
        submitTurn: function() {
            firstMove = this.chosenCoordinates[0];
            let move = new window.move(null, firstMove, secondMove);
            let playerMoveRequest = new window.playerMoveRequest(this.game.gameName, move);
            this._socket.send(this.buildAction(window.MessageType.PLAYER_MOVE, playerMoveRequest)) 
        },
        onMessage: function(event) {
            let msg = JSON.parse(event.data);
            let data = JSON.parse(msg.data);
            if (data.success) {
                switch(msg.type) {
                    case window.MessageType.HOST: 
                        this.game = new window.game(data.game);
                        this.player = new window.player(this.username, data.playerId);
                        this.playing = true;
                        break;
                    case window.MessageType.JOIN: 
                        this.game = new window.game(data.game);
                        this.playing = true;
                        this.player = new window.player(this.username, data.playerId);
                        this.showJoinOptions = false;
                        break;
                    case window.MessageType.LEAVE: 
                        this.playing = false;
                        break;
                    case window.MessageType.PLAYER_MOVE:
                    case window.MessageType.START:
                        this.game = new window.game(data.game);
                        break;
                    case window.MessageType.NEW_GAME:
                        this.avaliableGameNames.push(data.gameName);
                        break;
                    case window.MessageType.ALL_GAMES:
                        this.avaliableGameNames = data.gameNames;
                        break;
                    case window.MessageType.ALL_GAMES:
                        this.avaliableGameNames = data.gameNames;
                        break;
                    case window.MessageType.CHECK_MOVE:
                        if (data.moveAllowed) {
                            this.chosenCoordinates.push(new window.coordinates(data.move.firstMove.x, data.move.firstMove.y));
                            this.game.board[data.move.firstMove.x][data.move.firstMove.y].newPlayerId = this.player.id;
                            this.game.board[data.move.firstMove.x][data.move.firstMove.y].updateBackground();
                        }
                }
            }
            
        },
        host: function() {
            this._socket.send(this.buildAction(window.MessageType.HOST, null))
        },
        start: function() {
            this._socket.send(this.buildAction(window.MessageType.START, { gameName: this.game.gameName }))
        },
        join: function() {
            if (this.showJoinOptions && this.chosenGame != null) {
                this._socket.send(this.buildAction(window.MessageType.JOIN, { gameName: this.chosenGame }));
            }            
            else {
                this.showJoinOptions = true;
            }
        },
        quit: function() {
            
        },
        buildAction: function(type, data) {
            return JSON.stringify(new window.Message(this.username, type, data));
        }
    }
  })