$(document).ready(function() {
    $("input[name$='entrytype']").click(function() {
        var entryselection = $(this).val();
		$('#customFile').val('');
		$('#textEntry').val('');
        $('#entrybytext').hide();
        $('#entrybyimage').hide();
        $('#'+entryselection).show();
        $('#submitentry').show();
    });
});

function createentry(event) {
	event.preventDefault();
	if(($('#customFile').val() === '') && $('#textEntry').val() === ''){
		$('#errorModal .modal-body').text('Make an entry in text or upload an image to create the entry');
		$('#errorModal').modal('show');
		return;
	}
	var form = $('.form-signin')[0];
	var data = new FormData(form);
	var token = 'Bearer '+$('#token').val();
	$.ajax({
		  url: "http://localhost:8080/guest/create",
		  type: "POST",
		  enctype: 'multipart/form-data',
		  headers: { 'Authorization':  token},
		  contentType: false,
		  processData: false,
		  cache: false,
		  data : data,
		  error: function(err) {
			 $('#customFile').val('');
			 $('#textEntry').val('');
		     $('#errorModal .modal-body').text(err);
			 $('#errorModal').modal('show');
		  },
		  success: function(data) {
			  $('#customFile').val('');
			  $('#textEntry').val('');
			  $('#successModal .modal-body').text(data);
			  $('#successModal').modal('show');
		  }
		});

  }

function validateFileUploaded(){
	var file = $('#customFile')[0].files[0];
	if (file){
		var fileName=file.name;
		if(fileName.indexOf(".") >= 0){
			var idxDot = fileName.lastIndexOf(".") + 1;
		    var extFile = fileName.substr(idxDot, fileName.length).toLowerCase();
		    if(!(extFile=="jpg" || extFile=="jpeg" || extFile=="png")){
		    	$('#customFile').val('');
		    	$('#errorModal .modal-body').text('Only jpg/jpeg and png files are allowed!');
		        $('#errorModal').modal('show');
		    }else{
		    	if(file.size > 1048576 ){
		    		$('#customFile').val('');
		     		$('#errorModal .modal-body').text('Please select an image of size less than 1MB');
			 		$('#errorModal').modal('show');
		    	}
		    }
		}else{
			$('#customFile').val('');
			$('#errorModal .modal-body').text('Only jpg/jpeg and png files are allowed!');
			$('#errorModal').modal('show');
		}
	}
    
}

function logout(){
	window.location = '/';
}