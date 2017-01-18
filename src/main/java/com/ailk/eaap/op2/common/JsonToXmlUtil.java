package com.ailk.eaap.op2.common;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONFunction;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import net.sf.json.xml.XMLSerializer;
import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;
import nu.xom.Serializer;
import nu.xom.Text;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.asiainfo.foundation.log.Logger;

public class JsonToXmlUtil {
	  private static final Logger LOG = Logger.getLog(JsonToXmlUtil.class);
	  private static final String[] EMPTY_ARRAY = new String[0];
	  private static final String JSON_PREFIX = "json_";
	  private static final Log log = LogFactory.getLog(XMLSerializer.class);
	  private String arrayName;
	  private String elementName;
	  private String[] expandableProperties;
	  private boolean forceTopLevelObject;
	  private boolean namespaceLenient;
	  private Map namespacesPerElement = new TreeMap();
	  private String objectName;
	  private boolean removeNamespacePrefixFromElements;
	  private String rootName;
	  private Map rootNamespace = new TreeMap();
	  private boolean skipNamespaces;
	  private boolean skipWhitespace;
	  private boolean trimSpaces;
	  private boolean typeHintsCompatibility;
	  private boolean typeHintsEnabled;
	  public JsonToXmlUtil()
	  {
	    setObjectName("o");
	    setArrayName("a");
	    setElementName("e");
	    setTypeHintsEnabled(false);
	    setTypeHintsCompatibility(true);
	    setNamespaceLenient(false);
	    setSkipNamespaces(false);
	    setRemoveNamespacePrefixFromElements(false);
	    setTrimSpaces(false);
	    setExpandableProperties(EMPTY_ARRAY);
	    setSkipNamespaces(false);
	  }
	  
	  public void addNamespace(String prefix, String uri)
	  {
	    addNamespace(prefix, uri, null);
	  }

	  public void addNamespace(String prefix, String uri, String elementName)
	  {
	    if (StringUtils.isBlank(uri)) {
	      return;
	    }
	    if (prefix == null) {
	      prefix = "";
	    }
	    if (StringUtils.isBlank(elementName)) {
	      this.rootNamespace.put(prefix.trim(), uri.trim());
	    } else {
	      Map nameSpaces = (Map)this.namespacesPerElement.get(elementName);
	      if (nameSpaces == null) {
	        nameSpaces = new TreeMap();
	        this.namespacesPerElement.put(elementName, nameSpaces);
	      }
	      nameSpaces.put(prefix, uri);
	    }
	  }

	  public void clearNamespaces()
	  {
	    this.rootNamespace.clear();
	    this.namespacesPerElement.clear();
	  }

	  public void clearNamespaces(String elementName)
	  {
	    if (StringUtils.isBlank(elementName))
	      this.rootNamespace.clear();
	    else
	      this.namespacesPerElement.remove(elementName);
	  }

	  public String getArrayName()
	  {
	    return this.arrayName;
	  }

	  public String getElementName()
	  {
	    return this.elementName;
	  }

	  public String[] getExpandableProperties()
	  {
	    return this.expandableProperties;
	  }

	  public String getObjectName()
	  {
	    return this.objectName;
	  }

	  public String getRootName()
	  {
	    return this.rootName;
	  }

	  public boolean isForceTopLevelObject() {
	    return this.forceTopLevelObject;
	  }

	  public boolean isNamespaceLenient()
	  {
	    return this.namespaceLenient;
	  }

	  public boolean isRemoveNamespacePrefixFromElements()
	  {
	    return this.removeNamespacePrefixFromElements;
	  }

	  public boolean isSkipNamespaces()
	  {
	    return this.skipNamespaces;
	  }

	  public boolean isSkipWhitespace()
	  {
	    return this.skipWhitespace;
	  }

	  public boolean isTrimSpaces()
	  {
	    return this.trimSpaces;
	  }

	  public boolean isTypeHintsCompatibility()
	  {
	    return this.typeHintsCompatibility;
	  }

	  public boolean isTypeHintsEnabled()
	  {
	    return this.typeHintsEnabled;
	  }

	  public JSON read(String xml)
	  {
	    JSON json = null;
	    try {
	      Document doc = new Builder().build(new StringReader(xml));
	      Element root = doc.getRootElement();
	      if (isNullObject(root)) {
	        return JSONNull.getInstance();
	      }
	      String defaultType = getType(root, "string");
	      if (isArray(root, true)) {
	        json = processArrayElement(root, defaultType);
	        if (this.forceTopLevelObject) {
	          String key = removeNamespacePrefix(root.getQualifiedName());
	          json = new JSONObject().element(key, json);
	        }
	      } else {
	        json = processObjectElement(root, defaultType);
	        if (this.forceTopLevelObject) {
	          String key = removeNamespacePrefix(root.getQualifiedName());
	          json = new JSONObject().element(key, json);
	        }
	      }
	    } catch (JSONException jsone) {
	      throw jsone;
	    } catch (Exception e) {
	      throw new JSONException(e);
	    }
	    return json;
	  }

