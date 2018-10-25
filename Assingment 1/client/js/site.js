var app = new Vue({
    el: '#app',
    data: {
        playing: false,
        _socket: null,
        username: null,
        showJoinOptions: false,
        gameName: null
    },
    created: function() {
      this._socket = new WebSocketMock();  
      this._socket.onmessage = this.onMessage;
    },
    computed: {
        avaliableGames: function() {
            return this._socket.games;
        }
    },
    methods: {
        onMessage: function(event) {
            let msg = JSON.parse(event.msg);
            let data = msg.data;
            if (data.success) {
                switch(data.type) {
                    case 'host': 
                        this.playing = true;
                        break;
                    case 'join': 
                        this.playing = true;
                        break;
                    case 'leave': 
                        this.playing = false;
                        break;
                }
            }
            
        },
        host: function() {
            this._socket.send(this.buildAction('host', { username: this.username }))
        },
        join: function() {
            if (this.showJoinOptions) {
                this._socket.send(this.buildAction('join', { username: this.username }))
            }            
            else {
                this.showJoinOptions = true;
            }
        },
        quit: function() {
            this._socket.send(this.buildAction('leave', { username: this.username }))
        },
        buildAction: function(action, data) {
            return JSON.stringify( {
                action: action,
                data: data
            })
        }
    }
  })