package com.rakesh.sms.daoImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;

import com.rakesh.sms.beans.AlertServiceDetails;
import com.rakesh.sms.beans.SmsPromotion;
import com.rakesh.sms.dao.AlertsDao;
import com.rakesh.sms.entity.ActiveAlerts;
import com.rakesh.sms.entity.AlertLogs;
import com.rakesh.sms.entity.AlertsContent;
import com.rakesh.sms.entity.SmsSubscription;
import com.rakesh.sms.jpas.ActiveAlertsRepository;
import com.rakesh.sms.jpas.AlertLogsRepository;
import com.rakesh.sms.jpas.AlertsContentRepository;
import com.rakesh.sms.jpas.SmsSubscriptionRepository;
import com.rakesh.sms.util.CoreEnums;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

@Repository
public class AlertsDaoImpl implements AlertsDao {
	
	private final SmsSubscriptionRepository repo;
	
	 private final ActiveAlertsRepository activeAlterrepo;
	 
	 private final AlertsContentRepository alertContentrepo;
	 
	 private final AlertsContentRepository repository;

	    
	 private final AlertLogsRepository alertLogsRepository;

	   
	 public AlertsDaoImpl(SmsSubscriptionRepository repo,ActiveAlertsRepository activeAlterrepo,AlertsContentRepository alertContentrepo,AlertsContentRepository repository
			 ,AlertLogsRepository alertLogsRepository) {
	        this.repo = repo;
	        this.activeAlterrepo=activeAlterrepo;
	        this.alertContentrepo=alertContentrepo;
	        this.repository=repository;
	        this.alertLogsRepository=alertLogsRepository;
	    }

