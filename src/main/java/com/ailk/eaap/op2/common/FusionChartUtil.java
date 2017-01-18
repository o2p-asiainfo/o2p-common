package com.ailk.eaap.op2.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

/**
 * fusionChart 锟斤拷台锟斤拷织xml锟接匡拷通锟斤拷锟斤拷
 * 锟斤拷图锟斤拷锟斤拷,锟斤拷要锟斤拷锟斤拷锟斤拷锟斤拷维锟斤拷锟斤拷维图锟斤拷(锟斤拷状图,锟斤拷状图,锟斤拷锟斤拷图) 
 * @author chenwei
 */

public class FusionChartUtil {
	//default params
	private static final String CAPTION = "";
	private static final String XAXISNAME = "";
	private static final String YAXISNAME = "";
	private static final Integer HEIGHT = 400;
	private static final Integer WIDTH = 600;
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	private static final String JSPATH = "/eaap_conf/resource/comm/js/FusionCharts.js";
	private static final String SWFPATH = "/eaap_conf/resource/comm/swf/";
	
	// user-defind
	private String caption;     //图锟轿的憋拷锟斤拷锟斤拷锟�
	private String subcaption;  //锟接憋拷锟斤拷
	private String xAxisName;   //锟斤拷锟斤拷锟斤拷锟斤拷锟�x锟斤拷)锟斤拷锟�
	private String yAxisName;   //锟斤拷锟斤拷锟斤拷锟斤拷锟�y锟斤拷)锟斤拷锟�
	private Integer width;      //x锟斤拷锟�
	private Integer height;     //Y锟斤拷锟�
	private String jsPath;
	private String swfPath;
	private String divId = "drawDiv" + UUID.randomUUID().toString();
	private String myChartName = "";
	// 一维锟斤拷锟�锟斤拷思锟角憋拷锟斤拷锟斤拷锟斤拷图锟斤拷只锟斤拷示一锟斤拷锟斤拷锟斤拷)
	private List<Map<Object,Object>> oneDimensionList = new ArrayList<Map<Object,Object>>();
	// 锟斤拷维锟斤拷锟�锟斤拷思锟角憋拷锟斤拷锟斤拷锟斤拷图锟斤拷锟斤拷示锟斤拷锟斤拷锟斤拷锟斤拷)
	private Map<Object,Map<Object,Object>> manyDimensionMap = new HashMap<Object,Map<Object,Object>>();
	private LinkedHashMap <Object,String> manyDimensionList = new LinkedHashMap<Object,String>();
	// x锟斤拷锟斤拷萍锟斤拷锟�锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟矫碉拷,锟斤拷要锟角讹拷锟斤拷锟斤拷瞎锟斤拷锟絰锟斤拷锟斤拷锟�
	private List<String> xAxisNameList = new ArrayList<String>();
	private List<String> xAxisValueList = new ArrayList<String>();
	//指锟斤拷锟接︼拷锟缴�
	private Map<String,String> colorMap = new HashMap<String,String>();
	
	//锟角憋拷锟斤拷实锟斤拷锟斤拷示值
	private String angularVal;
	//锟角憋拷锟教刻讹拷锟斤拷锟街�
	private String angularMaxVal;
	//锟角憋拷锟教等凤拷值
	private String majorTMNumber;
	//图锟�位锟斤拷缀
	private String numberSuffix;
	
	//默锟较的癸拷锟届函锟斤拷
	public FusionChartUtil(){
		this.verifyParams();
	}
	
	public FusionChartUtil(String caption,String xAxisName,String yAxisName,Integer width,
			Integer height,String jsPath,String swfPath,List<Map<Object,Object>> oneDimensionList){
		this.caption = caption;
		this.xAxisName = xAxisName;
		this.yAxisName = yAxisName;
		this.width = width;
		this.height = height;
		this.jsPath = jsPath;
		this.swfPath = swfPath;
		this.oneDimensionList = oneDimensionList;
		this.verifyParams();
	}
	
