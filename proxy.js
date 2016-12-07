mf=document.createElement('form');
document.body.appendChild(mf);
mf.method="post";
function go(url){
mf.action=url;return mf.submit();}
function addfrom(name,from){
if(!from)return;

if(from.substring(0,1)=='$'){
	if(from.substring(1,2)=='(')
		from=from.substring(2,from.length-1);
	else
		from=from.substring(1,from.length);
}else {
	add(name,from);
	return;
}
var ipt=document.getElementsByName(from);
if(ipt&&ipt.length>0)
	add(name,ipt[0].value);
else
	add(name,'');
}
function add(name,value){

var oInput = document.createElement("input");
oInput.setAttribute("type","hidden");
oInput.setAttribute("name",name);
oInput.setAttribute("value",value);
mf.appendChild(oInput);
}
function red(url){
window.location=url;
}
function tred(time, url){
setTimeout("red('"+url.replace(/&amp;/g,'&')+"')",time*100);
}