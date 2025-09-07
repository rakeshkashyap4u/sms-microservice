package com.rakesh.sms.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rakesh.sms.dao.GatewayDao;
import com.rakesh.sms.entity.MatchContent;
import com.rakesh.sms.entity.SMSCConfigs;
import com.rakesh.sms.entity.SMSCDetails;
import com.rakesh.sms.entity.SMSCFormats;
import com.rakesh.sms.jpas.MatchContentRepository;
import com.rakesh.sms.jpas.SMSCConfigsRepository;
import com.rakesh.sms.jpas.SMSCDetailsRepository;
import com.rakesh.sms.jpas.SMSCFormatsRepository;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

@Service
@Transactional
public class GatewayDaoImpl implements GatewayDao {

    private final SMSCDetailsRepository smscDetailsRepo;
    private final SMSCConfigsRepository smscConfigsRepo;
    private final SMSCFormatsRepository smscFormatsRepo;
    private final MatchContentRepository matchContentRepo;

    private SMSCDetails operator;

    public GatewayDaoImpl(SMSCDetailsRepository smscDetailsRepo,
                          SMSCConfigsRepository smscConfigsRepo,
                          SMSCFormatsRepository smscFormatsRepo,
                          MatchContentRepository matchContentRepo) {
        this.smscDetailsRepo = smscDetailsRepo;
        this.smscConfigsRepo = smscConfigsRepo;
        this.smscFormatsRepo = smscFormatsRepo;
        this.matchContentRepo = matchContentRepo;
    }

    private void setDetails() {
        try {
            operator = smscDetailsRepo.findByOperatorAndCountryAndProtocol(
                CoreUtils.getProperty("operator"),
                CoreUtils.getProperty("country"),
                CoreUtils.getProperty("protocol")
            ).orElse(null);

            if (operator == null) {
                Logger.sysLog(LogValues.error, this.getClass().getName(),
                        "NO Details FOUND for given Operator & Country & Protocol");
            }
        } catch (Exception e) {
            Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
        }
    }

    public String addFormatDetails(SMSCFormats format) {
        try {
            smscFormatsRepo.save(format);
            return format.getCid();
        } catch (Exception e) {
            Logger.sysLog(LogValues.error, this.getClass().getName(),
                    "Add SMSCFormat :: " + Logger.getStack(e));
            return "-1";
        }
    }

    public SMSCConfigs getConfigDetails(String circle) {
        if (operator == null) setDetails();
        try {
            if (circle != null && !circle.trim().isEmpty()) {
                return smscConfigsRepo.findByOpIdAndCircle(operator.getId(), circle.trim())
                        .orElse(null);
            } else {
                List<SMSCConfigs> list = smscConfigsRepo.findAllByOpIdOrderByCidAsc(operator.getId());
                return list.isEmpty() ? null : list.get(0);
            }
        } catch (Exception e) {
            Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
            return null;
        }
    }

    public SMSCFormats getFormatDetails(String cid) {
        try {
            return smscFormatsRepo.findByCid(cid).orElse(null);
        } catch (Exception e) {
            Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
            return null;
        }
    }

    public List<SMSCConfigs> getAllCircles() {
        try {
            return smscConfigsRepo.findAll(Sort.by(Sort.Direction.ASC, "cid"));
        } catch (Exception e) {
            Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
            return new ArrayList<>();
        }
    }

    public MatchContent getMatchContent(int id, String matchType) {
        try {
            return matchContentRepo.findByIdAndType(id, matchType).orElse(null);
        } catch (Exception e) {
            Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
            return null;
        }
    }
}
