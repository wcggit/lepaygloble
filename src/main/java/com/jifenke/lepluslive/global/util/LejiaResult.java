package com.jifenke.lepluslive.global.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wcg on 16/3/9.
 */
public class LejiaResult implements Serializable{

  // 定义jackson对象
  private static final ObjectMapper MAPPER = new ObjectMapper();

  // 响应业务状态
  private Integer status;

  // 响应消息
  private String msg;

  // 响应中的数据
  private Object data;

  public static LejiaResult build(Integer status, String msg, Object data) {
    return new LejiaResult(status, msg, data);
  }

  public static LejiaResult ok(Object data) {
    return new LejiaResult(data);
  }

  public static LejiaResult ok() {
    return new LejiaResult(null);
  }

  public LejiaResult() {

  }

  public static LejiaResult build(Integer status, String msg) {
    return new LejiaResult(status,msg,null);
  }

  public LejiaResult(Integer status, String msg, Object data) {
    this.status = status;
    this.msg = msg;
    this.data = data;
  }

  public LejiaResult(Object data) {
    this.status = 200;
    this.msg = "OK";
    this.data = data;
  }

//    public Boolean isOK() {
//        return this.status == 200;
//    }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  /**
   * 将json结果集转化为TaotaoResult对象
   *
   * @param jsonData json数据
   * @param clazz
   */
  public static LejiaResult formatToPojo(String jsonData, Class<?> clazz) {
    try {
      if (clazz == null) {
        return MAPPER.readValue(jsonData,LejiaResult.class);
      }
      JsonNode jsonNode = MAPPER.readTree(jsonData);
      JsonNode data = jsonNode.get("data");
      Object obj = null;
      if (clazz != null) {
        if (data.isObject()) {
          obj = MAPPER.readValue(data.traverse(), clazz);
        } else if (data.isTextual()) {
          obj = MAPPER.readValue(data.asText(), clazz);
        }
      }
      return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 没有object对象的转化
   */
  public static LejiaResult format(String json) {
    try {
      return MAPPER.readValue(json,LejiaResult.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static LejiaResult formatToList(String jsonData, Class<?> clazz) {
    try {
      JsonNode jsonNode = MAPPER.readTree(jsonData);
      JsonNode data = jsonNode.get("data");
      Object obj = null;
      if (data.isArray() && data.size() > 0) {
        obj = MAPPER.readValue(data.traverse(),
                               MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
      }
      return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
    } catch (Exception e) {
      return null;
    }
  }
}
