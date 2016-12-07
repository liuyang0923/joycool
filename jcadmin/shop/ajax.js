/********************************************
 * a simple ajax framework
 * @date 2008-01-01
 * @author honeyLee 
 ********************************************/
/**
-----------useage: send an ajax request without param:
 1) define the methods for callback
 var handlesuccess = function(instance) {
 	//'instance' is the XMLHttpRequest object, you can use 'instance' like 'instance.responseText'
 	var status = instance.status;
 	var responseText = instance.responseText;
 	var responseXml = instance.responseXML;
 }
 
 var handleloading = function() {
 	//some operator for loading
 }
 
 var handlefailure = function() {
 	//the failure operator for request failed..
 }
 
 2) define the callback object
 
 var callback = {
 	success: handlesuccess,// the handlesuccess have been defined in the previous step 
 	loading: handleloading,// the handleloading have been defined in the previous step
 	failure: handlefailure,// the handlefailure have been defined in the previous step
 }
 3)if send request by 'POST' method, you should define the 'postData'
 	var postData = 'param1=data1&param2=data2'
   if send request by 'GET' method, you just define the 'postData' to null
 4)send a ajax request
   4.1)Ajax.request('POST', url, postData, callback)  //by 'POST' method
   4.2)Ajax.request('GET', url, null, callback)  //by 'GET' method
   
   
---------usage: send a request with params
  var handlesuccess = function(instance, param) {
 	//'instance' is the XMLHttpRequest object, you can use 'instance' like 'instance.responseText'
 	var status = instance.status;
 	var responseText = instance.responseText;
 	var responseXml = instance.responseXML;
 	var param1 = param['param1'];
 	var param2 = param['param2'];
 }
 
 var handleloading = function(param) {
 	//some operator for loading
 	var param1 = param['param1'];
 	var param2 = param['param2'];
 }
 
 var handlefailure = function(param) {
 	//the failure operator for request failed..
 	var param1 = param['param1'];
 	var param2 = param['param2'];
 }
 
 2) define the callback object
 
 var callback = {
 	success: handlesuccess,// the handlesuccess have been defined in the previous step 
 	loading: handleloading,// the handleloading have been defined in the previous step
 	failure: handlefailure,// the handlefailure have been defined in the previous step
 	param:{param1:'param1', param2:'param2'}
 }
 3)if send request by 'POST' method, you should define the 'postData'
 	var postData = 'param1=data1&param2=data2'
   if send request by 'GET' method, you just define the 'postData' to null
 4)send a ajax request
   4.1)Ajax.request('POST', url, postData, callback)  //by 'POST' method
   4.2)Ajax.request('GET', url, null, callback)  //by 'GET' method
 */ 
/*************************************************************************************************************
#an example with param
var handleSuccess = function(o, param){ 
	var id = param['uid'];
	//alert(id);
	//alert('a');
	///var id = o.responseText;
	///GlobalJS.Action.delById(id);
	var load = document.getElementById('div' + id);
	load.innerHTML = o.responseText;
}
				
var handleLoading = function(param) {
	var id = param['uid'];
	var load = document.getElementById('div' + id);
	///load.style.display = "block";
	load.innerHTML = "加载中...";
}
				
var handleFailure = function(param) {
	var id = param['uid'];
	var load = document.getElementById('div' + id);
	load.innerHTML = "加载失败...";
}
			 
var callback = 
{
	success:handleSuccess, 
	failure:handleFailure,
	loading:handleLoading,
	param: {} 
};
				
function look(id) {
	callback['param'].uid = id;
	var url = "actor?uid=" + id;
	Ajax.requestWithParam('GET', url, null, callback);
}
*********************************************************************************************/


