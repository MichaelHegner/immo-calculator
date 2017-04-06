$(document).ready(function() {
	registerEventTableRowClick();	// OVERVIEW PAGE
	registerNewButton();			// OVERVIEW PAGE
	registerDeactivateButton();		// INVESTMENT PAGE
	registerActivateButton();		// INVESTMENT PAGE
});

function registerNewButton() {
	$("#buttonNewProperty").click(function(){
		window.location = '/property/edit';
	});
}

function registerEventTableRowClick() {
	$("tr.property-row").click(function(e){
		var propertyId = $(this).find("td:first").text();
		window.location = '/property/edit/' + propertyId;
	});
}

function registerDeactivateButton() {
	$("#buttonDeactivate").click(function(){
		window.location = getURL() + '?deactivate=true';
	});
}

function registerActivateButton() {
	$(".btnActivate").each(function(){
		$(this).click(function(){
			var creditId = $(this).parent().find('input[type=hidden]').val();
			window.location =getURL() + '?activate=' + creditId;
		});
	});
}

function getURL() {
	return location.protocol + '//' + location.host + location.pathname
}