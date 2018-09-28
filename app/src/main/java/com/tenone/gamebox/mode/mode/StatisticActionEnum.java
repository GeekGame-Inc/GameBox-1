package com.tenone.gamebox.mode.mode;

public enum StatisticActionEnum {
	INDEX_EARN_COINS( "\u9996\u9875_\u8d5a\u91d1\u5e01" ),
	BT_NEW_GAME( "BT_\u65b0\u6e38" ),
	BT_GAME_TOP( "BT_\u6392\u884c\u699c" ),
	BT_GIVE_VIP( "BT_\u9001\u6ee1V" ),
	BT_OPEN_SERVER( "BT_\u5f00\u670d\u8868" ),
	BT_GAME_CLASSIFY( "BT_\u6e38\u620f\u5206\u7c7b" ),
	DISCOUNT_NEW_GAME( "\u6298\u6263_\u65b0\u6e38" ),
	DISCOUNT_GAME_TOP( "\u6298\u6263_\u6392\u884c\u699c" ),
	DISCOUNT_ULTRA_LOW( "\u6298\u6263_\u8d85\u4f4e\u6298\u6263" ),
	DISCOUNT_OPEN_SERVER( "\u6298\u6263_\u5f00\u670d\u8868" ),
	DISCOUNT_GAME_CLASSIFY( "\u6298\u6263_\u6e38\u620f\u5206\u7c7b" ),
	H5_NEW_GAME( "H5_\u65b0\u6e38" ),
	H5_GAME_TOP( "H5_\u6392\u884c\u699c" ),
	H5_EARN_COINS( "H5_\u8d5a\u91d1\u5e01" ),
	H5_OPEN_SERVER( "H5_\u5f00\u670d\u8868" ),
	H5_GAME_CLASSIFY( "H5_\u6e38\u620f\u5206\u7c7b" ),
	PERSONAL_REGISTER( "\u4e2a\u4eba\u4e2d\u5fc3_\u767b\u5f55\u6ce8\u518c" ),
	PERSONAL_OPEN_VIP( "\u4e2a\u4eba\u4e2d\u5fc3_\u5f00\u901a\u4f1a\u5458" ),
	PERSONAL_SIGN( "\u4e2a\u4eba\u4e2d\u5fc3_\u6bcf\u65e5\u7b7e\u5230" ),
	PERSONAL_COMMENT( "\u4e2a\u4eba\u4e2d\u5fc3_\u6bcf\u65e5\u8bc4\u8bba" ),
	PERSONAL_SHARE( "\u4e2a\u4eba\u4e2d\u5fc3_\u9080\u8bf7\u597d\u53cb" ),
	PERSONAL_SHARE_TOP( "\u4e2a\u4eba\u4e2d\u5fc3_\u9080\u8bf7\u6392\u884c" ),
	PERSONAL_DRIVE( "\u4e2a\u4eba\u4e2d\u5fc3_\u6bcf\u65e5\u53d1\u8f66" ),
	PERSONAL_EXCHANGE_COINS( "\u4e2a\u4eba\u4e2d\u5fc3_\u5151\u6362\u91d1\u5e01" ),
	PERSONAL_RAFFLE( "\u4e2a\u4eba\u4e2d\u5fc3_\u91d1\u5e01\u62bd\u5956" ),
	PERSONAL_REBATE( "\u4e2a\u4eba\u4e2d\u5fc3_\u8fd4\u5229\u7533\u8bf7" ),
	PERSONAL_TRANSFER( "\u4e2a\u4eba\u4e2d\u5fc3_\u7533\u8bf7\u8f6c\u6e38" ),
	PERSONAL_MANAGER( "\u4e2a\u4eba\u4e2d\u5fc3_\u5e94\u7528\u7ba1\u7406" ),
	PERSONAL_GIFTS( "\u4e2a\u4eba\u4e2d\u5fc3_\u6211\u7684\u793c\u5305" ),
	PERSONAL_COLLECT( "\u4e2a\u4eba\u4e2d\u5fc3_\u6211\u7684\u6536\u85cf" ),
	PERSONAL_MESSAGE( "\u4e2a\u4eba\u4e2d\u5fc3_\u6211\u7684\u6d88\u606f" ),
	PERSONAL_CALL_CENTER( "\u4e2a\u4eba\u4e2d\u5fc3_\u5ba2\u670d\u4e2d\u5fc3" ),
	PERSONAL_CHANGE_PASSWORD( "\u4e2a\u4eba\u4e2d\u5fc3_\u4fee\u6539\u5bc6\u7801" ),
	PERSONAL_BIND_MOBILE( "\u4e2a\u4eba\u4e2d\u5fc3_\u7ed1\u5b9a\u624b\u673a" ),
	PERSONAL_DETAIL( "\u4e2a\u4eba\u4e2d\u5fc3_\u8d27\u5e01\u660e\u7ec6" ),
	PERSONAL_ABOUT( "\u4e2a\u4eba\u4e2d\u5fc3_\u5173\u4e8e\u6211\u4eec" ),
	PERSONAL_DO_ANSWER( "\u4e2a\u4eba\u4e2d\u5fc3_\u6bcf\u65e5\u95ee\u7b54" ),
	PERSONAL_MY_QUESTIONS( "\u4e2a\u4eba\u4e2d\u5fc3_\u6211\u7684\u63d0\u95ee" ),
	REGISTRATION_COMPLETE( "\u6ce8\u518c\u5b8c\u6210" );
	private final String value;

	StatisticActionEnum(String s) {
		this.value = s;
	}

	public String getValue() {
		return value;
	}
}