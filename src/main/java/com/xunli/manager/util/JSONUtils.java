package com.xunli.manager.util;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanglei on 2015/9/18.
 */
public class JSONUtils
{

	private final static ObjectMapper objectMapper = new ObjectMapper();

	private JSONUtils()
	{

	}

	public static ObjectMapper getInstance()
	{

		return objectMapper;
	}

	/**
	 * javaBean,list,array convert to json string
	 * @throws JsonProcessingException
	 */
	public static String obj2json(Object obj) throws JsonProcessingException
	{
		return objectMapper.writeValueAsString(obj);
	}

	/**
	 * json string convert to javaBean
	 */
	public static <T> T json2pojo(String jsonStr, Class<T> clazz)
			throws Exception
	{
		return objectMapper.readValue(jsonStr, clazz);
	}

	/**
	 * json string convert to map
	 */
	public static <T> Map<String, Object> json2map(String jsonStr)
			throws Exception
	{
		return objectMapper.readValue(jsonStr, Map.class);
	}

	/**
	 * json string convert to map with javaBean
	 */
	public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz)
			throws Exception
	{
		Map<String, Map<String, Object>> map = objectMapper.readValue(jsonStr,
				new TypeReference<Map<String, T>>()
				{});
		Map<String, T> result = new HashMap<String, T>();
		for (Map.Entry<String, Map<String, Object>> entry : map.entrySet())
		{
			result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
		}
		return result;
	}

	/**
	 * json array string convert to list with javaBean
	 */
	public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz)
			throws Exception
	{
		List<Map<String, Object>> list = objectMapper.readValue(jsonArrayStr,
				new TypeReference<List<T>>()
				{});
		List<T> result = new ArrayList<T>();
		for (Map<String, Object> map : list)
		{
			result.add(map2pojo(map, clazz));
		}
		return result;
	}

	/**
	 * map convert to javaBean
	 */
	public static <T> T map2pojo(Map map, Class<T> clazz)
	{
		return objectMapper.convertValue(map, clazz);
	}

	/**
	 * 将对象转换成json并写入文件
	 * 
	 * @param resultFile
	 * @param obj
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 * @throws IOException
	 */
	public static void Obj2File(File resultFile, Object obj) throws IOException 
	{
		objectMapper.writeValue(resultFile, obj);
	}

	/**
	 * 将文件中的json内容转换成相应对象
	 * 
	 * @param src
	 * @param classType
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> T File2Obj(File src, Class<T> classType)
			throws JsonParseException, JsonMappingException, IOException
	{
		return objectMapper.readValue(src, classType);
	}

	/**
	 * 拼写出返回给客户端的数据结构
	 * 
	 * @param resCode
	 * @param obj
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String GetResultJson(String resCode, String msg,
			String msgChinese, Object obj)
	{
		String resStr = "";
		try
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("data", obj);
			map.put("msg", msg);
			map.put("msgChinese", msgChinese);
			map.put("code", resCode);
			resStr = objectMapper.writeValueAsString(map);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return resStr;
	}

	public static String GetStringFromJSONObject(JSONObject jsonobj,
			String fieldName)
	{
		if (fieldName == null || fieldName.isEmpty())
		{
			return "";
		}

		Object obj = jsonobj.get(fieldName);
		return obj == JSONObject.NULL ? "" : obj.toString();
	}

	public static String toJsonStr(Map<String, Object> map){
		StringBuffer sbf = new StringBuffer("{");
		int index = 0;
		for(String key : map.keySet()){
			if(index > 0){
				sbf.append(",");
			}
			index++;
			sbf.append("\"" + key + "\":\"" + map.get(key) + "\"");
		}
		sbf.append("}");
		return sbf.toString();
	}
	
	/*public static int GetIntFromJSONObject(JSONObject jsonobj,
			String fieldName)
	{
		if (fieldName == null || fieldName.isEmpty())
		{
			return "";
		}

		Object obj = jsonobj.get(fieldName);
		return obj == JSONObject.NULL ? "" : obj.toString();
	}*/

}
