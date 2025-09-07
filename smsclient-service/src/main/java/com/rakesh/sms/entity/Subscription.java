package com.rakesh.sms.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;

@Entity
//@Table(name="subscription", uniqueConstraints = @UniqueConstraint(columnNames={"MSISDN", "SUBSERVICE_ID"}))
//@Entity(dynamicInsert = true, dynamicUpdate = true) 
public class Subscription  implements java.io.Serializable 
{
   	private static final long serialVersionUID = 1494813255077714968L;
	private Integer id;
    private String msisdn;
    private String subserviceId;
    private Date startDate;
    private Date endDate;
    private String schedulerRenewalStatus;
    private String status;
    private Date lastRenewDate;
    private Date lastCallDate;
    private String transactionStatus;
    private String circle;
    private String country;
    private String language;
    private Integer price;
    private Integer callAttempts;
    private String primaryActMode;
    private String secondaryActMode;
    private String errorMsg;
    private String subType;
    private Integer subTimeLeft;
    private String subServiceName;
    private String operator;
    private String giftId;
    private String serviceId;
    private Integer retryCount;
    private Date nextRetryDate;
    private Integer cpid;
    private String schedulerRetryStatus;
    private String schedulerStatechangerStatus;
    private String schedulerSubsMsgsStatus;
    private String serviceName;
    private String transactionId;
    private String action;
    private String autoRenew;
    private String option1;
    private boolean makeOnlyCdr=false;
    private Date nextMsgDate;
    //private String smsPromoStatus;


	public Subscription() 
    {
    }
    public Subscription(String schedulerRenewalStatus) 
    {
        this.schedulerRenewalStatus = schedulerRenewalStatus;
    }
    public Subscription(String msisdn, String subserviceId, Date startDate, Date endDate, String schedulerRenewalStatus, String status, Date lastRenewDate, Date lastCallDate, String transactionStatus, String circle, String country, String language, Integer price, Integer callAttempts, String primaryActMode, String secondaryActMode, String errorMsg, String subType, Integer subTimeLeft, String subServiceName, String operator, String giftId, String serviceId, Integer retryCount, Date nextRetryDate, Integer cpid, String schedulerRetryStatus,String schedulerStatechangerStatus,String schedulerSubsMsgsStatus,String serviceName,String channel,String transactionId,String action,String autoRenew,String option1,String request) 
    {
       this.msisdn = msisdn;
       this.subserviceId = subserviceId;
       this.startDate = startDate;
       this.endDate = endDate;
       this.schedulerRenewalStatus = schedulerRenewalStatus;
       this.status = status;
       this.lastRenewDate = lastRenewDate;
       this.lastCallDate = lastCallDate;
       this.transactionStatus = transactionStatus;
       this.circle = circle;
       this.country = country;
       this.language = language;
       this.price = price;
       this.callAttempts = callAttempts;
       this.primaryActMode = primaryActMode;
       this.secondaryActMode = secondaryActMode;
       this.errorMsg = errorMsg;
       this.subType = subType;
       this.subTimeLeft = subTimeLeft;
       this.subServiceName = subServiceName;
       this.operator = operator;
       this.giftId = giftId;
       this.serviceId = serviceId;
       this.retryCount = retryCount;
       this.nextRetryDate = nextRetryDate;
       this.cpid = cpid;
       this.schedulerRetryStatus = schedulerRetryStatus;
       this.schedulerStatechangerStatus = schedulerStatechangerStatus;
       this.schedulerSubsMsgsStatus = schedulerSubsMsgsStatus;
       this.serviceName = serviceName;
       this.transactionId = transactionId;
       this.action = action;
       this.autoRenew=autoRenew;
       this.option1=option1;
       //this.smsPromoStatus = smsPromoStatus;

    }
   
    @Id @GeneratedValue(strategy=IDENTITY)
    @Column(name="ID", unique=true, nullable=false)
    public Integer getId() 
    {
        return this.id;
    }
    public void setId(Integer id) 
    {
        this.id = id;
    }
    
    @Column(name="MSISDN", length=25)
    public String getMsisdn() 
    {
        return this.msisdn;
    }
    public void setMsisdn(String msisdn) 
    {
        this.msisdn = msisdn;
    }
    
    @Column(name="SUBSERVICE_ID", length=25)
    public String getSubserviceId() 
    {
        return this.subserviceId;
    }
    public void setSubserviceId(String subserviceId) 
    {
        this.subserviceId = subserviceId;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="START_DATE", length=19)
    public Date getStartDate() 
    {
        return this.startDate;
    }
    public void setStartDate(Date startDate) 
    {
        this.startDate = startDate;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="END_DATE", length=19)
    public Date getEndDate() 
    {
        return this.endDate;
    }
    public void setEndDate(Date endDate) 
    {
        this.endDate = endDate;
    }
    
