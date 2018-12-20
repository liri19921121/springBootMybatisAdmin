var _ctx = $("meta[name='_ctx']").attr("content");
_ctx = _ctx.substr(0, _ctx.length - 1);
$(function(){
    $("[data-toggle='tooltip']").tooltip();
})
//日期格式化
function dateFtt(fmt,date)   { 
	if(date == ""){
		return "NULL";
	}
date = new Date(date);
  var o = {   
    "M+" : date.getMonth()+1,                 //月份   
    "d+" : date.getDate(),                    //日   
    "h+" : date.getHours(),                   //小时   
    "m+" : date.getMinutes(),                 //分   
    "s+" : date.getSeconds(),                 //秒   
    "q+" : Math.floor((date.getMonth()+3)/3), //季度   
    "S"  : date.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}

//判断是否是中文
function isChina(s){ 
	var patrn=/[\u4E00-\u9FA5]|[\uFE30-\uFFA0]/gi; 
	if(!patrn.exec(s)){ 
		return false; 
	}else{ 
		return true; 
	} 
}