window.router = Object.freeze({
    goTo: function(action, data) {
        let currentAction = window.location.href.substr(window.location.href.lastIndexOf('/') + 1);
        window.location = window.location.href.replace(currentAction, action + '.html') + this._urlEncodeData(data);
    },
    _urlEncodeData: function(data) {
        var str = [];
        for (var p in data)
            if (data.hasOwnProperty(p)) {
            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(data[p]));
        }
        
        return str.length > 0 ? '?' + str.join("&") : '';
    },
    isActive(action) {
        let currentAction = window.location.href.substr(window.location.href.lastIndexOf('/') + 1).replace('.html', '');
        return action == currentAction;
    }
})