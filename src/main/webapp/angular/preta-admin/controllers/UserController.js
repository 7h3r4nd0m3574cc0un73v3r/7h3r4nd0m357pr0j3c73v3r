App.controller( 'UserController', [ '$rootScope', '$scope', '$state', '$http', '$mdDialog', 'UserService', 'ToastService', 'SoundService',
                                     function( $rootScope, $scope, $state, $http, $mdDialog, UserService, ToastService, SoundService) {
	
	/* Loading Mutex */
	$scope.isLoading = true;
	
	/* Load Function */
	$scope.loadEntity = function() {
		$scope.isLoading = true;
		UserService.loadLoggedUser()
					.then( function( response) {
						console.log( response);
						$scope.entity = response;
						
						$rootScope.loggedUser = $scope.entity;
						
						/* Convert Time Stamp to Javascript Date Object for md-datepicker */
						$scope.entity.userInfo.birthDay = new Date( $scope.entity.userInfo.birthDay);
						
						$scope.isLoading = false;
					}, function( response) {
						consoler.error( response);
						$scope.isLoading = false;
					});
	};
	
	$scope.loadEntity();
	
	/* Modal */
	$scope.showDialog = function(ev) {
	    $mdDialog.show({
	        controller: DialogController,
	        templateUrl: 'angular/common/dialogs/new-account.tmpl.html',
	        parent: angular.element(document.querySelector('#popupContainer')),
	        targetEvent: ev,
	        clickOutsideToClose: true,
	        fullscreen: false,
	        zIndex: 9999
	      })
	      .then( function(answer) {
	    	  console.log( answer);
	          if( answer) {
	        	  $scope.loadEntity();
	          }
	      }, function() {
	          
	      });
    };

	$scope.showPWDialog = function(ev) {
	    $mdDialog.show({
	        controller: DialogPWChangeController,
	        templateUrl: 'angular/common/dialogs/change-password.tmpl.html',
	        parent: angular.element(document.body),
	        targetEvent: ev,
	        clickOutsideToClose: true,
	        fullscreen: false,
	      })
	      .then( function(answer) {
	    	  
	      }, function() {
	          
	      });
    };
	    
	function DialogController( $scope, $rootScope, $mdDialog) {
		/* Init Vars */
		$scope.form = { newEAccount: { account: null, isDefault: true}};
		$scope.formErrors = { invalidAccount: false, accountConflict: false};
		
		$scope.hide = function() {
		  $mdDialog.hide();
	    };

	    $scope.cancel = function() {
	      $mdDialog.cancel();
	    };

	    $scope.answer = function( answer) {
	      if( answer) {
	    	  console.log( $scope.form.newEAccount);
	    	  UserService.addEAccount( $scope.form.newEAccount)
	    	  			 .then( function( response) {
	    	  				 ToastService.openToast( 'info', 'Votre nouveau compte de monnaie numérique a été créé avec succès.', 'Compte créé');
	    	  				 SoundService.success();
	    	  				 $mdDialog.hide( true);
	    	  			 }, function( response) {
	    	  				 console.error( response);
	    	  				ToastService.openToast( 'error', 'Une erreur est survenue lors de la création du compte. Veuillez vérifier les informations fournies', 'Erreur');
	    	  				SoundService.danger();
	    	  			 })
	      }
	    };
	}

	function DialogPWChangeController( $scope, $rootScope, $mdDialog) {
		/* Init Vars */
		$scope.form = { currentPassword: null, password: null, confirmPassword: null};
		$scope.formErrors;
		
		$scope.hide = function() {
		  $mdDialog.hide();
	    };

	    $scope.cancel = function() {
	      $mdDialog.cancel();
	    };

	    $scope.answer = function( answer) {
	      if( answer) {
	    	  if( isValid()) {
		    	  UserService.changePassword( $scope.form)
		    	  			 .then( function( response) {
		    	  				 ToastService.clearToasts();
		    	  				 ToastService.openToast( 'info', 'Votre mot de passe a \xE9t\xE9 changé avec succès.', 'Mot de passe changé');
		    	  				 SoundService.success();
		    	  				 $mdDialog.hide( true);
		    	  			 }, function( response) {
		    	  				ToastService.clearToasts();
		    	  				ToastService.openToast( 'error', 'Une erreur est survenue lors de la création du compte. Veuillez vérifier les informations fournies', 'Erreur');
		    	  				SoundService.danger();
		    	  				if( response.code == 400) {
		    	  					initFormErrors();
		    	  					angular.forEach( response.data.errors, function( error) {
		    	  						if( error.field == 'currentPassword' && error.code == 'Invalid') {
		    	  							$scope.form.currentPassword.$setValidity("currentPassword", false);
		    	  							$scope.formErrors.currentPassword.invalid = true;
		    	  						}
		    	  						if( error.field == 'password' && error.code == 'Mismatch') {
		    	  							$scope.pwForm.confirmPassword.$setValidity("password", false);
		    	  							$scope.pwForm.confirmPassword.$setValidity("confirmPassword", false);
		    	  							$scope.formErrors.password.mismatch = true;
		    	  						}
		    	  					});
		    	  				}
		    	  			 });
	    	  }
	    	  else {
	    		  	console.error( $scope.formErrors);
	  				ToastService.clearToasts();
	  				ToastService.openToast( 'error', 'Une erreur est survenue lors de la création du compte. Veuillez vérifier les informations fournies', 'Erreur');
	  				SoundService.danger();
	    	  }
	      }
	    };
	    
	    function initFormErrors() {
	    	$scope.formErrors = {
	    			currentPassword: {
	    				invalid: false
	    			},
	    			password: {
	    				mismatch: false
	    			}
	    	}
	    }
	    
	    function isValid() {
	    	initFormErrors();
	    	if( $scope.form.currentPassword == null || $scope.form.currentPassword == '')
	    		return false;
	    	if( $scope.form.password == null || $scope.form.password == '')
	    		return false;
	    	if( $scope.form.confirmPassword == null || $scope.form.confirmPassword == '')
	    		return false;
	    	if( $scope.form.password.length < 8)
	    		return false;
	    	if( $scope.form.confirmPassword.length < 8)
	    		return false;
	    	if( $scope.form.password != $scope.form.confirmPassword) {
	    		/* $scope.pwForm.password.$setValidity( "password", false);
	    		$scope.pwForm.confirmPassword.$setValidity( "confirmPassword", false); */
	    		$scope.formErrors.password.mismatch = true;
	    		return false;
	    	}
	    	
	    	return true;
	    }
	}
	
	$scope.updateProfile = function() {
		$scope.formErrors = {};
		if( $scope.entity.userInfo.email == null || $scope.entity.userInfo.email == ""){
			$scope.formErrors.invalidEmail = true;
			return;
		}
		
		$scope.isLoading = true;
		
		UserService.updateProfile( $scope.entity)
				   .then( function( response) {
					   $scope.loadEntity();
					   $scope.isLoading = false;
				   }, function( response) {
					  console.error( response);
					  $scope.isLoading = false;
				   });
	};
	
	/* EAccount Management */
	$scope.setEAccountAsDefault = function( id) {
		$scope.isLoading = true;
		
		UserService.setEAccountAsDefault( id)
				   .then( function( response) {
					   console.log( response);
					   $scope.loadEntity();
					   $scope.isLoading = false;
				   }, function( response) {
					   console.error( response);
					   $scope.isLoading = false;
				   });
	}
	
	/* Confirm Dialog */
	$scope.showDeleteModal = function(id, ev) {
	    // Appending dialog to document.body to cover sidenav in docs app
	    var confirm = $mdDialog.confirm()
	          .title('Confirmation de suppression')
	          .textContent('Etes vous sur de vouloir supprimer ce compte ?')
	          .ariaLabel('Confirmation de supression')
	          .targetEvent(ev)
	          .ok('Oui')
	          .cancel('Non');

	    $mdDialog.show(confirm).then(function() {
	    	$scope.isLoading = true;
			
			UserService.deleteEAccount( id)
						.then( function( response) {
							$scope.isLoading = false;
							$scope.loadEntity();
							ToastService.openToast( 'info', 'Votre compte a bien x\E9t\xE9 supprim\xE9', 'Compte supprim\xE9');
							SoundService.success();
						}, function( response) {
							console.error( response);
							$scope.isLoading = false;
						});
	    }, function() {
	    	
	    });
	  };
	
	/* Init Country List */
	$scope.countries = [ 
	                    {name: 'Afghanistan', code: 'AF'},
	                    {name: 'Åland Islands', code: 'AX'}, 
	                    {name: 'Albania', code: 'AL'}, 
	                    {name: 'Algeria', code: 'DZ'}, 
	                    {name: 'American Samoa', code: 'AS'},
	                    {name: 'AndorrA', code: 'AD'}, 
	                    {name: 'Angola', code: 'AO'}, 
	                    {name: 'Anguilla', code: 'AI'}, 
	                    {name: 'Antarctica', code: 'AQ'}, 
	                    {name: 'Antigua and Barbuda', code: 'AG'}, 
	                    {name: 'Argentina', code: 'AR'}, 
	                    {name: 'Armenia', code: 'AM'}, 
	                    {name: 'Aruba', code: 'AW'}, 
	                    {name: 'Australia', code: 'AU'}, 
	                    {name: 'Austria', code: 'AT'}, 
	                    {name: 'Azerbaijan', code: 'AZ'}, 
	                    {name: 'Bahamas', code: 'BS'}, 
	                    {name: 'Bahrain', code: 'BH'}, 
	                    {name: 'Bangladesh', code: 'BD'}, 
	                    {name: 'Barbados', code: 'BB'}, 
	                    {name: 'Belarus', code: 'BY'}, 
	                    {name: 'Belgium', code: 'BE'}, 
	                    {name: 'Belize', code: 'BZ'}, 
	                    {name: 'Benin', code: 'BJ'}, 
	                    {name: 'Bermuda', code: 'BM'}, 
	                    {name: 'Bhutan', code: 'BT'}, 
	                    {name: 'Bolivia', code: 'BO'}, 
	                    {name: 'Bosnia and Herzegovina', code: 'BA'}, 
	                    {name: 'Botswana', code: 'BW'}, 
	                    {name: 'Bouvet Island', code: 'BV'}, 
	                    {name: 'Brazil', code: 'BR'}, 
	                    {name: 'British Indian Ocean Territory', code: 'IO'}, 
	                    {name: 'Brunei Darussalam', code: 'BN'}, 
	                    {name: 'Bulgaria', code: 'BG'}, 
	                    {name: 'Burkina Faso', code: 'BF'}, 
	                    {name: 'Burundi', code: 'BI'}, 
	                    {name: 'Cambodia', code: 'KH'}, 
	                    {name: 'Cameroon', code: 'CM'}, 
	                    {name: 'Canada', code: 'CA'}, 
	                    {name: 'Cape Verde', code: 'CV'}, 
	                    {name: 'Cayman Islands', code: 'KY'}, 
	                    {name: 'Central African Republic', code: 'CF'}, 
	                    {name: 'Chad', code: 'TD'}, 
	                    {name: 'Chile', code: 'CL'}, 
	                    {name: 'China', code: 'CN'}, 
	                    {name: 'Christmas Island', code: 'CX'}, 
	                    {name: 'Cocos (Keeling) Islands', code: 'CC'}, 
	                    {name: 'Colombia', code: 'CO'}, 
	                    {name: 'Comoros', code: 'KM'}, 
	                    {name: 'Congo', code: 'CG'}, 
	                    {name: 'Congo, The Democratic Republic of the', code: 'CD'}, 
	                    {name: 'Cook Islands', code: 'CK'}, 
	                    {name: 'Costa Rica', code: 'CR'}, 
	                    {name: 'Cote D\'Ivoire', code: 'CI'}, 
	                    {name: 'Croatia', code: 'HR'}, 
	                    {name: 'Cuba', code: 'CU'}, 
	                    {name: 'Cyprus', code: 'CY'}, 
	                    {name: 'Czech Republic', code: 'CZ'}, 
	                    {name: 'Denmark', code: 'DK'}, 
	                    {name: 'Djibouti', code: 'DJ'}, 
	                    {name: 'Dominica', code: 'DM'}, 
	                    {name: 'Dominican Republic', code: 'DO'}, 
	                    {name: 'Ecuador', code: 'EC'}, 
	                    {name: 'Egypt', code: 'EG'}, 
	                    {name: 'El Salvador', code: 'SV'}, 
	                    {name: 'Equatorial Guinea', code: 'GQ'}, 
	                    {name: 'Eritrea', code: 'ER'}, 
	                    {name: 'Estonia', code: 'EE'}, 
	                    {name: 'Ethiopia', code: 'ET'}, 
	                    {name: 'Falkland Islands (Malvinas)', code: 'FK'}, 
	                    {name: 'Faroe Islands', code: 'FO'}, 
	                    {name: 'Fiji', code: 'FJ'}, 
	                    {name: 'Finland', code: 'FI'}, 
	                    {name: 'France', code: 'FR'}, 
	                    {name: 'French Guiana', code: 'GF'}, 
	                    {name: 'French Polynesia', code: 'PF'}, 
	                    {name: 'French Southern Territories', code: 'TF'}, 
	                    {name: 'Gabon', code: 'GA'}, 
	                    {name: 'Gambia', code: 'GM'}, 
	                    {name: 'Georgia', code: 'GE'}, 
	                    {name: 'Germany', code: 'DE'}, 
	                    {name: 'Ghana', code: 'GH'}, 
	                    {name: 'Gibraltar', code: 'GI'}, 
	                    {name: 'Greece', code: 'GR'}, 
	                    {name: 'Greenland', code: 'GL'}, 
	                    {name: 'Grenada', code: 'GD'}, 
	                    {name: 'Guadeloupe', code: 'GP'}, 
	                    {name: 'Guam', code: 'GU'}, 
	                    {name: 'Guatemala', code: 'GT'}, 
	                    {name: 'Guernsey', code: 'GG'}, 
	                    {name: 'Guinea', code: 'GN'}, 
	                    {name: 'Guinea-Bissau', code: 'GW'}, 
	                    {name: 'Guyana', code: 'GY'}, 
	                    {name: 'Haiti', code: 'HT'}, 
	                    {name: 'Heard Island and Mcdonald Islands', code: 'HM'}, 
	                    {name: 'Holy See (Vatican City State)', code: 'VA'}, 
	                    {name: 'Honduras', code: 'HN'}, 
	                    {name: 'Hong Kong', code: 'HK'}, 
	                    {name: 'Hungary', code: 'HU'}, 
	                    {name: 'Iceland', code: 'IS'}, 
	                    {name: 'India', code: 'IN'}, 
	                    {name: 'Indonesia', code: 'ID'}, 
	                    {name: 'Iran, Islamic Republic Of', code: 'IR'}, 
	                    {name: 'Iraq', code: 'IQ'}, 
	                    {name: 'Ireland', code: 'IE'}, 
	                    {name: 'Isle of Man', code: 'IM'}, 
	                    {name: 'Israel', code: 'IL'}, 
	                    {name: 'Italy', code: 'IT'}, 
	                    {name: 'Jamaica', code: 'JM'}, 
	                    {name: 'Japan', code: 'JP'}, 
	                    {name: 'Jersey', code: 'JE'}, 
	                    {name: 'Jordan', code: 'JO'}, 
	                    {name: 'Kazakhstan', code: 'KZ'}, 
	                    {name: 'Kenya', code: 'KE'}, 
	                    {name: 'Kiribati', code: 'KI'}, 
	                    {name: 'Korea, Democratic People\'S Republic of', code: 'KP'}, 
	                    {name: 'Korea, Republic of', code: 'KR'}, 
	                    {name: 'Kuwait', code: 'KW'}, 
	                    {name: 'Kyrgyzstan', code: 'KG'}, 
	                    {name: 'Lao People\'S Democratic Republic', code: 'LA'}, 
	                    {name: 'Latvia', code: 'LV'}, 
	                    {name: 'Lebanon', code: 'LB'}, 
	                    {name: 'Lesotho', code: 'LS'}, 
	                    {name: 'Liberia', code: 'LR'}, 
	                    {name: 'Libyan Arab Jamahiriya', code: 'LY'}, 
	                    {name: 'Liechtenstein', code: 'LI'}, 
	                    {name: 'Lithuania', code: 'LT'}, 
	                    {name: 'Luxembourg', code: 'LU'}, 
	                    {name: 'Macao', code: 'MO'}, 
	                    {name: 'Macedonia, The Former Yugoslav Republic of', code: 'MK'}, 
	                    {name: 'Madagascar', code: 'MG'}, 
	                    {name: 'Malawi', code: 'MW'}, 
	                    {name: 'Malaysia', code: 'MY'}, 
	                    {name: 'Maldives', code: 'MV'}, 
	                    {name: 'Mali', code: 'ML'}, 
	                    {name: 'Malta', code: 'MT'}, 
	                    {name: 'Marshall Islands', code: 'MH'}, 
	                    {name: 'Martinique', code: 'MQ'}, 
	                    {name: 'Mauritania', code: 'MR'}, 
	                    {name: 'Mauritius', code: 'MU'}, 
	                    {name: 'Mayotte', code: 'YT'}, 
	                    {name: 'Mexico', code: 'MX'}, 
	                    {name: 'Micronesia, Federated States of', code: 'FM'}, 
	                    {name: 'Moldova, Republic of', code: 'MD'}, 
	                    {name: 'Monaco', code: 'MC'}, 
	                    {name: 'Mongolia', code: 'MN'}, 
	                    {name: 'Montserrat', code: 'MS'}, 
	                    {name: 'Morocco', code: 'MA'}, 
	                    {name: 'Mozambique', code: 'MZ'}, 
	                    {name: 'Myanmar', code: 'MM'}, 
	                    {name: 'Namibia', code: 'NA'}, 
	                    {name: 'Nauru', code: 'NR'}, 
	                    {name: 'Nepal', code: 'NP'}, 
	                    {name: 'Netherlands', code: 'NL'}, 
	                    {name: 'Netherlands Antilles', code: 'AN'}, 
	                    {name: 'New Caledonia', code: 'NC'}, 
	                    {name: 'New Zealand', code: 'NZ'}, 
	                    {name: 'Nicaragua', code: 'NI'}, 
	                    {name: 'Niger', code: 'NE'}, 
	                    {name: 'Nigeria', code: 'NG'}, 
	                    {name: 'Niue', code: 'NU'}, 
	                    {name: 'Norfolk Island', code: 'NF'}, 
	                    {name: 'Northern Mariana Islands', code: 'MP'}, 
	                    {name: 'Norway', code: 'NO'}, 
	                    {name: 'Oman', code: 'OM'}, 
	                    {name: 'Pakistan', code: 'PK'}, 
	                    {name: 'Palau', code: 'PW'}, 
	                    {name: 'Palestinian Territory, Occupied', code: 'PS'}, 
	                    {name: 'Panama', code: 'PA'}, 
	                    {name: 'Papua New Guinea', code: 'PG'}, 
	                    {name: 'Paraguay', code: 'PY'}, 
	                    {name: 'Peru', code: 'PE'}, 
	                    {name: 'Philippines', code: 'PH'}, 
	                    {name: 'Pitcairn', code: 'PN'}, 
	                    {name: 'Poland', code: 'PL'}, 
	                    {name: 'Portugal', code: 'PT'}, 
	                    {name: 'Puerto Rico', code: 'PR'}, 
	                    {name: 'Qatar', code: 'QA'}, 
	                    {name: 'Reunion', code: 'RE'}, 
	                    {name: 'Romania', code: 'RO'}, 
	                    {name: 'Russian Federation', code: 'RU'}, 
	                    {name: 'RWANDA', code: 'RW'}, 
	                    {name: 'Saint Helena', code: 'SH'}, 
	                    {name: 'Saint Kitts and Nevis', code: 'KN'}, 
	                    {name: 'Saint Lucia', code: 'LC'}, 
	                    {name: 'Saint Pierre and Miquelon', code: 'PM'}, 
	                    {name: 'Saint Vincent and the Grenadines', code: 'VC'}, 
	                    {name: 'Samoa', code: 'WS'}, 
	                    {name: 'San Marino', code: 'SM'}, 
	                    {name: 'Sao Tome and Principe', code: 'ST'}, 
	                    {name: 'Saudi Arabia', code: 'SA'}, 
	                    {name: 'Senegal', code: 'SN'}, 
	                    {name: 'Serbia and Montenegro', code: 'CS'}, 
	                    {name: 'Seychelles', code: 'SC'}, 
	                    {name: 'Sierra Leone', code: 'SL'}, 
	                    {name: 'Singapore', code: 'SG'}, 
	                    {name: 'Slovakia', code: 'SK'}, 
	                    {name: 'Slovenia', code: 'SI'}, 
	                    {name: 'Solomon Islands', code: 'SB'}, 
	                    {name: 'Somalia', code: 'SO'}, 
	                    {name: 'South Africa', code: 'ZA'}, 
	                    {name: 'South Georgia and the South Sandwich Islands', code: 'GS'}, 
	                    {name: 'Spain', code: 'ES'}, 
	                    {name: 'Sri Lanka', code: 'LK'}, 
	                    {name: 'Sudan', code: 'SD'}, 
	                    {name: 'Suriname', code: 'SR'}, 
	                    {name: 'Svalbard and Jan Mayen', code: 'SJ'}, 
	                    {name: 'Swaziland', code: 'SZ'}, 
	                    {name: 'Sweden', code: 'SE'}, 
	                    {name: 'Switzerland', code: 'CH'}, 
	                    {name: 'Syrian Arab Republic', code: 'SY'}, 
	                    {name: 'Taiwan, Province of China', code: 'TW'}, 
	                    {name: 'Tajikistan', code: 'TJ'}, 
	                    {name: 'Tanzania, United Republic of', code: 'TZ'}, 
	                    {name: 'Thailand', code: 'TH'}, 
	                    {name: 'Timor-Leste', code: 'TL'}, 
	                    {name: 'Togo', code: 'TG'}, 
	                    {name: 'Tokelau', code: 'TK'}, 
	                    {name: 'Tonga', code: 'TO'}, 
	                    {name: 'Trinidad and Tobago', code: 'TT'}, 
	                    {name: 'Tunisia', code: 'TN'}, 
	                    {name: 'Turkey', code: 'TR'}, 
	                    {name: 'Turkmenistan', code: 'TM'}, 
	                    {name: 'Turks and Caicos Islands', code: 'TC'}, 
	                    {name: 'Tuvalu', code: 'TV'}, 
	                    {name: 'Uganda', code: 'UG'}, 
	                    {name: 'Ukraine', code: 'UA'}, 
	                    {name: 'United Arab Emirates', code: 'AE'}, 
	                    {name: 'United Kingdom', code: 'GB'}, 
	                    {name: 'United States', code: 'US'}, 
	                    {name: 'United States Minor Outlying Islands', code: 'UM'}, 
	                    {name: 'Uruguay', code: 'UY'}, 
	                    {name: 'Uzbekistan', code: 'UZ'}, 
	                    {name: 'Vanuatu', code: 'VU'}, 
	                    {name: 'Venezuela', code: 'VE'}, 
	                    {name: 'Viet Nam', code: 'VN'}, 
	                    {name: 'Virgin Islands, British', code: 'VG'}, 
	                    {name: 'Virgin Islands, U.S.', code: 'VI'}, 
	                    {name: 'Wallis and Futuna', code: 'WF'}, 
	                    {name: 'Western Sahara', code: 'EH'}, 
	                    {name: 'Yemen', code: 'YE'}, 
	                    {name: 'Zambia', code: 'ZM'}, 
	                    {name: 'Zimbabwe', code: 'ZW'} 
	                  ];
}]);