	  public JSON readFromFile(File file)
	  {
		  FileInputStream in = null;
	    if (file == null) {
	      throw new JSONException("File is null");
	    }
	    if (!file.canRead()) {
	      throw new JSONException("Can't read input file");
	    }
	    if (file.isDirectory())
	      throw new JSONException("File is a directory");
	    try
	    {
	      in  = new FileInputStream(file);
	      return readFromStream(in); 
	     } catch (IOException ioe) {
	    	 throw new JSONException(ioe);
	    }finally{
	    	if(in!=null){
	    		try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					LOG.error("something bad happend",e);
				}
	    	}
	    }
	    
	  }

	  public JSON readFromFile(String path)
	  {
	    return readFromStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
	  }

	  public JSON readFromStream(InputStream stream)
	  {
	    try
	    {
	      StringBuffer xml = new StringBuffer();
	      BufferedReader in = new BufferedReader(new InputStreamReader(stream));
	      String line = null;
	      while ((line = in.readLine()) != null) {
	        xml.append(line);
	      }
	      return read(xml.toString()); 
	     } catch (IOException ioe) {
	    	 throw new JSONException(ioe);
	    }
	    
	  }

	  public void removeNamespace(String prefix)
	  {
	    removeNamespace(prefix, null);
	  }

	  public void removeNamespace(String prefix, String elementName)
	  {
	    if (prefix == null) {
	      prefix = "";
	    }
	    if (StringUtils.isBlank(elementName)) {
	      this.rootNamespace.remove(prefix.trim());
	    } else {
	      Map nameSpaces = (Map)this.namespacesPerElement.get(elementName);
	      nameSpaces.remove(prefix);
	    }
	  }

	  public void setArrayName(String arrayName)
	  {
	    this.arrayName = (StringUtils.isBlank(arrayName) ? "a" : arrayName);
	  }

	  public void setElementName(String elementName)
	  {
	    this.elementName = (StringUtils.isBlank(elementName) ? "e" : elementName);
	  }

	  public void setExpandableProperties(String[] expandableProperties)
	  {
	    this.expandableProperties = (expandableProperties == null ? EMPTY_ARRAY : expandableProperties);
	  }

	  public void setForceTopLevelObject(boolean forceTopLevelObject) {
	    this.forceTopLevelObject = forceTopLevelObject;
	  }

	  public void setNamespace(String prefix, String uri)
	  {
	    setNamespace(prefix, uri, null);
	  }

	  public void setNamespace(String prefix, String uri, String elementName)
	  {
	    if (StringUtils.isBlank(uri)) {
	      return;
	    }
	    if (prefix == null) {
	      prefix = "";
	    }
	    if (StringUtils.isBlank(elementName)) {
	      this.rootNamespace.clear();
	      this.rootNamespace.put(prefix.trim(), uri.trim());
	    } else {
	      Map nameSpaces = (Map)this.namespacesPerElement.get(elementName);
	      if (nameSpaces == null) {
	        nameSpaces = new TreeMap();
	        this.namespacesPerElement.put(elementName, nameSpaces);
	      }
	      nameSpaces.clear();
	      nameSpaces.put(prefix, uri);
	    }
	  }

	  public void setNamespaceLenient(boolean namespaceLenient)
	  {
	    this.namespaceLenient = namespaceLenient;
	  }

	  public void setObjectName(String objectName)
	  {
	    this.objectName = (StringUtils.isBlank(objectName) ? "o" : objectName);
	  }

	  public void setRemoveNamespacePrefixFromElements(boolean removeNamespacePrefixFromElements)
	  {
	    this.removeNamespacePrefixFromElements = removeNamespacePrefixFromElements;
	  }

	  public void setRootName(String rootName)
	  {
	    this.rootName = (StringUtils.isBlank(rootName) ? null : rootName);
	  }

	  public void setSkipNamespaces(boolean skipNamespaces)
	  {
	    this.skipNamespaces = skipNamespaces;
	  }

	  public void setSkipWhitespace(boolean skipWhitespace)
	  {
	    this.skipWhitespace = skipWhitespace;
	  }

	  public void setTrimSpaces(boolean trimSpaces)
	  {
	    this.trimSpaces = trimSpaces;
	  }

	  public void setTypeHintsCompatibility(boolean typeHintsCompatibility)
	  {
	    this.typeHintsCompatibility = typeHintsCompatibility;
	  }

	  public void setTypeHintsEnabled(boolean typeHintsEnabled)
	  {
	    this.typeHintsEnabled = typeHintsEnabled;
	  }

	  public String write(JSON json)
	  {
	    return write(json, null);
	  }

	  public String write(JSON json, String encoding)
	  {
	    if (JSONNull.getInstance().equals(json))
	    {
	      Element root = null;
	      root = newElement(getRootName() == null ? getObjectName() : getRootName());
	      root.addAttribute(new Attribute(addJsonPrefix("null"), "true"));
	      Document doc = new Document(root);
	      return writeDocument(doc, encoding);
	    }if ((json instanceof JSONArray)) {
	      JSONArray jsonArray = (JSONArray)json;
	      Element root = processJSONArray(jsonArray, newElement(getRootName() == null ? getArrayName() : getRootName()), this.expandableProperties);

	      Document doc = new Document(root);
	      return writeDocument(doc, encoding);
	    }
	    JSONObject jsonObject = (JSONObject)json;
	    
	    Element root = null;
	    if (jsonObject.isNullObject()) {
	      root = newElement(getObjectName());
	      root.addAttribute(new Attribute(addJsonPrefix("null"), "true"));
	    } else {
	      root = newElement("ContractRoot");
	      jsonObject = jsonObject.getJSONObject("ContractRoot");
	      root = processJSONObject(jsonObject, root, this.expandableProperties, true);
	    }

	    Document doc = new Document(root);
	    return writeDocument(doc, encoding);
	  }

	  private String addJsonPrefix(String str)
	  {
	    if (!isTypeHintsCompatibility()) {
	      return "json_" + str;
	    }
	    return str;
	  }

	  private void addNameSpaceToElement(Element element) {
	    String elementName = null;
	    if ((element instanceof CustomElement))
	      elementName = ((CustomElement)element).getQName();
	    else {
	      elementName = element.getQualifiedName();
	    }
	    Map nameSpaces = (Map)this.namespacesPerElement.get(elementName);
	    if ((nameSpaces != null) && (!nameSpaces.isEmpty())) {
	      setNamespaceLenient(true);
	      Iterator entries = nameSpaces.entrySet().iterator();
	      while (entries.hasNext()) {
	        Map.Entry entry = (Map.Entry)entries.next();
	        String prefix = (String)entry.getKey();
	        String uri = (String)entry.getValue();
	        if (StringUtils.isBlank(prefix))
	          element.setNamespaceURI(uri);
	        else
	          element.addNamespaceDeclaration(prefix, uri);
	      }
	    }
	  }

	  private boolean checkChildElements(Element element, boolean isTopLevel)
	  {
	    int childCount = element.getChildCount();
	    Elements elements = element.getChildElements();
	    int elementCount = elements.size();

	    if ((childCount == 1) && ((element.getChild(0) instanceof Text))) {
	      return isTopLevel;
	    }

	    if (childCount == elementCount) {
	      if (elementCount == 0) {
	        return true;
	      }
	      if (elementCount == 1)
	      {
	        return (this.skipWhitespace) || ((element.getChild(0) instanceof Text));
	      }

	    }

	    if (childCount > elementCount) {
	      for (int i = 0; i < childCount; i++) {
	        Node node = element.getChild(i);
	        if ((node instanceof Text)) {
	          Text text = (Text)node;
	          if ((StringUtils.isNotBlank(StringUtils.strip(text.getValue()))) && (!this.skipWhitespace))
	          {
	            return false;
	          }
	        }
	      }
	    }

	    String childName = elements.get(0).getQualifiedName();

	    for (int i = 1; i < elementCount; i++) {
	      if (childName.compareTo(elements.get(i).getQualifiedName()) != 0)
	      {
	        return false;
	      }
	    }

	    return true;
	  }

	  private String getClass(Element element) {
	    Attribute attribute = element.getAttribute(addJsonPrefix("class"));
	    String clazz = null;
	    if (attribute != null) {
	      String clazzText = attribute.getValue().trim();

	      if ("object".compareToIgnoreCase(clazzText) == 0)
	        clazz = "object";
	      else if ("array".compareToIgnoreCase(clazzText) == 0) {
	        clazz = "array";
	      }
	    }
	    return clazz;
	  }

	  private String getType(Element element) {
	    return getType(element, null);
	  }

	  private String getType(Element element, String defaultType) {
	    Attribute attribute = element.getAttribute(addJsonPrefix("type"));
	    String type = null;
	    if (attribute != null) {
	      String typeText = attribute.getValue().trim();

	      if ("boolean".compareToIgnoreCase(typeText) == 0)
	        type = "boolean";
	      else if ("number".compareToIgnoreCase(typeText) == 0)
	        type = "number";
	      else if ("integer".compareToIgnoreCase(typeText) == 0)
	        type = "integer";
	      else if ("float".compareToIgnoreCase(typeText) == 0)
	        type = "float";
	      else if ("object".compareToIgnoreCase(typeText) == 0)
	        type = "object";
	      else if ("array".compareToIgnoreCase(typeText) == 0)
	        type = "array";
	      else if ("string".compareToIgnoreCase(typeText) == 0)
	        type = "string";
	      else if ("function".compareToIgnoreCase(typeText) == 0) {
	        type = "function";
	      }
	    }
	    else if (defaultType != null) {
//	      log.info("Using default type " + defaultType);
	      type = defaultType;
	    }

	    return type;
	  }

	  private boolean hasNamespaces(Element element) {
	    int namespaces = 0;
	    for (int i = 0; i < element.getNamespaceDeclarationCount(); i++) {
	      String prefix = element.getNamespacePrefix(i);
	      String uri = element.getNamespaceURI(prefix);
	      if (StringUtils.isBlank(uri)) {
	        continue;
	      }
	      namespaces++;
	    }
	    return namespaces > 0;
	  }

	  private boolean isArray(Element element, boolean isTopLevel) {
	    boolean isArray = false;
	    String clazz = getClass(element);
	    if ((clazz != null) && (clazz.equals("array")))
	      isArray = true;
	    else if (element.getAttributeCount() == 0)
	      isArray = checkChildElements(element, isTopLevel);
	    else if ((element.getAttributeCount() == 1) && ((element.getAttribute(addJsonPrefix("class")) != null) || (element.getAttribute(addJsonPrefix("type")) != null)))
	    {
	      isArray = checkChildElements(element, isTopLevel);
	    } else if ((element.getAttributeCount() == 2) && (element.getAttribute(addJsonPrefix("class")) != null) && (element.getAttribute(addJsonPrefix("type")) != null))
	    {
	      isArray = checkChildElements(element, isTopLevel);
	    }

	    if (isArray)
	    {
	      for (int j = 0; j < element.getNamespaceDeclarationCount(); j++) {
	        String prefix = element.getNamespacePrefix(j);
	        String uri = element.getNamespaceURI(prefix);
	        if (!StringUtils.isBlank(uri)) {
	          return false;
	        }
	      }
	    }

	    return isArray;
	  }

	  private boolean isFunction(Element element) {
	    int attrCount = element.getAttributeCount();
	    if (attrCount > 0) {
	      Attribute typeAttr = element.getAttribute(addJsonPrefix("type"));
	      Attribute paramsAttr = element.getAttribute(addJsonPrefix("params"));
	      if ((attrCount == 1) && (paramsAttr != null)) {
	        return true;
	      }
	      if ((attrCount == 2) && (paramsAttr != null) && (typeAttr != null) && ((typeAttr.getValue().compareToIgnoreCase("string") == 0) || (typeAttr.getValue().compareToIgnoreCase("function") == 0)))
	      {
	        return true;
	      }
	    }
	    return false;
	  }

	  private boolean isNullObject(Element element) {
	    if (element.getChildCount() == 0) {
	      if (element.getAttributeCount() == 0)
	        return true;
	      if (element.getAttribute(addJsonPrefix("null")) != null)
	        return true;
	      if ((element.getAttributeCount() == 1) && ((element.getAttribute(addJsonPrefix("class")) != null) || (element.getAttribute(addJsonPrefix("type")) != null)))
	      {
	        return true;
	      }if ((element.getAttributeCount() == 2) && (element.getAttribute(addJsonPrefix("class")) != null) && (element.getAttribute(addJsonPrefix("type")) != null))
	      {
	        return true;
	      }
	    }

	    return (this.skipWhitespace) && (element.getChildCount() == 1) && ((element.getChild(0) instanceof Text));
	  }

	  private boolean isObject(Element element, boolean isTopLevel)
	  {
	    boolean isObject = false;
	    if ((!isArray(element, isTopLevel)) && (!isFunction(element))) {
	      if (hasNamespaces(element)) {
	        return true;
	      }

	      int attributeCount = element.getAttributeCount();
	      if (attributeCount > 0) {
	        int attrs = element.getAttribute(addJsonPrefix("null")) == null ? 0 : 1;
	        attrs += (element.getAttribute(addJsonPrefix("class")) == null ? 0 : 1);
	        attrs += (element.getAttribute(addJsonPrefix("type")) == null ? 0 : 1);
	        switch (attributeCount) {
	        case 1:
	          if (attrs != 0) break;
	          return true;
	        case 2:
	          if (attrs >= 2) break;
	          return true;
	        case 3:
	          if (attrs >= 3) break;
	          return true;
	        default:
	          return true;
	        }
	      }

	      int childCount = element.getChildCount();
	      if ((childCount == 1) && ((element.getChild(0) instanceof Text))) {
	        return isTopLevel;
	      }

	      isObject = true;
	    }
	    return isObject;
	  }

	  private Element newElement(String name) {
	    if (name.indexOf(':') != -1) {
	      this.namespaceLenient = true;
	    }
	    return this.namespaceLenient ? new CustomElement(name) : new Element(name);
	  }

	  private JSON processArrayElement(Element element, String defaultType) {
	    JSONArray jsonArray = new JSONArray();

	    int childCount = element.getChildCount();
	    for (int i = 0; i < childCount; i++) {
	      Node child = element.getChild(i);
	      if ((child instanceof Text)) {
	        Text text = (Text)child;
	        if (StringUtils.isNotBlank(StringUtils.strip(text.getValue())))
	          jsonArray.element(text.getValue());
	      }
	      else if ((child instanceof Element)) {
	        setValue(jsonArray, (Element)child, defaultType);
	      }
	    }
	    return jsonArray;
	  }

	  private Object processElement(Element element, String type) {
	    if (isNullObject(element))
	      return JSONNull.getInstance();
	    if (isArray(element, false))
	      return processArrayElement(element, type);
	    if (isObject(element, false)) {
	      return processObjectElement(element, type);
	    }
	    return trimSpaceFromValue(element.getValue());
	  }

	  private Element processJSONArray(JSONArray array, Element root, String[] expandableProperties)
	  {
	    int l = array.size();
	    for (int i = 0; i < l; i++) {
	      Object value = array.get(i);
	      Element element = processJSONValue(value, root, null, expandableProperties);
	      root.appendChild(element);
	    }
	    return root;
	  }

	  private Element processJSONObject(JSONObject jsonObject, Element root, String[] expandableProperties, boolean isRoot)
	  {
	    if (jsonObject.isNullObject()) {
	      root.addAttribute(new Attribute(addJsonPrefix("null"), "true"));
	      return root;
	    }if (jsonObject.isEmpty()) {
	      return root;
	    }

	    if ((isRoot) && 
	      (!this.rootNamespace.isEmpty())) {
	      setNamespaceLenient(true);
	      Iterator entries = this.rootNamespace.entrySet().iterator();
	      while (entries.hasNext()) {
	        Map.Entry entry = (Map.Entry)entries.next();
	        String prefix = (String)entry.getKey();
	        String uri = (String)entry.getValue();
	        if (StringUtils.isBlank(prefix))
	          root.setNamespaceURI(uri);
	        else {
	          root.addNamespaceDeclaration(prefix, uri);
	        }
	      }

	    }

	    addNameSpaceToElement(root);

	    Object[] names = jsonObject.names().toArray();

	    Arrays.sort(names);
	    Element element = null;
	    for (int i = 0; i < names.length; i++) {
	      String name = (String)names[i];
	      Object value = jsonObject.get(name);
	      if (name.startsWith("@xmlns")) {
	        setNamespaceLenient(true);
	        int colon = name.indexOf(':');
	        if (colon == -1)
	        {
	          if (StringUtils.isBlank(root.getNamespaceURI()))
	            root.setNamespaceURI(String.valueOf(value));
	        }
	        else {
	          String prefix = name.substring(colon + 1);
	          if (StringUtils.isBlank(root.getNamespaceURI(prefix)))
	            root.addNamespaceDeclaration(prefix, String.valueOf(value));
	        }
	      }
	      else if (name.startsWith("@")) {
	        root.addAttribute(new Attribute(name.substring(1), String.valueOf(value)));
	      } else if (name.equals("#text")) {
	        if ((value instanceof JSONArray))
	          root.appendChild(((JSONArray)value).join("", true));
	        else
	          root.appendChild(String.valueOf(value));
	      }
	      else if (((value instanceof JSONArray)) && ((((JSONArray)value).isExpandElements()) || (ArrayUtils.contains(expandableProperties, name))))
	      {
	        JSONArray array = (JSONArray)value;
	        int l = array.size();
	        for (int j = 0; j < l; j++) {
	          Object item = array.get(j);
	          element = newElement(name);
	          if ((item instanceof JSONObject)) {
	            element = processJSONValue((JSONObject)item, root, element, expandableProperties);
	          }
	          else if ((item instanceof JSONArray))
	            element = processJSONValue((JSONArray)item, root, element, expandableProperties);
	          else {
	            element = processJSONValue(item, root, element, expandableProperties);
	          }
	          addNameSpaceToElement(element);
	          root.appendChild(element);
	        }
	      } else {
	        element = newElement(name);
	        element = processJSONValue(value, root, element, expandableProperties);
	        addNameSpaceToElement(element);
	        root.appendChild(element);
	      }
	    }
	    return root;
	  }

	  private Element processJSONValue(Object value, Element root, Element target, String[] expandableProperties)
	  {
	    if (target == null) {
	      target = newElement(getElementName());
	    }
	    if (JSONUtils.isBoolean(value)) {
	      if (isTypeHintsEnabled()) {
	        target.addAttribute(new Attribute(addJsonPrefix("type"), "boolean"));
	      }
	      target.appendChild(value.toString());
	    } else if (JSONUtils.isNumber(value)) {
	      if (isTypeHintsEnabled()) {
	        target.addAttribute(new Attribute(addJsonPrefix("type"), "number"));
	      }
	      target.appendChild(value.toString());
	    } else if (JSONUtils.isFunction(value)) {
	      if ((value instanceof String)) {
	        value = JSONFunction.parse((String)value);
	      }
	      JSONFunction func = (JSONFunction)value;
	      if (isTypeHintsEnabled()) {
	        target.addAttribute(new Attribute(addJsonPrefix("type"), "function"));
	      }
	      String params = ArrayUtils.toString(func.getParams());
	      params = params.substring(1);
	      params = params.substring(0, params.length() - 1);
	      target.addAttribute(new Attribute(addJsonPrefix("params"), params));
	      target.appendChild(new Text("<![CDATA[" + func.getText() + "]]>"));
	    } else if (JSONUtils.isString(value)) {
	      if (isTypeHintsEnabled()) {
	        target.addAttribute(new Attribute(addJsonPrefix("type"), "string"));
	      }
	      target.appendChild(value.toString());
	    } else if ((value instanceof JSONArray)) {
	      if (isTypeHintsEnabled()) {
	        target.addAttribute(new Attribute(addJsonPrefix("class"), "array"));
	      }
	      target = processJSONArray((JSONArray)value, target, expandableProperties);
	    } else if ((value instanceof JSONObject)) {
	      if (isTypeHintsEnabled()) {
	        target.addAttribute(new Attribute(addJsonPrefix("class"), "object"));
	      }
	      target = processJSONObject((JSONObject)value, target, expandableProperties, false);
	    } else if (JSONUtils.isNull(value)) {
	      if (isTypeHintsEnabled()) {
	        target.addAttribute(new Attribute(addJsonPrefix("class"), "object"));
	      }
	      target.addAttribute(new Attribute(addJsonPrefix("null"), "true"));
	    }
	    return target;
	  }

	  private JSON processObjectElement(Element element, String defaultType) {
	    if (isNullObject(element)) {
	      return JSONNull.getInstance();
	    }
	    JSONObject jsonObject = new JSONObject();

	    if (!this.skipNamespaces) {
	      for (int j = 0; j < element.getNamespaceDeclarationCount(); j++) {
	        String prefix = element.getNamespacePrefix(j);
	        String uri = element.getNamespaceURI(prefix);
	        if (StringUtils.isBlank(uri)) {
	          continue;
	        }
	        if (!StringUtils.isBlank(prefix)) {
	          prefix = ":" + prefix;
	        }
	        setOrAccumulate(jsonObject, "@xmlns" + prefix, trimSpaceFromValue(uri));
	      }

	    }

	    int attrCount = element.getAttributeCount();
	    for (int i = 0; i < attrCount; i++) {
	      Attribute attr = element.getAttribute(i);
	      String attrname = attr.getQualifiedName();
	      if ((isTypeHintsEnabled()) && ((addJsonPrefix("class").compareToIgnoreCase(attrname) == 0) || (addJsonPrefix("type").compareToIgnoreCase(attrname) == 0)))
	      {
	        continue;
	      }

	      String attrvalue = attr.getValue();
	      setOrAccumulate(jsonObject, "@" + removeNamespacePrefix(attrname), trimSpaceFromValue(attrvalue));
	    }

	    int childCount = element.getChildCount();
	    for (int i = 0; i < childCount; i++) {
	      Node child = element.getChild(i);
	      if ((child instanceof Text)) {
	        Text text = (Text)child;
	        if (StringUtils.isNotBlank(StringUtils.strip(text.getValue())))
	          setOrAccumulate(jsonObject, "#text", trimSpaceFromValue(text.getValue()));
	      }
	      else if ((child instanceof Element)) {
	        setValue(jsonObject, (Element)child, defaultType);
	      }
	    }

	    return jsonObject;
	  }

	  private String removeNamespacePrefix(String name) {
	    if (isRemoveNamespacePrefixFromElements()) {
	      int colon = name.indexOf(':');
	      return colon != -1 ? name.substring(colon + 1) : name;
	    }
	    return name;
	  }

	  private void setOrAccumulate(JSONObject jsonObject, String key, Object value) {
	    if (jsonObject.has(key)) {
	      jsonObject.accumulate(key, value);
	      Object val = jsonObject.get(key);
	      if ((val instanceof JSONArray))
	        ((JSONArray)val).setExpandElements(true);
	    }
	    else {
	      jsonObject.element(key, value);
	    }
	  }

	  private void setValue(JSONArray jsonArray, Element element, String defaultType) {
	    String clazz = getClass(element);
	    String type = getType(element);
	    type = type == null ? defaultType : type;

	    if ((hasNamespaces(element)) && (!this.skipNamespaces)) {
	      jsonArray.element(simplifyValue(null, processElement(element, type)));
	      return;
	    }if (element.getAttributeCount() > 0) {
	      if (isFunction(element)) {
	        Attribute paramsAttribute = element.getAttribute(addJsonPrefix("params"));
	        String[] params = null;
	        String text = element.getValue();
	        params = StringUtils.split(paramsAttribute.getValue(), ",");
	        jsonArray.element(new JSONFunction(params, text));
	        return;
	      }
	      jsonArray.element(simplifyValue(null, processElement(element, type)));
	      return;
	    }

	    boolean classProcessed = false;
	    if (clazz != null) {
	      if (clazz.compareToIgnoreCase("array") == 0) {
	        jsonArray.element(processArrayElement(element, type));
	        classProcessed = true;
	      } else if (clazz.compareToIgnoreCase("object") == 0) {
	        jsonArray.element(simplifyValue(null, processObjectElement(element, type)));
	        classProcessed = true;
	      }
	    }
	    if (!classProcessed)
	      if (type.compareToIgnoreCase("boolean") == 0) {
	        jsonArray.element(Boolean.valueOf(element.getValue()));
	      } else if (type.compareToIgnoreCase("number") == 0)
	      {
	        try {
	          jsonArray.element(Integer.valueOf(element.getValue()));
	        } catch (NumberFormatException e) {
	          jsonArray.element(Double.valueOf(element.getValue()));
	        }
	      } else if (type.compareToIgnoreCase("integer") == 0) {
	        jsonArray.element(Integer.valueOf(element.getValue()));
	      } else if (type.compareToIgnoreCase("float") == 0) {
	        jsonArray.element(Double.valueOf(element.getValue()));
	      } else if (type.compareToIgnoreCase("function") == 0) {
	        String[] params = null;
	        String text = element.getValue();
	        Attribute paramsAttribute = element.getAttribute(addJsonPrefix("params"));
	        if (paramsAttribute != null) {
	          params = StringUtils.split(paramsAttribute.getValue(), ",");
	        }
	        jsonArray.element(new JSONFunction(params, text));
	      } else if (type.compareToIgnoreCase("string") == 0)
	      {
	        Attribute paramsAttribute = element.getAttribute(addJsonPrefix("params"));
	        if (paramsAttribute != null) {
	          String[] params = null;
	          String text = element.getValue();
	          params = StringUtils.split(paramsAttribute.getValue(), ",");
	          jsonArray.element(new JSONFunction(params, text));
	        }
	        else if (isArray(element, false)) {
	          jsonArray.element(processArrayElement(element, defaultType));
	        } else if (isObject(element, false)) {
	          jsonArray.element(simplifyValue(null, processObjectElement(element, defaultType)));
	        }
	        else {
	          jsonArray.element(trimSpaceFromValue(element.getValue()));
	        }
	      }
	  }

	  private void setValue(JSONObject jsonObject, Element element, String defaultType)
	  {
	    String clazz = getClass(element);
	    String type = getType(element);
	    type = type == null ? defaultType : type;

	    String key = removeNamespacePrefix(element.getQualifiedName());
	    if ((hasNamespaces(element)) && (!this.skipNamespaces)) {
	      setOrAccumulate(jsonObject, key, simplifyValue(jsonObject, processElement(element, type)));

	      return;
	    }if ((element.getAttributeCount() > 0) && 
	      (isFunction(element))) {
	      Attribute paramsAttribute = element.getAttribute(addJsonPrefix("params"));
	      String text = element.getValue();
	      String[] params = StringUtils.split(paramsAttribute.getValue(), ",");
	      setOrAccumulate(jsonObject, key, new JSONFunction(params, text));
	      return;
	    }

	    boolean classProcessed = false;
	    if (clazz != null) {
	      if (clazz.compareToIgnoreCase("array") == 0) {
	        setOrAccumulate(jsonObject, key, processArrayElement(element, type));
	        classProcessed = true;
	      } else if (clazz.compareToIgnoreCase("object") == 0) {
	        setOrAccumulate(jsonObject, key, simplifyValue(jsonObject, processObjectElement(element, type)));

	        classProcessed = true;
	      }
	    }
	    if (!classProcessed)
	      if (type.compareToIgnoreCase("boolean") == 0) {
	        setOrAccumulate(jsonObject, key, Boolean.valueOf(element.getValue()));
	      } else if (type.compareToIgnoreCase("number") == 0)
	      {
	        try {
	          setOrAccumulate(jsonObject, key, Integer.valueOf(element.getValue()));
	        } catch (NumberFormatException e) {
	          setOrAccumulate(jsonObject, key, Double.valueOf(element.getValue()));
	        }
	      } else if (type.compareToIgnoreCase("integer") == 0) {
	        setOrAccumulate(jsonObject, key, Integer.valueOf(element.getValue()));
	      } else if (type.compareToIgnoreCase("float") == 0) {
	        setOrAccumulate(jsonObject, key, Double.valueOf(element.getValue()));
	      } else if (type.compareToIgnoreCase("function") == 0) {
	        String[] params = null;
	        String text = element.getValue();
	        Attribute paramsAttribute = element.getAttribute(addJsonPrefix("params"));
	        if (paramsAttribute != null) {
	          params = StringUtils.split(paramsAttribute.getValue(), ",");
	        }
	        setOrAccumulate(jsonObject, key, new JSONFunction(params, text));
	      } else if (type.compareToIgnoreCase("string") == 0)
	      {
	        Attribute paramsAttribute = element.getAttribute(addJsonPrefix("params"));
	        if (paramsAttribute != null) {
	          String[] params = null;
	          String text = element.getValue();
	          params = StringUtils.split(paramsAttribute.getValue(), ",");
	          setOrAccumulate(jsonObject, key, new JSONFunction(params, text));
	        }
	        else if (isArray(element, false)) {
	          setOrAccumulate(jsonObject, key, processArrayElement(element, defaultType));
	        } else if (isObject(element, false)) {
	          setOrAccumulate(jsonObject, key, simplifyValue(jsonObject, processObjectElement(element, defaultType)));
	        }
	        else {
	          setOrAccumulate(jsonObject, key, trimSpaceFromValue(element.getValue()));
	        }
	      }
	  }

	  private Object simplifyValue(JSONObject parent, Object json)
	  {
	    if ((json instanceof JSONObject)) {
	      JSONObject object = (JSONObject)json;
	      if (parent != null)
	      {
	        Iterator entries = parent.entrySet().iterator();
	        while (entries.hasNext()) {
	          Map.Entry entry = (Map.Entry)entries.next();
	          String key = (String)entry.getKey();
	          Object value = entry.getValue();
	          if ((key.startsWith("@xmlns")) && (value.equals(object.opt(key)))) {
	            object.remove(key);
	          }
	        }
	      }
	      if ((object.size() == 1) && (object.has("#text"))) {
	        return object.get("#text");
	      }
	    }
	    return json;
	  }
	  private String trimSpaceFromValue(String value) {
	    if (isTrimSpaces()) {
	      return value.trim();
	    }
	    return value;
	  }
	  private String writeDocument(Document doc, String encoding) {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
	      XomSerializer serializer = encoding == null ? new XomSerializer(baos) : new XomSerializer(baos, encoding);

	      serializer.write(doc);
	      encoding = serializer.getEncoding();
	    } catch (IOException ioe) {
	      throw new JSONException(ioe);
	    }

	    String str = null;
	    try {
	      str = baos.toString(encoding);
	    } catch (UnsupportedEncodingException uee) {
	      throw new JSONException(uee);
	    }
	    return str;
	  }

	  private class XomSerializer extends Serializer
	  {
	    public XomSerializer(OutputStream out)
	    {
	      super(out);
	    }

	    public XomSerializer(OutputStream out, String encoding) throws UnsupportedEncodingException {
	      super(out,encoding);
	    }

	    protected void write(Text text) throws IOException {
	      String value = text.getValue();
	      if ((value.startsWith("<![CDATA[")) && (value.endsWith("]]>"))) {
	        value = value.substring(9);
	        value = value.substring(0, value.length() - 3);
	        writeRaw("<![CDATA[");
	        writeRaw(value);
	        writeRaw("]]>");
	      } else {
	        super.write(text);
	      }
	    }

	    protected void writeEmptyElementTag(Element element) throws IOException {
	      if (((element instanceof JsonToXmlUtil.CustomElement)) && (JsonToXmlUtil.this.isNamespaceLenient())) {
	        writeTagBeginning((JsonToXmlUtil.CustomElement)element);
	        writeRaw("/>");
	      } else {
	        super.writeEmptyElementTag(element);
	      }
	    }

	    protected void writeEndTag(Element element) throws IOException {
	      if (((element instanceof JsonToXmlUtil.CustomElement)) && (JsonToXmlUtil.this.isNamespaceLenient())) {
	        writeRaw("</");
	        writeRaw(((JsonToXmlUtil.CustomElement)element).getQName());
	        writeRaw(">");
	      } else {
	        super.writeEndTag(element);
	      }
	    }

	    protected void writeNamespaceDeclaration(String prefix, String uri) throws IOException {
	      if (!StringUtils.isBlank(uri))
	        super.writeNamespaceDeclaration(prefix, uri);
	    }

	    protected void writeStartTag(Element element) throws IOException
	    {
	      if (((element instanceof JsonToXmlUtil.CustomElement)) && (JsonToXmlUtil.this.isNamespaceLenient())) {
	        writeTagBeginning((JsonToXmlUtil.CustomElement)element);
	        writeRaw(">");
	      } else {
	        super.writeStartTag(element);
	      }
	    }

	    private void writeTagBeginning(JsonToXmlUtil.CustomElement element) throws IOException {
	      writeRaw("<");
	      writeRaw(element.getQName());
	      writeAttributes(element);
	      writeNamespaceDeclarations(element);
	    }
	  }

	  private static class CustomElement extends Element
	  {
	    private String prefix;

	    private static String getName(String name)
	    {
	      int colon = name.indexOf(':');
	      if (colon != -1) {
	        return name.substring(colon + 1);
	      }
	      return name;
	    }

	    private static String getPrefix(String name) {
	      int colon = name.indexOf(':');
	      if (colon != -1) {
	        return name.substring(0, colon);
	      }
	      return "";
	    }

	    public CustomElement(String name)
	    {
	      super(name);
	      this.prefix = getPrefix(name);
	    }

	    public final String getQName() {
	      if (this.prefix.length() == 0) {
	        return getLocalName();
	      }
	      return this.prefix + ":" + getLocalName();
	    }
	  }
}
