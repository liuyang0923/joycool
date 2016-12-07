<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="no" doctype-public="HTML" doctype-system="-//W3C//DTD HTML 4.0 Transitional//EN"/>
<xsl:template match="wml/card"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="viewport" content="width=240,initial-scale=1.29,user-scalable=0"/><meta name="MobileOptimized" content="236"/>
<title>乐酷游戏社区</title>
<link href="/proxy.css?v=2" rel="stylesheet" type="text/css"/>
</head>
<body>
<script src="/proxy.js?v=2">;</script>
<div align="center" class="div1">
  <font color="yellow"><xsl:value-of select="@title" /></font>
</div>
<div class="div2">
<xsl:apply-templates />
</div>
</body>
</html>
</xsl:template>

<xsl:template match="a">
	<a>
	<xsl:copy-of select="@*" />
	<xsl:apply-templates />
	</a>
</xsl:template>

<xsl:template match="img">
	<img>
	<xsl:copy-of select="@*" />
	<xsl:apply-templates />
	</img>
</xsl:template>

<xsl:template match="br">
	<br/>
</xsl:template>

<xsl:template match="input">
	<input type="text">
	<xsl:copy-of select="@name | @value | @maxlength | @id" />
	</input>
</xsl:template>

<xsl:template match="anchor">
	<a href="javascript:void(0);">
	<xsl:apply-templates select="go|prev|refresh"/>
	<xsl:copy-of select=" text() " />
	</a>
</xsl:template>

<xsl:template match="go">
	<xsl:attribute name="onclick"><xsl:for-each select="postfield">addfrom('<xsl:value-of select="@name" />','<xsl:value-of select="@value" />');</xsl:for-each>go('<xsl:value-of select="@href" />');return false;</xsl:attribute>
</xsl:template>    

<xsl:template match="prev">
	<xsl:attribute name="onclick">
	window.history.back();return false;
	</xsl:attribute>
</xsl:template>

<xsl:template match="refresh">
<!--	<xsl:attribute name="onclick">
	window.location.reload();return false;
	</xsl:attribute>-->
</xsl:template>

<xsl:template match="select">
	<select>
	<xsl:copy-of select="@id | @title | @name | @tabindex | @multiple" /> 
	<xsl:apply-templates select="option" /> 
	</select>
</xsl:template>

<xsl:template match="option">
	<option>
	  <xsl:copy-of select="@id | @onpick | @title | @value | text()" /> 
	</option>
</xsl:template>

<xsl:template match="timer">
	<script>tred(<xsl:value-of select="@value" />,'<xsl:value-of select="../@ontimer" />')</script>
</xsl:template>

</xsl:stylesheet>