	public FusionChartUtil(String caption,String xAxisName,String yAxisName,Integer width,
			Integer height,String jsPath,String swfPath,
			List<String> xAxisNameList,List<String> xAxisValueList,
			Map<Object,Map<Object,Object>> manyDimensionMap,
			LinkedHashMap<Object,String> manyDimensionList,
			Map<String,String> colorMap,
			String angularVal,String angularMaxVal,String majorTMNumber,String numberSuffix){
		this.caption = caption;
		this.xAxisName = xAxisName;
		this.yAxisName = yAxisName;
		this.width = width;
		this.height = height;
		this.jsPath = jsPath;
		this.swfPath = swfPath;
		this.xAxisNameList = xAxisNameList;
		this.xAxisValueList = xAxisValueList;
		this.manyDimensionMap = manyDimensionMap;
		this.colorMap = colorMap;
		this.angularVal = angularVal;
		this.angularMaxVal = angularMaxVal;
		this.majorTMNumber = majorTMNumber;
		this.numberSuffix = numberSuffix;
		this.manyDimensionList = manyDimensionList;
	}
	
	//拼装一维图锟轿碉拷xml锟斤拷萁涌锟�
	public String getOneDimensionsXmlData(){
		StringBuilder str = new StringBuilder();
		str.append("<chart caption='")
		   .append(this.caption)
		   .append("' xAxisName='")
		   .append(this.xAxisName)
		   .append("' yAxisName='")
		   .append(this.yAxisName)
		  //.append("' showValues='0' labelStep='10' palette='3' rotateYAxisName='0' baseFontSize='10' lineThickness='3' lineColor='FF5904' useRoundEdges='1' showBorder='0' slantLabels='1' divLineAlpha='20' divLineIsDashed='1' showAlternateHGridColor='1' bgColor='FFFFFF,CC3300' shadowAlpha='40' numvdivlines='5' >");
		   .append("' showValues='0' rotateYAxisName='0' lineThickness='3' lineColor='FF6666' labelStep='10' labelDisplay='NONE' paletteThemeColor='5D57A5' divLineColor='5D57A5' divLineAlpha='40' vDivLineAlpha='40' baseFontSize='10'>");
		  for (int i=0;i<xAxisNameList.size();i++){
			  str.append(" <set label='"+xAxisNameList.get(i)+"'");
			  if (xAxisValueList.size() > i) {
				  str.append(" value= '"+xAxisValueList.get(i)+"' />");
			 }else{
			  str.append(" value= '' />");
			 }
		  }
		 
		   str.append("<styles><definition><style type='font' name='captionFont' size='18' /><style type='font' name='subcaptionFont' size='13' /><style type='font' name='realTimeValueFont' size='10' /><style type='font' name='dataValueFont' size='9' /></definition><application><apply toObject='Caption' styles='captionFont' /><apply toObject='Realtimevalue' styles='captionFont' /><apply toObject='subcaption' styles='subcaptionFont' /><apply toObject='Realtimevalue' styles='realTimeValueFont' /><apply toObject='Datavalues' styles='dataValueFont' /></application></styles></chart>");
		   return str.toString();
	}
	
	//3d锟斤拷维锟斤拷锟斤拷
	public String getMultiMs3DLineXmlData(){
		StringBuilder str = new StringBuilder();
		str.append("<chart caption='")
		   .append(this.caption)
		   .append("' xAxisName='")
		   .append(this.xAxisName)
		   .append("' yAxisName='")
		   .append(this.yAxisName)
		   .append("' showValues='0' labelStep='10' showLabels='1' bgColor='E9E9E9' outCnvBaseFontColor='666666' plotFillAlpha='70' numVDivLines='10' showAlternateVGridColor='1' AlternateVGridColor='e1f5ff' divLineColor='e1f5ff' vdivLineColor='e1f5ff' baseFontColor='666666' canvasBorderThickness='1' showPlotBorder='0' plotBorderThickness='0' startAngX='7' endAngX='7' startAngY='-18' endAngY='-18' zgapplot='20' zdepth='60' exeTime='2'>");
		str.append("<categories>");
		for (String xName : xAxisNameList) {
			str.append("<category label='"+xName+"' />");
		}
		str.append("</categories>"); 
		for (Map.Entry<Object, String> maps : this.manyDimensionList.entrySet()) {
			str.append("<dataset seriesName='").append(maps.getKey()).append("'")
			   .append(" color='").append(colorMap.get(maps.getKey())).append("'")
			   .append(" plotBorderColor='").append(colorMap.get(maps.getKey())).append("'")
			   .append(" renderAs='Area' ")
			   .append(">");
			String[] data = maps.getValue().toString().split(",");
			if (data.length >0 ){
				for (int i=0;i<data.length;i++){
					str.append("<set value='").append(data[i]).append("' />");
				}
			}
			str.append("</dataset>");
		}
		str.append("<styles><definition><style name='captionFont' type='font' size='12'/></definition><application><apply toObject='caption' styles='captionfont' /></application></styles>");
		str.append("</chart>");
		return str.toString();
	}
	
