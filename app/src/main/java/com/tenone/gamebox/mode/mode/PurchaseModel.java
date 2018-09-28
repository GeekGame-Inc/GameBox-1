package com.tenone.gamebox.mode.mode;

/**
 * 今日头条支付统计
 */
public class PurchaseModel {
	private String content_type;
	private String content_name;
	private String content_id;
	private int content_num;
	private String payment_channel;
	private String currency;
	private boolean is_success;
	private int currency_amount;

	public PurchaseModel(String content_type, String content_name, String content_id, int content_num, String payment_channel, String currency, boolean is_success, int currency_amount) {
		this.content_type = content_type;
		this.content_name = content_name;
		this.content_id = content_id;
		this.content_num = content_num;
		this.payment_channel = payment_channel;
		this.currency = currency;
		this.is_success = is_success;
		this.currency_amount = currency_amount;
	}

	public PurchaseModel() {
	}

	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	public String getContent_name() {
		return content_name;
	}

	public void setContent_name(String content_name) {
		this.content_name = content_name;
	}

	public String getContent_id() {
		return content_id;
	}

	public void setContent_id(String content_id) {
		this.content_id = content_id;
	}

	public int getContent_num() {
		return content_num;
	}

	public void setContent_num(int content_num) {
		this.content_num = content_num;
	}

	public String getPayment_channel() {
		return payment_channel;
	}

	public void setPayment_channel(String payment_channel) {
		this.payment_channel = payment_channel;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public boolean isIs_success() {
		return is_success;
	}

	public void setIs_success(boolean is_success) {
		this.is_success = is_success;
	}

	public int getCurrency_amount() {
		return currency_amount;
	}

	public void setCurrency_amount(int currency_amount) {
		this.currency_amount = currency_amount;
	}
}
