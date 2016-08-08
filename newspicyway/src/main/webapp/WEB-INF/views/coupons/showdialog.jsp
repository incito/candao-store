<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="addCouponDialog2" style="overflow: hidden;">			
			<div style="width: 100%; float: left;">
				<font>活动设置</font>
				<hr/>
				<div style="width: 100%; float: left;">
                    <div style="width: 100%; float: left;margin-top:10px;">
                                                                     优惠名称: <input type="text" id="couponsName2" name="couponsName2" /><span style="color: red">*</span> &nbsp;&nbsp;
					         展示终端: <input type="radio" id="showpositionPad2" name="showposition2" value="0" checked />&nbsp;&nbsp;PAD端
					   <input type="radio" id="showpositionPos2" name="showposition2" value="1" />&nbsp;&nbsp;POS端
					    &nbsp;&nbsp;  优惠时段:
					    <select id="coupontype2" name="coupontype2" onchange="addCheckBox('selectBox2','coupontype2');">
						    <option value="0">每天</option>
						    <option value="1">每周</option>
						    <option value="2">每月</option>
						</select>
                    </div>
					<div id="selectBox2" style="width: 100%; float: left;margin-top:10px;">					       
					</div>                                       
                    <div style="width: 100%; float: left;margin-top:10px;">
                                                                      优惠对象:<input type="radio" id="couponcustomerp2" name="couponcustomer2" value="0" checked />&nbsp;&nbsp;所有顾客
					   <input type="radio" id="couponcustomerps2" name="couponcustomer2" value="1"/>&nbsp;&nbsp;仅限会员                                                                 
                    </div>
                    <div style="width: 100%; float: left;margin-top:10px;">
                                                                      满足条件:订单金额 满<input type="text" style="width:60px;" id="orderMoney2" name="orderMoney2"/>减扣<input type="text" style="width:60px;" id="reduceMoney2" name="reduceMoney2"/>元                                                         
                    </div>
                    <div style="width: 100%; float: left;margin-top:10px;">                                                                 
                                                                       有效期:&nbsp;&nbsp; <input id="begintime2" readonly name="begintime2" class="" onFocus="WdatePicker({startDate:'%y-%M-%d %h:%m:%s',dateFmt:'yyyy-MM-dd HH:mm:ss'})" />&nbsp;&nbsp;至: <input id="endtime2" readonly name="endtime2" class="" onFocus="WdatePicker({startDate:'%y-%M-%d %h:%m:%s',dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
                    </div>
                    <div style="width:8%; float: left;margin-top:10px;">
                                                                      活动简介:
                    </div>
                    <div style="width:82%; float: left;margin-top:10px;">
                           <textarea rows="3" cols="60" id="description2" name="description2" style="resize:none;"></textarea>
                    </div>	
				</div>
			</div>
		</div>
		
		<div id="addCouponDialog3" style="overflow: hidden;">			
			<div style="width: 100%; float: left;">
				<font size="20" color="red">此功能暂未完成，敬请期待！！</font>			
			</div>
		</div>
		
		<div id="addCouponDialog4" style="overflow: hidden;">			
			<div style="width: 100%; float: left;">
				<font>活动设置</font>
				<hr/>
				<div style="width: 100%; float: left;">
                    <div style="width: 100%; float: left;margin-top:10px;">
                                                                     优惠名称: <input type="text" id="couponsName4" name="couponsName4" /><span style="color: red">*</span> &nbsp;&nbsp;
					         展示终端: <input type="radio" id="showpositionPad4" name="showposition4" value="0" checked />&nbsp;&nbsp;PAD端
					   <input type="radio" id="showpositionPos4" name="showposition4" value="1" />&nbsp;&nbsp;POS端
					    &nbsp;&nbsp;  优惠时段:
					    <select id="coupontype4" name="coupontype4" onchange="addCheckBox('selectBox4','coupontype4');">
						    <option value="0">每天</option>
						    <option value="1">每周</option>
						    <option value="2">每月</option>
						</select>
                    </div>
					<div id="selectBox4" style="width: 100%; float: left;margin-top:10px;">					       
					</div>                                       
                    <div style="width: 100%; float: left;margin-top:10px;">