	//拼装锟斤拷维锟斤拷锟竭碉拷xml锟斤拷萁涌锟�lineThickness锟斤拷锟皆匡拷锟斤拷锟斤拷锟竭的粗讹拷 ,rotateYAxisName 为1Y锟结垂直锟斤拷示锟斤拷锟斤拷 锟斤拷labelDisplay='NONE' X锟斤拷锟斤拷锟秸故�X锟斤拷锟斤拷斜45锟斤拷展示labelDisplay='Rotate' slantLabels='1'
	public String getMultiMsLineXmlData(){
		StringBuilder str = new StringBuilder();
		str.append("<chart caption='")
		   .append(this.caption)
		   .append("' xAxisName='")
		   .append(this.xAxisName)
		   .append("' yAxisName='")
		   .append(this.yAxisName)
		   //.append("' showValues='0' rotateYAxisName='0' formatNumberScale='0' anchorRadius='2' divLineAlpha='20' divLineColor='CC3300' divLineIsDashed='1' showAlternateHGridColor='1' alternateHGridColor='CC3300' shadowAlpha='40' labelStep='10' numvdivlines='5' chartRightMargin='35' bgColor='FFFFFF,CC3300' bgAngle='270' bgAlpha='10,10' alternateHGridAlpha='5' unescapeLinks='0' >");
		   .append("' showValues='0' labelStep='10' rotateYAxisName='1' labelDisplay='NONE' paletteThemeColor='5D57A5' divLineColor='5D57A5' divLineAlpha='40' vDivLineAlpha='40' >" );
		str.append("<categories>");
		for (String xName : xAxisNameList) {
			str.append("<category label='"+xName+"' />");
		}
		str.append("</categories>"); 
		for (Map.Entry<Object, String> maps : this.manyDimensionList.entrySet()) {
			str.append("<dataset lineThickness='3' seriesName='").append(maps.getKey()).append("'")
			   .append(" color='").append(colorMap.get(maps.getKey())).append("'")
			   .append(" anchorBorderColor='").append(colorMap.get(maps.getKey())).append("'")
			   .append(" anchorBgColor='").append(colorMap.get(maps.getKey())).append("'")
			   .append(">");
			String[] data = maps.getValue().toString().split(",");
			if (data.length >0 ){
				for (int i=0;i<data.length;i++){
					str.append("<set value='").append(data[i]).append("' />");
				}
			}
			str.append("</dataset>");
		}
		str.append("<styles><definition><style name='CaptionFont' type='font' size='12'/></definition><application><apply toObject='CAPTION' styles='CaptionFont' /><apply toObject='SUBCAPTION' styles='CaptionFont' /></application></styles>");
		str.append("</chart>");
		return str.toString();
	}
	
	//拼装 锟斤拷锟�锟斤拷维锟斤拷锟竭碉拷xml锟斤拷萁涌锟�支锟斤拷锟斤拷锟斤拷
	public String getComponentMsLineXmlData(String dstCode){
		StringBuilder str = new StringBuilder();
		str.append("<chart caption='")
		   .append(this.caption)
		   .append("' xAxisName='")
		   .append(this.xAxisName)
		   .append("' yAxisName='")
		   .append(this.yAxisName)
		  // .append("' showValues='0' rotateYAxisName='0' formatNumberScale='0' anchorRadius='2' divLineAlpha='20' divLineColor='CC3300' divLineIsDashed='1' showAlternateHGridColor='1' alternateHGridColor='CC3300' shadowAlpha='40' labelStep='10' numvdivlines='5' chartRightMargin='35' bgColor='FFFFFF,CC3300' bgAngle='270' bgAlpha='10,10' alternateHGridAlpha='5' unescapeLinks='0' >");
		    .append("' showValues='0' labelStep='10' rotateYAxisName='1' labelDisplay='NONE' paletteThemeColor='5D57A5' divLineColor='5D57A5' divLineAlpha='40' vDivLineAlpha='40' >" );
		str.append("<categories>");
		for (String xName : xAxisNameList) {
			str.append("<category label='"+xName+"' />");
		}
		str.append("</categories>");  
		for (Map.Entry<Object, String> maps : this.manyDimensionList.entrySet()) {
			str.append("<dataset lineThickness='3' seriesName='").append(maps.getKey()).append("'")
			   .append(" color='").append(colorMap.get(maps.getKey())).append("'")
			   .append(" anchorBorderColor='").append(colorMap.get(maps.getKey())).append("'")
			   .append(" anchorBgColor='").append(colorMap.get(maps.getKey())).append("'")
			   .append(">");
			String[] data = maps.getValue().toString().split(",");
			if (data.length >0 ){
				for (int i=0;i<data.length;i++){
					str.append("<set value='").append(data[i]).append("' link='n-../monitorView/showComponentServiceView.shtml?componentId=").append(dstCode).append("' />");
				}
			}
			str.append("</dataset>");
		}
		str.append("<styles><definition><style name='CaptionFont' type='font' size='12'/></definition><application><apply toObject='CAPTION' styles='CaptionFont' /><apply toObject='SUBCAPTION' styles='CaptionFont' /></application></styles>");
		str.append("</chart>");
		return str.toString();
	}
	
