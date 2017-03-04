'use strict';

App.factory( 'SoundService', [ 'ngAudio', function( ngAudio ) {
	var successSnd;
	var dangerSnd;
	
	/* Load Sound Vars */
	dangerSnd  = ngAudio.load( 'angular/common/snd/danger.mp3');
	successSnd = ngAudio.load( 'angular/common/snd/success.mp3');
	
	return {
		success: function() {
			successSnd = ngAudio.load( 'angular/common/snd/success.mp3');
			successSnd.play();
			return;
		},
		danger: function() {
			dangerSnd  = ngAudio.load( 'angular/common/snd/danger.mp3');
			dangerSnd.play();
			return;
		}
	}
}]);

