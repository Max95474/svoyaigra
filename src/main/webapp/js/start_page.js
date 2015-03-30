$("document").ready(function() {
    $(".btn").on("click", function() {
        $.ajax({
            url: "login",

            data: {
                name: $("input").val(),
                issingle: true
            },

            type: "POST",
            dataType: "json",
            success: function( json ) {

            },
            error: function( xhr, status, errorThrown ) {
                console.log( "Sorry, there was a problem! START METHOD" );
                console.log( "Error: " + errorThrown );
                console.log( "Status: " + status );
                console.dir( xhr );
            },
            complete: function( xhr, status ) {
                console.log( "The first request is complete!" );
            }
        });
        window.location.href = 'game.html';
    });
});