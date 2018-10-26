var block = function() {
    this.ownedBy = null;

    this.select = function(player) {
        this.ownedBy = player;
    }
}

window.block = block;