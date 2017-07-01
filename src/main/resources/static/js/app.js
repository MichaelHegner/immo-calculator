$(document).ready(function() {
	registerEventTableRowClick();	// OVERVIEW PAGE
	registerNewButton();			// OVERVIEW PAGE
	registerDeactivateButton();		// INVESTMENT PAGE
	registerActivateButton();		// INVESTMENT PAGE
});

function registerNewButton() {
	$("#buttonNew").click(function(){
		directToEditUrl();
	});
}

function registerEventTableRowClick() {
	$("tr.property-row").click(function(e){
		var propertyId = $(this).find("td:first").text();
		window.location = getEditUrl(propertyId);
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

function getEditUrl(id) {
	var url = $('#linkEditPage').attr('href');
	
	if(id !== null && id !== undefined) {
		url += "/" + id;
	}

	return url;
}

function getURL() {
	return location.protocol + '//' + location.host + location.pathname
}

function directToEditUrl(id) {
	window.location = getEditUrl(id);
}

function directToUrl(url) {
	window.location = url;
}