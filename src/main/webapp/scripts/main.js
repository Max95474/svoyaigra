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

		$(".package_title").click(function() {
			$("#quest_topics").hide();
			$("#quest_game").load("quest.html", function() {
				$(this).fadeIn(500);
				clearTimeout(timer);//progress-bar
				perc = 0;
				animateUpdate();
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
