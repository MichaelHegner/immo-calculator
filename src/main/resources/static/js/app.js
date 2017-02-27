$(document).ready(function() {
	registerEventTableRowClick();
	registerNewButton();
	registerReturnButton();
});

function registerNewButton() {
	$("#buttonNewProperty").click(function(){
		window.location = '/property/edit';
	});
}

function registerReturnButton() {
	$("#buttonReturn").click(function(){
		window.location = '/property/list';
	});
}

function registerEventTableRowClick() {
	$("tr.property-row").click(function(e){
		var propertyId = $(this).find("td:first").text();
		window.location = '/property/edit/' + propertyId;
	});
}