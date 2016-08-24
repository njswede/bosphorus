$(document ).on( "tabsload", function( event, ui ) {
  console.log(arguments);
  ui.panel.enhanceWithin();
});

$(function () {
	  var token = $("meta[name='_csrf']").attr("content");
	  var header = $("meta[name='_csrf_header']").attr("content");
	  $(document).ajaxSend(function(e, xhr, options) {
	    xhr.setRequestHeader(header, token);
	  });
	});


function registerPagination(file, parent, size) {
	console.log(this)
}

function changeListPage(event, file, parent, value, size) {
	event.preventDefault()
	$.ajax({url:file + "?limit=" + size + " + &start=" + value, success: function(response) {
        $(parent).replaceWith($(response).find(parent))
        $(parent).enhanceWithin()
    }})
}

function chainPopup(id, nextId, uri) {
	$( id ).popup( 'close' );  
    $( id ).bind({popupafterclose: function(event, ui) 
            { 
                $( nextId ).popup( "open" );
            }
            });     
}