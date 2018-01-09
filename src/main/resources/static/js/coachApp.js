var coachApp = angular.module('coachApp', ['ui.router'])
  coachApp.config( function($stateProvider, $httpProvider) {
  
    var getCalendar = {
        name:   'getCalendar',
        url:    '/getCalendar',
        templateUrl:    'calendar.html',
        controller:     'getCalendar'
    }
 
    var manageStudents = {
        name:   'manageStudents',
        url:    '/manageStudents',
        templateUrl:    'students.html',
        controller:     'manageStudents'
    }
    
    var attendance = {
        name:   'attendance',
        url:    '/attendance',
        templateUrl:    'attendance.html',
        controller:     'attendance'
    }

    var login = {
      name: 'login',
      url:  '/login',
      templateUrl:'login.html',
      controller : 'login'
  }
   
    
    var home = {
      name: 'home',
      url:  '/home',
      templateUrl: 'home.html',
      controller: 'home'
      
    } 
    
    var getKidsInGroup = {
        name:   'getKidsInGroup',
        url:    '/getKidsInGroup?date?groupID',
        templateUrl:    'kids.html',
        controller:     'kids'
        
    }
    
    var manageClasses = {
        name:   'manageClasses',
        url:    '/manageClasses',
        templateUrl:    'manageClasses.html',
        controller:     'manageClasses'
    }
    
    var addNewClass = {
        name:   'addNewClass',
        url:    '/addNewClass',
        templateUrl:    'addNewClass.html',
        controller:     'addNewClass'
    }
    
    var addKids = {
        name:   'addKids',
        url:    '/addKids',
        templateUrl:    'addKids.html',
        controller:     'addKids'
    }
    
    var editKids = {
        name:   'editKids',
        url:    '/editKids?name?kidID?group?groupID',
        templateUrl:    'editKids.html',
        controller:     'editKids'
    }
    
    var addGroups = {
        name:   'addGroups',
        url:    '/addGroups',
        templateUrl:    'addGroups.html',
        controller:     'addGroups'
    }
    
    var logout = {
        name:   'logout',
        url:    '/logout',
        templateUrl:    'logout.html',
        controller:     'logout'
    }
    
    var groups = {
        name:   'groups',
        url:    '/groups',
        templateUrl:    'groups.html',
        controller:     'groups'
    }
    
    var schedule = {
        name:   'schedule',
        url:    '/schedule',
        templateUrl:    'schedule.html',
        controller:     'schedule'
    }
    
    var editGroups = {
        name:   'editGroups',
        url:    '/editGroups?groupName?groupID',
        templateUrl:    'editGroups.html',
        controller:     'editGroups'
    }
    
    var editSchedule = {
        name:   'editSchedule',
        url:    '/editSchedule?time?date?groupID?groupName?calendarID',
        templateUrl:    'editSchedule.html',
        controller:     'editSchedule'
    }
    
    $stateProvider.state(login);
    $stateProvider.state(editKids);
    $stateProvider.state(home);
    $stateProvider.state(attendance);
    $stateProvider.state(manageStudents);
    $stateProvider.state(getCalendar);
    $stateProvider.state(getKidsInGroup); 
    $stateProvider.state(manageClasses); 
    $stateProvider.state(addNewClass); 
    $stateProvider.state(addKids); 
    $stateProvider.state(addGroups);  
    $stateProvider.state(logout);
    $stateProvider.state(schedule);  
    $stateProvider.state(groups);    
    $stateProvider.state(editSchedule);    
    $stateProvider.state(editGroups);    

    
    
});