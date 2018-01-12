coachApp.controller('home', function($scope, $http, $rootScope, $location) {
    alert("inside home controller");
    
          var request = {
                    method: "POST",
                    url: "/logout",
                    data: {test: "test"},
    
                    headers: {
                       "Solace-Reply-Wait-Time-In-ms":"50000" ,
                       'Access-Control-Allow-Origin' : '*',
                       'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
                    }
                  };
            alert("about to send http request for logout");

          /*  $http(request).success(function(data) {
                alert("in suceess http request");
                $scope.greeting = data;
            })*/
        
    
    
   /*  $scope.logout = function() {
         alert("Inside  $scope.logout");
        $http(request).success(function(data) {
            alert("Inside success of $scope.logout");
            $rootScope.authenticated = false;
            $location.path("/home");
        }).error(function(data) {
            alert("Inside error of $scope.logout, data = " + data);
            $rootScope.authenticated = false;
        });
    }*/
    

}); 

coachApp.controller('logout', function($scope, $http, $rootScope, $location) {
    alert("inside logout controller");
    
          var request = {
                    method: "POST",
                    url: "/logout",
                    data: {test: "test"},
    
                    headers: {
                       "Solace-Reply-Wait-Time-In-ms":"50000" ,
                       'Access-Control-Allow-Origin' : '*',
                       'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
                    }
                  };
            alert("about to send http request for logout");

           $http(request).success(function(data) {
                alert("Inside success of $scope.logout");
                $rootScope.authenticated = false;
                $location.path("/home");
            }).error(function(data) {
                alert("Inside error of $scope.logout, data = " + data);
                $rootScope.authenticated = false;
        });
        
    
    
    /* $scope.logout = function() {
         alert("Inside  $scope.logout");
        $http(request).success(function(data) {
            alert("Inside success of $scope.logout");
            $rootScope.authenticated = false;
            $location.path("/home");
        }).error(function(data) {
            alert("Inside error of $scope.logout, data = " + data);
            $rootScope.authenticated = false;
        });*/
    
    

}); 


coachApp.controller('login', function($rootScope, $scope, $http, $location) {
    alert("inside login controller");
    
    var authenticate = function(credentials, callback) {
       // alert("Inside authenticate");
        var headers = credentials ? { authorization: "Basic "
                                    + btoa(credentials.username + ":"
                                    +
                                    credentials.password)} : {} ;
        

        //alert ("credentials =  "+ credentials);
        var request = {
                    method: "POST",
                    url: "/resourceLogin",
                    data : {test: "test"},
    
                    headers: headers
                  };
           // alert("about to send http request from authenticate");

            $http(request).success(function(data) {
               // $http.get('resourceLogin', { headers:headers }).success(function(data){
                alert("in suceess http request from authenticate");
                if (data.name) {
                    $rootScope.authenticated = true;
                    $location.path("/home");
                    //$scope.error = false;
                    
                }   else {
                    $rootScope.authenticated = false;
                    $location.path("/login");
                   // $scope.error = true;
                }
                
               // alert("authenticated flag is " + $rootScope.authenticated);
                
                callback && callback();
            
            }).error(function() {
               // alert("In error from http response from authenticate");
                $rootScope.authenticated = false;
                callback && callback();
    });

    }
    
    //alert("Calling authenticate now");
    authenticate();
    
    $scope.credentials = {};
    $scope.login = function() {
       // alert("crenedtials = " + $scope.credentials.username);
        authenticate($scope.credentials, function() {
            if ($rootScope.authenticated) {
                $location.path("/home");
                $scope.error = false;
            } else {
               // alert("setting error as true");
                $location.path("/login");
                $scope.error = true;
            }
        });
                    
                    
    }
    
   
}); 

