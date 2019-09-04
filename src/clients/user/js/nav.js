var app = new Vue({
    el: '#nav',
    methods: {
        goToHome: function() {
            window.router.goTo('index', null);
        },
        goToGames: function() {
            window.router.goTo('games', null);
        }
    },
    computed: {
        homeActive: function() {
            return window.router.isActive('index');
        },
        gamesActive: function() {
            return window.router.isActive('games');
        }
    }
});