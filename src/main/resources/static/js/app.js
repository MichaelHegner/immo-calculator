$(document).ready(function() {
	registerEventTableRowClick();	// OVERVIEW PAGE
	registerNewButton();			// OVERVIEW PAGE
	registerDeactivateButton();		// INVESTMENT PAGE
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
		window.location = window.location.href + '?deactivate=true';
	});
}