coachApp.controller('getCalendar', function($scope, $http, $state) {
    alert("inside getCalendar older controller");
    
    $scope.date=new Date();
    
    $scope.calendar={
        date: new Date()
    };
    
    alert("date = "+ $scope.calendar.date);
    
    $scope.getCalendarForToday = function(){
        var request = {
                    method: "POST",
                    url: "/getCalendar",
                    data: { date: $scope.date},

                    headers: {
                       "Solace-Reply-Wait-Time-In-ms":"50000" ,
                       'Access-Control-Allow-Origin' : '*',
                       'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
                    }
                  };


            alert("sending http request with date = "+ request.data.date);
            $http(request).success(function(data) {
                alert("in suceess http request");
                $scope.calendarList = data.Schedule;
                if($scope.calendarList){
                    $scope.resultObtainedFromServer=true;}
                else {$scope.resultObtainedFromServer=false;}
                
            });    
    }
   
    
    $scope.submit = function(){
        //var data = this.formData;
        //TODO get the kids name and perform DB call on the child's name
        
        
        alert("Date to check is : " + $scope.calendar.date);
        
        
        //$scope.objectValue.data = $scope.calendar.date;
	  
          var request = {
                    method: "POST",
                    url: "/getCalendar",
                    data: { date: $scope.calendar.date},

                    headers: {
                       "Solace-Reply-Wait-Time-In-ms":"50000" ,
                       'Access-Control-Allow-Origin' : '*',
                       'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
                    }
                  };


            alert("sending http request with date = "+ request.data.date);
            $http(request).success(function(data) {
                alert("in suceess http request");
                /*$scope.calendar.time = data.time;
                $scope.calendar.date = data.date;
                $scope.calendar.GroupName = data.GroupName;*/
                //alert("data.time = " + data.Schedule);
                $scope.calendarList = data.Schedule;
                if($scope.calendarList){
                    $scope.resultObtainedFromServer=true;}
                else {$scope.resultObtainedFromServer=false;}
                
            });
            
        
        
    }
    
    
                
    
    $scope.goBack = function() {
        $state.go("home");
    }
    

});

coachApp.controller('kids', function($scope, $http, $stateParams, $filter, $state) {
    alert("inside kids  controller");
    
    
    var dateForAttendance = $stateParams.date;
    var groupID = $stateParams.groupID;
    $scope.groupID = groupID;
    //Boolean present = true;
    
    alert ("date is still with me " +  dateForAttendance);
    
    
    alert("preparing to query for GroupID = "+ groupID);
    
    var checkForAttendance = function(dateToSend, groupIDToSend){
        alert("in fn checkForAttendance() with date,groupID = "+dateToSend + groupIDToSend);
        var request = {
                        method: "POST",
                        url: "/checkAttendance",
                        data: { groupID : groupIDToSend,
                                date: dateToSend},

                        headers: {
                           "Solace-Reply-Wait-Time-In-ms":"50000" ,
                           'Access-Control-Allow-Origin' : '*',
                           'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
                        }
                      };


        alert("http request with date = "+request.data.date );
        alert("http request with groupID = " + request.data.groupID);
    
        $http(request).success(function(data) {
                
            alert("in suceess http request for checkAttendance");
            $scope.attendanceList = data.attendanceList;
            
            $scope.kidsList.forEach(function (x) {
                $scope.attendanceList.forEach(function (y) {
                    if (y.presentAbsent=="P") {
                        x.checked=true;
                        x.present="P";
                    }else {
                        x.checked=false;
                        x.present="A";
                    }
                })
            });
            
            
            
           
        });
        
        
    }
    var request = {
                        method: "POST",
                        url: "/getKidsInGroup",
                        data: { groupID : $scope.groupID},

                        headers: {
                           "Solace-Reply-Wait-Time-In-ms":"50000" ,
                           'Access-Control-Allow-Origin' : '*',
                           'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
                        }
                      };


        alert("sending http request with groupID = "+ $scope.groupID);
    
        $http(request).success(function(data) {
                
            alert("in suceess http request of getKidsInGroup");
                
            $scope.kidsList = data.kidsList;
                
            checkForAttendance(dateForAttendance, $scope.groupID);
            
               
        });

    
$scope.submit = function(){
                
    alert("save the checkbox data in attendance table");
        
        //  send the selection list back to insert the names of present kids
    alert("on submit of mark attendance");
    $scope.kidsList.forEach(function (x) {
                //alert(x.kidName, x.checked);
                if(x.checked==true){
                    alert("found a check");
                    x.present="P";
                }else {
                    x.present="A";
                }
    });
    var request = {
                    method: "POST",
                    url: "/markAttendance",
                    data: { kidsList : $scope.kidsList,
                            groupID : $scope.groupID,
                            date: dateForAttendance},

                    headers: {
                       "Solace-Reply-Wait-Time-In-ms":"50000" ,
                       'Access-Control-Allow-Origin' : '*',
                       'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
                    }
                  };
        alert("sending request to update attendance ");
        
        $http(request).success(function(data) {
                
                alert("in suceess http request, data.content = " + data.result);
        
                    
     
                
    
        });
    }
    
     $scope.goBack = function() {
        $state.go("getCalendar");
    };
    
});

