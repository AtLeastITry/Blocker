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
        chosenCoordinates: [],
        playerCards: []
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
        powerupSelected: function() {
            let selected = false;

            for (let i = 0; i < this.playerCards.length; i++) {
                const card = this.playerCards[i];
                if (card.selected) {
                    selected = true;
                    break;
                }
            }

            return selected;
        },
        doubleSelected: function() {
            return this.player.selectedCard == window.InfluenceCard.DOUBLE;
        },
        freedomSelected: function() {
            return this.player.selectedCard == window.InfluenceCard.FREEDOM;
        },
        replacementSelected: function() {
            return this.player.selectedCard == window.InfluenceCard.REPLACEMENT;
        },
        playerLost: function() {
            if (!this.game.players) {
                return false;
            }
            var player = this.game.players.filter(player => player.playerId == this.player.id);
            if (player == null || player.length < 1) {
                return false;
            }

            return !player[0].canMove;
        },
        playerWon: function() {
            if (!this.game.players) {
                return false;
            }
            var player = this.game.players.filter(player => player.playerId == this.player.id);
            if (player == null || player.length < 1) {
                return false;
            }

            return player[0].canMove && this.game.finished;
        },
        canMove: function() {
            return this.game.inProgress && this.game.playerTurn == this.player.id && !this.playerLost;
        },
        username: function() {
            if (this.player != null) {
                return "player " + this.player.id
            }

            return "client";
        },
        moveText: function() {
            if (!this.canMove && this.game.inProgress) {
                if (this.playerLost) {
                    return "You are blocked";
                }

                return "player " + this.game.playerTurn + "'s turn";
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

            if (this.chosenCoordinates.length == 1 && this.chosenCoordinates[0].x == rowIndex && this.chosenCoordinates[0].y == blockIndex) {
                this.chosenCoordinates = [];
                this.game.board[rowIndex][blockIndex].newPlayerId = null;
                this.game.board[rowIndex][blockIndex].updateBackground();
                return;
            }

            if (this.chosenCoordinates.length > 1) {
                if (this.chosenCoordinates[0].x == rowIndex && this.chosenCoordinates[0].y == blockIndex) {
                    return;
                }

                if (this.chosenCoordinates[1].x == rowIndex && this.chosenCoordinates[1].y == blockIndex) {
                    this.game.board[rowIndex][blockIndex].newPlayerId = null;
                    this.game.board[rowIndex][blockIndex].updateBackground();

                    this.chosenCoordinates.splice(1, 1);
                    return;
                }

                return;
            }

            if (this.chosenCoordinates.length > 0) {
                if (this.doubleSelected) {
                    let firstCoordinate = this.chosenCoordinates[0];
                    let secondCoordinate = new window.coordinatesModel(rowIndex, blockIndex);
                    let move = new window.moveModel(this.player.selectedCard, firstCoordinate, secondCoordinate);
                    let checkMoveRequest = new window.checkMoveRequest(this.game.gameName, move, this.player.id);
                    this._socket.send(this.buildAction(window.MessageType.CHECK_MOVE, checkMoveRequest));
                }

                return;
            }

            let move = new window.moveModel(this.player.selectedCard, new window.coordinatesModel(rowIndex, blockIndex));
            let checkMoveRequest = new window.checkMoveRequest(this.game.gameName, move, this.player.id);
            this._socket.send(this.buildAction(window.MessageType.CHECK_MOVE, checkMoveRequest));

            return;
        },
        selectCard: function(card) {
            if (this.player.selectedCard != "" && this.player.selectedCard != card.value) {
                return false;
            }
            if (this.player.selectedCard == card.value) {
                this.player.selectedCard = "";
                card.selected = false;

                this.chosenCoordinates.forEach(coordinate => {
                    this.game.board[coordinate.x][coordinate.y].newPlayerId = null;
                    this.game.board[coordinate.x][coordinate.y].updateBackground();
                });

                this.chosenCoordinates = [];

                return false;
            }

            this.player.selectedCard = card.value
            card.selected = true;
            return true;
        },
        submitTurn: function() {
            firstMove = this.chosenCoordinates[0];
            secondMove = null;

            if (this.chosenCoordinates.length > 1 && this.doubleSelected) {
                secondMove = this.chosenCoordinates[1];
            }
            let move = new window.moveModel(this.player.selectedCard, firstMove, secondMove);
            let playerMoveRequest = new window.playerMoveRequest(this.game.gameName, move);
            this._socket.send(this.buildAction(window.MessageType.PLAYER_MOVE, playerMoveRequest)) 
        },
        onMessage: function(event) {
            let msg = JSON.parse(event.data);
            let data = JSON.parse(msg.data);
            if (data.success) {
                switch(msg.type) {
                    case window.MessageType.HOST: 
                        this.game = new window.gameModel(data.game);
                        this.player = new window.playerModel(this.username, data.playerId);
                        this.playing = true;
                        this.playerCards = this.game.getInfluenceCards(this.player.id);
                        Vue.nextTick(function () {
                            $('[data-toggle="popover"]').popover({ trigger: 'hover' })
                        });
                        break;
                    case window.MessageType.JOIN: 
                        this.game = new window.gameModel(data.game);
                        this.playing = true;
                        this.player = new window.playerModel(this.username, data.playerId);
                        this.showJoinOptions = false;
                        this.playerCards = this.game.getInfluenceCards(this.player.id);
                        Vue.nextTick(function () {
                            $('[data-toggle="popover"]').popover({ trigger: 'hover' })
                        });
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
                            this.game = new window.gameModel(data.game);
                        }                        
                        break;
                    case window.MessageType.PLAYER_MOVE:
                    case window.MessageType.START:
                        this.game = new window.gameModel(data.game);
                        this.chosenCoordinates = [];
                        this.player.selectedCard = "";
                        this.playerCards = this.game.getInfluenceCards(this.player.id);
                        Vue.nextTick(function () {
                            $('[data-toggle="popover"]').popover({ trigger: 'hover' })
                        });
                        break;
                    case window.MessageType.NEW_GAME:
                        this.avaliableGameNames.push(data.gameName);
                        break;
                    case window.MessageType.ALL_GAMES:
                        this.avaliableGameNames = data.gameNames;
                        break;
                    case window.MessageType.CHECK_MOVE:
                        if (data.moveAllowed) {
                            if (data.move.secondMove != null) {
                                this.chosenCoordinates.push(new window.coordinatesModel(data.move.secondMove.x, data.move.secondMove.y));
                                this.game.board[data.move.secondMove.x][data.move.secondMove.y].newPlayerId = this.player.id;
                                this.game.board[data.move.secondMove.x][data.move.secondMove.y].updateBackground();
                            }
                            else {
                                this.chosenCoordinates.push(new window.coordinatesModel(data.move.firstMove.x, data.move.firstMove.y));
                                this.game.board[data.move.firstMove.x][data.move.firstMove.y].newPlayerId = this.player.id;
                                this.game.board[data.move.firstMove.x][data.move.firstMove.y].updateBackground();
                            }
                        }
                        break;
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