$(function(){
	$('#columnTag').combobox({
	    url:global_Path + '/article/getColumnTag.json',
	    valueField:'id',
	    textField:'name',
	    panelHeight : 'auto',
	    loadFilter : function(row){
	    	return row;
		}
	});
	
	
		$('#addArticleDialog').dialog({   
		    title: '增加文章 (*号部分为必填项)',   
		    width: 800,   
		    height: 560,
		    zIndex:10000000000,
		    closed: true,   
		    cache: false,  
		    inline : true,
		    modal: true,
			buttons : [ {
				text : '保存',
				handler :
					function(){
					$('#title').validatebox({required: true,validType:'maxLength[1,30]',missingMessage:"标题名称不能为空！"});
					$('#introduction').validatebox({required: true,missingMessage:"标题简介不能为空！"});
					$('#source').validatebox({required: false,validType:'maxLength[1,30]'});
					$('#author').validatebox({required: false,validType:'maxLength[1,20]'});
					$("#title").validatebox('enableValidation');
					$("#introduction").validatebox('enableValidation');
					$("#source").validatebox('enableValidation');
					$("#author").validatebox('enableValidation');
					if($("#file1").val()!=""){
						$('#file1').validatebox({required:true,validType:'fileType',missingMessage:"图片不能为空！"});
						$("#file1").validatebox('enableValidation');
						if($('#title').validatebox('isValid') && $('#introduction').validatebox('isValid') && $('#source').validatebox('isValid') && $('#author').validatebox('isValid') && $('#file1').validatebox('isValid')){
							save_article();
						}
					}else{
						if($('#title').validatebox('isValid') && $('#introduction').validatebox('isValid') && $('#source').validatebox('isValid') && $('#author').validatebox('isValid')){
							save_article();
						}
					}
				}
					
			}, {
				text : '关闭',
				handler : function() {
					$('#addArticleDialog').dialog('close');
					init_article_data();
					$('#tables').datagrid('reload');
				}
			} ],
		onClose : function() {
			init_article_data();
		}
		});
		
});
	

	
	
	function init_article_data(){
		$("#removepic").hide(); 
		   imagePath='';
		  $("#id").val("");
		  $("#title").val("");
		  $("#label").val("");
 	      $("#introduction").val("");
 	      $("#source").val("");
	      $("#author").val("");
	      $("#file1Img").attr("src","");
		  $("#file1Img").attr("style","visible:hidden");
	      $("#file1").val("");
	      $("#columnTag").combobox('setValue','选择所属栏目');
	      $("#articlereleasetime").val("");
	      $("#isdisplay").prop("checked",true);
	      $("#ishead").prop("checked",false); 
	      $("#isselect").prop("checked",false); 
	      UE.getEditor('content').setContent("");
	      $("#title").validatebox('disableValidation');
		  $("#introduction").validatebox('disableValidation');
		  $("#source").validatebox('disableValidation');
		  $("#author").validatebox('disableValidation');
		  $("#file1").validatebox('disableValidation');
	}
	
	function save_article(){
		
		var check=check_article_validate();
		if(check){
			  var image;
			  if($("#file1").val()==''){
//				    	alert("file1 is null");
			    	image=imagePath;
			    }else{
			    	image="";
			    }
				 $.ajaxFileUpload({
				    fileElementId: ['file1'],  
				    url:global_Path +'/article/save',  
				    dataType: 'json',
				    contentType:'application/json;charset=UTF-8',
				    data: { 
				    	    id:	$("#id").val(),
				    	    title: $("#title").val(),
				    	    label: $.trim($("#label").val()),
				    	    introduction: $("#introduction").val(),
				    	    source:$("#source").val(),
			        	    author:$("#author").val(),
			        	    image:image,
			        	    columnid:$("#columnTag").combobox('getValue'),
			        	    releasetime:$("#articlereleasetime").val(),
			        	    isdisplay:$("#isdisplay").is(":checked")?1:0,
			        	    ishead:$("#ishead").is(":checked")?1:0,
			        	    isselect:$("#isselect").is(":checked")?1:0,
			        	    content:UE.getEditor('content').getContent()
				    	   },  
				    beforeSend: function (XMLHttpRequest) {  
				      //("loading");  
				    },  
				    success: function (data, textStatus) {
				    	if(data.message=='添加成功'){
				    		alert(data.message);
				    		i++;
				    		var imagepath=global_Path+"/"+data.tbArticle.image;
				    		 var img=document.createElement("img");
				    		 var imgage="<img src="+imagepath+"/>";
				    		 img.src=imagepath;
				    		 thistemplate.innerHTML="<span id='id' style='display:none'>"+data.tbArticle.id+"</span><span id='image' style='display:none'>"+data.tbArticle.image+"</span><Strong>标题：<span>"+data.tbArticle.title+"</span><br>简介：<span>"+data.tbArticle.introduction+"</span></Strong><br>";
				    		 if(data.tbArticle.image!=""&&data.tbArticle.image!='undefined'&&data.tbArticle.image!=undefined){
				    		 thistemplate.style.backgroundImage="url("+imagepath+")";
//				    		 thistemplate.style.width="100%";
				    		 thistemplate.style.backgroundRepeat="no-repeat";
				    		 }else{
				    			 thistemplate.style.backgroundImage="url('')";
//				    			 var node=document.createTextNode(data.tbArticle.source);
				    			 var ele_p=document.createElement("p");
				    			 ele_p.innerHTML="<Strong>来源：<span>"+data.tbArticle.source+"</span></Strong>";
//				    			 ele_p.appendChild(node);
				    			 thistemplate.appendChild(ele_p);
				    		 }
//				    		thistemplate.innerHTML=data.tbArticle.title;
					    	$('#addArticleDialog').dialog('close');
				    	}
				    	
				    	if(data.message=='修改成功'){
				    		alert(data.message);
				    		$('#tables').datagrid('reload');
				    		i++;
				    		var imagepath=global_Path+"/"+data.tbArticle.image;
				    		 var img=document.createElement("img");
				    		 var imgage="<img src="+imagepath+"/>";
				    		 img.src=imagepath;
				    		 thistemplate.innerHTML="<span id='id' style='display:none'>"+data.tbArticle.id+"</span><span id='image' style='display:none'>"+data.tbArticle.image+"</span><Strong>标题：<span>"+data.tbArticle.title+"</span><br>简介：<span>"+data.tbArticle.introduction+"</span></Strong><br>";
				    		 if(data.tbArticle.image!=""&&data.tbArticle.image!='undefined'&&data.tbArticle.image!=undefined){
				    		 thistemplate.style.backgroundImage="url("+imagepath+")";
//				    		 thistemplate.style.width="100%";
				    		 thistemplate.style.backgroundRepeat="no-repeat";
				    		 }else{
				    			 thistemplate.style.backgroundImage="url('')";
//				    			 var node=document.createTextNode(data.tbArticle.source);
				    			 var ele_p=document.createElement("p");
				    			 ele_p.innerHTML="<Strong>来源：<span>"+data.tbArticle.source+"</span></Strong>";
//				    			 ele_p.appendChild(node);
				    			 thistemplate.appendChild(ele_p);
				    		 }
//				    		thistemplate.innerHTML=data.tbArticle.title;
				    		$('#addArticleDialog').dialog('close');
				    	}
				    	init_data();
				    	init_article_data();
				    	//alert(textStatus);
				    },  
//				    error: function (XMLHttpRequest, textStatus, errorThrown) {  
//				      var img = "操作失败！";  
//				      $("#" + imgcontainerId).append(img);  
//				      var msg = "服务器出错，错误内容：" + XMLHttpRequest.responseText;  
//				      $.messager.showWin({ msg: msg, title: '错误提示', color: 'red' });  
//				    	alert("error");
//				    },  
//				    complete: function (XMLHttpRequest, textStatus) {  
//				      //("loaded");  
//				    } 
			    });

		}
	}
	
		function check_article_validate(){
			var flag=true;
     	   var content=UE.getEditor('content').getContent();
			if($.trim(content)==""){
				alert("请填写文章内容");
				flag=false;
			}
			var articlereleasetime=$("#articlereleasetime").val();
			if(articlereleasetime.trim()==""){
				$.messager.alert("提示","发布时间不能为空");
				return false;
			}
			$.ajax({
				type : "post",
				async : false,
				data:{
					id:	$("#id").val(),
		    	    title: $("#title").val()
				},
				url : global_Path+"/article/validateArticle.json",
				dataType : "json",
				success : function(result) {
					if(result.message=='文章标题不能重复'){
						alert(result.message);
						flag=false;
						
					}
				}
				
			});
		
			return flag;
		}

		//修改查询
		function modify_article(id){
			$.ajax({
				type : "post",
				async : false,
				url : global_Path+"/article/findById/"+id+".json",
				dataType : "json",
				success : function(result) {
					$("#id").val(result.id);
					$("#title").val(result.title);
					$("#label").val(result.label);
					$("#introduction").val(result.introduction);
					$("#source").val(result.source);
					$("#author").val(result.author);
//   					$("#file1").val(result.image);
                     imagePath=result.image;
					$("#file1Img").attr("src",global_Path+'/'+result.image);
					$("#file1Img").attr("style","width:30px;height:20px");
					if(result.image!=""){
						$("#removepic").show();
					};
//					$('#columnTag').combobox('select',result.columnid);
					var comdata=$('#columnTag').combobox('getData');
					var flag=false;
					if(comdata.length>0){
					   for (var i=0;i<comdata.length;i++) {
		                     if (comdata[i].id == result.columnid) {
		                    	 flag=true;
		                    	// alert(comdata[i].id);
		                     }
		                 }
					}
					if(flag){
						$('#columnTag').combobox('select', result.columnid);
					}else{
						$('#columnTag').combobox('select','选择所属栏目');
					}
				    $("#articlereleasetime").val(result.releasetime);
					if(result.isdisplay==1){
						$("#isdisplay").prop("checked",true);
					 }else{
						$("#isdisplay").prop("checked",false);
					 }
					if(result.ishead==1){
						$("#ishead").prop("checked",true);
					 }else{
						$("#ishead").prop("checked",false);
					 }
					if(result.isselect==1){
						$("#isselect").prop("checked",true);
					 }else{
						$("#isselect").prop("checked",false);
					 }
					  UE.getEditor('content').setContent(result.content);
					$('#addArticleDialog').dialog('open');  
				}
			});
		}
		
