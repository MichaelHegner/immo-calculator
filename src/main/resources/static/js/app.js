$(document).ready(function() {
	registerEventTableRowClick();	// OVERVIEW PAGE
	registerNewButton();			// OVERVIEW PAGE
	registerSubmitButton();			// EDIT PAGE
	registerDeactivateButton();		// INVESTMENT PAGE
	registerActivateButton();		// INVESTMENT PAGE
});

function registerNewButton() {
	$("#buttonNew").click(function(){
		directToNewUrl();
	});
}

function registerSubmitButton() {
	$("#buttonSubmit").click(function(){
		$('form').submit();
	});
}

function registerEventTableRowClick() {
	$("tr.property-row").click(function(e){
		var propertyId = $(this).find("td:first").text();
		directToSelectUrl(propertyId);
	});
}

function registerDeactivateButton() {
	$(".buttonDeactivate").each(function(){
		$(this).click(function(){
			// TODO: LINK
			var creditId = $(this).parent().find('input[type=hidden]').val();
			window.location = getBaseURL() + '/investment/edit/' + propertyId + '/credit/' + creditId + '/swap';
		});
	});
}

function registerActivateButton() {
	$(".btnActivate").each(function(){
		$(this).click(function(){
			// TODO: LINK
			var propertyId = $("#propertyId").val();
			var creditId = $(this).parent().find('input[type=hidden]').val();
			window.location = getBaseURL() + '/investment/edit/' + propertyId + '/credit/' + creditId + '/swap';
		});
	});
}

function getSelectUrl(id) {
	var url = $('#linkSelectFromTable').attr('href');
	
	if(id !== null && id !== undefined) {
		url += "/" + id;
	}

	return url;
}

function getNewUrl() {
	return $('#linkNewPage').attr('href');
}

function getListUrl() {
	return $('#linkListPage').attr('href');
}

function getEditUrl(id) {
	var url = $('#linkEditPage').attr('href');
	
	if(id !== null && id !== undefined) {
		url += "/" + id;
	}

	return url;
}

function getBaseURL() {
	return location.protocol + '//' + location.host;
}

function getURL() {
	return getBaseURL() + location.pathname
}

function directToSelectUrl(id) {
	if(getSelectUrl() !== undefined) { // CHECK SELECT URL EXISTING
		window.location = getSelectUrl(id);
	}
}

function directToListUrl() {
	directToUrl(getListUrl());
}

function directToNewUrl() {
	directToUrl(getNewUrl());
}

function directToEditUrl(id) {
	directToUrl(getEditUrl(id));
}

function directToUrl(url) {
	window.location = url;
}