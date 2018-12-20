$(function(){

    $("#addItem").click(function(){
        $("input[name='id']").val("");
        $("input[name='actionurl']").val("/config/add");
        $("#modelHead").text("添加");
        $("#submitBtn").text("添加");
        setData("","","","","");
        $("#itemModal").modal("show");
        // $("#configName").attr("disabled",false);
    });

    //提交按钮
    $("#submitBtn").click(function(){
        var id = $("input[name='id']").val();
        var url = $("input[name='actionurl']").val();
        var configType = $("#configType").val();
        var configName = $("#configName").val();
        var configValue = $("#configValue").val();
        var configDesc = $("#configDesc").val();

        if(checkData(configType,configName,configValue,configDesc)){
            $.ajax({
                type:"POST",
                url:_ctx+url,
                cache:false,
                data:{time:new Date().getTime(),configType:configType,configName:configName,configValue:configValue,id:id,configDesc:configDesc},
                dataType:"json",
                success:function(data){
                    if(data.status == "200"){
                        window.location.href=window.location.href;
                    }else{
                        alert(data.message);
                    }
                }
            });
            $("#itemModal").modal("hide");
        }

    });



    //搜索
    $(".searcher").click(function(){
        skipPage("");
    });

})

function setData(configType,configName,configValue,configDesc){
    $("#configType").val(configType);
    $("#configName").val(configName);
    $("#configValue").val(configValue);
    $("#configDesc").text(configDesc);
}

function skipPage(page){
    var keyword = $("#keyword").val();
    var url="/config/list";
    if(page != ""){
        url =url+ "/"+page+"?t=1";
    }else {
        url = url + "?t=1";
    }
    if(keyword != ""){
        url =url+"&keyword="+keyword;
    }
    window.location.href=_ctx+url;
}
//编辑商户
function edit(id){
    $("#modelHead").text("编辑");
    $("#submitBtn").text("编辑");
    $("input[name='id']").val(id);
    $("input[name='actionurl']").val("/config/edit");
    // $("#configName").attr("disabled",true);
    $.get(_ctx+"/config/detail",{t:new Date().getTime(),id:id},function(data){
        console.log("data:",data);
        if(data.status == "200"){
            var item = data.data;
            console.log("item:",item);
            setData(item.config_type,item.config_name,item.config_value,item.config_desc)
        }else{
            alert(data.message);
        }
    })
    $("#itemModal").modal("show");
}
//删除
function del(id){
    if(confirm("您确定要删除这条记录吗？"))
        $.post(_ctx+"/config/del",{t:new Date().getTime(),id:id},function(data){
            if(data.status == "200"){
                window.location.href=window.location.href;
            }else{
                alert(data.message);
            }
        });
}

//校验
function checkData(configType,configName,configValue,configDesc){
    if(configType == ""){
        alert("配置类型不能为空");
        $("input[name='configType']").focus();
        return false;
    }
    if(configName == ""){
        alert("配置名称不能为空");
        $("input[name='configName']").focus();
        return false;
    }
    if(configValue == ""){
        alert("配置值不能为空");
        $("input[name='configValue']").focus();
        return false;
    }
    if(configDesc == ""){
        alert("配置描述不能为空");
        $("input[name='configDesc']").focus();
        return false;
    }

    return true;
}