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
		$(".package_title").click(function() {
			$("#quest_topics").hide();
			$("#quest_game").load("quest.html", function() {
				$(this).fadeIn(500);
				updateProgressbar(40);//progress-bar
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

function updateProgressbar(progressBarWidth) {
	$(".progress-bar").find('span').animate({ width: progressBarWidth + "%" });
	// if(progressBarWidth > 25){
	// 	$(".progress-bar span").animate({background: "green"}, 500);
	// }
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
			//console.log(JSON.stringify(json));
			for(var pack in json.packages) {
				$('#accordion').append('<li class="package"><a href="#" class="package_title">' + json.packages[pack] + '</a></li>');
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
