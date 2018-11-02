window.cardModel = function(card) {
    this.value = card;
    this.selected = false;
    this.icon = {
        'fas fa-dice-two': card == window.InfluenceCard.DOUBLE,
        'fas fa-expand-arrows-alt': card == window.InfluenceCard.FREEDOM,
        'fas fa-exchange-alt': card == window.InfluenceCard.REPLACEMENT,
    }
}