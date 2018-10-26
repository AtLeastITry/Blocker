var block = function() {
    this.ownedBy = null;

    this.select = function(player) {
        this.ownedBy = player;
        this.selected = !this.selected;
    }

    this.selected = false;
}

window.block = block;