package util;

import org.w3c.dom.Text;

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
	
	
}
