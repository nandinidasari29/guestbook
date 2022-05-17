function getallentries(){
$.ajax({
    url: "/admin/listall",
    type: "get",
    headers: { 'Authorization':  'Bearer '+$('#token').val()},
    contentType: "application/json",
    beforeSend: function() {
        $('#loading-area').show();
    },
    success: function(data) {
        $('#loading-area').hide();
        if(data == 'There is no data for this request') {
            swal("Data Not Found", "There is no data with us for this request", "error");
        }
        else {
            var tabledata = [];
            var obj = data;

            $("#guestbook-datatable > tbody").html("");
            for(var k = 0; k < obj.length; k++) {
                var row = {};
                row["SLNo"] = k+1;;
                var tempDate= new Date(obj[k].createdDate);
                row["CreatedOn"] = tempDate.toUTCString();
                if(obj[k].textEntry!=null){
                    row["entry"] = obj[k].textEntry;
                }
                else{
                    row["entry"] ='<a href="#" onClick=viewImage("'+obj[k].id+'")>'+obj[k].fileName+'</a>';
                }
                row["CreatedBy"] = obj[k].createdBy;
                row["status"] = obj[k].status;
                row["id"] = obj[k].id;
                tabledata.push(row);
                
                var code;
                code = '<tbody><tr><td style="padding: 4px;vertical-align: middle;">' + row.SLNo + '</td><td style="vertical-align: middle;">' + row.CreatedOn + '</td><td style="vertical-align: middle;">' + row.entry + '</td><td style="vertical-align: middle;">'+ row.CreatedBy + '</td><td style="vertical-align: middle;">' + row.status + '</td>'
                    + '<td class="text-center">'
                    + '<button type="button" class="editBtn" id="edit_entry" style="padding:0px;padding-right:5px;border: 0px;" data-entryid="'+row.id+'"><img style="width:60px" src="edit.png"/></button>';
                    if(row.status!=='Approved'){
                    code = code + '<button type="button" class="approveBtn" id="approve_entry" style="padding:0px;padding-right:5px;border: 0px;" data-entryid="'+row.id+'"><img style="width:60px" src="approve.png"/></button>';
                    }
                    code = code + '<button type="button" class="deleteBtn" style="padding: 0px;border: 0px;" id="delete_entry" data-entryid="'+row.id+'"><img style="width:60px" src="reject.png"/></button>'
                    +'</td></tr></tbody>';
                $('#guestbook-datatable').append(code);
            }
        }
    },
    error: function(data) {
        console.error(data);
        $('#loading-area').hide();
    }
});
}

$(document).ready(function() {
  $(".search").keyup(function () { 
    var searchTerm = $(".search").val();
    var searchSplit = searchTerm.replace(/ /g, "'):containsi('")
    
  $.extend($.expr[':'], {'containsi': function(elem, i, match, array){
        return (elem.textContent || elem.innerText || '').toLowerCase().indexOf((match[3] || "").toLowerCase()) >= 0;
    }
  });
    
  $(".results tbody tr").not(":containsi('" + searchSplit + "')").each(function(e){
    $(this).attr('style','display: none');
  });

  $(".results tbody tr:containsi('" + searchSplit + "')").each(function(e){
    $(this).attr('style','display: table-row');
  });

  var jobCount = $('.results tbody tr[visible="true"]').length;
    $('.counter').text(jobCount + ' item');

  if(jobCount == '0') {$('.no-result').show();}
    else {$('.no-result').hide();}
		  });
});

$(document).on("click",".approveBtn", function(){
	var token = 'Bearer '+$('#token').val();
	$.ajax({
		  url: 'http://localhost:8080/admin/approve/'+$(this).data('entryid'),
		  type: "POST",
		  headers: { 'Authorization':  token},
		  contentType: "application/json",
		  error: function(err) {		    
		     $('#errorModal .modal-body').text(err);
			 $('#errorModal').modal('show');
		  },
		  success: function(data) {			  
			  $('#successModal .modal-body').text(data);
			  $('#successModal').modal('show');
			  getallentries();
		  }
		});
});

$(document).on("click",".deleteBtn", function(){
	var token = 'Bearer '+$('#token').val();
	$.ajax({
		  url: "http://localhost:8080/admin/delete/"+$(this).data('entryid'),
		  type: "DELETE",
		  headers: { 'Authorization':  token},
		  contentType: "application/json",
		  error: function(err) {
			 $('#errorModal .modal-body').text(err);
			 $('#errorModal').modal('show');
		  },
		  success: function(data) {
			  $('#successModal .modal-body').text(data);
			  $('#successModal').modal('show');
			  getallentries();
		  }
		});
});

function viewImage(id){
	var token = 'Bearer '+$('#token').val();
	var url = 'http://localhost:8080/admin/getimage/'+id;
	var xhr = new XMLHttpRequest();
	xhr.open("GET",url,true);
	xhr.setRequestHeader("Authorization",token);
	xhr.responseType = "arraybuffer";
	xhr.addEventListener("load", function(){
		if(xhr.status===200){
			const blob = new Blob([xhr.response]);
		    const url = URL.createObjectURL(blob);
		    $('#imageEntryId').attr('src',url);
		    $('#imageEntryModal').modal();
		}
	});
	xhr.send();
}

$(document).on("click",".editBtn", function(){
	 $('#customFile').val('');
	 $('#textEntry').val('');
	 $('#customFilename').val('');
	var token = 'Bearer '+$('#token').val();
	$.ajax({
		  url: 'http://localhost:8080/admin/find/'+$(this).data('entryid'),
		  type: "GET",
		  headers: { 'Authorization':  token},
		  contentType: "application/json",
		  error: function(err) {
		    $('#errorModal .modal-body').text(err);
			$('#errorModal').modal('show');
		  },
		  success: function(entrydata) {
			  $('#textEntry').val(entrydata.textEntry);
			  $('#customFilename').html(entrydata.fileName);
			  $('#entryid').val(entrydata.id);
			  if(entrydata.textEntry != null){
				$('#entrybytext').show();
				$('#entrybyimage').hide();
			  }else{
				$('#entrybytext').hide();
				$('#entrybyimage').show();
			  }
			  $("#editModal").modal();
		  }
		});
});

function updateentry(event){
	event.preventDefault();
	if(($('#customFile').val() === '') && $('#textEntry').val() === ''){
		$('#errorModal .modal-body').text('Make an entry to update the entry');
		$('#errorModal').modal('show');
		return;
	}
	var form = $('.form-signin')[0];
	var data = new FormData(form);
	var token = 'Bearer '+$('#token').val();
	$.ajax({
		  url: 'http://localhost:8080/admin/update/'+$('#entryid').val(),
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
			 $('#customFilename').val('');
		     $('#errorModal .modal-body').text(err);
			 $('#errorModal').modal('show');
		  },
		  success: function(data) {
			  $('#customFile').val('');
			  $('#textEntry').val('');
			  $('#customFilename').val('');			  
			  $('#successModal .modal-body').text(data);
			  $('#successModal').modal('show');
			  getallentries();
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


$(document).ready(function() {
	getallentries();
	});

function logout(){
	window.location = '/';
}