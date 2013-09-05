'use strict';

/**
 * provides common animations for the fixed-header-box ( part of the base )
 */
var animate = function() {

    var duration = 200;
    var delay = 2000;

    function slide(id, down, up, complete) {
        if (down && up) $(id).slideDown(duration).delay(delay).slideUp(duration, function() { if (complete !== undefined) complete(); });
        else if (down && !up) $(id).slideDown(duration, function() { if (complete !== undefined) complete(); })
        else if (!down && up) $(id).slideUp(duration, function() { if (complete !== undefined) complete(); })
    }

    return {
        slideDown:      function(id, complete) { slide(id, true, false, complete) },
        slideUp:        function(id, complete) { slide(id, false, true, complete) },
        slideDownAndUp: function(id, complete) { slide(id, true, true, complete) }
    }
}
var Animate = animate();

var messageAPI = function() {

    function performAnimation(msg, style) {
        var id = '#global-message'
        $(id).addClass(style);
        $('#global-message-span').html(msg);
        Animate.slideDownAndUp(id, function() {
            $(id).removeClass(style);
        })
    }

    return {
        info :       function(msg) { performAnimation(msg, 'success'); },
        warn :       function(msg) { performAnimation(msg, 'secondary'); },
        error:       function(msg) { performAnimation(msg, 'alert'); },
        errorCommon: function() { performAnimation('Es ist ein Fehler aufgetreten.', 'alert'); }
    }
 };

var MessageAPI = messageAPI();

// ---------------------------------------------------------------------------------------------------------------------

var continueComposingAPI = function() {
    return {
        showOption: function() {
            console.log('SHOW OPTION');
            Animate.slideDown('#continue-composing');
        }, // TODO remove LOG
        hideOption: function() {
            console.log('HIDE OPTION');
            Animate.slideUp('#continue-composing');
        } // TODO remove LOG
    }
}
var ContinueComposingAPI = continueComposingAPI();