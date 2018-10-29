var app = new Vue({
    el: '#app',
    data: {
        playing: false,
        _socket: null,
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
    computed: {
        canMove: function() {
            return this.game.inProgress && this.game.playerTurn == this.player.id;
        },
        username: function() {
            if (this.player != null) {
                return "player " + this.player.id
            }

            return "client";
        },
        moveText: function() {
            if (!this.canMove && this.game.inProgress) {
                return "player " + this.game.playerTurn + "'s turn"
            }

            return "";
        },
        userColor: function() {
            let color = {
                'bg-primary': false,
                'bg-success': false,
                'bg-info': false,
                'bg-warning': false,
                'bg-danger': false,
            };
            switch(this.player.id) {
                case 1:
                    color["bg-primary"] = true;
                    break;
                case 2:
                    color["bg-success"] = true;
                    break;
                case 3:
                    color["bg-info"] = true;
                    break;
                case 4:
                    color["bg-warning"] = true;
                    break;
                case 5:
                    color["bg-danger"] = true;
                    break;
            }

            console.log(color);

            return color;
        }
    },
    methods: {
        selectBlock: function(rowIndex, blockIndex) {
            if (!this.canMove) {
                return;
            }

            if (this.chosenCoordinates.length > 0 && this.chosenCoordinates[0].x == rowIndex && this.chosenCoordinates[0].y == blockIndex) {
                this.chosenCoordinates = [];
                this.game.board[rowIndex][blockIndex].newPlayerId = null;
                this.game.board[rowIndex][blockIndex].updateBackground();
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
            let move = new window.move(null, firstMove, null);
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
                        if (data.hasLeft) {
                            this.playing = false;
                            this.player= null;
                            this.game = null;
                            this.chosenCoordinates = [];
                            this.chosenGame = null;
                        }
                        else {
                            this.game = new window.game(data.game);
                        }                        
                        break;
                    case window.MessageType.PLAYER_MOVE:
                    case window.MessageType.START:
                        this.game = new window.game(data.game);
                        this.chosenCoordinates = [];
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
            this._socket.send(this.buildAction(window.MessageType.LEAVE, { gameName: this.game.gameName, playerId: this.player.id }))
        },
        buildAction: function(type, data) {
            return JSON.stringify(new window.Message(this.username, type, data));
        }
    }
  })