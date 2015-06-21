$(document).ready(function(){
	$("#quest_topics").load("package.html",function(){

		$('#accordion > li ul').click(function(event){
			event.stopPropagation();
		})
		.hide();
		themesList();
		$('#accordion > li, #accordion > li > ul > li').click(function(){
			var selfClick = $(this).find('ul:first').is(':visible');
			if(!selfClick) {
			  $(this)
				.parent()
				.find('> li ul:visible')
				.slideToggle();
			}
			$(this).find('ul:first').stop(true, true).slideToggle();
		});
		$('body').on('click', '.package_title', function() {
			var packId = $(this).attr('id');
			$("#quest_topics").hide();
			$("#quest_game").load("quest.html", function() {
				$(this).fadeIn(500);
				clearTimeout(timer);//progress-bar
				perc = 0;
				//animateUpdate();
					 $.fn.animate_Text = function() {
					  var string = this.text();
					  return this.each(function(){
					   var $this = $(this);
					   $this.html(string.replace(/./g, '<span class="new">$&</span>'));
					   $this.find('span.new').each(function(i, el){
					    setTimeout(function(){ $(el).addClass('div_opacity'); }, 30 * i);
					   });
					  });
					 };
					$('#quest_title').show();
					$('#quest_title').animate_Text();

				startGame(packId);

				$("#quest_button").click(function() {
					$("#reply_field").load("reply_field.html", function(){
						$("#quest_button").hide();
						$("#quest_button_reply").click(function(){
							$("#reply_field_show").hide();
							$("#quest_button").show();
						});
					})
				});
				$("#icon_a").click(function() {
					$("#quest_topics").fadeIn(500);
					$("#quest_game").empty().hide();
				});
			});
		});
	});
});

var timer = 0;
var perc = 0;

function updateProgress(percentage) {
    $('.progress-bar').find('span').css("width", percentage + "%");
}

function animateUpdate() {
	var updatetime = 100; //1% = 100
    perc++;
    updateProgress(perc);
    if(perc < 100) {
        timer = setTimeout(animateUpdate, updatetime);
    }
}

function themesList() {
	$.ajax({
		url: "svoyak",
		data: {
			method: "packages"
		},
		type: "GET",
		dataType: "json",
		success: function(json) {
			for(var pack in json.packages) {
				$('#accordion').append('<li class="package"><a id="' + pack + '" class="package_title">' + json.packages[pack] + '</a></li>');
			}
		},
		error: function( xhr, status, errorThrown ) {
			console.log( "Sorry, there was a problem!" );
			console.log( "Error: " + errorThrown );
			console.log( "Status: " + status );
			console.dir( xhr );
		},
		complete: function( xhr, status ) {
			console.log("Status: " + status);
		}

	});
}

function startGame(packId) {
	var themeCount = 0;
	var questionCount = 0;

	$.ajax({
		url: "svoyak",

		data: {
			method: "start",
			name: "player",
			packageIndex: packId
		},

		type: "POST",
		dataType: "json",
		success: function(json) {
			themeCount++;
			questionCount++;
			console.log('Package: ' + json.package);
			$('#quest_package_title').text(json.package);
			$('#quest_theme').text(themeCount + '. ' + json.theme);
			$('#quest_title').text(questionCount + '. ' + json.question);
			$('#quest_cost').text(json.points);
			$('#quest_score').text('0');
			console.log('points = ' + json.points);
		},
		error: function(xhr, status, errorThrown) {
			console.log("Sorry, there was a problem! START METHOD");
			console.log("Error: " + errorThrown);
			console.log("Status: " + status);
			console.dir(xhr);
		},
		complete: function(xhr, status) {
			console.log( "Game started" );
		}
	});

	setInterval(function(){
		$.ajax({
			url: "svoyak",
			data: {
				method: "refresh"
			},
			type: "GET",
			dataType: "json",
			success: function(json) {
				/*
				 * TYPE_STOPPED = 0
				 * TYPE_THINK   = 1
				 * TYPE_ANSWER  = 2
				 */
				console.log('timerstatus = ' + json.timerstatus);
				switch (json.timerstatus) {
					case 1:
						console.log(json.time);
						updateProgress((json.time * 100) / 5);
						break;
					case 0:
						updateProgress(0);
						$.ajax({
							url: "svoyak",
							data: {
								method: "question"
							},
							type: "POST",
							dataType: "json",
							success: function(json) {
								console.log('questionCount = ' + questionCount);
								console.log('themeCount = ' + themeCount);
								if(questionCount == 5) {
									questionCount = 1;
									themeCount++;
								} else {
									questionCount++;
								}

								$('#quest_theme').text(themeCount + '. ' + json.theme);
								$('#quest_title').text(questionCount + '. ' + json.question);
								$('#quest_cost').text(json.points);
								$('#quest_score').text(json.points);
								console.log('points = ' + json.points);
							},
							error: function(xhr, status, errorThrown) {
								console.log("Sorry, there was a problem!");
								console.log("Error: " + errorThrown);
								console.log("Status: " + status);
								console.dir(xhr );
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
	}, 500);
}