window.cardModel = function(card) {
    this.value = card;
    this.selected = false;
    this.description = '';
    this.title = '';
    this.icon = {
        'fas fa-dice-two': card == window.InfluenceCard.DOUBLE,
        'fas fa-expand-arrows-alt': card == window.InfluenceCard.FREEDOM,
        'fas fa-exchange-alt': card == window.InfluenceCard.REPLACEMENT,
    }

    switch(this.value) {
        case window.InfluenceCard.DOUBLE:
            this.title = 'Double-move card';
            this.description = 'Allows the player to place two stones in one move';
            break;
        case window.InfluenceCard.REPLACEMENT:
            this.title = 'Replacement card';
            this.description = 'Allows the player to place a stone even if the cell is not empty. The adjacency rule still applies.';
            break;
        case window.InfluenceCard.FREEDOM:
            this.title = 'Freedom card';
            this.description = 'Allows the player to place a stone on any empty cell, even if it is not adjacent to their existing stones.';
            break;
    }
}