	//拼装锟角憋拷锟教碉拷xml锟斤拷萁涌锟�
	public String getAngularGaugeXmlData(){
		StringBuilder str = new StringBuilder();
		//upperLimit锟斤拷锟斤拷 锟教讹拷值锟斤拷锟睫ｏ拷majorTMNumber 锟角憋拷锟教分成的等凤拷值
		str.append(" <chart bgColor='FFFFFF' upperLimit='").append(this.angularMaxVal).append("'")
		   .append(" majorTMNumber='").append(this.majorTMNumber).append("'")
		   .append(" numberSuffix='").append(this.numberSuffix).append("'")
		   .append(" lowerLimit='0' baseFontColor='FFFFFF' majorTMColor='FFFFFF' majorTMHeight='8' minorTMNumber='5' minorTMColor='FFFFFF' minorTMHeight='3' toolTipBorderColor='FFFFFF' toolTipBgColor='333333' gaugeOuterRadius='100' gaugeStartAngle='225' gaugeEndAngle='-45' placeValuesInside='1' gaugeInnerRadius='80%' annRenderDelay='0' gaugeFillMix='' pivotRadius='10' showPivotBorder='0' pivotFillMix='{CCCCCC},{333333}' pivotFillRatio='50,50' showShadow='0' ")
		   .append(" gaugeOriginX='300' gaugeOriginY='135' >");
		str.append("<colorRange>");
		for (Map.Entry<Object, String> maps : this.manyDimensionList.entrySet()){
			str.append("<color alpha='40' code='").append(colorMap.get(maps.getKey())).append("'");
			String[] data = maps.getValue().toString().split(",");
			if (data.length >0 ){
				str.append(" minValue='").append(data[0]).append("' maxValue='").append(data[1]).append("'/>");
			}
		}
		str.append("</colorRange>");
		str.append("<dials>")
		   .append("<dial value='").append(this.angularVal).append("' borderColor='FFFFFF' bgColor='000000,CCCCCC,000000' borderAlpha='0' baseWidth='10'/>")
		   .append("</dials>")
		   .append("<annotations>")
		   .append("<annotationGroup ")
		   .append("x='300' y='135' ")
		   .append("showBelow='1'><annotation type='circle' x='0' y='0' radius='130' fillColor='CCCCCC,111111'  fillPattern='linear' fillAlpha='100,100'  fillRatio='50,50' fillAngle='-45'/><annotation type='circle' x='0' y='0' radius='120'  fillColor='111111,cccccc'  fillPattern='linear' fillAlpha='100,100'  fillRatio='50,50' fillAngle='-45'/><annotation type='circle' x='0' y='0' radius='110'  color='666666'/></annotationGroup>")
		   .append("</annotations></chart>");
		return str.toString();
	}
	/**
	 * 锟斤拷图锟斤拷锟斤拷要锟角帮拷js锟斤拷xml锟接口ｏ拷swf锟斤拷装锟斤拷锟捷诧拷使锟斤拷
	 * @param swfName
	 * @param xmlData
	 * @return
	 */
	private String getDrawChart(String swfName,String xmlData){
		StringBuffer buffer = new StringBuffer(this.getDivAndJs()+"<script type=\"text/javascript\">");
		buffer.append("var ").append(this.myChartName)
		      .append(" = new FusionCharts(\""+this.getSwfPath(swfName)
		    		  +"\",\"myChart"+UUID.randomUUID().toString()
		    		  +"\",\""+this.width+"\",\""+this.height+"\",\"0\",\"1\");")
		      .append(this.myChartName)
		      .append(".setDataXML(\""+xmlData+"\");")
		      .append(this.myChartName).append(".render(\""+this.divId+"\");</script>");
		return buffer.toString();
	}
	
