(function () {
    'use strict';

    angular
            .module('springApp')
            .controller('UserController', UserController);

    UserController.$inject = ['UserService', '$scope'];

    function UserController(UserService, $scope) {

        $scope.user = {};
        $scope.init = function () {
            $scope.passw1 = '';
            $scope.passw2 = '';

            $scope.term = '';
            $scope.hideform = true;
            $scope.edit = true;
            $scope.error = false;
            $scope.incomplete = false;

            UserService.query(function (response) {
                console.log(response);
                $scope.searchResults = response;
            });
        };

        $scope.getUser = function (id) {
            console.log('id: ' + id);
            UserService.getUser({'id': id}, function (response) {
                console.log(response);
            });
        };

        $scope.search = function () {
            UserService.query($scope.term, function (response) {
                console.log(response);
                var results = response.filter(function (item) {
                    return JSON.stringify(item).toLowerCase().includes($scope.term.toLowerCase());
                });
                $scope.searchResults = results;
            });
        };

        $scope.editUser = function (id) {
            $scope.hideform = false;
            if (id === 'new') {
                $scope.edit = true;
                $scope.incomplete = true;
                $scope.user = {};
            } else {
                UserService.getUser({'id': id}, function (response) {
                    $scope.edit = false;
                    $scope.incomplete = false;
                    $scope.user.userId = response.userId;
                    $scope.user.name = response.name;
                    $scope.user.lastName = response.lastName;
                    $scope.user.email = response.email;
                });
            }
        };

        $scope.updateUser = function () {
            UserService.updateUser({'id': $scope.user.userId}, $scope.user, function (response) {
                console.log(response);
                $scope.init();
                $scope.message = 'User data updated!';
                $scope.errorMessage = '';
            });
        };

        $scope.addUser = function () {
            if ($scope.user !== null && $scope.user.email && $scope.user.name) {
                $scope.user.password = $scope.passw1;
                UserService.addUser($scope.user, function (response) {
                    console.log(response);
                    $scope.init();
                    $scope.message = 'User data created!';
                    $scope.errorMessage = '';
                });
            }
        };

        $scope.deleteUser = function (id) {
            console.log('id: ' + id);
            UserService.deleteUser({'id': id}, function (response) {
                console.log(response);
                $scope.init();
            });
        };


        $scope.$watch('passw1', function () {
            $scope.test();
        });
        $scope.$watch('passw2', function () {
            $scope.test();
        });

        $scope.test = function () {
            if ($scope.passw1 !== $scope.passw2) {
                $scope.error = true;
            } else {
                $scope.error = false;
            }
            $scope.incomplete = false;
        };

    }

})();
