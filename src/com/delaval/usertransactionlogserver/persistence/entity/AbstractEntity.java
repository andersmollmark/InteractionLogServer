package com.delaval.usertransactionlogserver.persistence.entity;

import com.delaval.usertransactionlogserver.websocket.WebSocketMessage;
import simpleorm.dataset.SFieldScalar;
import simpleorm.dataset.SRecordInstance;
import simpleorm.dataset.SRecordMeta;

import java.util.Optional;

/**
 */
public abstract class AbstractEntity extends SRecordInstance {

	@Override
	public SRecordMeta<?> getMeta() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


	public SFieldScalar getPrimaryKey() {
		return getMeta().getPrimaryKeys()[0];
	}
	public String getPrimaryKeyValue() {
		return getString(getPrimaryKey());
	}

	protected static String getLogContextId(WebSocketMessage webSocketMessage){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(webSocketMessage.getUsername()).
				append(webSocketMessage.getClient()).
				append(webSocketMessage.getTarget());
		return stringBuilder.toString();
	}

	public static <T> Optional<T> convertInstanceOfObject(Object o, Class<T> c) {
		if(o == null){
			return Optional.<T>empty();
		}
		try {
			return Optional.of(c.cast(o));
		} catch (ClassCastException e) {
			e.printStackTrace();
			return Optional.<T>empty();
		}
	}
}
