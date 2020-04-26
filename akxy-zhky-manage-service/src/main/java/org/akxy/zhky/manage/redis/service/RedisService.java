/*package org.akxy.zhky.manage.redis.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akxy.zhky.akxy_common.redis.ListTranscoder;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

 *//**

  * @ClassName: IRedisService.java
  * @Description: redis服务
  * @date: 2018年6月14日
  *//*
@Service
public class RedisService {
	@Autowired
	private JedisPool jedisPool;

	private <T>	T execute(Function<T,Jedis> fun){
		Jedis jedis = null;
		try{
			//从连接池中获取到jedis对象
			try{
				jedis = jedisPool.getResource();
			}catch(Exception e){
				e.printStackTrace();
			}
		} finally{
			if(null != jedis){
				//关闭，监测连接是否有效，有效则放回到连接池中，无效则重置状态
				jedis.close();
			}
		}
		return fun.callback(jedis);
	}

   *//**
   * @Title: set
   * @Description: redis的String类型的set操作
   * @param: @param key
   * @param: @param value
   * @param: @return
   * @return: String
   * @throws
   *//*
	public String set(final String key, final String value) {
		return this.execute(new Function<String, Jedis>() {
			@Override
			public String callback(Jedis e) {
				return e.set(key, value);
			}
		});
	}

    *//**
    * @Title: get
    * @Description: redis的Strin的get操作
    * @param: @param key
    * @param: @return
    * @return: String
    * @throws
    *//*
	public String get(final String key) {
		return this.execute(new Function<String, Jedis>() {
			@Override
			public String callback(Jedis e) {
				return e.get(key);
			}
		});
	}
     *//**
     * @Title: SetList
     * @Description: redis对List对象进行set操作
     * @param: @param key
     * @param: @param list
     * @param: @return
     * @return: String
     * @throws
     *//*
	public String setList(final String key,final List<Object> list){
		return this.execute(new Function<String, Jedis>() {
			@Override
			public String callback(Jedis e) {
				if(list!=null&&list.size()>0){
					ListTranscoder<String> listTranscoder = new ListTranscoder<String>();
					return e.set("oldlist".getBytes(), listTranscoder.serialize(list));
				}else{
					return null;
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<Object> getList(final String key){
		return this.execute(new Function<List, Jedis>() {
			@Override
			public List<String> callback(Jedis e) {
				try {
					ListTranscoder<String> listTranscoder = new ListTranscoder<String>();
					byte[] list = e.get(key.getBytes());
					List<String> newlist = listTranscoder.deserialize(list);
					return newlist;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return null;
			}
		});
	}

}
      */