var Ajax = {
	
	_default_form_header:'application/x-www-form-urlencoded;charset=utf-8',
	
	
	_xmlhttp: function() {
		if(window.XMLHttpRequest) {
			return new XMLHttpRequest();
		} else if(window.ActiveXObject) {
			var msxmls = new Array(
				'Msxml2.XMLHTTP.5.0',
				'Msxml2.XMLHTTP.4.0',
				'Msxml2.XMLHTTP.3.0',
				'Microsoft.XMLHTTP');
			for(var i = 0;i < msxmls.length; i++) {
				try {
					return new ActiveXObject(msxmls[i]);
				} catch(e) {}
			}
		}
		throw new Error("could not instantiate XHR");
	},
	
	setEvent: function(instance, callback) {
		var loading = callback['loading'];
		var success = callback['success'];
		var failure = callback['failure'];
		instance.onreadystatechange = function() {
			switch(instance.readyState) {
				case 1:
				case 2:
				case 3:
					loading();
					break;
				case 4:
					if(Ajax.isSuccess(instance)) {
						success(instance);
					} else {
						failure();
					}
					break;
			}
		}
	},
	setEventWithParam: function(instance, callback) {
		var loading = callback['loading'];
		var success = callback['success'];
		var failure = callback['failure'];
		var param = callback['param'];
		instance.onreadystatechange = function() {
			switch(instance.readyState) {
				case 1:
				case 2:
				case 3:
					loading(param);
					break;
				case 4:
					if(Ajax.isSuccess(instance)) {
						success(instance, param);
					} else {
						failure(param);
					}
					break;
			}
		}
	},
	
	isSuccess:function(instance) {
		return !instance.status
        || (instance.status >= 200 && instance.status < 300);
	},
	
	initHeader:function(key, value) {
		
		this._default_form_header = value;
	},
	
	setForm:function(formId) {
		var elements = document.getElementById(formId).elements;
		var oElement, oName, oValue, oPostData = '';
		for(var i = 0; i < elements.length; i++) {
			oElement = elements[i];
			oName = oElement.name;
			oValue = oElement.value;
			
			switch(oElement.type) {
				case 'select-one':
				case 'select-multiple':
				case 'radio':
				case 'checkbox':
				case 'file':
						// stub case as XMLHttpRequest will only send the file path as a string.
				case undefined:
						// stub case for fieldset element which returns undefined.
				case 'reset':
						// stub case for input type reset button.
				case 'submit':
				case 'button':
						// stub case for input type button elements.
					break;
				default:
					oPostData += encodeURIComponent(oName) + '=' + encodeURIComponent(oValue) + '&';
			}
		}
		
		return oPostData;
	},
	successNoParam:function(o){
	
	},
	loadingNoParam:function(){
	
	},
	failureNoParam:function(){
	}
}

/**
 * 
 * @param {Object} method
 * @param {Object} url
 * @param {Object} postData
 * @param {Object} callback {success success, loading loading, failure failure}
 */
Ajax.request = function(method, url, postData, callback) {
	var instance = Ajax._xmlhttp();
	instance.open(method, url);
	this.setEvent(instance, callback);
	if(method.toUpperCase() == 'POST') {
		instance.setRequestHeader('Content-Type', this._default_form_header);
	}
	instance.setRequestHeader('If-Modified-Since',"0");
	instance.send(postData);
	
}

Ajax.requestWithParam = function(method, url, postData, callback) {
	var instance = Ajax._xmlhttp();
	instance.open(method, url);
	this.setEventWithParam(instance, callback);
	if(method.toUpperCase() == 'POST') {
		instance.setRequestHeader('Content-Type', this._default_form_header);
	}
	instance.setRequestHeader('If-Modified-Since',"0");
	instance.send(postData);
}

Ajax.requestWithForm = function(method, url, formId, callback) {
	var instance = Ajax._xmlhttp();
	instance.open(method, url);
	this.setEvent(instance, callback);
	if(method.toUpperCase() == 'POST') {
		instance.setRequestHeader('Content-Type', this._default_form_header);
	}
	var postData = this.setForm(formId);
	instance.setRequestHeader('If-Modified-Since',"0");
	instance.send(postData);
}