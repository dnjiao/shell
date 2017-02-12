package models;

public class Settlement {
	private String contractNo;
	private String smartNo;
	private String dealTrackNo;
	private String buySellFlag;
	private String cashFlowType;
	private String location;
	private String leaseNo;
	private String leaseName;
	private double volume;
	private double price;
	private double settleAmount;
	
	public Settlement() {
		super();
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getSmartNo() {
		return smartNo;
	}

	public void setSmartNo(String smartNo) {
		this.smartNo = smartNo;
	}

	public String getDealTrackNo() {
		return dealTrackNo;
	}

	public void setDealTrackNo(String dealTrackNo) {
		this.dealTrackNo = dealTrackNo;
	}

	public String getBuySellFlag() {
		return buySellFlag;
	}

	public void setBuySellFlag(String buySellFlag) {
		this.buySellFlag = buySellFlag;
	}

	public String getCashFlowType() {
		return cashFlowType;
	}

	public void setCashFlowType(String cashFlowType) {
		this.cashFlowType = cashFlowType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLeaseNo() {
		return leaseNo;
	}

	public void setLeaseNo(String leaseNo) {
		this.leaseNo = leaseNo;
	}

	public String getLeaseName() {
		return leaseName;
	}

	public void setLeaseName(String leaseName) {
		this.leaseName = leaseName;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getSettleAmount() {
		return settleAmount;
	}

	public void setSettleAmount(double settleAmount) {
		this.settleAmount = settleAmount;
	}
	
}
