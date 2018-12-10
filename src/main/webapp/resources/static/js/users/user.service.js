(function () {
    'use strict';

    angular
            .module('springApp')
            .factory('UserService', UserService);

    UserService.$inject = ['$resource'];

    function UserService ($resource) {
        var service = $resource('api/user/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'getAllUsers': {method: 'GET', isArray: true},
            'getUser': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'addUser': { method:'POST' },
            'updateUser': { method:'PUT' },
            'deleteUser':{ method:'DELETE'}
        });

        return service;
    }
    
/*    function UserService($http) {
   
        this.getUser = function getUser(userId) {
            return $http({
                method: 'GET',
                url: 'api/user/' + userId
            });
        };

        this.addUser = function addUser(name, email) {
            return $http({
                method: 'POST',
                url: 'api/user/',
                data: {
                    name: name,
                    email: email
                }
            });
        };

        this.updateUser = function updateUser(id, name, email) {
            return $http({
                method: 'PATCH',
                url: 'api/user/' + id,
                data: {
                    name: name,
                    email: email
                }
            });
        };

        this.deleteUser = function deleteUser(id) {
            return $http({
                method: 'DELETE',
                url: 'api/user/' + id
            });
        };

        this.getAllUsers = function getAllUsers() {
            return $http({
                method: 'GET',
                url: 'api/user/'
            });
        };

    }*/


})();