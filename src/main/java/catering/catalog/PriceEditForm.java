package catering.catalog;

import java.util.ArrayList;
import java.util.List;

/**
 * object to transfer the available options to the view
 */
public class PriceEditForm {

	private List<OptionFormitem> optionList;
	private String service;

	public PriceEditForm() {
		this.optionList = new ArrayList<>();
		this.service = "";
	}

	/**
	 * a parametrised constructor
	 * @param optionList list of all {@link Option} to be transfered to view
	 * @param service the selected service type, either eventcatering, partyserice, mobile breakfast or rent-a-cook
	 */
	public PriceEditForm(List<OptionFormitem> optionList, String service) {
		this.optionList = optionList;
		this.service = service;
	}

	/**
	 * getters and setters for the option list and the service type
	 */
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
