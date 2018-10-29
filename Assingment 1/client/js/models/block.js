window.block = function(playerId) {
    this.playerId = playerId;
    this.newPlayerId = null;
    this.background = "";

    this.updateBackground = function() {
        let tempPlayerId = this.playerId;
        if (this.newPlayerId != null && this.newPlayerId != "") {
            tempPlayerId = this.newPlayerId;
        }

        switch(tempPlayerId) {
            case 0:
                this.background = "";
                break;
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

    this.updateBackground();
}