    @Transactional
    public SmsSubscription saveOrUpdate(SmsSubscription request) {
        Date now = new Date();

        // Try to find existing record (with or without subserviceid)
        SmsSubscription existingUser;
        if (request.getSubserviceid() != null && !request.getSubserviceid().isEmpty()) {
            existingUser = repo.findByMsisdnAndServiceidAndSubserviceid(
                    request.getMsisdn(), request.getServiceid(), request.getSubserviceid()
            ).orElse(null);
        } else {
            existingUser = repo.findByMsisdnAndServiceid(
                    request.getMsisdn(), request.getServiceid()
            ).orElse(null);
        }

        if (existingUser != null) {
            // Update case
            Logger.sysLog(LogValues.info, this.getClass().getName(),
                    " SMS Alert | Update Existing User :: " + request.getStatus() + " :: " + existingUser);

            existingUser.setLanguage(request.getLanguage());
            existingUser.setMsgflag(request.getMsgflag());
            existingUser.setStatus(request.getStatus());
            existingUser.setLastprocessed(now);
            return repo.save(existingUser);
        } else if ("ACTIVE".equals(request.getStatus())) {
            // Insert new user
            request.setStartdate(now);
            request.setLastprocessed(now);

            Logger.sysLog(LogValues.info, this.getClass().getName(),
                    " SMS Alert | Add New Active User " + request);
            return repo.save(request);
        } else {
            Logger.sysLog(LogValues.error, this.getClass().getName(),
                    " Unable to SUB user for the given details :: " + request);
            return null;
        }
    }


	
	/**
	 * Not Transactional :: Bulk Update
	 */
	public void updateLastUpdated(List<SmsSubscription> msisdns) {

		SessionFactory factory = DBConnection.getSessionFactory("0");

		try {

			Session session = factory.openSession();
			Transaction trans = session.beginTransaction();
			Date now = new Date();

			Logger.sysLog(LogValues.info, this.getClass().getName(),
					" SMS Alerts | Updating " + msisdns.size() + " users status... ");

			try {

				for (int i = 0; i < msisdns.size(); i++) {

					SmsSubscription sub = msisdns.get(i);
					sub.setLastprocessed(now);
					session.update(sub);
					session.flush();
					session.clear();

				} // End Of Loop

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						" SMS Alerts Bulk Update ERROR [1]---\n" + Logger.getStack(e));
			} finally {
				trans.commit();
				session.close();
				Logger.sysLog(LogValues.debug, this.getClass().getName(),
						" SMS Alerts | Bulk Update session successfully closed. ");
			} // End Of Inner Try Catch

		} catch (CannotCreateTransactionException ccte) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" SMS Alerts | Bulk Update | CANNOT CREATE MYSQL Transaction | Please contact DEVELOPMENT Team URGENTLY ");
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" SMS Alerts Bulk Update ERROR [2]---\n" + Logger.getStack(e));
		}
	}// End Of Method

	  @Transactional
	    public void delete(SmsSubscription request) {
	        Logger.sysLog(LogValues.info, this.getClass().getName(),
	                "msisdn: " + request.getMsisdn() + " serviceid: " + request.getServiceid()
	                        + " subserviceid: " + request.getSubserviceid());

	        SmsSubscription existingUser;

	        if (request.getSubserviceid() != null && !request.getSubserviceid().isEmpty()) {
	            existingUser = repo.findByMsisdnAndServiceidAndSubserviceid(
	                    request.getMsisdn(), request.getServiceid(), request.getSubserviceid()
	            ).orElse(null);
	        } else {
	            existingUser = repo.findByMsisdnAndServiceid(
	                    request.getMsisdn(), request.getServiceid()
	            ).orElse(null);
	        }

	        if (existingUser != null) {
	            repo.delete(existingUser);
	            Logger.sysLog(LogValues.info, this.getClass().getName(), "User Successfully Unsubscribed");
	        } else {
	            Logger.sysLog(LogValues.info, this.getClass().getName(),
	                    "Unable to UNSUB user for the given details :: " + request);
	        }
	    }
	

	  @Transactional(readOnly = true)
	    public List<ActiveAlerts> getAllActiveAlerts() {
	        List<ActiveAlerts> result = activeAlterrepo.findAll();

	        Logger.sysLog(LogValues.info, this.getClass().getName(),
	                "getAllActiveAlerts " + result.size());

	        return result;
	    }
	  
	  @Transactional(readOnly = true)
	    public ActiveAlerts getActiveAlert(String serviceid, String subserviceid) {
	        try {
	            Optional<ActiveAlerts> result;

	            if (subserviceid != null && !subserviceid.isEmpty()) {
	                result = activeAlterrepo.findFirstByServiceidAndSubserviceid(serviceid, subserviceid);
	            } else {
	                result = activeAlterrepo.findFirstByServiceid(serviceid);
	            }

	            return result.orElse(null);

	        } catch (Exception e) {
	            Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
	            return null;
	        }
	    }

	  @Transactional(readOnly = true)
	    public List<SmsSubscription> getActiveUsers(String serviceid, String subserviceid, String language) {
	        try {
	            List<SmsSubscription> result;

	            String status = CoreEnums.SubscriptionStatus.ACTIVE.toString();

	            // Special case for ASL in Iraq
	            if ("ASL".equalsIgnoreCase(CoreUtils.getProperty("operator"))
	                    && "IRQ".equalsIgnoreCase(CoreUtils.getProperty("country"))) {

	                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	                String startingDate = sf.format(new Date());

	                Date sd = sf.parse(startingDate);
	                Date ed = new SimpleDateFormat("yyyy-MM-dd").parse(currentDate);

	                result = (List<SmsSubscription>) repo.findActiveInDateRange(serviceid, status, sd, ed);

	            } else {
	                // Base query
	                if (subserviceid != null && !subserviceid.isEmpty() &&
	                    language != null && !language.isEmpty()) {
	                    result = (List<SmsSubscription>) repo.findByServiceidAndStatusAndSubserviceidAndLanguage(serviceid, status, subserviceid, language);
	                } else if (subserviceid != null && !subserviceid.isEmpty()) {
	                    result = (List<SmsSubscription>) repo.findByServiceidAndStatusAndSubserviceid(serviceid, status, subserviceid);
	                } else if (language != null && !language.isEmpty()) {
	                    result = (List<SmsSubscription>) repo.findByServiceidAndStatusAndLanguage(serviceid, status, language);
	                } else {
	                    result = (List<SmsSubscription>) repo.findByServiceidAndStatus(serviceid, status);
	                }
	            }

	            return result;

	        } catch (Exception e) {
	            Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
	            return List.of();
	        }
	    }

	@Transactional
	public List<String> getServiceAlertUsers(String serviceid, String subserviceid, String language, int days) {

		List<String> users = new ArrayList<String>();

		try {
			Class.forName("com.mysql.jdbc.Driver");

			Statement stmt = null;
			Connection conn = null;

			String url = CoreUtils.subsDbUrl;
			String query = "select msisdn from subscription where date(LAST_CALL_DATE)=date(now()-interval " + days
					+ " day) and service_id='" + serviceid + "' and subservice_id='" + subserviceid
					+ "' and status='active' and language='" + language + "'";

			for (int i = 0; i < 10; i++) {
				try {
					String dbUrl = url.replace("{i}", Integer.toString(i));
					conn = DriverManager.getConnection(dbUrl, CoreUtils.dbUsername, CoreUtils.dbPassword);

					stmt = conn.createStatement();

					ResultSet rs = stmt.executeQuery(query);

					while (rs.next()) {
						String msisdn = rs.getString("msisdn");
						users.add(msisdn);
					}

				} catch (Exception e) {
					Logger.sysLog(LogValues.error, this.getClass().getName(),
							"Exception while fetching service alert users [1]: " + Logger.getStack(e));
					return null;
				} finally {
					stmt.close();
					conn.close();
				}
			}

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					"Exception while fetching service alert users [2]: " + Logger.getStack(e));
			return null;
		}

		return users;

	}// End Of Method

	@Transactional(readOnly = true)
    public AlertsContent getComboMessage(SmsSubscription request) {
        Random rand = new Random();
        Date now = new Date();
        String subserviceid = request.getSubserviceid();

        String subLog = " [" + request.getServiceid() + "][" + subserviceid + "]";

        try {
            // Calculate subscription days
            long diffInmilliSec = now.getTime() - request.getStartdate().getTime();
            int diffInDays = (int) (diffInmilliSec / (1000 * 60 * 60 * 24));
            diffInDays = (diffInDays % 31) + 1;

            Logger.sysLog(LogValues.info, this.getClass().getName(),
                    subLog + " Fetching Message for Combo Alert || Msisdn= " + request.getMsisdn()
                            + " | SubscriptionDays= " + diffInDays);

            List<AlertsContent> result;

            if (subserviceid != null && !subserviceid.isEmpty()) {
                result = alertContentrepo.findByServiceIdAndMsgFlagAndMsgDayAndSubServiceId(
                        request.getServiceid(), request.getMsgflag(), diffInDays, subserviceid
                );
            } else {
                result = alertContentrepo.findByServiceIdAndMsgFlagAndMsgDay(
                        request.getServiceid(), request.getMsgflag(), diffInDays
                );
            }

            if (result != null && !result.isEmpty()) {
                int index = (result.size() > 1) ? rand.nextInt(result.size()) : 0;
                return result.get(index);
            } else {
                Logger.sysLog(LogValues.info, this.getClass().getName(),
                        subLog + " No Combo Alert Found for the User :: " + request.getMsisdn());
                return null;
            }

        } catch (Exception e) {
            Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
            return null;
        }
    }

	@Transactional
	public AlertsContent getNamazMessage(String serviceid, String subserviceid, Date time, String language) {
	    String subLog = " [" + serviceid + "][" + subserviceid + "][" + language + "]";
	    Calendar cal = Calendar.getInstance();
	    AlertsContent content = null;

	    try {
	        Time currentTime = new Time(time.getTime()); 
	        int dateDay = cal.get(Calendar.DAY_OF_MONTH);
	        int month = cal.get(Calendar.MONTH) + 1;

	        if ("true".equalsIgnoreCase(CoreUtils.getProperty("weeklyalert"))) {
	            Date date = new Date();
	            Calendar c = Calendar.getInstance();
	            c.setTime(date);
	            dateDay = c.get(Calendar.DAY_OF_WEEK);
	            String dayWeekText = new SimpleDateFormat("EEEE").format(date);

	            Logger.sysLog(LogValues.info, this.getClass().getName(),
	                subLog + " Fetching Message for Namaz Alert || Month= " + month +
	                        " | Day of week= " + dateDay + " | Day is " + dayWeekText +
	                        " | Time=" + currentTime);
	        } else {
	            Logger.sysLog(LogValues.info, this.getClass().getName(),
	                subLog + " Fetching Message for Namaz Alert || Month= " + month +
	                        " | DateOfMonth= " + dateDay + " | Time=" + currentTime);
	        }

	        // ✅ Direct JPA call
	        List<AlertsContent> result = alertContentrepo.findNamazMessages(
	                serviceid, month, dateDay, subserviceid, language, currentTime
	        );

	        if (result != null && !result.isEmpty()) {
	            content = result.get(0);
	        } else {
	            Logger.sysLog(LogValues.info, this.getClass().getName(),
	                subLog + " No Namaz Alert Found for Month=" + month + " and Day=" + dateDay);
	        }

	    } catch (Exception e) {
	        Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
	    }

	    return content;
	}


	@Transactional
	public AlertsContent getServiceAlertMessage(String serviceid, String subserviceid, Date time, String language) {
	    String subLog = " [" + serviceid + "][" + subserviceid + "][" + language + "]";
	    AlertsContent content = null;

	    try {
	        Time currentTime = new Time(time.getTime());

	        Logger.sysLog(LogValues.info, this.getClass().getName(),
	            subLog + " Fetching Message for Service Alert | Time=" + currentTime);

	        // ✅ Replace Criteria with JPA query
	        List<AlertsContent> result = alertContentrepo.findServiceAlertMessages(
	                serviceid, subserviceid, language, currentTime
	        );

	        if (result != null && !result.isEmpty()) {
	            content = result.get(0);
	        } else {
	            Logger.sysLog(LogValues.info, this.getClass().getName(),
	                subLog + " No Service Alert Found for serviceid=" + serviceid);
	        }

	    } catch (Exception e) {
	        Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
	    }

	    return content;
	}

	@Transactional
	public boolean isSubscribedForAlerts(String msisdn, String serviceid, String subserviceid) {
	    try {
	        msisdn = CoreUtils.stripCodes(msisdn);
	        return repo.isSubscribed(msisdn, serviceid, subserviceid);
	    } catch (Exception e) {
	        Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
	        return false;
	    }
	}


	 @Transactional
	    public int addPromotionLog(SmsPromotion details) {
	        try {
	            AlertLogs log = new AlertLogs();
	            log.setName(details.getJobName());
	            log.setCli(details.getCallerId());
	            log.setCircle(details.getCircle());
	            log.setStatus(details.getStatus());
	            log.setBaseSize(details.baseSize());
	            log.setExpiresAt(details.getExpiry());
	            log.setTimestamp(details.getTimestamp());

	            AlertLogs savedLog = alertLogsRepository.save(log);
	            return savedLog.getPid();

	        } catch (Exception e) {
	            Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
	            return -1;
	        }
	    }

	@Transactional
	public void updateFlag(String msisdn, String serviceid, String subserviceid, String flag) {
	    try {
	        msisdn = CoreUtils.stripCodes(msisdn);
	        int updated = repo.updateFlag(msisdn, serviceid, subserviceid, flag);

	        if (updated == 0) {
	            Logger.sysLog(LogValues.info, this.getClass().getName(),
	                "No active subscription found for msisdn=" + msisdn + ", serviceid=" + serviceid);
	        }
	    } catch (Exception e) {
	        Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
	    }
	}

	/**
	 * Not Transactional :: Bulk Insert
	 */
	public boolean namazMigration(List<SmsSubscription> msisdns) {

		SessionFactory factory = DBConnection.getSessionFactory("0");
		boolean status = false;

		try {

			Session session = factory.openSession();
			Transaction trans = session.beginTransaction();
			Date now = new Date();
			int count = 0;

			Logger.sysLog(LogValues.info, this.getClass().getName(),
					" Namaz Migration | Adding " + msisdns.size() + " users... ");

			try {

				for (int i = 0; i < msisdns.size(); i++) {

					SmsSubscription user = msisdns.get(i);
					user.setLastprocessed(now);
					user.setStartdate(now);

					try {
						session.save(user);
						session.flush();
						count++;
					} catch (ConstraintViolationException cve) {
						String reason = cve.getSQLException().getMessage();
						int errCode = cve.getErrorCode();

						if (errCode == 1062 && reason.contains("Duplicate entry")) {
							Logger.sysLog(LogValues.info, this.getClass().getName(), " Namaz Migration | User "
									+ user.getMsisdn() + " already exists for given NamazId ");
						} else {
							Logger.sysLog(LogValues.error, this.getClass().getName(),
									" ---Namaz Migration--- \n" + Logger.getStack(cve));
						}
					} // End Of Try Catch
					session.clear();
				} // End Of Loop

				status = true;

			} catch (SecurityException ie) {
				Logger.sysLog(LogValues.warn, this.getClass().getName(),
						" Namaz Migration Bulk Insert Interrupted  |  " + count + " Msisdns Inserted ");
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						" Namaz Migration Bulk Insert ERROR [1]---\n" + Logger.getStack(e));
			} finally {
				trans.commit();
				session.close();
				Logger.sysLog(LogValues.debug, this.getClass().getName(),
						" Namaz Migration | Bulk Insert session successfully closed. ");
			} // End Of Inner Try Catch

		} catch (CannotCreateTransactionException ccte) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Namaz Migration | Bulk Insert | CANNOT CREATE MYSQL Transaction | Please contact DEVELOPMENT Team URGENTLY ");
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Namaz Migration Bulk Insert ERROR [2]---\n" + Logger.getStack(e));
		} // End Of Try Catch

		return status;

	}// End Of Method

	/**
	 * Not Transactional :: Bulk Insert
	 */
