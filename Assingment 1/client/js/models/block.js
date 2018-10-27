window.block = function(playerId) {
    this.playerId = playerId;

    this.background = "";

    switch(playerId) {
        case 1:
            this.background="bg-primary";
            break;
        case 2:
            this.background="bg-success";
            break;
        case 3:
            this.background="bg-info";
            break;
        case 4:
            this.background="bg-warning";
            break;
        case 5:
            this.background="bg-danger";
            break;
    }
}