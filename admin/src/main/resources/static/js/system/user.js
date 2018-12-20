$(function(){
	 //提示框
    $("[data-toggle='tooltip']").tooltip();
	//表格分页
    $('#roleList').DataTable({
    	"scrollX"	  : true,
  		'paging'      : true,
  	    'lengthChange': true,
  	    'searching'   : true,
  	    'ordering'    : true,
  	    'info'        : true,
  	    'autoWidth'   : false,
  	  	"pagingType"  : "full_numbers",
  	  	"pageLength"  : 10
    });
	
	$("#addUser").click(function(){
		reloadModelData(_ctx+"/user/add","添加用户","添加","","","","","","","add");
		$("#userModal").modal("show");
	});
	
	//确认按钮
	$("#imgSubmit").click(function(){
		var userId = $("input[name='user_id']").val();
		var oldpath = $("input[name='oldpath']").val();
		var url = $("input[name='url']").val();
		var base64Img= $("input[name='pics']").val();
		var username= $("input[name='username']").val();
		var nickName= $("input[name='nick_name']").val();
		var password= $("input[name='password']").val();
		if(url == _ctx+'/user/add'){
			if(!checkAccount(username,nickName,password)){
				return false;
			}
		}
		$.ajax({
			type:"post",
			url:url,
			cache:false,
			dataType:"json",
			data:{user_id:userId,oldpath:oldpath,username:username,password:password,nick_name:nickName,pics:base64Img,time:new Date().getTime()},
			success:function(data){
				if(data.status == 'success'){
					$("#userModal").modal("hide");
					window.location.href=window.location.href;
				}else{
					alert(data.msg);
				}
			}
		});
	});
	
	//确认按钮
	$("#roleSubmit").click(function(){
		var ids="";
		$("input[name='roleids']:checked").each(function(){
			ids = ids + $(this).val() +",";
		});
		var userId = $("input[name='role_user_id']").val();
		$.ajax({
			type:"POST",
			url:_ctx+"/user/editRole",
			cache:false,
			dataType:"json",
			data:{user_id:userId,ids:ids,time:new Date().getTime()},
			success:function(data){
				if(data.status == 'success'){
					window.location.href= window.location.href;
				}else{
					alert(data.msg);
				}
			}
		});
	});
})

//删除用户
function delUser(userId,path){
	if(confirm("你确定要删除此用户吗？")){
		$.ajax({
			type:"POST",
			url:_ctx+"/user/del",
			dataType:"json",
			data:{user_id:userId,pic_path:path},
			cache:false,
			success:function(data){
				if(data.status == 'success'){
					window.location.href=window.location.href;
				}else{
					alert(data.msg);
				}
			}
		});
	}
}

//分配角色
function allotRole(userId){
	$(".modal-body .cbp p").remove();
	$.ajax({
		type:"GET",
		url:_ctx+"/user/getRole",
		dataType:"json",
		data:{user_id:userId,time:new Date().getTime()},
		cache:false,
		success:function(data){
			if(data.status == 'success'){
				var str = "<p><input type='hidden' name='role_user_id' value='"+userId+"' />";
				var arr = data.data;
				for(var i =0;i<arr.length;i++){
					var rolestr = "";
					if(arr[i].checked){
						rolestr="<p><input type='checkbox' name='roleids' id='id"+(i+1)+"' value='"+arr[i].role_id+"' class='chk_1' checked />";
					}else{
						rolestr="<p><input type='checkbox' name='roleids' id='id"+(i+1)+"' value='"+arr[i].role_id+"' class='chk_1'/>";
					}
					rolestr = rolestr +"<label for='id"+(i+1)+"'></label><label data-toggle='tooltip' title='"+arr[i].role_desc+"' data-placement='right' class='font'>"+arr[i].role_name+"</label></p>";
					str = str + rolestr;
				}
				$(".modal-body .cbp").append(str);
				//这是为了让title 属性生效
				$.getScript("/js/tip.js");
				$("#edituserRoleModal").modal("show");
			}else{
				alert(data.msg);
			}
		}
	});
}

//编辑用户
function editUser(user_id,username,nickName,path){
	console.log(user_id+","+username+","+nickName+","+path);
	reloadModelData(_ctx+"/user/edit","编辑用户","更新",user_id,"",path,username,nickName,"","edit");
	$("#userModal").modal("show");
}

//验证数据
function checkAccount(username,nickName,password){
	if(username == ''){
		$("input[name='username']").focus();
		alert("用户名不能为空");
		return false;
	}
	if(nickName == ''){
		$("input[name='nick_name']").focus();
		alert("昵称不能为空");
		return false;
	}
	if(password == ''){
		$("input[name='password']").focus();
		alert("密码不能为空");
		return false;
	}else if(password.length < 4){
		$("input[name='password']").focus();
		alert("密码个数不能少于4位");
		return false;
	}
	return true;
}

//加载模态框的数据
function reloadModelData(url,title,btntext,user_id,pics,oldpath,username,nick_name,password,type){
	$("#userModal #usermodelHead").text(title);
	$("#userModal #imgSubmit").text(btntext);
	$("#showImg").attr("src",oldpath);
	$("input[name='url']").val(url);
	$("input[name='user_id']").val(user_id);
	$("input[name='pics']").val(pics);
	$("input[name='oldpath']").val(oldpath);
	$("input[name='username']").val(username);
	$("input[name='nick_name']").val(nick_name);
	$("input[name='password']").val(password);
	if(type == 'add'){
		$("input[name='username']").attr("disabled",false);
	}else{
		$("input[name='username']").attr("disabled",true);
	}
}

var input = document.getElementById("imgfile");
 if (typeof (FileReader) === 'undefined') {
     result.innerHTML = "抱歉，你的浏览器不支持 FileReader，请使用现代浏览器操作！";
     input.setAttribute('disabled', 'disabled');
 } else {
     input.addEventListener('change', readFile, false);
 }
function readFile() {
	var file = this.files[0];
     //判断是否是图片类型
     if (!/image\/\w+/.test(file.type)) {
         alert("只能选择图片");
         return false;
    }
     var reader = new FileReader();
     reader.readAsDataURL(file);
     reader.onload = function (e) {
     	base64Code=this.result;
     	$("#userModal input[name='pics']").val(this.result);
     	$("#showImg").attr("src",this.result);
     }
  }