coachApp.controller('manageClasses', function($scope, $http) {
    alert("inside manageClasses  controller");
    //$scope.calendar.date = "Hello, You need to check the calendar for date?";
    
    //alert("Kids name : "+ $scope.kidname);
   
    
   /* $scope.submit = function(){
        //var data = this.formData;
        //TODO get the kids name and perform DB call on the child's name
        
        
        alert("Date to check is : " + $scope.calendar.date);
        
        
        //$scope.objectValue.data = $scope.calendar.date;
	  
          var request = {
                    method: "POST",
                    url: "/getCalendar",
                    data: { date: $scope.calendar.date},

                    headers: {
                       "Solace-Reply-Wait-Time-In-ms":"50000" ,
                       'Access-Control-Allow-Origin' : '*',
                       'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
                    }
                  };


            alert("sending http request with date = "+ $scope.calendar.date);
            $http(request).success(function(data) {
                alert("in suceess http request");
                $scope.calendarList = data.Schedule;
                alert("setting dummy now");
            
                $scope.dummy = function(x){
                    alert("hi, im dummy");
                };
                
            });
            
        
        
    }*/
    

});

coachApp.controller('addNewClass', function($scope, $http, $state) {
    alert("inside addNewClass  controller");
    
    $scope.schedule = {
        time: "TimeOfClass",
        date: new Date()};
   
          var request = {
                    method: "POST",
                    url: "/getGroups",
                    data: { user: "gen1"},

                    headers: {
                       "Solace-Reply-Wait-Time-In-ms":"50000" ,
                       'Access-Control-Allow-Origin' : '*',
                       'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
                    }
                  };

                $http(request).success(function(data) {
                    alert("in suceess http request for getGroups in add Schedule");
                    $scope.groupList = data.groupList;    
                    $scope.selectedGroup = {groupID:$scope.groupList[0].groupID};
            
                });
    $scope.fcsFillTextBox = function() {
        if($scope.schedule.time="TimeOfClass"){$scope.schedule.time=""};
    };
    
    $scope.submit = function(){
        //var data = this.formData;
        //TODO get the kids name and perform DB call on the child's name
        
        
        alert("Date to enter for  : " + $scope.schedule.date);
        
        alert("Time to enter : " + $scope.schedule.time);
        
        //$scope.objectValue.data = $scope.calendar.date;
	  
          var request = {
                    method: "POST",
                    url: "/addSchedule",
                    data: { date: $scope.schedule.date, 
                            groupID : $scope.selectedGroup.groupID,
                            time: $scope.schedule.time},

                    headers: {
                       "Solace-Reply-Wait-Time-In-ms":"50000" ,
                       'Access-Control-Allow-Origin' : '*',
                       'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
                    }
                  };


           $http(request).success(function(data) {
                alert("in suceess http request");
                
                $scope.result = data.result;
                
            });
            
        
        
    };
    
    $scope.goBack = function() {
        $state.go("schedule");
    };
    
    
    

});

