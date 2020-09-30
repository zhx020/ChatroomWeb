package com.su.base.converter;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import com.su.base.constant.DateConstant;
import com.su.base.util.DateUtil;

public class DateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String stringDate) {
        return DateUtil.strConvertDate(stringDate, DateConstant.Y_M_D);
    }

}