//	public boolean uploadAlertContent(List<AlertsContent> contents) {
//
//		SessionFactory factory = DBConnection.getSessionFactory("0");
//		boolean status = false;
//
//		try {
//
//			Session session = factory.openSession();
//			Transaction trans = session.beginTransaction();
//			int i = 0;
//
//			Logger.sysLog(LogValues.info, this.getClass().getName(),
//					" Upload Content | Adding " + contents.size() + " entries... ");
//
//			try {
//
//				for (i = 0; i < contents.size(); i++) {
//					AlertsContent content = contents.get(i);
//					session.save(content);
//					session.flush();
//					session.clear();
//				} // End Of Loop
//
//				status = true;
//
//			} catch (SecurityException ie) {
//				Logger.sysLog(LogValues.warn, this.getClass().getName(),
//						" Upload Alert Content, Bulk Insert Interrupted  |  " + i + " Entries Inserted ");
//			} catch (Exception e) {
//				Logger.sysLog(LogValues.error, this.getClass().getName(),
//						" Upload Alert Content, Bulk Insert ERROR [1]---\n" + Logger.getStack(e));
//			} finally {
//				trans.commit();
//				session.close();
//				Logger.sysLog(LogValues.debug, this.getClass().getName(),
//						" Upload Content | Bulk Insert session successfully closed. ");
//			} // End Of Inner Try Catch
//
//		} catch (CannotCreateTransactionException ccte) {
//			Logger.sysLog(LogValues.error, this.getClass().getName(),
//					" Upload Content | Bulk Insert | CANNOT CREATE MYSQL Transaction | Please contact DEVELOPMENT Team URGENTLY ");
//		} catch (Exception e) {
//			Logger.sysLog(LogValues.error, this.getClass().getName(),
//					" Upload Alert Content, Bulk Insert ERROR [2]---\n" + Logger.getStack(e));
//		} // End Of Try Catch
//
//		return status;
//
//	}// End Of Method
	
	
	 @Transactional
	    public boolean uploadAlertContent(List<AlertsContent> contents) {
	        try {
	            repository.saveAll(contents);  // Bulk insert
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	@Transactional
	public List<AlertServiceDetails> getServicesWithSMSContent() {
	    try {
	        return alertContentrepo.findServicesWithSMSContent();
	    } catch (Exception e) {
	        Logger.sysLog(LogValues.error, this.getClass().getName(),
	            " getServicesWithSMSContent ---\n" + Logger.getStack(e));
	        return new ArrayList<>();
	    }
	}

	/**
	 * Not Transactional :: Delete Content
	 */
	@Transactional
	public void deleteAlertContentOf(List<AlertServiceDetails> services) {
	    int totalRowsDeleted = 0;

	    for (AlertServiceDetails service : services) {
	        int result;
	        if (service.getSubserviceid() != null && !service.getSubserviceid().isEmpty()) {
	            result = alertContentrepo.deleteByServiceIdAndSubServiceId(
	                service.getServiceid(), service.getSubserviceid());
	        } else {
	            result = alertContentrepo.deleteByServiceIdAndNoSubService(service.getServiceid());
	        }

	        totalRowsDeleted += result;
	    }

	    Logger.sysLog(LogValues.info, this.getClass().getName(),
	        "Alert Content DELETED | Total Rows Affected = " + totalRowsDeleted);
	}


}