	private String getSwfPath(String swfName){
		return this.swfPath + swfName;
	}
	
	private String getDivAndJs(){
		return "<script type=\"text/javascript\" src=\""
				+ this.jsPath
				+ "\"></script>"
				+ "<div id=\""
				+ this.divId +"\" align = \"center\"></div>";
	}
	
	private void verifyParams(){
		if (this.width == null || this.width <=0){
			this.width = WIDTH;
		}
		if (this.height == null || this.height <=0){
			this.height = HEIGHT;
		}
		if (StringUtils.isEmpty(xAxisName)) {
			this.xAxisName = XAXISNAME;
		}
		if (StringUtils.isEmpty(yAxisName)) {
			this.yAxisName = YAXISNAME;
		}
		if (StringUtils.isEmpty(jsPath)) {
			this.jsPath = JSPATH;
		}
		if (StringUtils.isEmpty(swfPath)) {
			this.swfPath = SWFPATH;
		}
		if (StringUtils.isEmpty(caption)) {
			this.caption = CAPTION;
		}
		if (this.oneDimensionList.isEmpty()) {
			this.oneDimensionList = new ArrayList<Map<Object,Object>>();
		}
		if (this.manyDimensionMap.isEmpty()) {
			this.manyDimensionMap = new HashMap<Object,Map<Object,Object>>();
		}
		if (this.xAxisNameList.isEmpty()) {
			this.xAxisNameList = new ArrayList<String>();
		}
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getSubcaption() {
		return subcaption;
	}

	public void setSubcaption(String subcaption) {
		this.subcaption = subcaption;
	}

	public String getXAxisName() {
		return xAxisName;
	}

	public void setXAxisName(String axisName) {
		xAxisName = axisName;
	}

	public String getYAxisName() {
		return yAxisName;
	}

	public void setYAxisName(String axisName) {
		yAxisName = axisName;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public List<Map<Object, Object>> getOneDimensionList() {
		return oneDimensionList;
	}

	public void setOneDimensionList(List<Map<Object, Object>> oneDimensionList) {
		this.oneDimensionList = oneDimensionList;
	}

	public Map<Object, Map<Object, Object>> getManyDimensionMap() {
		return manyDimensionMap;
	}

	public void setManyDimensionMap(
			Map<Object, Map<Object, Object>> manyDimensionMap) {
		this.manyDimensionMap = manyDimensionMap;
	}

	public List<String> getXAxisNameList() {
		return xAxisNameList;
	}

	public void setXAxisNameList(List<String> axisNameList) {
		xAxisNameList = axisNameList;
	}

	public String getJsPath() {
		return jsPath;
	}

	public void setJsPath(String jsPath) {
		this.jsPath = jsPath;
	}

	public String getSwfPath() {
		return swfPath;
	}

	public void setSwfPath(String swfPath) {
		this.swfPath = swfPath;
	}

	public List<String> getXAxisValueList() {
		return xAxisValueList;
	}

	public void setXAxisValueList(List<String> axisValueList) {
		xAxisValueList = axisValueList;
	}

	public Map<String, String> getColorMap() {
		return colorMap;
	}

	public void setColorMap(Map<String, String> colorMap) {
		this.colorMap = colorMap;
	}

	public LinkedHashMap<Object, String> getManyDimensionList() {
		return manyDimensionList;
	}

	public void setManyDimensionList(LinkedHashMap<Object, String> manyDimensionList) {
		this.manyDimensionList = manyDimensionList;
	}

	public String getAngularVal() {
		return angularVal;
	}

	public void setAngularVal(String angularVal) {
		this.angularVal = angularVal;
	}

	public String getAngularMaxVal() {
		return angularMaxVal;
	}

	public void setAngularMaxVal(String angularMaxVal) {
		this.angularMaxVal = angularMaxVal;
	}

	public String getNumberSuffix() {
		return numberSuffix;
	}

	public void setNumberSuffix(String numberSuffix) {
		this.numberSuffix = numberSuffix;
	}
	
	public String getMajorTMNumber() {
		return majorTMNumber;
	}

	public void setMajorTMNumber(String majorTMNumber) {
		this.majorTMNumber = majorTMNumber;
	}

}
