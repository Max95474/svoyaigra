$(document).ready(function(){
	$("#quest_topics").load("package.html",function(){
		$('#accordion > li ul').click(function(event){
			event.stopPropagation();
		})
		.hide();
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
		$(".theme_item").click(function() {
			$("#quest_topics").hide();
			$("#quest_game").load("quest.html", function() {
				$(this).fadeIn(500);
				$("#quest_button").click(function() {
					$("#reply_field").load("reply_field.html", function(){
						$("#quest_button").hide();
						$("reply_field_show").fadeIn(500);
					});
				});
				$("#icon_a").click(function() {
					$("#quest_topics").fadeIn(500);
					$("#quest_game").empty().hide();
				});
			});
		});
	});
});







	