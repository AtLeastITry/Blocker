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
    },
    getParam(name) {
        debugger;
        var url = window.location.href;
        name = name.replace(/[\[\]]/g, '\\$&');
        var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, ' '));
    }
})