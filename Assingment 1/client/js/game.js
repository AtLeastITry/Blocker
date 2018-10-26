var game = function() {
    this.name = window.utils.GUID();
    this.players = [];
    this.state = [];

    for (let i = 0; i < 6; i++) {
        var temp = [];
        for (let j = 0; j < 12; j++) {
            temp.push(new window.block());
        }
        this.state.push(temp);
    }
};

window.game = game;