    @Column(name="scheduler_renewal_status", nullable=false, length=25)
    public String getSchedulerRenewalStatus() 
    {
        return this.schedulerRenewalStatus;
    }
    public void setSchedulerRenewalStatus(String schedulerRenewalStatus) 
    {
        this.schedulerRenewalStatus = schedulerRenewalStatus;
    }
    
    @Column(name="STATUS", length=20)
    public String getStatus() 
    {
        return this.status;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="LAST_RENEW_DATE", length=19)
    public Date getLastRenewDate() 
    {
        return this.lastRenewDate;
    }
    public void setLastRenewDate(Date lastRenewDate) 
    {
        this.lastRenewDate = lastRenewDate;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="LAST_CALL_DATE", length=19)
    public Date getLastCallDate() 
    {
        return this.lastCallDate;
    }
    public void setLastCallDate(Date lastCallDate) 
    {
        this.lastCallDate = lastCallDate;
    }
    
    @Column(name="TRANSACTION_STATUS", length=15)
    public String getTransactionStatus() 
    {
        return this.transactionStatus;
    }
    public void setTransactionStatus(String transactionStatus) 
    {
        this.transactionStatus = transactionStatus;
    }
    
    @Column(name="CIRCLE", length=3)
    public String getCircle() 
    {
        return this.circle;
    }
    public void setCircle(String circle) 
    {
        this.circle = circle;
    }
    
    @Column(name="COUNTRY", length=3)
    public String getCountry() 
    {
        return this.country;
    }
    public void setCountry(String country) 
    {
        this.country = country;
    }
    
    @Column(name="LANGUAGE", length=3)
    public String getLanguage() 
    {
        return this.language;
    }
    public void setLanguage(String language) 
    {
        this.language = language;
    }
    
    @Column(name="PRICE")
    public Integer getPrice() 
    {
        return this.price;
    }
    public void setPrice(Integer price) 
    {
        this.price = price;
    }
    
    @Column(name="CALL_ATTEMPTS")
    public Integer getCallAttempts() 
    {
        return this.callAttempts;
    }
    public void setCallAttempts(Integer callAttempts) 
    {
        this.callAttempts = callAttempts;
    }
    
    @Column(name="PRIMARY_ACT_MODE", length=25)
    public String getPrimaryActMode() 
    {
        return this.primaryActMode;
    }
    public void setPrimaryActMode(String primaryActMode) 
    {
        this.primaryActMode = primaryActMode;
    }
    @Column(name="SECONDARY_ACT_MODE", length=25)
    public String getSecondaryActMode() 
    {
        return this.secondaryActMode;
    }
    public void setSecondaryActMode(String secondaryActMode) 
    {
        this.secondaryActMode = secondaryActMode;
    }
    
    @Column(name="ERROR_MSG", length=25)
    public String getErrorMsg()
    {
        return this.errorMsg;
    }
    public void setErrorMsg(String errorMsg) 
    {
        this.errorMsg = errorMsg;
    }
    
    @Column(name="SUB_TYPE", length=10)
    public String getSubType() 
    {
        return this.subType;
    }
    public void setSubType(String subType) 
    {
        this.subType = subType;
    }
    
    @Column(name="SUB_TIME_LEFT")
    public Integer getSubTimeLeft() 
    {
        return this.subTimeLeft;
    }
    public void setSubTimeLeft(Integer subTimeLeft) 
    {
        this.subTimeLeft = subTimeLeft;
    }
    
    @Column(name="SUB_SERVICE_NAME", length=25)
    public String getSubServiceName() 
    {
        return this.subServiceName;
    }
    public void setSubServiceName(String subServiceName) 
    {
        this.subServiceName = subServiceName;
    }
    
    @Column(name="OPERATOR", length=25)
    public String getOperator() 
    {
        return this.operator;
    }
    public void setOperator(String operator) 
    {
        this.operator = operator;
    }
    
    @Column(name="GIFT_ID", length=25)
    public String getGiftId() 
    {
        return this.giftId;
    }
    public void setGiftId(String giftId) 
    {
        this.giftId = giftId;
    
    }
    
    @Column(name="SERVICE_ID", length=25)
    public String getServiceId() 
    {
        return this.serviceId;
    }
    public void setServiceId(String serviceId) 
    {
        this.serviceId = serviceId;
    }
    
    @Column(name="RETRY_COUNT")
    public Integer getRetryCount() 
    {
        return this.retryCount;
    }
    public void setRetryCount(Integer retryCount) 
    {
        this.retryCount = retryCount;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="NEXT_RETRY_DATE", length=19)
    public Date getNextRetryDate() 
    {
        return this.nextRetryDate;
    }    
    public void setNextRetryDate(Date nextRetryDate) 
    {
        this.nextRetryDate = nextRetryDate;
    }
    
    @Column(name="CPID")
    public Integer getCpid() 
    {
        return this.cpid;
    }
    public void setCpid(Integer cpid) 
    {
        this.cpid = cpid;
    }
    
