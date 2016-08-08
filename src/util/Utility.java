package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import db.CoolWeatherDB;
import model.City;
import model.County;
import model.Province;

public class Utility {
	//解析和处理服务器返回的省级数据
	public synchronized static boolean handleProvincesResponse (CoolWeatherDB coolWeatherDB, String response) {
		if(!(TextUtils.isEmpty(response))) {
			String[] allProvince = response.split(",");
			if(allProvince != null && allProvince.length != 0){ 
				for(String pro : allProvince) {
					String[] tempArray = pro.split("\\|");
					Province province = new Province();
					province.setProvinceCode(tempArray[0]);
					province.setProvinceName(tempArray[1]);
					//将province存储到数据库的Province表
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		
		return false;
	}
	
	//解析和处理服务器返回的市级数据
	public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId) {
		if(!(TextUtils.isEmpty(response))) {
			String[] allCities = response.split(",");
			if(allCities != null && allCities.length != 0) {
				for(String cit : allCities) {
					String[] tempArray = cit.split("\\|");
					City city = new City();
					city.setCityCode(tempArray[0]);
					city.setCityName(tempArray[1]);
					city.setProvinceId(provinceId);
					//将城市数据存储到数据库的City表
					coolWeatherDB.saveCity(city);
				}
				return true;
			}	
		}
		
		return false;
	}
	
	//解析和处理服务器返回的县级数据
	public synchronized static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB, String response, int cityId) {
		if(!(TextUtils.isEmpty(response))) {
			String[] allCounties = response.split(",");
			if(allCounties != null && allCounties.length != 0) {
				for(String cou : allCounties) {
					String[] tempArray = cou.split("\\|");
					County county = new County();
					county.setCountyCode(tempArray[0]);
					county.setCountyName(tempArray[1]);
					county.setCityId(cityId);
					//将城市数据存储到数据库的City表
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}	
		}
		
		return false;
	}
	
	//解析服务器返回的JSON数据，并将解析出的数据存储到本地
	public static void handleWeatherResponse(Context context, String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	//将服务器返回的所有天气信息存储到SharedPreferences文件中
	public static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1,
			String temp2, String weatherDesp, String publishTime) {
		// TODO Auto-generated method stub
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_time", simpleDateFormat.format(new Date()));
		editor.commit();
	}
	
}








