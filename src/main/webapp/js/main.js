$("document").ready(function() {
	
    var package;
    var theme;
    var question;
    var points;
    var number = 1;

    function setNewQuestionInfo() {
        $(".package").text(package);
    	$(".theme").text(theme);
    	$(".question").text(question);
    	$(".points").text(points);
    }

    $("#start-btn").on('click', function(){
        $.ajax({
            url: "svoyak",

            data: {
                method: "start",
                name: "player",
                issingle: true
            },

            type: "POST",
            dataType: "json",
            success: function( json ) {
                //console.log(json)
                package = json.package;
                theme = json.theme;
                question = json.question;
                points = json.points;
                setNewQuestionInfo();
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
        $(this).replaceWith('<button class="btn btn-warning" id="end-btn">Закончить игру</button>');
        $("#end-btn").on("click", function(){
            alert("Спасибо за игру!\nВаши очки : " + $("#total").text());
        });

        var interval = 500;

        var intervalId = setInterval(function(){
            $.ajax({
                url: "svoyak",
                data: {
                    method: "refresh"
                },
                type: "GET",
                dataType: "json",
                success: function(json) {
                    /*
                     * ALIVE    = 0
                     * STOPPED  = 1
                     * TIME_OUT = 2
                     */
                    switch (json.timerstatus) {
                        case 0:
                            $("#time").text(json.time);
                            break;
                        case 1:
                            $("#time").text("Введите ответ");
                            break;
                        case 2:
                            $.ajax({
                                url: "svoyak",
                                data: {
                                    method: "question"
                                },
                                type: "POST",
                                dataType: "json",
                                success: function(json) {
                                    console.log(json);
                                    package = json.package;
                                    theme = json.theme;
                                    question = json.question;
                                    points = json.points;
                                    setNewQuestionInfo();
                                },
                                error: function() {
                                    console.log("error: after 'answer send' clicked . POST (method: 'question')");
                                }
                            });
                            break;
                    }

                },
                error: function( xhr, status, errorThrown ) {
                    console.log( "Sorry, there was a problem!" );
                    console.log( "Error: " + errorThrown );
                    console.log( "Status: " + status );
                    console.dir( xhr );
                },
                complete: function( xhr, status) {

                }
            })
        }, interval);


    });

    $("#answer-btn").on("click", function(){
        $.ajax({
            url: "svoyak",
            data: {
                method: "rbclick"
            },
            type: "POST",
            dataType: "json",
            success: function() {
            },
            error: function( xhr, status, errorThrown ) {
            console.log( "Sorry, there was a problem!" );
            console.log( "Error: " + errorThrown );
            console.log( "Status: " + status );
            console.dir( xhr );
            },
            complete: function( xhr, status ) {
                console.log("answer-btn clicked!");
            }     

        });
        $("#answer-btn").attr('disabled', 'disabled');
    });

    $("#send").on("click", function() {
        /*var answerText = ;*/
        $.ajax({
            url: "svoyak",
            data: {
                method: "answer",
                text: $("#answer").val()
            },
            type: "POST",
            dataType: "json",
            success: function(json) {
                $.ajax({
                    url: "svoyak",
                    data: {
                        method: "question"
                    },
                    type: "POST",
                    dataType: "json",
                    success: function(json) {
                        package = json.package;
                        theme = json.theme;
                        question = json.question;
                        points = json.points;
                        setNewQuestionInfo();
                    },
                    error: function() {
                        console.log("error: after 'answer send' clicked . POST (method: 'question')");
                    }
                });
                $("#right").text(json.rightanswer);
                $("#total").text(json.total);

            },
            error: function( xhr, status, errorThrown ) {

            },
            complete: function( xhr, status ) {
                console.log("send button clicked!");
            }     

        });
        $("#answer-btn").removeAttr("disabled");
    });


/*
    var element = $('#example-clock-container');

    var seconds = new ProgressBar.Circle(element, {
        duration: 200,
        color: "#FCB03C",
        trailColor: "#ddd"
    });

    setInterval(function() {
        var second = new Date().getSeconds();
        seconds.animate(second / 60, function() {
            seconds.setText(second);
        });
    }, 1000);
*/
/*
    function animateProgressbar() {
        for(var i = 1 ; i <= 3 ; i++) {
            
        }
    }
*/
});