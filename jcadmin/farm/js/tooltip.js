function tooltip_funcs()
{
this.showingdscp = 0;
this.canmovedscp = 1;
this.commtip;
this.usingcommtip = false;
if (navigator.userAgent.indexOf('MSIE 6.0')!=-1)
	{
		this.isIE6 = true;
	}
else
	{
		this.isIE6 = false;
	}

this.createcommtip = function()
{
this.commtip = document.createElement('div');
this.delay_show = null;
this.ttipid = 'tootip'+Math.ceil(Math.random()*10000);
this.commtip.id = this.ttipid
this.commtip.className = 'ttip';
this.commtip.style.display = 'none';
this.commtip.innerHTML = "<table border='0' cellpadding='0' cellspacing='0' class='s7'><tr><td class='b3_tl b3s'></td><td class='b3_t'></td><td class='b3_tr b3s'></td></tr><tr><td class='b3_l'></td><td class='b3_c'><div class='s11' id='"+this.commtip.id+"content'></div></td><td class='b3_r'></td></tr><tr><td class='b3_bl b3s'></td><td class='b3_b'></td><td class='b3_br b3s'></td></tr></table>";
document.body.appendChild(this.commtip);
this.commtip.content = document.getElementById(this.commtip.id+'content');
}
//fe
this.showdscp = function (event,dscpobj,usecommtip)
{
	if (!event) var event = window.event;
	if (!dscpobj) return
	else if (typeof(dscpobj)=='string') var dscpobj = document.getElementById(dscpobj);
	if (usecommtip)
		{
			if (!this.commtip)
				{
					this.createcommtip();
				}
			this.commtip.content.innerHTML = dscpobj.innerHTML;
			dscpobj = this.commtip;
			this.usingcommtip = true;
		}
	this.cruent_dscpobj = dscpobj;
	this.setpos(dscpobj, event.clientX, event.clientY);
	dscpobj.style.display = 'block';
	this.showingdscp = 1;
	this.canmovedscp = 1;
}

this.setpos = function (o,ex,ey)
{
var cw = 1024;
var ch = 768;
var xf = 0;
var yf = 0;
var pw = 0;
var ph = 0;
if (document.documentElement.clientWidth)
	{
		xf = document.documentElement.scrollLeft;
		yf = document.documentElement.scrollTop;
		pw = document.documentElement.scrollWidth;
		ph = document.documentElement.scrollHeight;
		cw = document.documentElement.clientWidth;
		ch = document.documentElement.clientHeight;
	}
else if (document.body.clientWidth)
	{
		xf = document.body.scrollLeft;
		yf = document.body.scrollTop;
		pw = document.body.scrollWidth;
		ph = document.body.scrollHeight;
		cw = document.body.clientWidth;
		ch = document.body.clientHeight;
	}
if (false)
	{
		o.style.left = 'auto';
		ex = cw-ex-xf+16;
		o.style.right = ex+'px';
	}
else
	{
		o.style.right = 'auto';
		ex = ex+xf+16;
		o.style.left = ex+'px';
	}
if (false)
	{
		o.style.top = 'auto';
		if (this.isIE6)
			{
				yf = 0;
			}
		o.style.bottom = (ch-ey-yf-16)+'px';
	}
else
	{
		o.style.bottom = 'auto';
		o.style.top = (ey+yf-16)+'px';
	}
}
//fe

this.movedscp = function (event,dscpobj)
{
	if (this.showingdscp == 0  || this.canmovedscp == 0 || !dscpobj)
		{
			return;
		}
	if (!event)
		{
			var event = window.event;
		}
	if (!this.usingcommtip)
		{
			if (typeof(dscpobj)=='string')
				{
				dscpobj = document.getElementById(dscpobj);
				}
		}
	else
		{
			dscpobj = this.commtip;
		}
	if (!dscpobj)
		{
			return;
		}
	this.setpos(dscpobj, event.clientX, event.clientY);
}
this.hinddscp = function(event,dscpobj){this.hidedscp(event,dscpobj)}
this.hidedscp = function(event,dscpobj)
{
	if (this.canmovedscp == 0 || !dscpobj)
		{
			return;
		}
	this.showingdscp = 0;
	if (!event)
		{
			var event = window.event;
		}
	if (!this.usingcommtip)
		{
			if (typeof(dscpobj)=='string')
				{
				dscpobj = document.getElementById(dscpobj);
				}
		}
	else
		{
			dscpobj = this.commtip;
		}
	if (!dscpobj)
		{
			return;
		}
	if (this.delay_show)
		{
			window.clearTimeout(this.delay_show);
			this.delay_show = null;
		}
	dscpobj.style.display = 'none';
	dscpobj.style.top = 0 +'px';
	dscpobj.style.left = 0 +'px';
	this.usingcommtip = false;
}

this.delay_showdscp = function(event,dscpobj,delay,usecommtip)
{
	if (!event) var event = window.event;
	if (!dscpobj) return
	else if (typeof(dscpobj)=='string') var dscpobj = document.getElementById(dscpobj);
	if (usecommtip)
		{
			if (!this.commtip)
				{
					this.createcommtip();
				}
			this.commtip.content.innerHTML = dscpobj.innerHTML;
			dscpobj = this.commtip;
			this.usingcommtip = true;
		}
	this.setpos(dscpobj, event.clientX, event.clientY);
	this.showingdscp = 1;
	this.delay_show = window.setTimeout("delay_showdscp_act('"+dscpobj.id+"')",delay);
}
//fe

this.delay_showdscp_act = function(dscpobj)
{
	if (typeof(dscpobj)=='string')
		{
		dscpobj = document.getElementById(dscpobj);
		}
	dscpobj.style.display = 'block';
}
//fe
this.reg_key_event = function()
{
var self = this;
document.oncontextmenu = function(e){
	if (self.showingdscp)
		{
			if (self.canmovedscp)
				{
					self.canmovedscp = 0;
					return false;
				}
			else
				{
					self.canmovedscp = 1;
				}
		}
	else
		{
			self.canmovedscp = 1;
		}
	};
document.onkeydown = function(event)
	{
		if (!event)
			{
				var event = window.event;
			}
		if (event.keyCode == 16)
			{
				this.canmovedscp = 0;
			}
	};
document.onkeyup = function(event)
	{
		if (!event)
			{
				var event = window.event;
			}
		if (event.keyCode == 16)
			{
				this.canmovedscp = 1;
			}
	};
}
//fe
}
//ce

var tTip = new tooltip_funcs('tTip');



function showdscp(event,dscpid)
{
tTip.showdscp(event,dscpid)
}
//fe
function movedscp(event,dscpid)
{
tTip.movedscp(event,dscpid)
}
//fe
function hinddscp(event,dscpid)
{
tTip.hinddscp(event,dscpid)
}
//fe
function hidedscp(event,dscpid)
{
tTip.hinddscp(event,dscpid)
}
//fe
function delay_showdscp(event,dscpid,delay)
{
tTip.delay_showdscp(event,dscpid,delay)
}
//fe
function delay_showdscp_act(t,l,dscpid)
{
tTip.delay_showdscp_act(t,l,dscpid)
}
//fe
function dont_move_ttip(event)
{
tTip.dont_move_ttip(event)
}
//fe
function can_move_ttip(event)
{
tTip.can_move_ttip(event)
}
//fe
function reg_key_event()
{
tTip.reg_key_event()
}
//fe