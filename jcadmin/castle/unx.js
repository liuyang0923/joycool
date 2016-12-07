var timer=new Object();var ab=new Object();var bb=new Object();var cb=db();var eb=0;var auto_reload=1;var fb=new Object();var	is_opera=window.opera!==undefined;var	is_ie=document.all!==undefined&&window.opera===undefined;var is_ie6p=document.compatMode!==undefined&&document.all!==undefined&&window.opera===undefined;var is_ie7=document.documentElement!==undefined&&document.documentElement.style.maxHeight!==undefined;var is_ie6=is_ie6p&&!is_ie7;var is_ff2p=window.Iterator!==undefined;var is_ff3p=document.getElementsByClassName!==undefined;var is_ff2=is_ff2p&&!is_ff3p
function gb(){return hb('height');}
function ib(){return hb('width');}
function hb(jb){var kb=0,lb=0;if(typeof(window.innerWidth)=='number'){kb=window.innerWidth;lb=window.innerHeight;}
else if(document.documentElement&&(document.documentElement.clientWidth||document.documentElement.clientHeight)){kb=document.documentElement.clientWidth;lb=document.documentElement.clientHeight;}
else if(document.body&&(document.body.clientWidth||document.body.clientHeight)){kb=document.body.clientWidth;lb=document.body.clientHeight;}
if(jb=='height')return lb;if(jb=='width')return kb;}
var gmwds=false;function start(){mb("l1");mb("l2");mb("l3");mb("l4");initCounter();if(typeof init_local=='function'){init_local();}
if(quest.number===null){qst_handle();}
if(gmwds){gmwd();}
}
function nb(){return new Date().getTime();}
function db(){return Math.round(nb()/1000);}
function ob(pb){p=pb.innerHTML.split(":");qb=p[0]*3600+p[1]*60+p[2]*1;return qb;}
function rb(s){var sb,tb,ub;if(s>-2){sb=Math.floor(s/3600);tb=Math.floor(s/60)%60;ub=s%60;t=sb+":";if(tb<10){t+="0";}
t+=tb+":";if(ub<10){t+="0";}
t+=ub;}
else
{t="<a href=\"#\" onClick=\"return Popup(2,5);\"><span class=\"c0 t\">0:00:0</span>?</a>";}
return t;}
function initCounter(){for(var i=1;;i++){pb=document.getElementById("tp"+i);if(pb!=null){ab[i]=new Object();ab[i].node=pb;ab[i].counter_time=ob(pb);}
else{break;}
}
for(i=1;;i++){pb=document.getElementById("timer"+i);if(pb!=null){bb[i]=new Object();bb[i].node=pb;bb[i].counter_time=ob(pb);}
else{break;}
}
executeCounter();}
function executeCounter(){for(var i in ab){vb=db()-cb;wb=rb(ab[i].counter_time+vb);ab[i].node.innerHTML=wb;}
for(i in bb){vb=db()-cb;xb=bb[i].counter_time-vb
if(eb==0&&xb<1){eb=1;if(auto_reload==1){setTimeout("document.location.reload()",1000);}
else if(auto_reload==0){setTimeout("mreload()",1000);}
}
else{}
wb=rb(xb);bb[i].node.innerHTML=wb;}
if(eb==0){window.setTimeout("executeCounter()",1000);}
}
function mb(yb){pb=document.getElementById(yb);if(pb!=null){fb[yb]=new Object();var zb=pb.innerHTML.match(/(\d+)\/(\d+)/);element=zb[0].split("/");$b=parseInt(element[0]);_b=parseInt(element[1]);ac=pb.title;if(ac!=0){bc=nb();timer[yb]=new Object();timer[yb].start=bc;timer[yb].production=ac;timer[yb].start_res=$b;timer[yb].max_res=_b;timer[yb].ms=3600000/ac;cc=100;if(timer[yb].ms<cc){timer[yb].ms=cc;}
timer[yb].node=pb;executeTimer(yb);}
else
{timer[yb]=new Object();fb[yb].value=$b;}
}
}
function executeTimer(yb){vb=nb()-timer[yb].start;if(vb>=0){dc=Math.round(timer[yb].start_res+vb*(timer[yb].production/3600000));if(dc>=timer[yb].max_res){dc=timer[yb].max_res;}
else
{window.setTimeout("executeTimer('"+yb+"')",timer[yb].ms);}
fb[yb].value=dc;timer[yb].node.innerHTML=dc+'/'+timer[yb].max_res;}
}
var ec=new Array(0,0,0,0,0);function add_res(fc){gc=fb['l'+(5-fc)].value;hc=haendler*carry;ec[fc]=ic(ec[fc],gc,hc,carry);document.getElementById('r'+fc).value=ec[fc];}
function upd_res(fc,jc){gc=fb['l'+(5-fc)].value;hc=haendler*carry;if(jc){kc=gc;}
else
{kc=parseInt(document.getElementById('r'+fc).value);}
if(isNaN(kc)){kc=0;}
ec[fc]=ic(parseInt(kc),gc,hc,0);document.getElementById('r'+fc).value=ec[fc];}
function ic(lc,mc,nc,oc){pc=lc+oc;if(pc>mc){pc=mc;}
if(pc>nc){pc=nc;}
if(pc==0){pc='';}
return pc;}
function qc(n,d){var p,i,x;if(!d)d=document;if((p=n.indexOf("?"))>0&&parent.frames.length){d=parent.frames[n.substring(p+1)].document;n=n.substring(0,p);}
if(!(x=d[n])&&d.all)x=d.all[n];for(var i=0;!x&&i<d.forms.length;i++)x=d.forms[i][n];for(var i=0;!x&&d.layers&&i<d.layers.length;i++)x=qc(n,d.layers[i].document);return x;}
function btm0(){var i,x,a=document.MM_sr;for(var i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++)x.src=x.oSrc;}
function btm1(){var i,j=0,x,a=btm1.arguments;document.MM_sr=new Array;for(var i=0;i<(a.length-2);i+=3)if((x=qc(a[i]))!=null){document.MM_sr[j++]=x;if(!x.oSrc)x.oSrc=x.src;x.src=a[i+2];}
}
function Popup(i,j){pb=document.getElementById("ce");if(pb!=null){var rc="<div class=\"popup3\"><iframe frameborder=\"0\" id=\"Frame\" src=\"manual.php?s="+i+"&typ="+j+"\" width=\"412\" height=\"440\" border=\"0\"></iframe></div><a href=\"#\" onClick=\"Close(); return false;\"><img src=\"img/un/a/x.gif\" border=\"1\" class=\"popup4\" alt=\"Close\"></a>";pb.innerHTML=rc;}
sc();if(!is_ie6&&!tc)return false;else return true;}
function sc(){if(gb()<700||ib()<700){document.getElementById("ce").style.position='absolute';tc=true;}
else{document.getElementById("ce").style.position='fixed';tc=false;}
}
function Close(){pb=document.getElementById("ce");if(pb!=null){pb.innerHTML='';}
if(quest.anmstep!==false){quest.anmstep=false;}
}
function Allmsg(){for(var x=0;x<document.msg.elements.length;x++){var y=document.msg.elements[x];if(y.name!='s10')y.checked=document.msg.s10.checked;}
}
function xy(){uc=screen.width+":"+screen.height;document.snd.w.value=uc;}
function my_village(){var vc=Math.round(0);var wc;var e=document.snd.dname.value;for(var i=0;i<dorfnamen.length;i++){if(dorfnamen[i].indexOf(e)>-1){vc++;wc=dorfnamen[i];}
}
if(vc==1){document.snd.dname.value=wc;}
}
function map(xc,yc,zc,$c,x,y){document.getElementById('x').firstChild.nodeValue=x;document.getElementById('y').firstChild.nodeValue=y;pb=document.getElementById("tb");if(pb!=null){if($c==''){$c='-';}
var _c="<table cellspacing='1' cellpadding='2' class='tbg f8'><tr><td class='rbg f8' colspan='2'></a>"+xc+"</td></tr><tr><td width='45%' class='s7 f8'>"+text_spieler+"</td><td class='s7 f8'>"+yc+"</td></tr><tr><td class='s7 f8'>"+text_einwohner+"</td><td class='s7 f8' id='ew'>"+zc+"</td></tr><tr><td class='s7 f8'>"+text_allianz+"</td><td class='s7 f8'>"+$c+"</td></tr></table>";var ad="<table class='f8 map_infobox_grey' cellspacing='1' cellpadding='2'><tr><td class='c b' colspan='2' align='center'></a>"+text_details+"</td></tr><tr><td width='45%' class='c s7'>"+text_spieler+"</td><td class='c s7'>-</td></tr><tr><td class='c s7'>"+text_einwohner+"</td><td class='c s7'>-</td></tr><tr><td class='c s7'>"+text_allianz+"</td><td class='c s7'>-</td></tr></table>";if(yc!=''){pb.innerHTML=_c;}
else{pb.innerHTML=ad;}
}
}
function x_y(x,y){document.getElementById('x').firstChild.nodeValue=x;document.getElementById('y').firstChild.nodeValue=y;}
function pop(bd){cd=window.open(bd,"map","top=100,left=25,width=975,height=550");cd.focus();return false;}
var dd=document.getElementById?1:0;var ed=document.all?1:0;var fd=(navigator.userAgent.indexOf("Mac")>-1)?1:0;var gd=(ed&&(!fd)&&(typeof(window.offscreenBuffering)!='undefined'))?1:0;var hd=gd;var id=gd&&(window.navigator.userAgent.indexOf("SV1")!=-1);function changeOpacity(jd,opacity){if(gd){jd.style.filter='progid:DXImageTransform.Microsoft.Alpha(opacity='+(opacity*100)+')';}
else if(dd){jd.style.MozOpacity=opacity;}
}
function kd(url,ld,md,nd){if(md===undefined){md='GET';}
var od;if(window.XMLHttpRequest){od=new XMLHttpRequest();}
else if(window.ActiveXObject){try{od=new ActiveXObject("Msxml2.XMLHTTP");}
catch(e){try{od=new ActiveXObject("Microsoft.XMLHTTP");}
catch(e){}
}
}
else{throw'Can not create XMLHTTP-instance';}
od.onreadystatechange=function(){if(od.readyState==4){if(od.status==200){var pd=od.getResponseHeader('Content-Type');pd=pd.substr(0,pd.indexOf(';'));switch(pd){case'application/json':ld((od.responseText==''?null:eval('('+od.responseText+')')));break;case'text/plain':ld(od.responseText);break;default:throw'Illegal content type';}
}
else{throw'An error has occurred during request';}
}
}
;od.open(md,url,true);if(md=='POST'){od.setRequestHeader('Content-Type','application/x-www-form-urlencoded; charset=utf-8');var qd=rd(nd);}
else{var qd=null;}
od.send(qd);}
function rd(sd){var td='';var ud=true;for(var vd in sd){td+=(ud?'':'&')+vd+'='+window.encodeURI(sd[vd]);if(ud){ud=false;}
}
return td;}
function mreload(){param='reload=auto';url=window.location.href;if(url.indexOf(param)==-1){if(url.indexOf('?')==-1){url+='?'+param;}
else
{url+='&'+param;}
}
document.location.href=url;}
function wd(){var xd=1*this.id.substring(4,5);var yd=1*(this.id.substring(5,7)=='p7'?7:1);zd(xd,yd);return false;}
function $d(xd,yd,_d){if(_d==null){_d=0;}
if(m_c.size==null){throw'Globale Variable m_c.size muss auf den Wert von $travian[map_prefetch_rows]) gesetzt werden.';}
var ae,be;if(null===yd||1===yd){be=m_c.size-1;}
else if(7==yd){ae=7;be=-6;}
else{throw'Parameter steps muss 1 oder 7 sein.';}
var x,y,ce,de,z;z=m_c.z;switch(xd){case 1:x=z.x+3;y=z.y+3+_d;ce=z.x-3;de=y+be;break;case 2:x=z.x+3+_d;y=z.y-3;ce=x+be;de=z.y+3;break;case 3:x=z.x+3;y=z.y-3-_d;ce=z.x-3;de=y-be;break;case 4:x=z.x-3-_d;y=z.y-3;ce=x-be;de=z.y+3;break;}
return{'x':x,'y':y,'xx':ce,'yy':de}
;}
function ee(x,y){if(x===null||y===null){throw('Koordinate muss x und y Werte haben.');}
if(y>400){y-=801;}
if(x>400){x-=801;}
if(y<-400){y+=801;}
if(x<-400){x+=801;}
return{'x':x,'y':y}
;}
function fe(xd,yd){var z={}
;z.x=m_c.z.x*1;z.y=m_c.z.y*1;switch(xd){case 1:z.y+=yd;break;case 2:z.x+=yd;break;case 3:z.y-=yd;break;case 4:z.x-=yd;break;}
m_c.z=(ee(z.x,z.y));}
function ge(he){return'ajax.php?f=k7&x='+he.x+'&y='+he.y+'&xx='+he.xx+'&yy='+he.yy;}
function zd(xd,yd){var he,ie;if(je){return false;}
if(ke()){if(le){return false;}
je=true;me();m_c.usealternate=false;m_c.cindex=0;fe(xd,yd);he=$d(xd,yd);ne=ge(he);kd(ne,oe);}
else{if(pe()){if(le){return false;}
le=true;fe(xd,yd);he=$d(xd,yd,2);ne=ge(he);kd(ne,oe);}
else if(qe()){fe(xd,yd);re();me();}
else{fe(xd,yd);}
se(xd,yd);}
function oe(te){var ue;if(pe()){ue=ve(m_c.cindex);m_c.usealternate=false;le=false;}
else{ue=m_c.cindex;}
m_c.fields[ue]=te;if(ke()){se(xd,yd);we(xd);je=false;}
}
function pe(){return m_c.usealternate;}
function ke(){return(xd!=m_c.dir||yd==7||(yd==1&&yd!=m_c.steps));}
function qe(){return(m_c.index==m_c.size);}
}
function xe(xd,yd){m_c.dir=xd;m_c.steps=yd;}
function me(){m_c.index=0;}
function ye(){m_c.index++;if(m_c.index==m_c.size-2){m_c.usealternate=true;}
}
function re(){m_c.cindex=ve(m_c.cindex);}
function se(xd,yd){if(1==yd){ze(xd);$e(m_c.fields[m_c.cindex],xd,yd);we(xd);ye();_e();}
else if(7==yd){af(m_c.fields[m_c.cindex]);_e();}
xe(xd,yd);}
function ve(ue){return(ue==0?1:0);}
function af(te){for(var i=0;i<7;i++){for(var j=0;j<7;j++){bf(i,j,te[i][j]);}
}
}
function cf(df,ef){if(df==''){if(ef.href!=''){ef.removeAttribute('href');ef.style.cursor='default';}
}
else{ef.href=df;}
}
function bf(ff,gf,hf){if(jf){return true;}
var kf,area;var lf;kf=mf(ff,gf,'i');area=mf(ff,gf,'a');kf.src=hf.src;cf(hf.href,area);area.title=hf.title;area.details=[];if(null==hf.name){lf=['x','y'];}
else{lf=['dname','name','ew','ally','x','y'];}
for(var i in lf){area.details[lf[i]]=hf[lf[i]];}
}
function nf(e){if(jf){return true;}
var vd=(window.event)?event.keyCode:e.keyCode;var xd=of(vd);if(xd!=0){return false;}
}
function map_init(){if(null==m_c.az){throw'm_c.az muss seitenspezifisch initialisiert werden.';}
for(var p in m_c.az){document.getElementById('ma_'+p).onclick=wd;}
var pf=['mcx','mcy'];for(var i in pf){document.getElementById(pf[i]).onfocus=function(){jf=true;}
;document.getElementById(pf[i]).onblur=function(){jf=false;}
;}
document.onkeyup=qf;document.onkeydown=rf;document.onkeypress=nf;for(var i=0;i<7;i++){for(var j=0;j<7;j++){area=mf(i,j,'a');area.onmouseover=sf;area.onmouseout=tf;area.details=m_c.ad[i][j];}
}
}
function sf(){if(null==this.details.name){x_y(this.details.x,this.details.y);}
else{map(this.details.dname,this.details.name,this.details.ew,this.details.ally,this.details.x,this.details.y);}
}
function tf(){if(null==this.details.name){uf();}
else{_e();}
}
var m_c={'index':0,'dir':0,'size':null,'fields':[],'cindex':0,'usealternate':false}
;var je=false;var le=false;var vf=false;var jf=false;function $e(te,xd,yd){var wf,xf;for(var i=0;i<7;i++){switch(xd){case 1:wf=i;xf=6;hf=te[i][m_c.index];break;case 2:wf=6;xf=i;hf=te[m_c.index][i];break;case 3:wf=i;xf=0;hf=te[i][m_c.size-m_c.index-1];break;case 4:wf=0;xf=i;hf=te[m_c.size-m_c.index-1][i];break;}
bf(wf,xf,hf);}
}
function yf(x,y,ce,de){mf(ce,de,'i')['src']=mf(x,y,'i')['src'];var zf=['onclick','onmouseover','onmouseout','details','title'];var hf=mf(x,y,'a');var $f=mf(ce,de,'a');for(var f in zf){$f[zf[f]]=hf[zf[f]];}
cf(hf.href,$f);}
function ze(xd){for(var i=0;i<7;i++){for(var j=1;j<7;j++){switch(xd){case 1:yf(i,j,i,j-1);break;case 2:yf(j,i,j-1,i);break;case 3:yf(i,6-j,i,7-j);break;case 4:yf(6-j,i,7-j,i);break;}
}
}
}
var _f=[];_f[38]=1;_f[39]=2;_f[40]=3;_f[37]=4;function of(vd){if(_f[vd]!==undefined){return _f[vd];}
return 0;}
var ag=0;function qf(e){if(jf){return true;}
var vd=((window.event)?event.keyCode:e.keyCode);if(16==vd){vf=false;}
var xd=of(vd);if(xd==ag){ag=0;}
}
function m_r(xd,bg){if(ag==xd&&bg==cg){window.setTimeout('m_r('+xd+', '+bg+');',100);zd(xd,1);}
}
function we(xd){if(1==xd||3==xd){var jb='y';}
else if(2==xd||4==xd){var jb='x';}
else{throw'Nur Richtungen 1-4 sind erlaubt';}
var dg,eg,fg;for(var i=0;i<7;i++){if(jb=='x'){eg=i;fg=0;}
else{eg=0;fg=i;}
dg=mf(eg,fg,'a').details[jb];document.getElementById('m'+jb+i).innerHTML=dg;}
}
var cg=0;function rf(e){if(jf){return true;}
var vd=(window.event)?event.keyCode:e.keyCode;if(vd==16){vf=true;}
var xd=of(vd);if(xd!=0&&xd!=ag){var yd=(vf?7:1);zd(xd,yd);var bg=new Date().getTime();if(yd==1){window.setTimeout('m_r('+xd+', '+bg+');',500);}
cg=bg;ag=xd;}
if(xd!=0){return false;}
}
function mf(ff,gf,gg){c=hg(ff,gf,gg);return document.getElementById(hg(ff,gf,gg));}
function hg(ff,gf,gg){return gg+'_'+ff+'_'+gf;}
function uf(){var z=m_c.z;x_y(z.x,z.y);}
function _e(){var z=m_c.z;map('','','','',z.x,z.y);}
var quest={'anmstep':false}
;function ig(length,jg){if(length===undefined){length=8;}
if(jg===undefined){jg=0.5;}
var kg='0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz';var ig='';for(var i=0;i<length;i++){var lg=Math.floor((Math.random()+jg)*0.5*kg.length);ig+=kg.substring(lg,lg+1);}
return ig;}
function mg(){var ng='ajax.php?f=qst';var jg=(Math.abs(quest.number)+1)/(Math.abs(quest.last)+1);return ng+'&cr='+ig(4,jg);}
function og(){document.getElementById('ce').innerHTML='';var step;if(quest.anmstep===false){step={'step':{}
,'source':{}
,'current':{}
,'target':{'width':448,'height':482,'top':-1}
,'fps':50,'n':10,'i':0,'anm':{}
}
;step.target[quest.rtl?'right':'left']='-502';}
else{step=quest.anmstep;pg(false);}
step.anm=document.getElementById('anm');for(var qg in step.target){step.source[qg]=Number(step.anm.style[qg].substr(0,step.anm.style[qg].length-2));step.current[qg]=step.source[qg];step.step[qg]=Math.round((step.target[qg]-step.source[qg])/step.n);}
step.timeout=1000/step.fps;quest.cstep=step;quest.anmlock=true;window.setTimeout('anm_step()',step.timeout);}
function rg(step){for(var qg in step.target){step.anm.style[qg]=step.current[qg]+'px';}
}
function sg(step){step.i++;if(step.i==2){step.anm.style.visibility='visible';}
for(var qg in step.target){step.current[qg]+=step.step[qg];}
return step;}
function pg(tg){if(tg===undefined){tg==false;}
var ug=document.getElementById('ce');if(tg){var vg='<div id="popup3" class="popup3"></div><a href="#" onClick="qst_handle()"><img src="img/un/a/x.gif" border="1" class="popup4" alt="Close"></a>';ug.innerHTML=vg;wg();qst_wfm();sc();xg(true);}
else{ug.innerHTML='';xg(false);}
}
function xg(vis){if(!is_ie6){return;}
var yg=vis?'hidden':'visible';var zg=document.getElementsByTagName('select');var n=zg.length;for(var i=0;i<n;i++){zg[i].style.visibility=yg;}
}
function anm_step(){step=sg(quest.cstep);rg(step);if(step.i<step.n){window.setTimeout('anm_step()',step.timeout);}
else{step.anm.style.visibility='hidden';quest.anmlock=false;quest.cstep=false;if(quest.anmstep===false){step.current=step.target;step.target=step.source;step.source=step.current;rg(step);step.i=0;pg(true);quest.anmstep=step;}
else{quest.anmstep=false;if(quest.number>=quest.last){document.getElementById('qge').innerHTML='';}
}
}
}
function qst_fhandle(){nd={'val':1}
;kd(mg(),function($g){}
,'POST',nd
);qst_handle();}
function qst_handle(){if(quest.anmlock){return false;}
quest.markup=false;if(quest.anmstep===false){kd(mg(),function($g){for(var vd in $g){quest[vd]=$g[vd];}
}
);}
og();if(quest.ar){auto_reload=quest.ar;quest.ar=undefined;}
}
function qst_wfm(){var _g=document.getElementById('popup3');if(!quest.markup||!_g){if(!quest.anmlock){window.setTimeout('qst_wfm(true)',50);}
}
else{ah(quest);_g.innerHTML=quest.markup;bh=false;if(quest.reward.finish&&window.bld){var ch=document.getElementById('lbau1');if(bld.length<2&&bld[0].stufe==1&&bld[0].gid==1){document.getElementById('lbau1').innerHTML='';bh=0;}
else{for(var i in bld){if(bld[i].stufe==1&&bld[i].gid==1){document.getElementById('lbau1').getElementsByTagName('table')[0].deleteRow(i);bh=i;break;}
}
}
if(bh!==false){var dh=document.getElementById('rf'+bld[bh].aid);if(dh){dh.src='img/un/g/s/s'+(bld[bh].stufe)+'.gif';}
else{document.getElementById('f3').innerHTML+='<img src="img/un/g/s/s'+(bld[bh].stufe)+'.gif" class="rf'+bld[bh].aid+'">';}
}
quest.ar=auto_reload;auto_reload=-1;}
if(quest.reward.plus){var kf=document.getElementById('lleft').getElementsByTagName('img')[0];kf.src=kf.src.replace('0.gif','1.gif');}
quest.markup=false;quest.msg=false;}
}
function qst_weiter(){wg();kd(mg(),function($g){document.getElementById('popup3').innerHTML=$g.markup;var eh=document.getElementById('qgei');eh.src=$g.qgsrc;ah($g);}
);}
function wg(){document.getElementById('popup3').innerHTML='<img src="img/un/misc/xlo.gif" />';}
function qst_enter(fh){if(fh===undefined){fh=false;}
var nd;if(fh){nd={'x':document.getElementById('qst_val_x').value,'y':document.getElementById('qst_val_y').value}
;}
else{nd={'val':document.getElementById('qst_val').value}
;}
wg();kd(mg(),function($g){for(var vd in $g){quest[vd]=$g[vd];}
}
,'POST',nd
);qst_wfm();}
function qst_enter_coords(){qst_enter(true);}
function ah(gh){var eh=document.getElementById('qgei');if(eh&&gh.qgsrc){eh.src=gh.qgsrc;}
var hh=document.getElementById('n5');if(hh&&gh.msrc){hh.src=gh.msrc;}
if(gh.cookie){var date=new Date();date.setTime(date.getTime()+300000);document.cookie='t3fw=1; expires='+date.toUTCString()+';';}
if(gh.fest&&document.getElementById('lmid2').firstChild.nextSibling.className=='d1'){document.getElementById('lmid1').innerHTML+=gh.fest;}
}
function gmwd(){if(is_ff2&&document.getElementById("gmwi").offsetWidth<50){document.cookie="a3=2; expires=Wed, 1 Jan 2020 00:00:00 GMT";}
else{document.cookie="a3=1; expires=Wed, 1 Jan 2020 00:00:00 GMT";}
}
function gmc(){document.getElementById("gmw").style.display="none";document.cookie="a3=3; expires=Wed, 1 Jan 2020 00:00:00 GMT";}
