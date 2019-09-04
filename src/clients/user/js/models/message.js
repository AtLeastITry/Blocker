window.Message = function(username, type, data) {
    this.sent = new Date();
    this.type = type;
    this.sender = username;
    this.data = JSON.stringify(data);
}