coachApp.controller('addKids', function($scope, $http, $state) {
    alert("inside addKids  controller");
    
    //TODO : get the GroupList from backend or other controller
   
    
    var request = {
        method: "POST",
        url: "/getGroups",
        data: { user: "gen"},
        
        headers: {
            "Solace-Reply-Wait-Time-In-ms":"50000" ,
            'Access-Control-Allow-Origin' : '*',
            'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
        }
    };

    $http(request).success(function(data) {
        alert("in suceess http request for getGroups");
        $scope.groupList = data.groupList;   
        $scope.selectedGroup = {groupID:$scope.groupList[0].groupID};
                    
    });
    
    var request = {
        method: "POST",
        url: "/getPackages",
        data: { user: "gen"},
        
        headers: {
            "Solace-Reply-Wait-Time-In-ms":"50000" ,
            'Access-Control-Allow-Origin' : '*',
            'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
        }
    };

    $http(request).success(function(data) {
        alert("in suceess http request for get Packages");
        $scope.packageList = data.packageList;
        $scope.selectedPackage = {packageID:$scope.packageList[0].packageID};
                    
    });
            
            //alert("Selected group = " + $scope.selectedGroup);
    
    $scope.submit = function(){
       
         var request = {
                    method: "POST",
                    url: "/addKid",
                    data: { kidName: $scope.kidName,
                            groupID: $scope.selectedGroup.groupID,
                            packageID: $scope.selectedPackage.packageID},

                    headers: {
                       "Solace-Reply-Wait-Time-In-ms":"50000" ,
                       'Access-Control-Allow-Origin' : '*',
                       'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
                    }
                  };
        
        
        $http(request).success(function(data) {
            alert("in suceess http request for submit function");
                
            $scope.kidList = data.kidList;
               
        });
   }
    
    $scope.goBack = function() {
        $state.go("manageStudents");
    }

});

coachApp.controller('editKids', function($scope, $http, $stateParams, $state) {
    alert("inside edit Kids  controller");
    
    //TODO : get the GroupList from backend or other controller
    $scope.name= $stateParams.name;
    $scope.group = $stateParams.group;
    $scope.groupID = $stateParams.groupID;
    $scope.kidID = $stateParams.kidID;
    
    $scope.resultObtainedFromServer = false;
    $scope.resultObtainedFromDelete = false;
    
    alert("group = "+ $scope.group);
    
    $scope.selectedGroup = {groupID:$scope.groupID};
    
    var request = {
                    method: "POST",
                    url: "/getGroups",
                    data: { user: "gen"},

                    headers: {
                       "Solace-Reply-Wait-Time-In-ms":"50000" ,
                       'Access-Control-Allow-Origin' : '*',
                       'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
                    }
                  };

                $http(request).success(function(data) {
                    alert("in suceess http request for getGroups");
                    $scope.groupList = data.groupList; 
                    
                });
    
    
    
    $scope.submit = function(){
        alert("Kid Name to add : " + $scope.name);
        alert("Group = " + $scope.selectedGroup.groupID);
        
        
        
         var request = {
                    method: "POST",
                    url: "/updateKid",
                    data: { kidID: $scope.kidID,
                            kidName:$scope.name,
                            groupID: $scope.selectedGroup.groupID},

                    headers: {
                       "Solace-Reply-Wait-Time-In-ms":"50000" ,
                       'Access-Control-Allow-Origin' : '*',
                       'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
                    }
                  };
        
        alert("sending groupID as : " +request.data.groupID );
        
        $http(request).success(function(data) {
            alert("in suceess http request for submit of update kids function");
                
            $scope.result = data.result;
            $scope.resultObtainedFromServer = true;
               
        });
    }
    
    $scope.goBack = function() {
        $state.go("manageStudents");
    }
    
    $scope.delete = function() {
        alert("Do you really want to delete " + $scope.name + "'s record?");
        
        var request = {
            method: "POST",
            url: "/deleteKid",
            data: { kidID: $scope.kidID,
                    kidName:$scope.name,
                    groupID: $scope.selectedGroup.groupID},

            headers: {
               "Solace-Reply-Wait-Time-In-ms":"50000" ,
               'Access-Control-Allow-Origin' : '*',
               'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
            }
        };
        
        $http(request).success(function(data) {
            alert("in suceess http request for delete kids fn");
                
            $scope.result = data.result;
            $scope.resultObtainedFromDelete = true;
               
        });
    }

});

