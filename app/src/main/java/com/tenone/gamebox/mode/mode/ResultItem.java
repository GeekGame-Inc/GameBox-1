package com.tenone.gamebox.mode.mode;

import android.annotation.SuppressLint;

import com.tenone.gamebox.view.utils.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


@SuppressLint("DefaultLocale")
public class ResultItem extends Object implements Serializable {

	private static final long serialVersionUID = 4288348598571503031L;

	protected Map<String, Object> values = new HashMap<String, Object>();

	public ResultItem() {
	}

	public ResultItem(Map<String, Object> valuetemp) {
		setValues(valuetemp);
	}

	public void addValue(String key, Object obj) {
		values.put(key.toUpperCase(), obj);
	}

	public void setValues(Map<String, Object> valuetemp) {
		values = valuetemp;
	}

	public void appendValues(ResultItem item) {
		if (item != null) {
			appendValues(item.getValues());
		}
	}

	public void appendValues(Map<String, Object> valuetemp) {
		if (!BeanUtils.isEmpty(valuetemp)) {
			for (Entry<String, Object> temp : valuetemp.entrySet()) {
				addValue(temp.getKey(), temp.getValue());
			}
		}
	}

	public Map<String, Object> getValues() {
		return values;
	}

	@SuppressLint("DefaultLocale")
	public String getString(String key) {
		String message = "";
		try {
			Object value = getValue(key.toUpperCase());
			if (value != null) {
				message = value.toString();
				if ("null".equals(message.trim())) {
					message = "";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	public Object getValue(String key) {
		Object value = null;
		try {
			if (!BeanUtils.isEmpty(key)) {
				String[] pathNames = key.split("\\|");
				if (pathNames.length == 1) {
					value = values.get(key.toUpperCase());
				} else {
					value = getValueByPath(key);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public List<ResultItem> getItems(String key) {
		List<ResultItem> items = null;
		try {
			Object value = getValue(key);
			if (value instanceof List) {
				items = (List<ResultItem>) value;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	public ResultItem getItem(String key) {
		ResultItem item = null;
		try {
			item = (ResultItem) getValue(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	public Date getDate(String key) {
		return getDate(key, "yyyy-MM-dd");
	}

	public Date getDate(String key, String formatstr) {
		formatstr = BeanUtils.isEmpty(formatstr) ? "yyyy-MM-dd" : formatstr;
		return BeanUtils.parseDate(getString(key), formatstr);
	}

	public int getIntValue(String key) {
		int i = 0;
		try {
			Number number = new BigDecimal(getString(key));
			i = number.intValue();
		} catch (Exception e) {
		}
		return i;
	}

	public float getFloatValue(String key) {
		float f = 0;
		try {
			Number number = new BigDecimal(getString(key));
			f = number.floatValue();
		} catch (Exception e) {
		}
		return f;
	}

	public double getDoubleValue(String key) {
		double d = 0;
		try {
			d = Double.valueOf(getString(key));
		} catch (Exception e) {
		}
		return d;
	}

	public boolean getBooleanValue(String key) {
		boolean b = false;
		try {
			b = Boolean.valueOf(getString(key));
		} catch (Exception e) {

		}
		return b;
	}

	public boolean getBooleanValue_(char key) {
		boolean b = false;
		try {
			b = Boolean.valueOf(getString(String.valueOf(key)));
		} catch (Exception e) {

		}
		return b;
	}

	public boolean getBooleanValue(String key, int compareValue) {
		boolean b = false;
		try {
			int a = getIntValue(key);
			b = a == compareValue;
		} catch (Exception e) {

		}
		return b;
	}

	public boolean getBooleanValue(String key, float compareValue) {
		boolean b = false;
		try {
			float a = getFloatValue(key);
			b = a == compareValue;
		} catch (Exception e) {

		}
		return b;
	}

	public boolean getBooleanValue(String key, double compareValue) {
		boolean b = false;
		try {
			double a = getDoubleValue(key);
			b = a == compareValue;
		} catch (Exception e) {

		}
		return b;
	}

	public boolean getBooleanValue(String key, String compareValue) {
		boolean b = false;
		try {
			String a = getString(key);
			b = a.equals(compareValue);
		} catch (Exception e) {

		}
		return b;
	}

	public void clear() {
		values.clear();
	}

	protected Object getValueByPath(String paths) {
		Object resulitems = null;
		if (!BeanUtils.isEmpty(paths)) {
			String[] pathNames = paths.split("\\|");
			if (!BeanUtils.isEmpty(pathNames)) {
				ResultItem currentItem = this;
				for (int i = 0; i < pathNames.length; i++) {
					resulitems = currentItem.getValue(pathNames[i]);
					if (i != pathNames.length - 1) {
						if (resulitems instanceof ResultItem) {
							currentItem = (ResultItem) resulitems;
						} else {
							break;
						}
					}
				}
			}
		}
		return resulitems;
	}

	public ResultItem searchItem(String rootKey, String queryKey, String value) {
		ResultItem item = null;
		try {
			Object obj = getValue(rootKey);
			if (obj != null) {
				if (obj instanceof ResultItem) {
					String temp = ((ResultItem) obj).getString(queryKey);
					if (!BeanUtils.isEmpty(temp) && temp.equals(value)) {
						item = (ResultItem) obj;
					}
				} else if (obj instanceof List<?>) {
					List<?> resuls = (List<?>) obj;
					if (!BeanUtils.isEmpty(resuls)) {
						for (Object tempObj : resuls) {
							ResultItem tempItem = (ResultItem) tempObj;
							String temp = tempItem.getString(queryKey);
							if (!BeanUtils.isEmpty(temp) && temp.equals(value)) {
								item = tempItem;
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return item;
	}
}
