$(document).ready(function() {
	registerEventModalDialog();
	registerEventTableRowClick();
	handleModalDialog();
	registerFormSubmitEvent();
});
			
function registerEventModalDialog() {
	$("#buttonNewProperty").click(function(){
		var params = [{name:'displayEdit', value:true}];
		window.location = './list?' + $.param(params);
	});
	$("#buttonCloseEditForm").click(function(){
		window.location = './list';
	});
}


function registerEventTableRowClick() {
	$("tr.property-row").click(function(e){
		var params = [{name:'propertyId', value:$(this).find("td:first").text()}, {name:'displayEdit', value:true}];
		window.location = './list?' + $.param(params);
	});
}


function handleModalDialog() {
	var currentURL = $(location).attr('href');
	var displayEdit = getParameterByName("displayEdit", currentURL);
	
	if(undefined !== displayEdit) {
		if("true" === displayEdit) {
			displayModalDialog();
		}
	}
}

function displayModalDialog() {
	$("#dialogEditProperty").modal({"backdrop": "static"});
}

function registerFormSubmitEvent() {
	var $form = $("#propertyEditForm");
	$form.validate({
		ignore: false,
		invalidHandler: function(form, validator) {
			var errorList = validator.errorList;
			var numberOfErrors = $(errorList.length)[0];

			if(0 < numberOfErrors) {
				var errorPanel = $(errorList[0].element).closest(".panel-collapse");
				$('.panel-collapse').each(function(i, panel){
					var errorPanelId = errorPanel.attr('id');
					var currentPanelId = $(panel).attr('id');
					if(currentPanelId === errorPanelId) {
						$(panel).collapse('show');
					} else {
						$(panel).collapse('hide');
					}
				});
			}
		}		
	});
}
