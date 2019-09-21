$(document).ready(function() {
	registerNewButton();			// OVERVIEW PAGE
	registerSubmitButton();			// EDIT PAGE
	registerDeactivateButton();		// FINANCE PAGE
	registerActivateButton();		// FINANCE PAGE
	registerEditButton();			// LIST PAGE
	registerDeleteButton();			// LIST PAGE
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

function registerEditButton() {
	$(".btnEdit").each(function(){
		$(this).click(function(){
			var propertyId = $(this).closest('tr').find("td:first").text();
			directToEditUrl(propertyId);
		});
	});
}

function registerDeleteButton() {
	$(".btnDelete").each(function(){
		$(this).click(function(){
			// TODO: LINK
			var propertyId = $(this).closest('tr').find("td:first").text();
			var url = getBaseURL() + getDeleteUrl();
			
			$.ajax({
			   url: getBaseURL() + getDeleteUrl() + "/" + propertyId,
			   type: 'DELETE',
			   success: function(response) {
				   directToListUrl();
			   }
			});
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

function getDeleteUrl() {
	return $('#linkDeletePage').attr('href');
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

function directToDeleteUrl(id) {
	directToUrl(getDeleteUrl(id));
}

function directToUrl(url) {
	window.location = url;
}