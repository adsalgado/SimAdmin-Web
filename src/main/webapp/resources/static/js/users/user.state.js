(function () {
    'use strict';
 
    angular.module('springApp')
        .config(stateConfig);
 
    stateConfig.$inject = ['$stateProvider'];
 
    function stateConfig($stateProvider) {
        $stateProvider
            .state('search', {
                url: '/user/search',
                templateUrl: 'search/search.html',
                controller: 'SearchController',
                controllerAs: 'vm'
            });
    }
})();