coachApp.controller('editGroups', function($scope, $http, $stateParams, $state) {
    alert("inside edit Groups  controller");
    
    //TODO : get the GroupList from backend or other controller
    $scope.groupName = $stateParams.groupName;
    $scope.groupID = $stateParams.groupID;
    
    $scope.resultObtainedFromServer = false;
    $scope.resultObtainedFromDelete = false;
    
    alert("group = "+ $scope.groupName);
    
    $scope.submit = function(){
        alert("Group to update = " + $scope.groupID);
        
        
        
         var request = {
                    method: "POST",
                    url: "/updateGroup",
                    data: { groupID: $scope.groupID,
                            groupName:$scope.groupName},

                    headers: {
                       "Solace-Reply-Wait-Time-In-ms":"50000" ,
                       'Access-Control-Allow-Origin' : '*',
                       'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
                    }
                  };
        
        alert("sending groupID as : " +request.data.groupID );
        
        $http(request).success(function(data) {
            alert("in suceess http request for submit of update group fn");
                
            $scope.result = data.result;
            $scope.resultObtainedFromServer = true;
               
        });
    }
    
    $scope.goBack = function() {
        $state.go("groups");
    }
    
    $scope.delete = function() {
        alert("Do you really want to delete " + $scope.name + " group's record?");
        
        
        
       /* var request = {
            method: "POST",
            url: "/deleteGroup",
            data: { groupID: $scope.groupID,
                    groupName:$scope.groupName},

            headers: {
               "Solace-Reply-Wait-Time-In-ms":"50000" ,
               'Access-Control-Allow-Origin' : '*',
               'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
            }
        };
        
        $http(request).success(function(data) {
            alert("in suceess http request for delete kids fn");
                
            $scope.result = data.result;
            $scope.resultObtainedFromDelete = true;
               
        });*/
    }

});


coachApp.controller('addGroups', function($scope, $http, $state) {
    alert("inside addGroups  controller");
    //$scope.calendar.date = "Hello, You need to check the calendar for date?";
    
    //alert("Kids name : "+ $scope.kidname);
   $scope.groupName = "GroupName"
    
    $scope.submit = function(){
        //var data = this.formData;
        //TODO get the kids name and perform DB call on the child's name
        
        
        alert("Group Name to add : " + $scope.groupName);
        
        
        //$scope.objectValue.data = $scope.calendar.date;
	  
          var request = {
                    method: "POST",
                    url: "/addGroup",
                    data: { groupName: $scope.groupName},

                    headers: {
                       "Solace-Reply-Wait-Time-In-ms":"50000" ,
                       'Access-Control-Allow-Origin' : '*',
                       'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
                    }
                  };


            
            $http(request).success(function(data) {
                alert("in suceess http request");
                
                $scope.groupList = data.groupList;
                
                
                
            });
            
        
        
    }
    
    $scope.fcsFillTextBox = function() {
        if($scope.groupName="GroupName"){$scope.groupName=""};
    }
    
    $scope.goHome = function() {
        $state.go("home");
    }
    

});

coachApp.controller('manageStudents', function($scope, $http, $state) {
    alert("inside manageStudents  controller");
    
        
    alert("Get Kid List from DB first" );
    getInfo();   
    function getInfo() {
        var request = {
             method: "POST",
             url: "/getKidInfo",
             data: { test: 'test'},

             headers: {
                 "Solace-Reply-Wait-Time-In-ms":"50000" ,
                 'Access-Control-Allow-Origin' : '*',
                 'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
             }
         };

        $http(request).success(function(data) {
            alert("in suceess http request");
            $scope.kidList = data.kidList;

        });
    }
    
    
        
    $scope.goBack = function() {
        $state.go("home");
    }

});

coachApp.controller('attendance', function($scope, $http) {
    alert("inside attendance  controller");
    //$scope.calendar.date = "Hello, You need to check the calendar for date?";
    
    //alert("Kids name : "+ $scope.kidname);
   $scope.groupName = "<<GroupName>>"
    
    $scope.submit = function(){
        //var data = this.formData;
        //TODO get the kids name and perform DB call on the child's name
        
        
        alert("Group Name to add : " + $scope.groupName);
        
        
        //$scope.objectValue.data = $scope.calendar.date;
	  
          var request = {
                    method: "POST",
                    url: "/addGroup",
                    data: { groupName: $scope.groupName},

                    headers: {
                       "Solace-Reply-Wait-Time-In-ms":"50000" ,
                       'Access-Control-Allow-Origin' : '*',
                       'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
                    }
                  };


            
            $http(request).success(function(data) {
                alert("in suceess http request");
                
                $scope.groupList = data.groupList;
                
                
                
            });
            
        
        
    }
    

});

