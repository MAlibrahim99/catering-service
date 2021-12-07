package catering.catalog;

import java.util.ArrayList;
import java.util.List;

public class PriceEditForm {

	private List<OptionFormitem> optionList;
	private String service;

	public PriceEditForm() {
		this.optionList = new ArrayList<>();
		this.service = "";
	}

	public PriceEditForm(List<OptionFormitem> optionList, String service) {
		this.optionList = optionList;
		this.service = service;
	}

	public List<OptionFormitem> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<OptionFormitem> optionList) {
		this.optionList = optionList;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}
}