<!--                                                                       团购网站:<select id="groupWeb4" name="groupWeb4" > -->
<!--                                   <option value="0" >美团网</option>  -->
<!--                                   <option value="1" >大众点评网</option>  -->
<!--                                   <option value="2" >美食网</option>                                      -->
<!--                               </select> &nbsp;&nbsp; -->
                                                                     团购网站:<input id="groupWeb4" name="groupWeb4"/>
                                                                      代金券金额:<input type="text" id="couponcash4" name="couponcash4" style="width:60px;" />元&nbsp;&nbsp;      
                                                                      可抵用金额:<input type="text" id="freeamount4" name="freeamount4" style="width:60px;" />元 &nbsp;&nbsp;    
                                                                      每次使用数量:<input type="text" id="couponnum4" name="couponnum4" style="width:60px;"/>张                                                                                                       
                    </div>
                    <div style="width: 100%; float: left;margin-top:10px;">                                                                 
                                                                       有效期:&nbsp;&nbsp; <input  readonly onFocus="WdatePicker({startDate:'%y-%M-%d %h:%m:%s',dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="begintime4" name="begintime4" />&nbsp;&nbsp;至: <input  readonly onFocus="WdatePicker({startDate:'%y-%M-%d %h:%m:%s',dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="endtime4" name="endtime4" />
                    </div>
                    <div style="width:8%; float: left;margin-top:10px;">
                                                                      活动简介:
                    </div>
                    <div style="width:82%; float: left;margin-top:10px;">
                           <textarea rows="3" cols="60" id="description4" name="description4" style="resize:none;"></textarea>
                    </div>	
				</div>
			</div>
		</div>
		
		<div id="addCouponDialog5" style="overflow: hidden;">			
			<div style="width: 100%; float: left;">
				<font>活动设置</font>
				<hr/>
				<div style="width: 100%; float: left;">
                    <div style="width: 100%; float: left;margin-top:10px;">
                                                                     优惠名称: <input type="text" id="couponsName5" name="couponsName5" /><span style="color: red">*</span> &nbsp;&nbsp;
					         展示终端: <input type="radio" id="showpositionPad5" name="showposition5" checked value="0" />&nbsp;&nbsp;PAD端
					   <input type="radio" id="showpositionPos5" name="showposition5" value="1" />&nbsp;&nbsp;POS端
					    &nbsp;&nbsp;  优惠时段:
					    <select id="coupontype5" name="coupontype5" onchange="addCheckBox('selectBox5','coupontype5');">
						    <option value="0">每天</option>
						    <option value="1">每周</option>
						    <option value="2">每月</option>
						</select>
                    </div>
					<div id="selectBox5" style="width: 100%; float: left;margin-top:10px;">					       
					</div>                                       
                    <div style="width: 100%; float: left;margin-top:10px;">
<!--                                                                       发卡银行:<select id="banktype5" name="banktype5" > -->
<!--                                   <option value="0" >中国工商银行</option>  -->
<!--                                   <option value="1" >中国农业银行</option>  -->
<!--                                   <option value="2" >中国建设银行</option>  -->
<!--                                    <option value="3" >其他银行</option>                                       -->
<!--                               </select> &nbsp;&nbsp; -->
                                                                    发卡银行:  <input id="banktype5"/>       
                                                                     单笔金额 满:<input type="text" id="totalamount5" name="totalamount5" style="width:60px;" />元&nbsp;&nbsp;      
                                                                      优惠内容:<select id="couponway5" name="couponway5" onchange="couponwayChange('couponway5','showNum5');">
                                  <option value="0" >整单打折</option> 
                                  <option value="1" >整单优惠</option>                                     
                              </select> &nbsp;&nbsp;    
                         <input type="text" id="couponnum5" name="couponnum5" style="width:60px;"/>&nbsp;&nbsp; 
                         <select id="showNum5" name="showNum5"  onfocus="this.defOpt=this.selectedIndex" onchange="this.selectedIndex=this.defOpt;">
                             <option value="0" >折</option> 
                             <option value="1" >元</option>                                     
                         </select>
                                                                                                                               
                    </div>
                    <div style="width: 100%; float: left;margin-top:10px;">                                                                 
                                                                       有效期:&nbsp;&nbsp; <input  readonly onFocus="WdatePicker({startDate:'%y-%M-%d %h:%m:%s',dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="begintime5" name="begintime5" />&nbsp;&nbsp;至: <input id="endtime5" readonly name="endtime5"  onFocus="WdatePicker({startDate:'%y-%M-%d %h:%m:%s',dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
                    </div>
                    <div style="width:8%; float: left;margin-top:10px;">
                                                                      活动简介:
                    </div>
                    <div style="width:82%; float: left;margin-top:10px;">
                           <textarea rows="3" cols="60" id="description5" name="description5" style="resize:none;"></textarea>
                    </div>	
				</div>
			</div>
		</div>
		
		<div id="addCouponDialog6" style="overflow: hidden;">			
			<div style="width: 100%; float: left;">
				<font>活动设置</font>
				<hr/>
				<div style="width: 100%; float: left;">
                    <div style="width: 100%; float: left;margin-top:10px;">
                                                                     优惠名称: <input type="text" id="couponsName6" name="couponsName6" /><span style="color: red">*</span> &nbsp;&nbsp;
					         展示终端: <input type="radio" id="showpositionPad6" name="showposition6" value="0" checked />&nbsp;&nbsp;PAD端
					   <input type="radio" id="showpositionPos6" name="showposition6" value="1" />&nbsp;&nbsp;POS端
					    &nbsp;&nbsp;  优惠时段:
					    <select id="coupontype6" name="coupontype6" onchange="addCheckBox('selectBox6','coupontype6');">
						    <option value="0">每天</option>
						    <option value="1">每周</option>
						    <option value="2">每月</option>
						</select>
                    </div>
					<div id="selectBox6" style="width: 100%; float: left;margin-top:10px;">					       
					</div>                                       
                    <div style="width: 100%; float: left;margin-top:10px;">
                                                                      优惠类型:<select id="coupontypeChild6" name="coupontypeChild6" onchange="coupontypeChild();">
                                  <option value="13" >内部员工优惠</option>                                                                     
                                  <option value="14" >合作单位优惠</option> 
                              </select> &nbsp;&nbsp;
                              <font id="fontcs">合作单位类型：</font>
                             <span id="partnernameflags"> 
                              <select id="cooperateType" name="cooperateType" onchange="cooperateType();" style="width:50px;"></select>                                 
                             </span>    
                       <font id="fontc"> 合作单位名称:</font><span id="partnernameflag">                     
                       <input id="partnername6" name="partnername6"/></span>&nbsp;&nbsp;     
                                                                      优惠方式:打<input type="text" id="couponrate6" name="couponrate6" style="width:60px;" />折
                       <span id="brid"><br/><br/></span>                                               
                       <font id="debitfont6"> 挂账金额:</font> <input type="text" id="debitamount6" name="debitamount6"style="width:60px;" />&nbsp;&nbsp;   
                       <font id="freefont6"> 优免金额:</font> <input type="text" id="freeamount6" name="freeamount6" style="width:60px;" />&nbsp;&nbsp; 
                       <font id="orderfont6"> 是否整单优惠:</font>
                       <select id="wholesingle6" name="wholesingle6" onchange="wholeSingle();" >
                           <option value="0">是</option>                           
                           <option value="1">否</option>
                       </select>&nbsp;&nbsp;
                       <input type="button" id="selectOrderCoupon6" name="selectOrderCoupon6" style="display: none;" value="请选择菜品" onclick="save_selectOrderCoupon();" />                                                                                                                                                                                                                    
                    </div>
                    <div style="width: 100%; float: left;margin-top:10px;">                                                                 
                                                                       有效期:&nbsp;&nbsp; <input  readonly onFocus="WdatePicker({startDate:'%y-%M-%d %h:%m:%s',dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="begintime6" name="begintime6" />&nbsp;&nbsp;至: <input  readonly onFocus="WdatePicker({startDate:'%y-%M-%d %h:%m:%s',dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="endtime6" name="endtime6" />
                    </div>
                    <div style="width:8%; float: left;margin-top:10px;">
                                                                      活动简介:
                    </div>
                    <div style="width:82%; float: left;margin-top:10px;">
                           <textarea rows="3" cols="60" id="description6" name="description6" style="resize:none;"></textarea>
                    </div>	
				</div>
			</div>
		</div>
		
	    <div id="dishDetialSecond">
		    <table id="dishListSecond">
		    </table> 
		</div>
		 