    @Column(name="SCHEDULER_RETRY_STATUS", length=45)
    public String getSchedulerRetryStatus() 
    {
        return this.schedulerRetryStatus;
    }    
    public void setSchedulerRetryStatus(String schedulerRetryStatus) 
    {
        this.schedulerRetryStatus = schedulerRetryStatus;
    }

    @Column(name="scheduler_statechanger_status", length=25)
	public String getSchedulerStatechangerStatus() 
    {
		return schedulerStatechangerStatus;
	}
    public void setSchedulerStatechangerStatus(String schedulerStatechangerStatus) 
    {
		this.schedulerStatechangerStatus = schedulerStatechangerStatus;
	}
    
    @Column(name="scheduler_subs_msgs_status")
	public String getSchedulerSubsMsgsStatus() {
		return schedulerSubsMsgsStatus;
	}
	public void setSchedulerSubsMsgsStatus(String schedulerSubsMsgsStatus) {
		this.schedulerSubsMsgsStatus = schedulerSubsMsgsStatus;
	}
	
	@Column(name="serviceName", length=25)
	public String getServiceName() 
	{
		return serviceName;
	}
	public void setServiceName(String serviceName) 
	{
		this.serviceName = serviceName;
	}

	@Column(name="transactionId", length=25)
	public String getTransactionId() 
	{
		return transactionId;
	}
	public void setTransactionId(String transactionId) 
	{
		this.transactionId = transactionId;
	}

	@Column(name="nextAction", length=25)
	public String getAction() 
	{
		return action;
	}
	public void setAction(String action) 
	{
		this.action = action;
	}

	@Column(name="autoRenew")
	public String getAutoRenew() 
	{
		return autoRenew;
	}
	public void setAutoRenew(String autoRenew) 
	{
		this.autoRenew = autoRenew;
	}
	
	@Column(name="option1")
	public String getOption1() 
	{
		return option1;
	}
	public void setOption1(String option1) 
	{
		this.option1 = option1;
	}
	
	
	@Transient
	public boolean isMakeOnlyCdr() {
		return makeOnlyCdr;
	}
	public void setMakeOnlyCdr(boolean makeOnlyCdr) {
		this.makeOnlyCdr = makeOnlyCdr;
	}
	
		
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="NEXT_MSG_DATE", length=19)
	public Date getNextMsgDate() {
		return nextMsgDate;
	}
	public void setNextMsgDate(Date nextMsgDate) {
		this.nextMsgDate = nextMsgDate;
	}
	
	/*@Column(name="smsPromoStatus")
	public String getSmsPromoStatus() {
		return smsPromoStatus;
	}
	public void setSmsPromoStatus(String smsPromoStatus) {
		this.smsPromoStatus = smsPromoStatus;
	}*/
	
	@Override
    public String toString() 
    {
       	return "[ID="+this.id+", MSISDN="+this.msisdn+", SubServiceID="+this.subserviceId+", StartDate="+this.startDate+", EndDate="+this.endDate+",Status="+this.status+", SchedulerRenewalStatus="+this.schedulerRenewalStatus+", SchedulerRetryStatus="+this.schedulerRetryStatus+", SchedulerStatechangerStatus="+this.schedulerStatechangerStatus+", LastRenewDate="+this.lastRenewDate+", LastCallDate="+this.lastCallDate+", TransactionStatus="+this.transactionStatus+", Circle="+this.circle+", Country="+this.country+", Language="+this.language+", Price="+this.price+", CallAttempts="+this.callAttempts+", PrimaryMode="+this.primaryActMode+", SecondaryMode="+this.secondaryActMode+", ErrorMSG="+this.errorMsg+", SubType="+this.subType+", SubTimeLeft="+this.subTimeLeft+", SubServiceName="+this.subServiceName+", Operator="+this.operator+", GiftID="+this.giftId+", ServiceID="+this.serviceId+", RetryCount="+this.retryCount+", NextRetryDate="+this.nextRetryDate+", CPID="+this.cpid+", ServiceName="+this.serviceName+", TransactionID="+this.transactionId+", AutoRenew="+this.autoRenew+", Option1="+this.option1+" ]";
    }
	
   /* public String getString() 
    {
       	return "[ID="+this.id+", MSISDN="+this.msisdn+", SubServiceID="+this.subserviceId+", StartDate="+this.startDate+", EndDate="+this.endDate+",Status="+this.status+", SchedulerRenewalStatus="+this.schedulerRenewalStatus+", SchedulerRetryStatus="+this.schedulerRetryStatus+", SchedulerStatechangerStatus="+this.schedulerStatechangerStatus+", LastRenewDate="+this.lastRenewDate+", RetryCount="+this.retryCount+", NextRetryDate="+this.nextRetryDate+", AutoRenew="+this.autoRenew+"]";
    }*/
}