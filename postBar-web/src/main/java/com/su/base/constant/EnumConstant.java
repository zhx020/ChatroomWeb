package com.su.base.constant;

public class EnumConstant {
	public enum RoleEnum {
		STUDENT(1,"学生"),
		SPORTER(2,"运动员"),
		JUDGER(3,"裁判");
		int code;
		String name;
		private RoleEnum(int code,String name) {
			this.code = code;
			this.name = name;
		}
		public int getCode() {
			return code;
		}
		public String getName() {
			return name;
		}
	}
	public enum SexEnum {
		MAN(1,"男"),
		WOMAN(0,"女"),
		ALL(-1,"无");
		int code;
		String name;
		private SexEnum(int code,String name) {
			this.code = code;
			this.name = name;
		}
		public int getCode() {
			return code;
		}
		public String getName() {
			return name;
		}
		public static SexEnum getInstanceByCode(Integer code) {
			for(SexEnum sexEnum : values()) {
				if(Integer.valueOf(sexEnum.getCode()).equals(code)) {
					return sexEnum;
				}
			}
			return ALL;
		}
	}
	public enum CompetitionStatusEnum {
		WAIT(0,"未开始"),
		OVER(1,"已结束"),
		CANCEL(2,"取消");
		int code;
		String name;
		private CompetitionStatusEnum(int code,String name) {
			this.code = code;
			this.name = name;
		}
		public int getCode() {
			return code;
		}
		public String getName() {
			return name;
		}
		public static CompetitionStatusEnum getInstanceByCode(int code) {
			for(CompetitionStatusEnum sexEnum : values()) {
				if(sexEnum.getCode() == code) {
					return sexEnum;
				}
			}
			return null;
		}
	}
}
