package com.njpalmin.smartlist.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SyncDataHandler extends DefaultHandler {
	boolean mInName = false;
	boolean mInPrice = false;
	String mProductName;
	String mProductPrice;
	
	public String getProductName(){
		return mProductName;
	}
	
	public String getProductPrice(){
		return mProductPrice;
	}
	
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals("name")){
			mInName = true;
		}else if(localName.equals("price")){
			mInPrice = true;
		}
	}
	

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		if(mInName){
			mProductName  = String.valueOf(ch, start, length);
		}else if(mInPrice){
			mProductPrice  = String.valueOf(ch, start, length);
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if(localName.equals("name")){
			mInName = false;
		}else if(localName.equals("price")){
			mInPrice = false;
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}
	

}
