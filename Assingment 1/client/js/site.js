var app = new Vue({
    el: '#app',
    data: {
        playing: false,
        _socket: null,
        username: null,
        showJoinOptions: false,
        game: null,
        player: null
    },
    created: function() {
      this._socket = new WebSocket("ws://localhost:8080/assignment/game");
      this._socket.onmessage = this.onMessage;
    },
    methods: {
        selectBlock: function(rowIndex, blockIndex) {
            firstMove = new window.coordinates(rowIndex, blockIndex);
            this._socket.send(this.buildAction(window.MessageType.PLAYER_MOVE, new window.move(null, firstMove, null)))            
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
                        this.game = new window.game(data.game);
                        break;
                }
            }
            
        },
        host: function() {
            this._socket.send(this.buildAction(window.MessageType.HOST, null))
        },
        join: function() {
            if (this.showJoinOptions) {
                
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