coachApp.controller('groups', function($scope, $http, $state, $rootScope) {
    alert("inside manageGroups  controller");
    
        
    alert("Get Group List from DB first" );
    getInfo();   
    function getInfo() {
        var request = {
             method: "POST",
             url: "/getGroups",
             data: { test: 'test'},

             headers: {
                 "Solace-Reply-Wait-Time-In-ms":"50000" ,
                 'Access-Control-Allow-Origin' : '*',
                 'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
             }
         };

        $http(request).success(function(data) {
            alert("in suceess http request for getGroupInfo");
            $scope.groupList = data.groupList;

        });
    }
    
 
        
    $scope.goBack = function() {
        $rootScope.authenticated=true;
        $state.go("manageClasses");
    }

});

coachApp.controller('schedule', function($scope, $http, $state,$rootScope) {
    alert("inside schedule older controller");
   
    var request = {
        method: "POST",
        url: "/getCalendarAll",
        data: { test: 'test'},

        headers: {
            "Solace-Reply-Wait-Time-In-ms":"50000" ,
            'Access-Control-Allow-Origin' : '*',
            'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
        }
    };


    alert("sending http request with date = "+ request.data.date);
    $http(request).success(function(data) {
        alert("in suceess http request");
        $scope.calendarList = data.Schedule;
                    
    });    
    
    $scope.goBack = function() {
        $rootScope.authenticated=true;
        $state.go("home");
    }
    

});

coachApp.controller('editSchedule', function($scope, $http, $stateParams, $state) {
    alert("inside edit Schedule  controller");
    
    //TODO : get the GroupList from backend or other controller
    $scope.groupName= $stateParams.groupName;
    $scope.date = $stateParams.date;
    $scope.groupID = $stateParams.groupID;
    $scope.time = $stateParams.time;
    $scope.calendarID = $stateParams.calendarID;
    
    $scope.resultObtainedFromServer = false;
    $scope.resultObtainedFromDelete = false;
    
    alert("group = "+ $scope.groupName);
    
    
    $scope.submit = function(){
        alert("Group Name to update : " + $scope.groupName);
        
        var request = {
                    method: "POST",
                    url: "/updateSchedule",
                    data: { calendarID: $scope.calendarID,
                            groupName:$scope.groupName,
                            groupID: $scope.groupID,
                            time:$scope.time,
                            date:$scope.date},

                    headers: {
                       "Solace-Reply-Wait-Time-In-ms":"50000" ,
                       'Access-Control-Allow-Origin' : '*',
                       'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
                    }
                  };
        
        alert("sending calendarID as : " +request.data.calendarID );
        
        $http(request).success(function(data) {
            alert("in suceess http request for submit of update schedule fn");
                
            $scope.result = data.result;
            $scope.resultObtainedFromServer = true;
               
        });
    }
    
    $scope.goBack = function() {
        $state.go("schedule");
    }
    
    $scope.delete = function() {
        alert("Do you really want to delete " + $scope.groupName + "'s entry from calendar?");
        
       /* var request = {
            method: "POST",
            url: "/deleteKid",
            data: { kidID: $scope.kidID,
                    kidName:$scope.name,
                    groupID: $scope.selectedGroup.groupID},

            headers: {
               "Solace-Reply-Wait-Time-In-ms":"50000" ,
               'Access-Control-Allow-Origin' : '*',
               'Access-Control-Allow-Methods' : 'POST, GET, OPTIONS, PUT'
            }
        };
        
        $http(request).success(function(data) {
            alert("in suceess http request for delete kids fn");
                
            $scope.result = data.result;
            $scope.resultObtainedFromDelete = true;
               
        });*/
    }

});

